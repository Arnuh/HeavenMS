/*
This file is part of the OdinMS Maple Story Server
Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
Matthias Butz <matze@odinms.de>
Jan Christian Meyer <vimes@odinms.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation version 3 as published by
the Free Software Foundation. You may not use, modify or distribute
this program under any other version of the GNU Affero General Public
License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package scripting.event;

import tools.Pair;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Properties;
import javax.script.ScriptException;
import net.server.audit.LockCollector;
import net.server.audit.locks.MonitoredLockType;
import net.server.audit.locks.MonitoredReentrantLock;
import net.server.audit.locks.MonitoredReentrantReadWriteLock;
import net.server.audit.locks.factory.MonitoredReentrantLockFactory;
import net.server.world.MapleParty;
import net.server.world.MaplePartyCharacter;
import server.MaplePortal;
import server.TimerManager;
import server.MapleStatEffect;
import server.expeditions.MapleExpedition;
import server.life.MapleMonster;
import server.maps.MapleMap;
import server.maps.MapleMapManager;
import server.maps.MapleReactor;
import client.MapleCharacter;
import client.SkillFactory;
import client.Skill;
import constants.ItemConstants;
import constants.ServerConstants;
import java.awt.Point;
import java.util.Collections;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Invocable;
import net.server.Server;
import net.server.coordinator.MapleEventRecallCoordinator;
import net.server.world.MapleRaid;
import scripting.AbstractPlayerInteraction;
import scripting.event.worker.EventScriptScheduler;
import server.MapleItemInformationProvider;
import server.ThreadManager;
import server.life.MapleLifeFactory;
import server.life.MapleNPC;
import server.life.Rank;
import server.maps.MapleMapFactory;
import tools.MaplePacketCreator;
import tools.Randomizer;

/**
 *
 * @author Matze
 * @author Ronan
 */
public class EventInstanceManager {

    private Map<Integer, MapleCharacter> chars = new HashMap<>();
    private int leaderId = -1, exitmap = -1;
    private List<MapleMonster> mobs = new LinkedList<>();
    private Map<MapleCharacter, Integer> killCount = new HashMap<>();
    private EventManager em;
    private EventScriptScheduler ess;
    public MapleMapManager mapManager;
    public MapleMap storedmap;
    private List<MapleMap> maps = new LinkedList<>();
    private String name;
    private Properties props = new Properties();
    private Map<String, Object> objectProps = new HashMap<>();
    private long timeStarted = 0;
    private long eventTime = 0;
    private MapleExpedition expedition = null;
    private List<Integer> mapIds = new LinkedList<>();
    private final ReentrantReadWriteLock lock = new MonitoredReentrantReadWriteLock(MonitoredLockType.EIM, true);
    private ReadLock rL = lock.readLock();
    private WriteLock wL = lock.writeLock();
    private MonitoredReentrantLock pL = MonitoredReentrantLockFactory.createLock(MonitoredLockType.EIM_PARTY, true);
    private MonitoredReentrantLock sL = MonitoredReentrantLockFactory.createLock(MonitoredLockType.EIM_SCRIPT, true);
    private ScheduledFuture<?> event_schedule = null;
    private boolean disposed = false;
    private boolean eventCleared = false;
    private boolean eventStarted = false;
    // multi-leveled PQ rewards!
    private Map<Integer, List<Integer>> collectionSet = new HashMap<>(ServerConstants.MAX_EVENT_LEVELS);
    private Map<Integer, List<Integer>> collectionQty = new HashMap<>(ServerConstants.MAX_EVENT_LEVELS);
    private Map<Integer, Integer> collectionExp = new HashMap<>(ServerConstants.MAX_EVENT_LEVELS);
    // Exp/Meso rewards by CLEAR on a stage
    private List<Integer> onMapClearExp = new ArrayList<>();
    private List<Integer> onMapClearMeso = new ArrayList<>();
    // registers player status on an event (null on this Map structure equals to 0)
    private Map<Integer, Integer> playerGrid = new HashMap<>();
    // registers all opened gates on the event. Will help late characters to encounter next stages gates already opened
    private Map<Integer, Pair<String, Integer>> openedGates = new HashMap<>();
    // forces deletion of items not supposed to be held outside of the event, dealt on a player's leaving moment.
    private Set<Integer> exclusiveItems = new HashSet<>();
    private Invocable iv;

    public EventInstanceManager(EventManager em, String name) {
        this.em = em;
        this.name = name;
        this.ess = new EventScriptScheduler();
        this.mapManager = new MapleMapManager(this, em.getWorldServer().getId(), em.getChannelServer().getId());
        this.iv = em.getIv();
    }

    public int getChannel() {
        return em.getChannelServer().getId();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExitMap(int map) {
        this.exitmap = map;
    }

    public int getExitMap() {
        return this.exitmap;
    }

    public EventManager getEm() {
        sL.lock();
        try {
            return em;
        } finally {
            sL.unlock();
        }
    }

    public boolean getdojorate() {
        return Server.getInstance().getdojorate();
    }

    public int getEventPlayersJobs() {
        //Bits -> 0: BEGINNER 1: WARRIOR 2: MAGICIAN
        //        3: BOWMAN 4: THIEF 5: PIRATE

        int mask = 0;
        for (MapleCharacter chr : getPlayers()) {
            mask |= (1 << chr.getJob().getJobNiche());
        }

        return mask;
    }

    public void applyEventPlayersItemBuff(int itemId) {
        List<MapleCharacter> players = getPlayerList();
        MapleStatEffect mse = MapleItemInformationProvider.getInstance().getItemEffect(itemId);

        if (mse != null) {
            for (MapleCharacter player : players) {
                mse.applyTo(player);
            }
        }
    }

    public void applyEventPlayersSkillBuff(int skillId) {
        applyEventPlayersSkillBuff(skillId, Integer.MAX_VALUE);
    }

    public void applyEventPlayersSkillBuff(int skillId, int skillLv) {
        List<MapleCharacter> players = getPlayerList();
        Skill skill = SkillFactory.getSkill(skillId);

        if (skill != null) {
            MapleStatEffect mse = skill.getEffect(Math.min(skillLv, skill.getMaxLevel()));
            if (mse != null) {
                for (MapleCharacter player : players) {
                    mse.applyTo(player);
                }
            }
        }
    }

    public void giveEventPlayersExp(int gain) {
        giveEventPlayersExp(gain, -1);
    }

    public MapleCharacter getRandomPlayer() {
        List<MapleCharacter> players = getPlayerList();
        Collections.shuffle(players);
        MapleCharacter mc = players.get(Randomizer.nextInt());
        return mc;
    }

    public void giveEventPlayersExp(int gain, int mapId) {
        if (gain == 0) {
            return;
        }
        List<MapleCharacter> players = getPlayerList();
        if (mapId == -1) {
            for (MapleCharacter mc : players) {
                mc.gainExp((int) (gain * mc.getExpRate()), true, true);
            }
        } else {
            for (MapleCharacter mc : players) {
                if (mc.getMapId() == mapId) {
                    mc.gainExp((int) (gain * mc.getExpRate()), true, true);
                }
            }
        }
    }

    public void giveEventPlayersExpLevel(int gain) {
        for (MapleCharacter mc : this.getPlayers()) {
            mc.gainExp(gain * mc.getTotalLevel(), true, true);
        }
    }

    public void giveEventPlayersMeso(int gain) {
        giveEventPlayersMeso(gain, -1);
    }

    public int random(int min, int max) {
        return Randomizer.random(min, max);
    }

    public int randomNextInt(int value) {
        return Randomizer.nextInt(value);
    }

    public void giveEventPlayersMeso(int gain, int mapId) {
        if (gain == 0) {
            return;
        }

        List<MapleCharacter> players = getPlayerList();

        if (mapId == -1) {
            for (MapleCharacter mc : players) {
                mc.gainMeso(gain * mc.getMesoRate());
            }
        } else {
            for (MapleCharacter mc : players) {
                if (mc.getMapId() == mapId) {
                    mc.gainMeso(gain * mc.getMesoRate());
                }
            }
        }

    }

    public Invocable getIv() {
        return iv;
    }

    public Object invokeScriptFunction(String name, Object... args) throws ScriptException, NoSuchMethodException {
        if (!disposed) {
            try {
                return em.getIv().invokeFunction(name, args);
            } catch (ScriptException e) {
                System.err.println("Exception in script \"" + this.getName() + "\" in function \"" + name + "\".");
                e.printStackTrace();
                throw e;
            }
        } else {
            return null;
        }
    }

    public void invokeScriptFunction(String name, boolean runEntryScript) {
        if (runEntryScript) {
            try {
                invokeScriptFunction(name, EventInstanceManager.this);
            } catch (ScriptException | NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
    }

    public synchronized void registerPlayer(final MapleCharacter chr) {
        registerPlayer(chr, true);
    }

    public synchronized void registerPlayer(final MapleCharacter chr, boolean runEntryScript) {
        if (chr == null || !chr.isLoggedinWorld() || disposed) {
            return;
        }

        wL.lock();
        try {
            if (chars.containsKey(chr.getId())) {
                return;
            }

            chars.put(chr.getId(), chr);
            chr.setLives(3);
            chr.setEventInstance(this);
        } finally {
            wL.unlock();
        }

        if (runEntryScript) {
            try {
                invokeScriptFunction("playerEntry", EventInstanceManager.this, chr);
            } catch (ScriptException | NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
    }

    public synchronized void registerSetup(final MapleCharacter chr, boolean runEntryScript) {
        if (chr == null || !chr.isLoggedinWorld() || disposed) {
            return;
        }
        if (runEntryScript) {
            try {
                invokeScriptFunction("setup", EventInstanceManager.this, chr);
            } catch (ScriptException | NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void exitPlayer(final MapleCharacter chr) {
        if (chr == null || !chr.isLoggedin()) {
            return;
        }

        this.unregisterPlayer(chr);

        try {
            invokeScriptFunction("playerExit", EventInstanceManager.this, chr);
        } catch (ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public void exitPlayer(MapleCharacter chr, int map) {
        if (chr == null) {
            return;
        }
        this.unregisterPlayer(chr);
        chr.changeMap(map, 0);
        if (this.getPlayers().isEmpty()) {
            dispose();
        } else {
            if (chr == this.getLeader()) {
                this.setLeader(getRandomPlayer());
            }
        }
    }

    public void revivePlayer(MapleCharacter chr, int map) {
        if (chr == null) {
            return;
        }

        if (chr == this.getLeader()) {
            exitParty(map);
        } else {
            this.unregisterPlayer(chr);
            chr.changeMap(map, 0);
            if (this.getPlayers().isEmpty()) {
                dispose();
            }
        }
        chr.cancelAllBuffs(false);
        chr.updateHpMp(chr.getMaxHp(), chr.getMaxMp());
        chr.setStance(0);
    }

    public void exitParty(int map) {
        if (getPlayers() == null) {
            return;
        }
        for (MapleCharacter chr : this.getPlayers()) {
            this.unregisterPlayer(chr);
            chr.changeMap(map, 0);
        }
        dispose();
    }

    public void exitParty() {
        if (getPlayers() == null) {
            return;
        }
        for (MapleCharacter chr : this.getPlayers()) {
            exitPlayer(chr);
        }
        dispose();
    }

    public void changeEventLeader(MapleCharacter chr) {
        MapleCharacter leader = this.getPlayers().get(this.getPlayers().size());
        this.setLeader(leader);
        dropMessage(5, "Event Leader has changed to " + leader.getName());
    }

    public void upgradePartyMedal() {
        if (getPlayers() == null) {
            return;
        }
        for (MapleCharacter chr : this.getPlayers()) {
            if (chr.isAlive()) {
                chr.upgradeMedal(chr.getTotalLevel() * 5);
            }
        }
    }

    public void upgradePartyMedal(int value) {
        if (getPlayers() == null) {
            return;
        }
        for (MapleCharacter chr : this.getPlayers()) {
            if (chr.isAlive()) {
                chr.upgradeMedal(value);
            }
        }
    }

    public void gainPartyItem(int id, short amount) {
        if (getPlayers() == null) {
            return;
        }
        for (MapleCharacter chr : this.getPlayers()) {
            if (chr.isAlive()) {
                chr.getAbstractPlayerInteraction().gainItem(id, amount);
            }
        }
    }

    public void gainPartySplitItem(int id, short amount) {
        if (getPlayers() == null) {
            return;
        }
        for (MapleCharacter chr : this.getPlayers()) {
            if (chr.isAlive()) {
                chr.getAbstractPlayerInteraction().gainItem(id, (short) (amount / this.getPlayers().size()));
            }
        }
    }

    public void changeMusic(String value) {
        if (getPlayers() == null) {
            return;
        }
        for (MapleCharacter chr : this.getPlayers()) {
            chr.announce(MaplePacketCreator.musicChange(value));
        }
    }

    public void victory(final int id) {
        for (MapleMap map : mapManager.getAllMaps()) {
            map.killAllMonsters(false);
        }
        showClearEffect();
        createEventTimer(45000);
        changeMusic("BgmCustom/Fanfare");
        dropMessage(5, "Event will end in 45 seconds.");
        rL.lock();
        try {
            if (this.ess != null) {
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        exitParty(id);
                    }
                };

                this.ess.registerEntry(r, 45000);
            }
        } finally {
            rL.unlock();
        }
    }

    public void dropMessage(int type, String message) {
        for (MapleCharacter chr : this.getPlayers()) {
            chr.dropMessage(type, message);
        }
    }

    public void dropTitle(String message) {
        for (MapleCharacter chr : this.getPlayers()) {
            chr.announce(MaplePacketCreator.earnTitleMessage(message));
        }
    }

    public void getDojoPoints(int amount) {
        for (MapleCharacter chr : this.getPlayers()) {
            chr.changeDojoPoints(amount);
            chr.announce(MaplePacketCreator.earnTitleMessage("+" + amount + " Points (Total: " + chr.getDojoPoints() + ")"));
        }
    }

    public void restartEventTimer(long time) {
        stopEventTimer();
        startEventTimer(time);
    }

    public void startEventTimer(long time) {
        timeStarted = System.currentTimeMillis();
        eventTime = time;

        for (MapleCharacter chr : getPlayers()) {
            chr.announce(MaplePacketCreator.getClock((int) (time / 1000)));
        }

        event_schedule = TimerManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                dismissEventTimer();

                try {
                    invokeScriptFunction("scheduledTimeout", EventInstanceManager.this);
                } catch (ScriptException | NoSuchMethodException ex) {
                    Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "Event '" + em.getName() + "' does not implement scheduledTimeout function.", ex);
                }
            }
        }, time);
    }

    public void createEventTimer(long time) {
        timeStarted = System.currentTimeMillis();
        eventTime = time;

        for (MapleCharacter chr : getPlayers()) {
            chr.announce(MaplePacketCreator.getClock((int) (time / 1000)));
        }

        event_schedule = TimerManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                dismissEventTimer();
            }
        }, time);
    }

    public void addEventTimer(long time) {
        if (event_schedule != null) {
            if (event_schedule.cancel(false)) {
                long nextTime = this.getTimeLeft() + time;
                eventTime += time;

                event_schedule = TimerManager.getInstance().schedule(new Runnable() {

                    @Override
                    public void run() {
                        dismissEventTimer();

                        try {
                            invokeScriptFunction("scheduledTimeout", EventInstanceManager.this);
                        } catch (ScriptException | NoSuchMethodException ex) {
                            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "Event '" + em.getName() + "' does not implement scheduledTimeout function.", ex);
                        }
                    }
                }, nextTime);
            }
        } else {
            startEventTimer(time);
        }
    }

    private void dismissEventTimer() {
        for (MapleCharacter chr : this.getPlayers()) {
            chr.announce(MaplePacketCreator.removeClock());
        }

        event_schedule = null;
        eventTime = 0;
        timeStarted = 0;
    }

    public void stopEventTimer() {
        if (event_schedule != null) {
            event_schedule.cancel(false);
            event_schedule = null;
        }

        dismissEventTimer();
    }

    public boolean isTimerStarted() {
        return eventTime > 0 && timeStarted > 0;
    }

    public long getTimeLeft() {
        return eventTime - (System.currentTimeMillis() - timeStarted);
    }

    public void registerParty(MapleCharacter chr) {
        if (chr.isPartyLeader()) {
            registerParty(chr.getParty(), chr.getMap());
        }
    }

    public boolean registerParty(MapleParty party, MapleMap map) {
        for (MaplePartyCharacter pc : party.getEligibleMembers()) {
            if (pc.isOnline()) {
                MapleCharacter c = map.getCharacterById(pc.getId());
                //System.out.println("HWID: " + c.getClient().getHWID());
                registerPlayer(c);
            }
        }
        return true;

    }

    public boolean registerRaid(MapleRaid raid, MapleMap map) {
        for (MapleCharacter pc : raid.getEligibleRaidMembers()) {
            MapleCharacter c = map.getCharacterById(pc.getId());
            registerPlayer(c);
        }
        return true;
    }

    public void registerExpedition(MapleExpedition exped) {
        expedition = exped;
        registerExpeditionTeam(exped, exped.getRecruitingMap().getId());
    }

    private void registerExpeditionTeam(MapleExpedition exped, int recruitMap) {
        expedition = exped;

        for (MapleCharacter chr : exped.getActiveMembers()) {
            if (chr.getMapId() == recruitMap) {
                registerPlayer(chr);
            }
        }
    }

    public void unregisterPlayer(final MapleCharacter chr) {
        try {
            invokeScriptFunction("playerUnregistered", EventInstanceManager.this, chr);
        } catch (ScriptException | NoSuchMethodException ex) {
            Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "Event '" + em.getName() + "' does not implement playerUnregistered function.", ex);
        }

        wL.lock();
        try {
            chars.remove(chr.getId());
            chr.setEventInstance(null);
        } finally {
            wL.unlock();
        }

        gridRemove(chr);
        dropExclusiveItems(chr);
    }

    public int getPlayerCount() {
        rL.lock();
        try {
            return chars.size();
        } finally {
            rL.unlock();
        }
    }

    public MapleCharacter getPlayerById(int id) {
        rL.lock();
        try {
            return chars.get(id);
        } finally {
            rL.unlock();
        }
    }

    public List<MapleCharacter> getPlayers() {
        rL.lock();
        try {
            return new ArrayList<>(chars.values());
        } finally {
            rL.unlock();
        }
    }

    private List<MapleCharacter> getPlayerList() {
        rL.lock();
        try {
            return new LinkedList<>(chars.values());
        } finally {
            rL.unlock();
        }
    }

    public void registerMonster(MapleMonster mob) {
        if (!mob.getStats().isFriendly()) { //We cannot register moon bunny
            if (mob.getId() > 3) {//we cannot register totems
                mobs.add(mob);
            }
        }
    }

    public void unregisterMonster(MapleMonster mob) {
        if (!mob.getStats().isFriendly()) { //We cannot register moon bunny
            mobs.remove(mob);
        }
    }

    public void movePlayer(final MapleCharacter chr) {
        try {
            invokeScriptFunction("moveMap", EventInstanceManager.this, chr);
        } catch (ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public void changedMap(final MapleCharacter chr, final int mapId) {
        try {
            invokeScriptFunction("changedMap", EventInstanceManager.this, chr, mapId);
        } catch (ScriptException | NoSuchMethodException ex) {
        } // optional
    }

    public void afterChangedMap(final MapleCharacter chr, final int mapId) {
        try {
            invokeScriptFunction("afterChangedMap", EventInstanceManager.this, chr, mapId);
        } catch (ScriptException | NoSuchMethodException ex) {
        } // optional
    }

    public synchronized void changedLeader(final MapleCharacter ldr) {
        try {
            invokeScriptFunction("changedLeader", EventInstanceManager.this, ldr);
        } catch (ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        this.leaderId = ldr.getId();
    }

    public void monsterKilled(final MapleMonster mob, final boolean hasKiller) {
        int scriptResult = 0;
        if (mob.getId() <= 3) {
            return;
        }
        sL.lock();
        try {
            mobs.remove(mob);
            if (eventStarted) {
                scriptResult = 1;

                if (mobs.isEmpty()) {
                    scriptResult = 2;
                }
            }
        } finally {
            sL.unlock();
        }

        if (scriptResult > 0) {
            try {
                invokeScriptFunction("monsterKilled", mob, EventInstanceManager.this, hasKiller);
            } catch (ScriptException | NoSuchMethodException ex) {
                ex.printStackTrace();
            }

            if (scriptResult > 1) {
                try {
                    invokeScriptFunction("allMonstersDead", EventInstanceManager.this, hasKiller);
                } catch (ScriptException | NoSuchMethodException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void friendlyKilled(final MapleMonster mob, final boolean hasKiller) {
        try {
            invokeScriptFunction("friendlyKilled", mob, EventInstanceManager.this, hasKiller);
        } catch (ScriptException | NoSuchMethodException ex) {
        } //optional
    }

    public void playerKilled(final MapleCharacter chr) {
        ThreadManager.getInstance().newTask(new Runnable() {

            @Override
            public void run() {
                try {
                    invokeScriptFunction("playerDead", EventInstanceManager.this, chr);
                } catch (ScriptException | NoSuchMethodException ex) {
                } // optional
            }
        });
    }

    public void reviveMonster(final MapleMonster mob) {
        try {
            invokeScriptFunction("monsterRevive", EventInstanceManager.this, mob);
        } catch (ScriptException | NoSuchMethodException ex) {
        } // optional
    }

    public void playerDisconnected(final MapleCharacter chr) {
        try {
            invokeScriptFunction("playerDisconnected", EventInstanceManager.this, chr);
        } catch (ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }

        MapleEventRecallCoordinator.getInstance().storeEventInstance(chr.getId(), this);
    }

    public void monsterKilled(MapleCharacter chr, final MapleMonster mob) {
        try {
            int inc = ((Double) invokeScriptFunction("monsterValue", EventInstanceManager.this, mob.getId())).intValue();

            if (inc != 0) {
                Integer kc = killCount.get(chr);
                if (kc == null) {
                    kc = inc;
                } else {
                    kc += inc;
                }
                killCount.put(chr, kc);
                if (expedition != null) {
                    expedition.monsterKilled(chr, mob);
                }
            }
        } catch (ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public void mobKilled(MapleMap map, MapleMonster mob, MapleCharacter chr) {
        try {
            invokeScriptFunction("mobKilled", EventInstanceManager.this, map, mob);
        } catch (ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public int getKillCount(MapleCharacter chr) {
        Integer kc = killCount.get(chr);
        return (kc == null) ? 0 : kc;
    }

    public void dispose() {
        rL.lock();
        try {
            for (MapleCharacter chr : chars.values()) {
                chr.setEventInstance(null);
            }
        } finally {
            rL.unlock();
        }

        dispose(false);
    }

    public synchronized void dispose(boolean shutdown) {    // should not trigger any event script method after disposed
        if (disposed) {
            return;
        }

        try {
            invokeScriptFunction("dispose", EventInstanceManager.this);
        } catch (ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        disposed = true;

        ess.dispose();

        wL.lock();
        try {
            for (MapleCharacter chr : chars.values()) {
                chr.setEventInstance(null);
            }
            chars.clear();
            mobs.clear();
            ess = null;
        } finally {
            wL.unlock();
        }

        if (event_schedule != null) {
            event_schedule.cancel(false);
            event_schedule = null;
        }
        killCount.clear();
        mapIds.clear();
        props.clear();
        objectProps.clear();
        maps.clear();

        disposeExpedition();

        sL.lock();
        try {
            if (!eventCleared) {
                em.disposedInstance(name);
            }
        } finally {
            sL.unlock();
        }
    }

    private void disposeLocks() {
        LockCollector.getInstance().registerDisposeAction(new Runnable() {

            @Override
            public void run() {
                emptyLocks();
            }
        });
    }

    private void emptyLocks() {
        pL = pL.dispose();
        sL = sL.dispose();
    }

    public MapleMapManager getMapFactory() {
        return mapManager;
    }

    public void schedule(final String methodName, long delay) {
        rL.lock();
        try {
            if (this.ess != null) {
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        try {
                            invokeScriptFunction(methodName, EventInstanceManager.this);
                        } catch (ScriptException | NoSuchMethodException ex) {
                            System.out.println("Error in Event Script " + name);
                            ex.printStackTrace();
                        }
                    }
                };

                this.ess.registerEntry(r, delay);
            }
        } finally {
            rL.unlock();
        }
    }

    public String getName() {
        return name;
    }

    /*
    public List<MapleMap> getMapsInstance() {
    return maps;
    }
    
    public MapleMap getMapInstance(int mapid) {
    
    for (MapleMap newmap : maps) {
    if (newmap.getId() == mapid) {
    return newmap;
    }
    }
    
    //System.out.println("Mape Error " + maps.get(mapid));
    return setMapInstance(mapid);
    }
    
    public MapleMap setMapInstance(int id) {
    MapleMap map = MapleMapFactory.loadMapFromWz(id, Server.getInstance().getWorld(0).getId(), this.getChannel(), this);
    maps.add(map);
    return map;
    }
    
     */
    public MapleMap getMapInstance(int mapId) {
        MapleMap map = mapManager.getMap(mapId);
        map.setEventInstance(this);

        if (!mapManager.isMapLoaded(mapId)) {
            sL.lock();
            try {
                if (em.getProperty("shuffleReactors") != null && em.getProperty("shuffleReactors").equals("true")) {
                    map.shuffleReactors();
                }
            } finally {
                sL.unlock();
            }
        }
        return map;
    }

    public void setIntProperty(String key, Integer value) {
        setProperty(key, value);
    }

    public void setProperty(String key, Integer value) {
        setProperty(key, "" + value);
    }

    public void setProperty(String key, String value) {
        pL.lock();
        try {
            props.setProperty(key, value);
        } finally {
            pL.unlock();
        }
    }

    public Object setProperty(String key, String value, boolean prev) {
        pL.lock();
        try {
            return props.setProperty(key, value);
        } finally {
            pL.unlock();
        }
    }

    public void setObjectProperty(String key, Object obj) {
        pL.lock();
        try {
            objectProps.put(key, obj);
        } finally {
            pL.unlock();
        }
    }

    public String getProperty(String key) {
        pL.lock();
        try {
            return props.getProperty(key);
        } finally {
            pL.unlock();
        }
    }

    public int getIntProperty(String key) {
        pL.lock();
        try {
            return Integer.parseInt(props.getProperty(key));
        } finally {
            pL.unlock();
        }
    }

    public Object getObjectProperty(String key) {
        pL.lock();
        try {
            return objectProps.get(key);
        } finally {
            pL.unlock();
        }
    }

    public void leftParty(final MapleCharacter chr) {
        try {
            invokeScriptFunction("leftParty", EventInstanceManager.this, chr);
        } catch (ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public void disbandParty() {
        try {
            invokeScriptFunction("disbandParty", EventInstanceManager.this);
        } catch (ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public void clearPQ() {
        try {
            invokeScriptFunction("clearPQ", EventInstanceManager.this);
        } catch (ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public void removePlayer(final MapleCharacter chr) {
        try {
            invokeScriptFunction("playerExit", EventInstanceManager.this, chr);
        } catch (ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isLeader(MapleCharacter chr) {
        return (chr.getParty().getLeaderId() == chr.getId());
    }

    public boolean isEventLeader(MapleCharacter chr) {
        return (chr.getId() == getLeaderId());
    }

    public final MapleMap getInstanceMap(final int mapid) {
        if (disposed) {
            return null;
        }
        mapIds.add(mapid);
        return getMapFactory().getMap(mapid);
    }

    public final boolean disposeIfPlayerBelow(final byte size, final int towarp) {
        if (disposed) {
            return true;
        }
        if (chars == null) {
            return false;
        }

        MapleMap map = null;
        if (towarp > 0) {
            map = this.getMapFactory().getMap(towarp);
        }

        List<MapleCharacter> players = getPlayerList();

        try {
            if (players.size() < size) {
                for (MapleCharacter chr : players) {
                    if (chr == null) {
                        continue;
                    }

                    unregisterPlayer(chr);
                    if (towarp > 0) {
                        chr.changeMap(map, map.getPortal(0));
                    }
                }

                dispose();
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public void spawnNpc(int npcId, Point pos, MapleMap map) {
        MapleNPC npc = MapleLifeFactory.getNPC(npcId);
        if (npc != null) {
            npc.setPosition(pos);
            npc.setCy(pos.y);
            npc.setRx0(pos.x + 50);
            npc.setRx1(pos.x - 50);
            npc.setFh(map.getFootholds().findBelow(pos).getId());
            map.addMapObject(npc);
            map.broadcastMessage(MaplePacketCreator.spawnNPC(npc));
        }
    }

    public void dispatchUpdateQuestMobCount(int mobid, int mapid) {
        Map<Integer, MapleCharacter> mapChars = getInstanceMap(mapid).getMapPlayers();
        if (!mapChars.isEmpty()) {
            List<MapleCharacter> eventMembers = getPlayers();

            for (MapleCharacter evChr : eventMembers) {
                MapleCharacter chr = mapChars.get(evChr.getId());

                if (chr != null && chr.isLoggedinWorld()) {
                    chr.updateQuestMobCount(mobid);
                }
            }
        }
    }

    public MapleMonster getMonster(int mid) {
        return (MapleLifeFactory.getMonster(mid, em.getChannelServer().getId()));
    }

    public Integer getMonsterRank(int mid) {
        return (MapleLifeFactory.getMonster(mid, em.getChannelServer().getId()).getStats().getScale());
    }

    private List<Integer> convertToIntegerArray(List<Double> list) {
        List<Integer> intList;
        if (ServerConstants.JAVA_8) {
            intList = new ArrayList<Integer>(new ArrayList(java.util.Arrays.asList(list.toArray())));
        } else {
            intList = new ArrayList<>();
            for (Double d : list) {
                intList.add(d.intValue());
            }
        }
        return intList;
    }

    public void setEventClearStageExp(List<Double> gain) {
        onMapClearExp.clear();
        onMapClearExp.addAll(convertToIntegerArray(gain));
    }

    public void setEventClearStageMeso(List<Double> gain) {
        onMapClearMeso.clear();
        onMapClearMeso.addAll(convertToIntegerArray(gain));
    }

    public Integer getClearStageExp(int stage) {    //stage counts from ONE.
        if (stage > onMapClearExp.size()) {
            return 0;
        }
        return onMapClearExp.get(stage - 1);
    }

    public Integer getClearStageMeso(int stage) {   //stage counts from ONE.
        if (stage > onMapClearMeso.size()) {
            return 0;
        }
        return onMapClearMeso.get(stage - 1);
    }

    public List<Integer> getClearStageBonus(int stage) {
        List<Integer> list = new ArrayList<>();
        list.add(getClearStageExp(stage));
        list.add(getClearStageMeso(stage));

        return list;
    }

    private void dropExclusiveItems(MapleCharacter chr) {
        AbstractPlayerInteraction api = chr.getAbstractPlayerInteraction();

        for (Integer item : exclusiveItems) {
            api.removeAll(item);
        }
    }

    public final void setExclusiveItems(List<Double> items) {
        List<Integer> exclusive = convertToIntegerArray(items);

        wL.lock();
        try {
            for (Integer item : exclusive) {
                exclusiveItems.add(item);
            }
        } finally {
            wL.unlock();
        }
    }

    public final void setEventRewards(List<Double> rwds, List<Double> qtys, int expGiven) {
        setEventRewards(1, rwds, qtys, expGiven);
    }

    public final void setEventRewards(List<Double> rwds, List<Double> qtys) {
        setEventRewards(1, rwds, qtys);
    }

    public final void setEventRewards(int eventLevel, List<Double> rwds, List<Double> qtys) {
        setEventRewards(eventLevel, rwds, qtys, 0);
    }

    public final void setEventRewards(int eventLevel, List<Double> rwds, List<Double> qtys, int expGiven) {
        // fixed EXP will be rewarded at the same time the random item is given

        if (eventLevel <= 0 || eventLevel > ServerConstants.MAX_EVENT_LEVELS) {
            return;
        }
        eventLevel--;    //event level starts from 1

        List<Integer> rewardIds = convertToIntegerArray(rwds);
        List<Integer> rewardQtys = convertToIntegerArray(qtys);

        //rewardsSet and rewardsQty hold temporary values
        wL.lock();
        try {
            collectionSet.put(eventLevel, rewardIds);
            collectionQty.put(eventLevel, rewardQtys);
            collectionExp.put(eventLevel, expGiven);
        } finally {
            wL.unlock();
        }
    }

    private byte getRewardListRequirements(int level) {
        if (level >= collectionSet.size()) {
            return 0;
        }

        byte rewardTypes = 0;
        List<Integer> list = collectionSet.get(level);

        for (Integer itemId : list) {
            rewardTypes |= (1 << ItemConstants.getInventoryType(itemId).getType());
        }

        return rewardTypes;
    }

    private boolean hasRewardSlot(MapleCharacter player, int eventLevel) {
        byte listReq = getRewardListRequirements(eventLevel);   //gets all types of items present in the event reward list

        //iterating over all valid inventory types
        for (byte type = 1; type <= 5; type++) {
            if ((listReq >> type) % 2 == 1 && !player.hasEmptySlot(type)) {
                return false;
            }
        }

        return true;
    }

    public final boolean giveEventReward(MapleCharacter player) {
        return giveEventReward(player, 1);
    }

    //gives out EXP & a random item in a similar fashion of when clearing KPQ, LPQ, etc.
    public final boolean giveEventReward(MapleCharacter player, int eventLevel) {
        List<Integer> rewardsSet, rewardsQty;
        Integer rewardExp;

        rL.lock();
        try {
            eventLevel--;       //event level starts counting from 1
            if (eventLevel >= collectionSet.size()) {
                return true;
            }

            rewardsSet = collectionSet.get(eventLevel);
            rewardsQty = collectionQty.get(eventLevel);

            rewardExp = collectionExp.get(eventLevel);
        } finally {
            rL.unlock();
        }

        if (rewardExp == null) {
            rewardExp = 0;
        }

        if (rewardsSet == null || rewardsSet.isEmpty()) {
            if (rewardExp > 0) {
                player.gainExp(rewardExp);
            }
            return true;
        }

        if (!hasRewardSlot(player, eventLevel)) {
            return false;
        }

        AbstractPlayerInteraction api = player.getAbstractPlayerInteraction();
        int rnd = (int) Math.floor(Math.random() * rewardsSet.size());

        api.gainItem(rewardsSet.get(rnd), rewardsQty.get(rnd).shortValue());
        if (rewardExp > 0) {
            player.gainExp(rewardExp);
        }
        return true;
    }

    private void disposeExpedition() {
        if (expedition != null) {
            expedition.dispose(eventCleared);

            sL.lock();
            try {
                expedition.removeChannelExpedition(em.getChannelServer());
            } finally {
                sL.unlock();
            }

            expedition = null;
        }
    }

    public final synchronized void startEvent() {
        eventStarted = true;

        try {
            invokeScriptFunction("afterSetup", EventInstanceManager.this);
        } catch (ScriptException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }

    public final void setEventCleared() {
        eventCleared = true;

        for (MapleCharacter chr : getPlayers()) {
            chr.awardQuestPoint(ServerConstants.QUEST_POINT_PER_EVENT_CLEAR);
        }

        sL.lock();
        try {
            em.disposeInstance(name);
        } finally {
            sL.unlock();
        }

        disposeExpedition();
    }

    public final boolean isEventCleared() {
        return eventCleared;
    }

    public final boolean isEventDisposed() {
        return disposed;
    }

    private boolean isEventTeamLeaderOn() {
        for (MapleCharacter chr : getPlayers()) {
            if (chr.getId() == getLeaderId()) {
                return true;
            }
        }

        return false;
    }

    public final boolean checkEventTeamLacking(boolean leavingEventMap, int minPlayers) {
        if (eventCleared && getPlayerCount() > 1) {
            return false;
        }

        if (!eventCleared && leavingEventMap && !isEventTeamLeaderOn()) {
            return true;
        }
        if (getPlayerCount() < minPlayers) {
            return true;
        }

        return false;
    }

    public final boolean isExpeditionTeamLackingNow(boolean leavingEventMap, int minPlayers, MapleCharacter quitter) {
        if (eventCleared) {
            if (leavingEventMap && getPlayerCount() <= 1) {
                return true;
            }
        } else {
            // thanks Conrad for noticing expeditions don't need to have neither the leader nor meet the minimum requirement inside the event
            if (getPlayerCount() <= 1) {
                return true;
            }
        }

        return false;
    }

    public final boolean isEventTeamLackingNow(boolean leavingEventMap, int minPlayers, MapleCharacter quitter) {
        if (eventCleared) {
            if (leavingEventMap && getPlayerCount() <= 1) {
                return true;
            }
        } else {
            if (leavingEventMap && getLeaderId() == quitter.getId()) {
                return true;
            }
            if (getPlayerCount() <= minPlayers) {
                return true;
            }
        }

        return false;
    }

    public final boolean isEventTeamTogether() {
        rL.lock();
        try {
            if (chars.size() <= 1) {
                return true;
            }

            Iterator<MapleCharacter> iterator = chars.values().iterator();
            MapleCharacter mc = iterator.next();
            int mapId = mc.getMapId();

            for (; iterator.hasNext();) {
                mc = iterator.next();
                if (mc.getMapId() != mapId) {
                    return false;
                }
            }

            return true;
        } finally {
            rL.unlock();
        }
    }

    public final void warpEventTeam(int warpFrom, int warpTo) {
        List<MapleCharacter> players = getPlayerList();

        for (MapleCharacter chr : players) {
            if (chr.getMapId() == warpFrom) {
                chr.changeMap(warpTo);
            }
        }
    }

    public final void warpEventTeam(int warpTo) {
        List<MapleCharacter> players = getPlayerList();

        for (MapleCharacter chr : players) {
            chr.changeMap(warpTo);
        }
    }

    public final void warpEventTeamToMapSpawnPoint(int warpFrom, int warpTo, int toSp) {
        List<MapleCharacter> players = getPlayerList();

        for (MapleCharacter chr : players) {
            if (chr.getMapId() == warpFrom) {
                chr.changeMap(warpTo, toSp);
            }
        }
    }

    public final void warpEventTeamToMapSpawnPoint(int warpTo, int toSp) {
        List<MapleCharacter> players = getPlayerList();

        for (MapleCharacter chr : players) {
            chr.changeMap(warpTo, toSp);
        }
    }

    public final int getLeaderId() {
        rL.lock();
        try {
            return leaderId;
        } finally {
            rL.unlock();
        }
    }

    public MapleCharacter getLeader() {
        rL.lock();
        try {
            return chars.get(leaderId);
        } finally {
            rL.unlock();
        }
    }

    public final void setLeader(MapleCharacter chr) {
        wL.lock();
        try {
            leaderId = chr.getId();
        } finally {
            wL.unlock();
        }
    }

    public final void showWrongEffect() {
        showWrongEffect(getLeader().getMapId());
    }

    public final void showWrongEffect(int mapId) {
        MapleMap map = getMapInstance(mapId);
        map.broadcastMessage(MaplePacketCreator.showEffect("quest/party/wrong_kor"));
        map.broadcastMessage(MaplePacketCreator.playSound("Party1/Failed"));
    }

    public final void showClearEffect() {
        showClearEffect(false);
    }

    public final void showClearEffect(boolean hasGate) {
        MapleCharacter leader = getLeader();
        if (leader != null) {
            showClearEffect(hasGate, leader.getMapId());
        }
    }

    public final void showClearEffect(int mapId) {
        showClearEffect(false, mapId);
    }

    public final void showClearEffect(boolean hasGate, int mapId) {
        showClearEffect(hasGate, mapId, "gate", 2);
    }

    public final void showClearEffect(int mapId, String mapObj, int newState) {
        showClearEffect(true, mapId, mapObj, newState);
    }

    public final void showClearEffect(boolean hasGate, int mapId, String mapObj, int newState) {
        MapleMap map = getMapInstance(mapId);
        map.broadcastMessage(MaplePacketCreator.showEffect("quest/party/clear"));
        map.broadcastMessage(MaplePacketCreator.playSound("Party1/Clear"));
        if (hasGate) {
            map.broadcastMessage(MaplePacketCreator.environmentChange(mapObj, newState));
            wL.lock();
            try {
                openedGates.put(map.getId(), new Pair<>(mapObj, newState));
            } finally {
                wL.unlock();
            }
        }
    }

    public final void recoverOpenedGate(MapleCharacter chr, int thisMapId) {
        Pair<String, Integer> gateData = null;

        rL.lock();
        try {
            if (openedGates.containsKey(thisMapId)) {
                gateData = openedGates.get(thisMapId);
            }
        } finally {
            rL.unlock();
        }

        if (gateData != null) {
            chr.announce(MaplePacketCreator.environmentChange(gateData.getLeft(), gateData.getRight()));
        }
    }

    public final void giveEventPlayersStageReward(int thisStage) {
        List<Integer> list = getClearStageBonus(thisStage);     // will give bonus exp & mesos to everyone in the event
        giveEventPlayersExp(list.get(0));
        giveEventPlayersMeso(list.get(1));
    }

    public final void linkToNextStage(int thisStage, String eventFamily, int thisMapId) {
        giveEventPlayersStageReward(thisStage);
        thisStage--;    //stages counts from ONE, scripts from ZERO

        MapleMap nextStage = getMapInstance(thisMapId);
        MaplePortal portal = nextStage.getPortal("next00");
        if (portal != null) {
            portal.setScriptName(eventFamily + thisStage);
        }
    }

    public final void linkPortalToScript(int thisStage, String portalName, String scriptName, int thisMapId) {
        giveEventPlayersStageReward(thisStage);
        thisStage--;    //stages counts from ONE, scripts from ZERO

        MapleMap nextStage = getMapInstance(thisMapId);
        MaplePortal portal = nextStage.getPortal(portalName);
        if (portal != null) {
            portal.setScriptName(scriptName);
        }
    }

    // registers a player status in an event
    public final void gridInsert(MapleCharacter chr, int newStatus) {
        wL.lock();
        try {
            playerGrid.put(chr.getId(), newStatus);
        } finally {
            wL.unlock();
        }
    }

    // unregisters a player status in an event
    public final void gridRemove(MapleCharacter chr) {
        wL.lock();
        try {
            playerGrid.remove(chr.getId());
        } finally {
            wL.unlock();
        }
    }

    // checks a player status
    public final int gridCheck(MapleCharacter chr) {
        rL.lock();
        try {
            Integer i = playerGrid.get(chr.getId());
            return (i != null) ? i : -1;
        } finally {
            rL.unlock();
        }
    }

    public final int gridSize() {
        rL.lock();
        try {
            return playerGrid.size();
        } finally {
            rL.unlock();
        }
    }

    public final void gridClear() {
        wL.lock();
        try {
            playerGrid.clear();
        } finally {
            wL.unlock();
        }
    }

    public boolean activatedAllReactorsOnMap(int mapId, int minReactorId, int maxReactorId) {
        return activatedAllReactorsOnMap(this.getMapInstance(mapId), minReactorId, maxReactorId);
    }

    public boolean activatedAllReactorsOnMap(MapleMap map, int minReactorId, int maxReactorId) {
        if (map == null) {
            return true;
        }

        for (MapleReactor mr : map.getReactorsByIdRange(minReactorId, maxReactorId)) {
            if (mr.getReactorType() != -1) {
                return false;
            }
        }

        return true;
    }

    public void modPartyStam(int amount) {
        if (getPlayers() == null) {
            return;
        }
        for (MapleCharacter chr : this.getPlayers()) {
            if (chr.isAlive() && chr.getStamina() < chr.getTotalLevel()) {
                chr.modStamina(amount);
            }
        }
    }

    public int getAvgPlayerLevel() {
        int avg = 0;
        for (MapleCharacter chr : this.getPlayers()) {
            avg += chr.getTotalLevel();
        }
        avg = avg / this.getPlayers().size();
        return avg;
    }

    public MapleMap getStoredmap() {
        return storedmap;
    }

    public void setStoredmap(MapleMap storedmap) {
        this.storedmap = storedmap;
    }

    public int getRandomNxWeapon() {
        return MapleItemInformationProvider.getInstance().getWeapons().get(Randomizer.nextInt(MapleItemInformationProvider.getInstance().getWeapons().size()));
    }

    public int getRandomNxEquip() {
        return MapleItemInformationProvider.getInstance().getEquips().get(Randomizer.nextInt(MapleItemInformationProvider.getInstance().getEquips().size()));
    }

    public void gainRandomNx() {
        if (getPlayers() == null) {
            return;
        }
        for (MapleCharacter chr : this.getPlayers()) {
            if (chr.isAlive()) {
                chr.getAbstractPlayerInteraction().gainItem(getRandomNxEquip(), 1, false);
            }
        }
    }
}
