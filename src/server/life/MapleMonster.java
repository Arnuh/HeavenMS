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
package server.life;

import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleJob;
import client.Skill;
import client.SkillFactory;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.ServerConstants;
import constants.skills.Crusader;
import constants.skills.DragonKnight;
import constants.skills.FPMage;
import constants.skills.Hermit;
import constants.skills.ILMage;
import constants.skills.NightLord;
import constants.skills.NightWalker;
import constants.skills.Priest;
import constants.skills.Shadower;
import constants.skills.WhiteKnight;
import java.awt.Point;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLong;
import net.server.audit.locks.MonitoredReentrantLock;
import net.server.channel.Channel;
import net.server.world.MaplePartyCharacter;
import scripting.event.EventInstanceManager;
import server.TimerManager;
import server.life.MapleLifeFactory.BanishInfo;
import server.maps.MapleMap;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.Randomizer;
import net.server.audit.LockCollector;
import net.server.audit.locks.MonitoredLockType;
import net.server.audit.locks.factory.MonitoredReentrantLockFactory;
import net.server.coordinator.MapleMonsterAggroCoordinator;
import server.MapleStatEffect;
import server.life.MapleLifeFactory.selfDestruction;
import server.loot.MapleLootManager;
import server.maps.MapleSummon;

public class MapleMonster extends AbstractLoadedMapleLife {

    private ChangeableStats ostats = null;  //unused, v83 WZs offers no support for changeable stats.
    private MapleMonsterStats stats;
    private AtomicLong hp = new AtomicLong(1);
    private AtomicLong maxHpPlusHeal = new AtomicLong(1);
    private int mp;
    public float difficulty = 1.0f;
    private WeakReference<MapleCharacter> controller = new WeakReference<>(null);
    private boolean controllerHasAggro, controllerKnowsAboutAggro, controllerHasPuppet;
    private Collection<MonsterListener> listeners = new LinkedList<>();
    private EnumMap<MonsterStatus, MonsterStatusEffect> stati = new EnumMap<>(MonsterStatus.class);
    private ArrayList<MonsterStatus> alreadyBuffed = new ArrayList<>();
    private MapleMap map;
    private int VenomMultiplier = 0;
    private boolean fake = false;
    private boolean dropsDisabled = false;
    private List<Pair<Integer, Integer>> usedSkills = new ArrayList<>();
    private Map<Pair<Integer, Integer>, Integer> skillsUsed = new HashMap<>();
    private Set<Integer> usedAttacks = new HashSet<>();
    private Set<Integer> calledMobOids = null;
    private WeakReference<MapleMonster> callerMob = new WeakReference<>(null);
    private List<Integer> stolenItems = new ArrayList<>();
    private int team;
    private int parentMobOid = 0;
    private final HashMap<Integer, AtomicLong> takenDamage = new HashMap<>();
    private Runnable removeAfterAction = null;
    private boolean availablePuppetUpdate = true;
    public int channel = 1;
    public int scale = 1;
    private MonitoredReentrantLock externalLock = MonitoredReentrantLockFactory.createLock(MonitoredLockType.MOB_EXT);
    private MonitoredReentrantLock monsterLock = MonitoredReentrantLockFactory.createLock(MonitoredLockType.MOB, true);
    private MonitoredReentrantLock statiLock = MonitoredReentrantLockFactory.createLock(MonitoredLockType.MOB_STATI);
    private MonitoredReentrantLock animationLock = MonitoredReentrantLockFactory.createLock(MonitoredLockType.MOB_ANI);
    private MonitoredReentrantLock aggroUpdateLock = MonitoredReentrantLockFactory.createLock(MonitoredLockType.MOB_AGGRO);

    public MapleMonster(MapleMonster monster, int level, int channel, int scale, boolean xp, boolean link, MapleCharacter chr) {
        super(monster);
        initWithStats(monster.getStats(), level, channel, scale, xp, link, chr);
    }

    public MapleMonster(int id, MapleMonsterStats stats, int level, int channel, int scale, boolean xp, boolean link, MapleCharacter chr) {
        super(id);
        initWithStats(stats, level, channel, scale, xp, link, chr);
    }

    public void lockMonster() {
        externalLock.lock();
    }

    public void unlockMonster() {
        externalLock.unlock();
    }

    private void initWithStats(MapleMonsterStats baseStats, int level, int channel, int scale, boolean xp, boolean link, MapleCharacter chr) {
        if (channel != -1) {
            setStance(5);
            this.stats = baseStats.copy();
            if (level == -1) {
                level = this.stats.getLevel();

            }
            this.stats.setLevel(level);
            if (scale == -1) {
                scale = this.stats.getScale();
            }
            this.stats.setScale(scale);
            long HP = (long) (Math.pow(level * scale, 3) * Math.pow(scale, 3) * Math.pow(scale, 2) / 1.6);
            long exp = (long) (Math.pow(level * Math.pow(scale, 3), 2));

            if (getId() == 1) {
                HP = 1;
                exp = 0;
            }
            if (getId() == 9500317 || getId() == 9500318 || getId() == 9500319 || getId() == 9500532) { //snowman stages 1-2-3
                HP = (long) (Math.pow(level, 2) * 10);
            }
            if (getId() == 8510100 || getId() == 8510200 || getId() == 8510300) {
                HP = (long) (Math.pow(level, 3)); //mines
            }


            this.stats.setHp(Randomizer.MaxLong(HP, Long.MAX_VALUE));
            this.stats.setExp(xp ? Randomizer.MaxLong(exp, Long.MAX_VALUE) : 0);

            this.stats.setAtk((int) (level * Math.pow(scale, 2)) * 3);
            this.stats.setDef((int) (scale * Math.pow(level, 2)));
            this.stats.setPower((int) (level * Math.pow(scale, 2) * 4));

            maxHpPlusHeal.set(hp.get());
            if (!link && !this.stats.getRevives().isEmpty()) {
                this.stats.removeRevives(this.stats.getRevives());

            }
            if (chr != null) {
                this.stats.setOwner(chr);
            }
            hp.set(stats.getHp());
            mp = stats.getMp();
        }
    }

    public void disableDrops() {
        this.dropsDisabled = true;
    }

    public void enableDrops() {
        this.dropsDisabled = false;
    }

    public boolean dropsDisabled() {
        return dropsDisabled;
    }

    public void setMap(MapleMap map) {
        this.map = map;
    }

    public int getParentMobOid() {
        return parentMobOid;
    }

    public void setParentMobOid(int parentMobId) {
        this.parentMobOid = parentMobId;
    }

    public int countAvailableMobSummons(int summonsSize, int skillLimit) {    // limit prop for summons has another conotation, found thanks to MedicOP
        int summonsCount;

        Set<Integer> calledOids = this.calledMobOids;
        if (calledOids != null) {
            summonsCount = calledOids.size();
        } else {
            summonsCount = 0;
        }

        return Math.min(summonsSize, skillLimit - summonsCount);
    }

    public void addSummonedMob(MapleMonster mob) {
        Set<Integer> calledOids = this.calledMobOids;
        if (calledOids == null) {
            calledOids = Collections.synchronizedSet(new HashSet<Integer>());
            this.calledMobOids = calledOids;
        }

        calledOids.add(mob.getObjectId());
        mob.setSummonerMob(this);
    }

    private void removeSummonedMob(int mobOid) {
        Set<Integer> calledOids = this.calledMobOids;
        if (calledOids != null) {
            calledOids.remove(mobOid);
        }
    }

    private void setSummonerMob(MapleMonster mob) {
        this.callerMob = new WeakReference<>(mob);
    }

    private void dispatchClearSummons() {
        MapleMonster caller = this.callerMob.get();
        if (caller != null) {
            caller.removeSummonedMob(this.getObjectId());
        }

        this.calledMobOids = null;
    }

    public void pushRemoveAfterAction(Runnable run) {
        this.removeAfterAction = run;
    }

    public Runnable popRemoveAfterAction() {
        Runnable r = this.removeAfterAction;
        this.removeAfterAction = null;

        return r;
    }

    public long getHp() {
        return hp.get();
    }

    public synchronized void addHp(long hp) {
        if (this.hp.get() <= 0) {
            return;
        }
        this.hp.addAndGet(hp);
    }

    public synchronized void setStartingHp(long hp) {
        stats.setHp(hp);    // refactored mob stats after non-static HP pool suggestion thanks to twigs
        this.hp.set(hp);
    }

    public long getMaxHp() {
        return stats.getHp();
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        if (mp < 0) {
            mp = 0;
        }
        this.mp = mp;
    }

    public int getMaxMp() {
        return stats.getMp();
    }

    public long getExp() {
        return stats.getExp();
    }

    public int getLevel() {
        return stats.getLevel();
    }

    public int getCP() {
        return stats.getCP();
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getVenomMulti() {
        return this.VenomMultiplier;
    }

    public void setVenomMulti(int multiplier) {
        this.VenomMultiplier = multiplier;
    }

    public MapleMonsterStats getStats() {
        return stats;
    }

    public boolean isBoss() {
        return stats.isBoss();
    }

    public boolean isSuperBoss() {
        return stats.isSuperBoss();
    }

    public int getAnimationTime(String name) {
        return stats.getAnimationTime(name);
    }

    private List<Integer> getRevives() {
        return stats.getRevives();
    }

    private byte getTagColor() {
        return stats.getTagColor();
    }

    private byte getTagBgColor() {
        return stats.getTagBgColor();
    }

    public void setHpZero() {     // force HP = 0
        applyAndGetHpDamage(Long.MAX_VALUE, false);
    }

    private boolean applyAnimationIfRoaming(int attackPos, MobSkill skill) {   // roam: not casting attack or skill animations
        if (!animationLock.tryLock()) {
            return false;
        }

        try {
            long animationTime;

            if (skill == null) {
                animationTime = MapleMonsterInformationProvider.getInstance().getMobAttackAnimationTime(this.getId(), attackPos);
            } else {
                animationTime = MapleMonsterInformationProvider.getInstance().getMobSkillAnimationTime(skill);
            }

            if (animationTime > 0) {
                return map.getChannelServer().registerMobOnAnimationEffect(map.getId(), this.hashCode(), animationTime);
            } else {
                return true;
            }
        } finally {
            animationLock.unlock();
        }
    }

    public synchronized Long applyAndGetHpDamage(long delta, boolean stayAlive) {
        long curHp = hp.get();
        if (curHp <= 0) {       // this monster is already dead
            return null;
        }

        if (delta >= 0) {
            if (stayAlive) {
                curHp--;
            }
            long trueDamage = Math.min(curHp, delta);

            hp.addAndGet(-trueDamage);
            return trueDamage;
        } else {
            long trueHeal = -delta;
            long hp2Heal = (curHp + trueHeal);
            long maxHp = getMaxHp();

            if (hp2Heal > maxHp) {
                trueHeal -= (hp2Heal - maxHp);
            }
            if (trueHeal > Integer.MAX_VALUE) {
                trueHeal = Integer.MAX_VALUE;
            }
            hp.addAndGet(trueHeal);
            return trueHeal;
        }
    }

    public synchronized void disposeMapObject() {     // mob is no longer associated with the map it was in
        hp.set(-1);
    }

    public void broadcastMobHpBar(MapleCharacter from) {
        if (hasBossHPBar()) {
            from.setPlayerAggro(this.hashCode());
            from.getMap().broadcastBossHpMessage(this, this.hashCode(), makeBossHPBarPacket(), getPosition());
        } else {
            int remainingHP = (int) Math.max(1, hp.get() * 100f / getMaxHp());
            byte[] packet = MaplePacketCreator.showMonsterHP(getObjectId(), remainingHP);
            from.announce(packet);
        }
    }

    public boolean damage(MapleCharacter attacker, long damage, boolean stayAlive) {
        boolean lastHit = false;

        this.lockMonster();
        try {
            if (!this.isAlive()) {
                return false;
            }
            if (damage > 0) {
                this.applyDamage(attacker, damage, stayAlive, false);
                if (!this.isAlive()) {  // monster just died
                    lastHit = true;
                }
            }
        } finally {
            this.unlockMonster();
        }

        return lastHit;
    }

    /**
     *
     * @param from the player that dealt the damage
     * @param damage
     * @param stayAlive
     */
    private void applyDamage(MapleCharacter from, long damage, boolean stayAlive, boolean fake) {
        Long trueDamage = applyAndGetHpDamage(damage, stayAlive);
        //from.dropMessage(5, "Total Damage: " + damage);
        //from.dropMessage(5, "True Damage: " + trueDamage);

        if (ServerConstants.USE_DEBUG) {
            from.dropMessage(5, "Hitted MOB " + this.getId() + ", OID " + this.getObjectId());
        }

        if (!fake) {
            dispatchMonsterDamaged(from, trueDamage);
        }

        if (!takenDamage.containsKey(from.getId())) {
            takenDamage.put(from.getId(), new AtomicLong(trueDamage));
        } else {
            takenDamage.get(from.getId()).addAndGet(trueDamage);
        }
        broadcastMobHpBar(from);
    }

    public void applyFakeDamage(MapleCharacter from, long damage, boolean stayAlive) {
        applyDamage(from, damage, stayAlive, true);
    }

    public void heal(int hp, int mp) {
        Long hpHealed = applyAndGetHpDamage(-hp, false);
        if (hpHealed == null) {
            return;
        }

        int mp2Heal = getMp() + mp;
        int maxMp = getMaxMp();
        if (mp2Heal >= maxMp) {
            mp2Heal = maxMp;
        }
        setMp(mp2Heal);

        if (hp > 0) {
            getMap().broadcastMessage(MaplePacketCreator.healMonster(getObjectId(), hp, (int) getHp(), getMaxHp()));
        }

        maxHpPlusHeal.addAndGet(hpHealed);
        dispatchMonsterHealed(hpHealed);
    }

    public boolean isAttackedBy(MapleCharacter chr) {
        return takenDamage.containsKey(chr.getId());
    }

    public boolean bannedMob(int id) {
        switch (id) {
            case 8510100:
            case 8510200:
            case 8510300:
                return true;
            default:
                return false;
        }
    }

    private void distributeExperience(MapleCharacter killer) {
        if (isAlive() || killer.getTested()) {
            return;
        }
        killer.setBattleCooldown(System.currentTimeMillis() + 60000);
        if (killer.getDrops()) {
            int amount = (int) (this.getLevel() * 50 * (1 + killer.mesoR));
            killer.gainMeso(amount, true, false);
        } 
        double chance = (double) killer.getTotalLevel() / (double) this.getLevel();
        if (Randomizer.nextInt((int) (2500 * chance)) == 0) {
            killer.addStamina(5);
        }
        //int power = this.getStats().getPower();
        double bonusExp = (double) this.getLevel() / (double) killer.getTotalLevel();
        long participationExp = (long) (this.getStats().getExp() * bonusExp * killer.expBonus() * getStatusExpMultiplier(killer) * killer.getExpRate());

        if (killer.isAlive()) {
            killer.gainExp((long) (participationExp), true, false, true);
            Collection<MapleCharacter> characters1 = killer.getMap().getAllPlayers();
            for (MapleCharacter victim : characters1) {
                if (victim.isAlive()) {
                victim.gainExp((long) (participationExp * 0.25), true, false, true);
                }
            }
        }
        /* if (killer.getParty() != null) {
            for (MapleCharacter player : killer.getPartyMembersOnSameMap()) {
                if (player.isAlive() && player != killer) {
                    //double bonusExp2 = (double) this.getLevel() / (double) player.getTotalLevel();
                    long participationExp2 = (long) (this.getStats().getExp() * bonusExp * killer.expBonus() * getStatusExpMultiplier(killer) * killer.getExpRate());
                    player.gainExp((long) (participationExp2 * 0.25), true, false, false);
                }
            }
        } */

        EventInstanceManager eim = getMap().getEventInstance();
        if (eim != null) {
            eim.monsterKilled(killer, this);
        }

    }
    
    public float getStatusExpMultiplier(MapleCharacter attacker) {
        float multiplier = 1.0f;

        // thanks Prophecy & Aika for finding out Holy Symbol not being applied on party bonuses
        Integer holySymbol = attacker.getBuffedValue(MapleBuffStat.HOLY_SYMBOL);
        if (holySymbol != null) {
            multiplier *= (1.0 + (holySymbol.doubleValue() / 100.0));
        }

        statiLock.lock();
        try {
            MonsterStatusEffect mse = stati.get(MonsterStatus.SHOWDOWN);
            if (mse != null) {
                multiplier *= (1.0 + (mse.getStati().get(MonsterStatus.SHOWDOWN).doubleValue() / 100.0));
            }
        } finally {
            statiLock.unlock();
        }
        return multiplier;
    }

    private static long expValueToInteger(double exp) {
        if (exp < Long.MIN_VALUE) {
            exp = Long.MIN_VALUE;
        }

        return (long) exp;
    }

    public void giveExpToCharacter(MapleCharacter attacker, Float personalExp, boolean white) {
        System.out.println("give exp1: " + personalExp);
        if (attacker.isAlive()) {
            if (personalExp != null) {
                personalExp *= getStatusExpMultiplier(attacker);
            } else {
                personalExp = 0.0f;
            }

            Integer expBonus = attacker.getBuffedValue(MapleBuffStat.EXP_INCREASE);
            if (expBonus != null) {     // exp increase player buff found thanks to HighKey21
                personalExp += expBonus;
            }
            long _personalExp = expValueToInteger(personalExp); // assuming no negative xp here

            attacker.gainExp(_personalExp, 0, true, false, white);
            System.out.println("give exp2: " + _personalExp);
            //attacker.increaseEquipExp(_personalExp);
        }
    }

    public List<MonsterDropEntry> retrieveRelevantDrops() {
        Map<Integer, MapleCharacter> pchars = map.getMapAllPlayers();

        List<MapleCharacter> lootChars = new LinkedList<>();
        for (Integer cid : takenDamage.keySet()) {
            MapleCharacter chr = pchars.get(cid);
            if (chr != null && chr.isLoggedinWorld()) {
                lootChars.add(chr);
            }
        }

        return MapleLootManager.retrieveRelevantDrops(this.getId(), lootChars);
    }

    public MapleCharacter killBy(final MapleCharacter killer) {
        if (killer != null) {
            if (killer.getParty() != null || killer.getRaid() != null) {
                for (MapleCharacter player : killer.allMembersonSameMap()) {
                    player.updateQuestMobCount(getId());
                }
            } else {
                killer.updateQuestMobCount(getId());
            }
            distributeExperience(killer);
        }

        final Pair<MapleCharacter, Boolean> lastController = aggroRemoveController();
        final List<Integer> toSpawn = this.getRevives();
        if (toSpawn != null) {
            final MapleMap reviveMap = map;
            Pair<Integer, String> timeMob = reviveMap.getTimeMob();
            if (timeMob != null) {
                if (toSpawn.contains(timeMob.getLeft())) {
                    reviveMap.broadcastMessage(MaplePacketCreator.serverNotice(6, timeMob.getRight()));
                }
            }

            if (toSpawn.size() > 0) {
                final EventInstanceManager eim = this.getMap().getEventInstance();

                TimerManager.getInstance().schedule(new Runnable() {

                    @Override
                    public void run() {
                        MapleCharacter controller = lastController.getLeft();
                        boolean aggro = lastController.getRight();

                        for (Integer mid : toSpawn) {
                            if (mid == 9300216) {
                                continue;
                            }
                            final MapleMonster mob = MapleLifeFactory.getMonster(mid, MapleMonster.this.getMap().getChannelServer().getId());
                            mob.setPosition(getPosition());
                            mob.setFh(getFh());
                            mob.setParentMobOid(getObjectId());

                            if (dropsDisabled()) {
                                mob.disableDrops();
                            }
                            reviveMap.spawnMonster(mob);

                            if (mob.getId() >= 8810010 && mob.getId() <= 8810017 && reviveMap.isHorntailDefeated()) {
                                boolean htKilled = false;
                                MapleMonster ht = reviveMap.getMonsterById(8810018);

                                if (ht != null) {
                                    ht.lockMonster();
                                    try {
                                        htKilled = ht.isAlive();
                                        ht.setHpZero();
                                    } finally {
                                        ht.unlockMonster();
                                    }

                                    if (htKilled) {
                                        reviveMap.killMonster(ht, killer, true);
                                        ht.broadcastMobHpBar(killer);
                                    }
                                }

                                for (int i = 8810017; i >= 8810010; i--) {
                                    reviveMap.killMonster(reviveMap.getMonsterById(i), killer, true);
                                }
                            } else if (controller != null) {
                                if (mob.isAlive()) {
                                    mob.aggroSwitchController(controller, aggro);
                                }
                            }

                            if (eim != null) {
                                if(mob != null) {
                                eim.reviveMonster(mob);
                                }
                            }
                        }
                    }
                }, getAnimationTime("die1"));
            }
        } else {  // is this even necessary?
            System.out.println("[CRITICAL LOSS] toSpawn is null for " + this.getName());
        }

        MapleCharacter looter = map.getCharacterById(getHighestDamagerId());

        return looter != null ? looter : killer;
    }

    private void dispatchUpdateQuestMobCount() {
        Set<Integer> attackerChrids = takenDamage.keySet();
        if (!attackerChrids.isEmpty()) {
            Map<Integer, MapleCharacter> mapChars = map.getMapPlayers();
            if (!mapChars.isEmpty()) {
                int mobid = getId();

                for (Integer chrid : attackerChrids) {
                    MapleCharacter chr = mapChars.get(chrid);

                    if (chr != null && chr.isLoggedinWorld()) {
                        chr.updateQuestMobCount(mobid);
                    }
                }
            }
        }
    }

    public void dispatchMonsterKilled(boolean hasKiller) {
        processMonsterKilled(hasKiller);

        EventInstanceManager eim = getMap().getEventInstance();
        if (eim != null) {
            if (!this.getStats().isFriendly()) {
                if (getId() > 3) {
                    eim.monsterKilled(this, hasKiller);
                }
            } else {
                eim.friendlyKilled(this, hasKiller);
            }
            eim.unregisterMonster(this);
        }
    }

    private synchronized void processMonsterKilled(boolean hasKiller) {
        if (!hasKiller) {    // players won't gain EXP from a mob that has no killer, but a quest count they should
            dispatchUpdateQuestMobCount();
        }

        this.aggroClearDamages();
        this.dispatchClearSummons();

        MonsterListener[] listenersList;
        statiLock.lock();
        try {
            listenersList = listeners.toArray(new MonsterListener[listeners.size()]);
        } finally {
            statiLock.unlock();
        }

        for (MonsterListener listener : listenersList) {
            listener.monsterKilled(getAnimationTime("die1"));
        }

        statiLock.lock();
        try {
            stati.clear();
            alreadyBuffed.clear();
            listeners.clear();
        } finally {
            statiLock.unlock();
        }
    }

    private void dispatchMonsterDamaged(MapleCharacter from, long trueDmg) {
        MonsterListener[] listenersList;
        statiLock.lock();
        try {
            listenersList = listeners.toArray(new MonsterListener[listeners.size()]);
        } finally {
            statiLock.unlock();
        }

        for (MonsterListener listener : listenersList) {
            listener.monsterDamaged(from, trueDmg);
        }
    }

    private void dispatchMonsterHealed(long trueHeal) {
        MonsterListener[] listenersList;
        statiLock.lock();
        try {
            listenersList = listeners.toArray(new MonsterListener[listeners.size()]);
        } finally {
            statiLock.unlock();
        }

        for (MonsterListener listener : listenersList) {
            listener.monsterHealed(trueHeal);
        }
    }

    public int getHighestDamagerId() {
        int curId = 0;
        long curDmg = 0;

        for (Entry<Integer, AtomicLong> damage : takenDamage.entrySet()) {
            curId = damage.getValue().get() >= curDmg ? damage.getKey() : curId;
            curDmg = damage.getKey() == curId ? damage.getValue().get() : curDmg;
        }

        return curId;
    }

    public boolean isAlive() {
        return this.hp.get() > 0;
    }

    public void addListener(MonsterListener listener) {
        statiLock.lock();
        try {
            listeners.add(listener);
        } finally {
            statiLock.unlock();
        }
    }

    public MapleCharacter getController() {
        return controller.get();
    }

    public void removeController() {
        controller.clear();
    }

    private void setController(MapleCharacter controller) {
        this.controller = new WeakReference<>(controller);
    }

    public boolean isControllerHasAggro() {
        return fake ? false : controllerHasAggro;
    }

    private void setControllerHasAggro(boolean controllerHasAggro) {
        if (!fake) {
            this.controllerHasAggro = controllerHasAggro;
        }
    }

    public boolean isControllerKnowsAboutAggro() {
        return fake ? false : controllerKnowsAboutAggro;
    }

    private void setControllerKnowsAboutAggro(boolean controllerKnowsAboutAggro) {
        if (!fake) {
            this.controllerKnowsAboutAggro = controllerKnowsAboutAggro;
        }
    }

    private void setControllerHasPuppet(boolean controllerHasPuppet) {
        this.controllerHasPuppet = controllerHasPuppet;
    }

    public byte[] makeBossHPBarPacket() {
        return MaplePacketCreator.showBossHP(getId(), getHp(), getMaxHp(), getTagColor(), getTagBgColor());
    }

    public byte[] makeCustomBossHPBarPacket(byte color, byte bgcolor) {
        return MaplePacketCreator.showBossHP(getId(), getHp(), getMaxHp(), color, bgcolor);
    }

    public boolean hasBossHPBar() {
        return isBoss() && getTagColor() > 0;
    }

    public void announceMonsterStatus(MapleClient client) {
        statiLock.lock();
        try {
            if (stati.size() > 0) {
                for (final MonsterStatusEffect mse : this.stati.values()) {
                    client.announce(MaplePacketCreator.applyMonsterStatus(getObjectId(), mse, null));
                }
            }
        } finally {
            statiLock.unlock();
        }
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        if (hp.get() <= 0) { // mustn't monsterLock this function
            return;
        }
        if (fake) {
            client.announce(MaplePacketCreator.spawnFakeMonster(this, 0));
        } else {
            client.announce(MaplePacketCreator.spawnMonster(this, false));
        }

        announceMonsterStatus(client);

        if (hasBossHPBar()) {
            client.announceBossHpBar(this, this.hashCode(), makeBossHPBarPacket());
        }
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        client.announce(MaplePacketCreator.killMonster(getObjectId(), false));
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.MONSTER;
    }

    public boolean isMobile() {
        return stats.isMobile();
    }

    @Override
    public boolean isFacingLeft() {
        int fixedStance = stats.getFixedStance();    // thanks DimDiDima for noticing inconsistency on some AOE mobskills
        if (fixedStance != 0) {
            return Math.abs(fixedStance) % 2 == 1;
        }

        return super.isFacingLeft();
    }

    public ElementalEffectiveness getElementalEffectiveness(Element e) {
        statiLock.lock();
        try {
            if (stati.get(MonsterStatus.DOOM) != null) {
                return ElementalEffectiveness.NORMAL; // like blue snails
            }
        } finally {
            statiLock.unlock();
        }

        return getMonsterEffectiveness(e);
    }

    private ElementalEffectiveness getMonsterEffectiveness(Element e) {
        monsterLock.lock();
        try {
            return stats.getEffectiveness(e);
        } finally {
            monsterLock.unlock();
        }
    }

    private MapleCharacter getActiveController() {
        MapleCharacter chr = getController();

        if (chr != null && chr.isLoggedinWorld() && chr.getMap() == this.getMap()) {
            return chr;
        } else {
            return null;
        }
    }

    private void broadcastMonsterStatusMessage(byte[] packet) {
        map.broadcastMessage(packet, getPosition());

        MapleCharacter chrController = getActiveController();
        if (chrController != null && !chrController.isMapObjectVisible(MapleMonster.this)) {
            chrController.announce(packet);
        }
    }

    private int broadcastStatusEffect(final MonsterStatusEffect status) {
        int animationTime = status.getSkill().getAnimationTime();
        byte[] packet = MaplePacketCreator.applyMonsterStatus(getObjectId(), status, null);
        broadcastMonsterStatusMessage(packet);

        return animationTime;
    }

    public boolean applyStatus(MapleCharacter from, final MonsterStatusEffect status, boolean poison, long duration) {
        return applyStatus(from, status, poison, duration, false);
    }

    public boolean applyStatus(MapleCharacter from, final MonsterStatusEffect status, boolean poison, long duration, boolean venom) {
        switch (getMonsterEffectiveness(status.getSkill().getElement())) {
            case IMMUNE:
            case STRONG:
            case NEUTRAL:
                return false;
            case NORMAL:
            case WEAK:
                break;
            default: {
                System.out.println("Unknown elemental effectiveness: " + getMonsterEffectiveness(status.getSkill().getElement()));
                return false;
            }
        }

        if (status.getSkill().getId() == FPMage.ELEMENT_COMPOSITION) { // fp compo
            ElementalEffectiveness effectiveness = getMonsterEffectiveness(Element.POISON);
            if (effectiveness == ElementalEffectiveness.IMMUNE || effectiveness == ElementalEffectiveness.STRONG) {
                return false;
            }
        } else if (status.getSkill().getId() == ILMage.ELEMENT_COMPOSITION) { // il compo
            ElementalEffectiveness effectiveness = getMonsterEffectiveness(Element.ICE);
            if (effectiveness == ElementalEffectiveness.IMMUNE || effectiveness == ElementalEffectiveness.STRONG) {
                return false;
            }
        } else if (status.getSkill().getId() == NightLord.VENOMOUS_STAR || status.getSkill().getId() == Shadower.VENOMOUS_STAB || status.getSkill().getId() == NightWalker.VENOM) {// venom
            if (getMonsterEffectiveness(Element.POISON) == ElementalEffectiveness.WEAK) {
                return false;
            }
        }
        if (poison && hp.get() <= 1) {
            return false;
        }

        final Map<MonsterStatus, Integer> statis = status.getStati();
        if (!statis.containsKey(MonsterStatus.SHOWDOWN) && stats.isBoss()) {
            if (!(statis.containsKey(MonsterStatus.SPEED)
                    && statis.containsKey(MonsterStatus.NINJA_AMBUSH)
                    && statis.containsKey(MonsterStatus.WATK))) {
                return false;
            }
        }

        final Channel ch = map.getChannelServer();
        final int mapid = map.getId();
        if (statis.size() > 0) {
            statiLock.lock();
            try {
                for (MonsterStatus stat : statis.keySet()) {
                    final MonsterStatusEffect oldEffect = stati.get(stat);
                    if (oldEffect != null) {
                        oldEffect.removeActiveStatus(stat);
                        if (oldEffect.getStati().isEmpty()) {
                            ch.interruptMobStatus(mapid, oldEffect);
                        }
                    }
                }
            } finally {
                statiLock.unlock();
            }
        }

        final Runnable cancelTask = new Runnable() {

            @Override
            public void run() {
                if (isAlive()) {
                    byte[] packet = MaplePacketCreator.cancelMonsterStatus(getObjectId(), status.getStati());
                    broadcastMonsterStatusMessage(packet);
                }

                statiLock.lock();
                try {
                    for (MonsterStatus stat : status.getStati().keySet()) {
                        stati.remove(stat);
                    }
                } finally {
                    statiLock.unlock();
                }

                setVenomMulti(0);
            }
        };

        Runnable overtimeAction = null;
        int overtimeDelay = -1;

        int animationTime;
        if (poison) {
            int poisonLevel = from.getSkillLevel(status.getSkill());
            int poisonDamage = Math.min(Short.MAX_VALUE, (int) (getMaxHp() / (70.0 - poisonLevel) + 0.999));
            status.setValue(MonsterStatus.POISON, Integer.valueOf(poisonDamage));
            animationTime = broadcastStatusEffect(status);

            overtimeAction = new DamageTask(poisonDamage, from, status, 0);
            overtimeDelay = 1000;
        } else if (venom) {
            if (from.getJob() == MapleJob.NIGHTLORD || from.getJob() == MapleJob.SHADOWER || from.getJob().isA(MapleJob.NIGHTWALKER3)) {
                int poisonLevel, matk, jobid = from.getJob().getId();
                int skillid = (jobid == 412 ? NightLord.VENOMOUS_STAR : (jobid == 422 ? Shadower.VENOMOUS_STAB : NightWalker.VENOM));
                poisonLevel = from.getSkillLevel(SkillFactory.getSkill(skillid));
                if (poisonLevel <= 0) {
                    return false;
                }
                matk = SkillFactory.getSkill(skillid).getEffect(poisonLevel).getMatk();
                int luk = from.getLuk();
                int maxDmg = (int) Math.ceil(Math.min(Short.MAX_VALUE, 0.2 * luk * matk));
                int minDmg = (int) Math.ceil(Math.min(Short.MAX_VALUE, 0.1 * luk * matk));
                int gap = maxDmg - minDmg;
                if (gap == 0) {
                    gap = 1;
                }
                int poisonDamage = 0;
                for (int i = 0; i < getVenomMulti(); i++) {
                    poisonDamage += (Randomizer.nextInt(gap) + minDmg);
                }
                poisonDamage = Math.min(Short.MAX_VALUE, poisonDamage);
                status.setValue(MonsterStatus.VENOMOUS_WEAPON, Integer.valueOf(poisonDamage));
                status.setValue(MonsterStatus.POISON, Integer.valueOf(poisonDamage));
                animationTime = broadcastStatusEffect(status);

                overtimeAction = new DamageTask(poisonDamage, from, status, 0);
                overtimeDelay = 1000;
            } else {
                return false;
            }
            /*
            } else if (status.getSkill().getId() == Hermit.SHADOW_WEB || status.getSkill().getId() == NightWalker.SHADOW_WEB) { //Shadow Web
            int webDamage = (int) (getMaxHp() / 50.0 + 0.999);
            status.setValue(MonsterStatus.SHADOW_WEB, Integer.valueOf(webDamage));
            animationTime = broadcastStatusEffect(status);
            
            overtimeAction = new DamageTask(webDamage, from, status, 1);
            overtimeDelay = 3500;
             */
        } else if (status.getSkill().getId() == 4121004 || status.getSkill().getId() == 4221004) { // Ninja Ambush
            final Skill skill = SkillFactory.getSkill(status.getSkill().getId());
            final int level = from.getSkillLevel(skill);
            final int damage = (int) ((from.getStr() + from.getLuk()) * ((3.7 * skill.getEffect(level).getDamage()) / 100));

            status.setValue(MonsterStatus.NINJA_AMBUSH, Integer.valueOf(damage));
            animationTime = broadcastStatusEffect(status);

            overtimeAction = new DamageTask(damage, from, status, 2);
            overtimeDelay = 1000;
        } else {
            animationTime = broadcastStatusEffect(status);
        }

        statiLock.lock();
        try {
            for (MonsterStatus stat : status.getStati().keySet()) {
                stati.put(stat, status);
                alreadyBuffed.add(stat);
            }
        } finally {
            statiLock.unlock();
        }

        ch.registerMobStatus(mapid, status, cancelTask, duration + animationTime - 100, overtimeAction, overtimeDelay);
        return true;
    }

    public final void dispelSkill(final MobSkill skillId) {
        List<MonsterStatus> toCancel = new ArrayList<MonsterStatus>();
        for (Entry<MonsterStatus, MonsterStatusEffect> effects : stati.entrySet()) {
            MonsterStatusEffect mse = effects.getValue();
            if (mse.getMobSkill() != null && mse.getMobSkill().getSkillId() == skillId.getSkillId()) { //not checking for level.
                toCancel.add(effects.getKey());
            }
        }
        for (MonsterStatus stat : toCancel) {
            debuffMobStat(stat);
        }
    }

    public void applyMonsterBuff(final Map<MonsterStatus, Integer> stats, final int x, int skillId, long duration, MobSkill skill, final List<Integer> reflection) {
        final Runnable cancelTask = new Runnable() {

            @Override
            public void run() {
                if (isAlive()) {
                    byte[] packet = MaplePacketCreator.cancelMonsterStatus(getObjectId(), stats);
                    broadcastMonsterStatusMessage(packet);

                    statiLock.lock();
                    try {
                        for (final MonsterStatus stat : stats.keySet()) {
                            stati.remove(stat);
                        }
                    } finally {
                        statiLock.unlock();
                    }
                }
            }
        };
        final MonsterStatusEffect effect = new MonsterStatusEffect(stats, null, skill, true);
        byte[] packet = MaplePacketCreator.applyMonsterStatus(getObjectId(), effect, reflection);
        broadcastMonsterStatusMessage(packet);

        statiLock.lock();
        try {
            for (MonsterStatus stat : stats.keySet()) {
                stati.put(stat, effect);
                alreadyBuffed.add(stat);
            }
        } finally {
            statiLock.unlock();
        }

        map.getChannelServer().registerMobStatus(map.getId(), effect, cancelTask, duration);
    }

    public void refreshMobPosition() {
        resetMobPosition(getPosition());
    }

    public void resetMobPosition(Point newPoint) {
        aggroRemoveController();

        setPosition(newPoint);
        map.broadcastMessage(MaplePacketCreator.moveMonster(this.getObjectId(), false, -1, 0, 0, 0, this.getPosition(), this.getIdleMovement(), getIdleMovementDataLength()));
        map.moveMonster(this, this.getPosition());

        aggroUpdateController();
    }

    private void debuffMobStat(MonsterStatus stat) {
        MonsterStatusEffect oldEffect;
        statiLock.lock();
        try {
            oldEffect = stati.remove(stat);
        } finally {
            statiLock.unlock();
        }

        if (oldEffect != null) {
            byte[] packet = MaplePacketCreator.cancelMonsterStatus(getObjectId(), oldEffect.getStati());
            broadcastMonsterStatusMessage(packet);
        }
    }

    public void debuffMob(int skillid) {
        MonsterStatus[] statups = {MonsterStatus.WEAPON_ATTACK_UP, MonsterStatus.WEAPON_DEFENSE_UP, MonsterStatus.MAGIC_ATTACK_UP, MonsterStatus.MAGIC_DEFENSE_UP, MonsterStatus.MAGIC_REFLECT, MonsterStatus.WEAPON_REFLECT, MonsterStatus.MAGIC_IMMUNITY, MonsterStatus.WEAPON_IMMUNITY};
        statiLock.lock();
        try {
            if (skillid == Hermit.SHADOW_MESO) {
                debuffMobStat(statups[1]);
                debuffMobStat(statups[3]);
            } else if (skillid == Priest.DISPEL) {
                for (MonsterStatus ms : statups) {
                    debuffMobStat(ms);
                }
            } else {    // is a crash skill
                int i = (skillid == Crusader.ARMOR_CRASH ? 1 : (skillid == WhiteKnight.MAGIC_CRASH ? 2 : 0));
                debuffMobStat(statups[i]);
                if (skillid == Crusader.ARMOR_CRASH || skillid == WhiteKnight.MAGIC_CRASH || skillid == DragonKnight.POWER_CRASH) {
                    if (!isBuffed(MonsterStatus.MAGIC_REFLECT)) {
                        debuffMobStat(MonsterStatus.MAGIC_IMMUNITY);
                    }
                    if (!isBuffed(MonsterStatus.WEAPON_REFLECT)) {
                        debuffMobStat(MonsterStatus.WEAPON_IMMUNITY);
                    }
                }
            }
        } finally {
            statiLock.unlock();
        }
    }

    public boolean isBuffed(MonsterStatus status) {
        statiLock.lock();
        try {
            return stati.containsKey(status);
        } finally {
            statiLock.unlock();
        }
    }

    public void setFake(boolean fake) {
        monsterLock.lock();
        try {
            this.fake = fake;
        } finally {
            monsterLock.unlock();
        }
    }

    public boolean isFake() {
        monsterLock.lock();
        try {
            return fake;
        } finally {
            monsterLock.unlock();
        }
    }

    public MapleMap getMap() {
        return map;
    }

    public int getChannel() {
        return this.getMap().getChannelServer().getId();
    }

    public MapleMonsterAggroCoordinator getMapAggroCoordinator() {
        return map.getAggroCoordinator();
    }

    public List<Pair<Integer, Integer>> getSkills() {
        return stats.getSkills();
    }

    public boolean hasSkill(int skillId, int level) {
        return stats.hasSkill(skillId, level);
    }

    public int getSkillPos(int skillId, int level) {
        int pos = 0;
        for (Pair<Integer, Integer> ms : this.getSkills()) {
            if (ms.getLeft() == skillId && ms.getRight() == level) {
                return pos;
            }

            pos++;
        }

        return -1;
    }

    public boolean canUseSkill(MobSkill toUse, boolean apply) {
        if (toUse == null) {
            return false;
        }

        int useSkillid = toUse.getSkillId();
        if (useSkillid >= 143 && useSkillid <= 145) {
            if (this.isBuffed(MonsterStatus.WEAPON_REFLECT) || this.isBuffed(MonsterStatus.MAGIC_REFLECT)) {
                return false;
            }
        }

        monsterLock.lock();
        try {
            for (Pair<Integer, Integer> skill : usedSkills) {   // thanks OishiiKawaiiDesu for noticing an issue with mobskill cooldown
                if (skill.getLeft() == useSkillid && skill.getRight() == toUse.getSkillLevel()) {
                    return false;
                }
            }

            int mpCon = toUse.getMpCon();
            if (mp < mpCon) {
                return false;
            }
            if (apply) {
                this.usedSkill(toUse);
            }
        } finally {
            monsterLock.unlock();
        }

        return true;
    }

    private void usedSkill(MobSkill skill) {
        final int skillId = skill.getSkillId(), level = skill.getSkillLevel();
        long cooltime = skill.getCoolTime();

        monsterLock.lock();
        try {
            mp -= skill.getMpCon();

            Pair<Integer, Integer> skillKey = new Pair<>(skillId, level);
            this.usedSkills.add(skillKey);

            Integer useCount = this.skillsUsed.remove(skillKey);
            if (useCount != null) {
                this.skillsUsed.put(skillKey, useCount + 1);
            } else {
                this.skillsUsed.put(skillKey, 1);
            }
        } finally {
            monsterLock.unlock();
        }

        //final MapleMonster mons = this;
        //MapleMap mmap = mons.getMap();
        Runnable r = new Runnable() {

            @Override
            public void run() {
                clearSkill(skillId, level);
            }
        };

        //mmap.getChannelServer().registerMobClearSkillAction(mmap.getId(), r, cooltime);
        this.getMap().getChannelServer().registerMobClearSkillAction(this.getMap().getId(), r, 10000);
    }

    private void clearSkill(int skillId, int level) {
        int index = -1;
        for (Pair<Integer, Integer> skill : usedSkills) {
            if (skill.getLeft() == skillId && skill.getRight() == level) {
                index = usedSkills.indexOf(skill);
                break;
            }
        }
        if (index != -1) {
            usedSkills.remove(index);
        }
    }

    public int canUseAttack(int attackPos, boolean isSkill) {
        monsterLock.lock();
        try {
            /*
            if (usedAttacks.contains(attackPos)) {
            return -1;
            }
             */

            Pair<Integer, Integer> attackInfo = MapleMonsterInformationProvider.getInstance().getMobAttackInfo(this.getId(), attackPos);
            if (attackInfo == null) {
                return -1;
            }

            int mpCon = attackInfo.getLeft();
            if (mp < mpCon) {
                return -1;
            }

            /*
            if (!this.applyAnimationIfRoaming(attackPos, null)) {
            return -1;
            }
             */
            usedAttack(attackPos, mpCon, attackInfo.getRight());
            return 1;
        } finally {
            monsterLock.unlock();
        }
    }

    private void usedAttack(final int attackPos, int mpCon, int cooltime) {
        monsterLock.lock();
        try {
            mp -= mpCon;
            usedAttacks.add(attackPos);

            final MapleMonster mons = this;
            MapleMap mmap = mons.getMap();
            Runnable r = new Runnable() {

                @Override
                public void run() {
                    mons.clearAttack(attackPos);
                }
            };

            mmap.getChannelServer().registerMobClearSkillAction(mmap.getId(), r, cooltime);
        } finally {
            monsterLock.unlock();
        }
    }

    private void clearAttack(int attackPos) {
        monsterLock.lock();
        try {
            usedAttacks.remove(attackPos);
        } finally {
            monsterLock.unlock();
        }
    }

    public int getNoSkills() {
        return this.stats.getNoSkills();
    }

    public boolean isFirstAttack() {
        return this.stats.isFirstAttack();
    }

    public int getBuffToGive() {
        return this.stats.getBuffToGive();
    }

    private final class DamageTask implements Runnable {

        private final int dealDamage;
        private final MapleCharacter chr;
        private final MonsterStatusEffect status;
        private final int type;
        private final MapleMap map;

        private DamageTask(int dealDamage, MapleCharacter chr, MonsterStatusEffect status, int type) {
            this.dealDamage = dealDamage;
            this.chr = chr;
            this.status = status;
            this.type = type;
            this.map = chr.getMap();
        }

        @Override
        public void run() {
            long curHp = hp.get();
            if (curHp <= 1) {
                map.getChannelServer().interruptMobStatus(map.getId(), status);
                return;
            }

            long damage = dealDamage;
            if (damage >= curHp) {
                damage = curHp - 1;
                if (type == 1 || type == 2) {
                    map.getChannelServer().interruptMobStatus(map.getId(), status);
                }
            }
            if (damage > Integer.MAX_VALUE) {
                damage = Integer.MAX_VALUE;
            }
            if (damage > 0) {
                lockMonster();
                try {
                    applyDamage(chr, damage, true, false);
                } finally {
                    unlockMonster();
                }

                if (type == 1) {
                    map.broadcastMessage(MaplePacketCreator.damageMonster(getObjectId(), (int) damage), getPosition());
                } else if (type == 2) {
                    if (damage < dealDamage) {    // ninja ambush (type 2) is already displaying DOT to the caster
                        map.broadcastMessage(MaplePacketCreator.damageMonster(getObjectId(), (int) damage), getPosition());
                    }
                }
            }
        }
    }

    public String getName() {
        return stats.getName();
    }

    public void addStolen(int itemId) {
        stolenItems.add(itemId);
    }

    public List<Integer> getStolen() {
        return stolenItems;
    }

    public void setTempEffectiveness(Element e, ElementalEffectiveness ee, long milli) {
        monsterLock.lock();
        try {
            final Element fE = e;
            final ElementalEffectiveness fEE = stats.getEffectiveness(e);
            if (!fEE.equals(ElementalEffectiveness.WEAK)) {
                stats.setEffectiveness(e, ee);

                MapleMap mmap = this.getMap();
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        stats.removeEffectiveness(fE);
                        stats.setEffectiveness(fE, fEE);
                    }
                };

                mmap.getChannelServer().registerMobClearSkillAction(mmap.getId(), r, milli);
            }
        } finally {
            monsterLock.unlock();
        }
    }

    public Collection<MonsterStatus> alreadyBuffedStats() {
        statiLock.lock();
        try {
            return Collections.unmodifiableCollection(alreadyBuffed);
        } finally {
            statiLock.unlock();
        }
    }

    public BanishInfo getBanish() {
        return stats.getBanishInfo();
    }

    public void setBoss(boolean boss) {
        this.stats.setBoss(boss);
    }

    public int getDropPeriodTime() {
        return stats.getDropPeriod();
    }

    public int getPADamage() {
        return stats.getPADamage();
    }

    public Map<MonsterStatus, MonsterStatusEffect> getStati() {
        statiLock.lock();
        try {
            return Collections.unmodifiableMap(stati);
        } finally {
            statiLock.unlock();
        }
    }

    public MonsterStatusEffect getStati(MonsterStatus ms) {
        statiLock.lock();
        try {
            return stati.get(ms);
        } finally {
            statiLock.unlock();
        }
    }

    // ---- one can always have fun trying these pieces of codes below in-game rofl ----
    public final ChangeableStats getChangedStats() {
        return ostats;
    }

    public final long getMobMaxHp() {
        if (ostats != null) {
            return ostats.hp;
        }
        return stats.getHp();
    }

    public final void setOverrideStats(final OverrideMonsterStats ostats) {
        this.ostats = new ChangeableStats(stats, ostats);
        this.hp.set(ostats.getHp());
        this.mp = ostats.getMp();
    }

    public final void changeLevel(final int newLevel) {
        changeLevel(newLevel, true);
    }

    public final void changeLevel(final int newLevel, boolean pqMob) {
        if (!stats.isChangeable()) {
            return;
        }
        this.ostats = new ChangeableStats(stats, newLevel, pqMob);
        this.hp.set(ostats.getHp());
        this.mp = ostats.getMp();
    }

    public float getdifficulty() {
        return difficulty;
    }

    public void setdifficulty(float diff) {
        this.difficulty = diff;
    }

    private float getDifficultyRate(final int diff) {
        switch (diff) {
            case 6:
                difficulty += (7.7f);
            case 5:
                difficulty += (5.6f);
            case 4:
                difficulty += (3.2f);
            case 3:
                difficulty += (2.1f);
            case 2:
                difficulty += (1.4f);
        }

        return difficulty;
    }

    private void changeLevelByDifficulty(final int difficulty, boolean pqMob) {
        changeLevel((int) (this.getLevel() * getDifficultyRate(difficulty)), pqMob);
    }

    public final void changeDifficulty(final int difficulty, boolean pqMob) {
        changeLevelByDifficulty(difficulty, pqMob);
    }

    // ---------------------------------------------------------------------------------
    private boolean isPuppetInVicinity(MapleSummon summon) {
        return summon.getPosition().distanceSq(this.getPosition()) < 177777;
    }

    public boolean isCharacterPuppetInVicinity(MapleCharacter chr) {
        MapleStatEffect mse = chr.getBuffEffect(MapleBuffStat.PUPPET);
        if (mse != null) {
            MapleSummon summon = chr.getSummonByKey(mse.getSourceId());

            // check whether mob is currently under a puppet's field of action or not
            if (summon != null) {
                return true;
                //if (isPuppetInVicinity(summon)) {
                //    return true;
                //}
            } else {
                map.getAggroCoordinator().removePuppetAggro(chr.getId());
            }
        }

        return false;
    }

    public boolean isLeadingPuppetInVicinity() {
        MapleCharacter chrController = this.getActiveController();

        if (chrController != null) {
            if (this.isCharacterPuppetInVicinity(chrController)) {
                return true;
            }
        }

        return false;
    }

    private MapleCharacter getNextControllerCandidate() {
        int mincontrolled = Integer.MAX_VALUE;
        MapleCharacter newController = null;

        int mincontrolleddead = Integer.MAX_VALUE;
        MapleCharacter newControllerDead = null;

        MapleCharacter newControllerWithPuppet = null;

        for (MapleCharacter chr : getMap().getAllPlayers()) {
            if (!chr.isGM()) {
                int ctrlMonsSize = chr.getNumControlledMonsters();

                if (isCharacterPuppetInVicinity(chr)) {
                    newControllerWithPuppet = chr;
                    break;
                } else if (chr.isAlive()) {
                    if (ctrlMonsSize < mincontrolled) {
                        mincontrolled = ctrlMonsSize;
                        newController = chr;
                    }
                } else {
                    if (ctrlMonsSize < mincontrolleddead) {
                        mincontrolleddead = ctrlMonsSize;
                        newControllerDead = chr;
                    }
                }
            }
        }

        if (newControllerWithPuppet != null) {
            return newControllerWithPuppet;
        } else if (newController != null) {
            return newController;
        } else {
            return newControllerDead;
        }
    }

    /**
     * Removes controllability status from the current controller of this mob.
     *
     */
    private Pair<MapleCharacter, Boolean> aggroRemoveController() {
        MapleCharacter chrController;
        boolean hadAggro;

        aggroUpdateLock.lock();
        try {
            chrController = getActiveController();
            hadAggro = isControllerHasAggro();

            this.setController(null);
            this.setControllerHasAggro(false);
            this.setControllerKnowsAboutAggro(false);
        } finally {
            aggroUpdateLock.unlock();
        }

        if (chrController != null) { // this can/should only happen when a hidden gm attacks the monster
            chrController.announce(MaplePacketCreator.stopControllingMonster(this.getObjectId()));
            chrController.stopControllingMonster(this);
        }

        return new Pair<>(chrController, hadAggro);
    }

    /**
     * Pass over the mob controllability and updates aggro status on the new
     * player controller.
     *
     */
    public void aggroSwitchController(MapleCharacter newController, boolean immediateAggro) {
        if (aggroUpdateLock.tryLock()) {
            try {
                MapleCharacter prevController = getController();
                if (prevController == newController) {
                    return;
                }

                aggroRemoveController();
                if (!(newController != null && newController.isLoggedinWorld() && newController.getMap() == this.getMap())) {
                    return;
                }

                this.setController(newController);
                this.setControllerHasAggro(immediateAggro);
                this.setControllerKnowsAboutAggro(false);
                this.setControllerHasPuppet(false);
            } finally {
                aggroUpdateLock.unlock();
            }

            this.aggroUpdatePuppetVisibility();
            aggroMonsterControl(newController.getClient(), this, immediateAggro);
            newController.controlMonster(this);
        }
    }

    public void aggroAddPuppet(MapleCharacter player) {
        MapleMonsterAggroCoordinator mmac = map.getAggroCoordinator();
        mmac.addPuppetAggro(player);

        aggroUpdatePuppetController(player);

        if (this.isControllerHasAggro()) {
            this.aggroUpdatePuppetVisibility();
        }
    }

    public void aggroRemovePuppet(MapleCharacter player) {
        MapleMonsterAggroCoordinator mmac = map.getAggroCoordinator();
        mmac.removePuppetAggro(player.getId());

        aggroUpdatePuppetController(null);

        if (this.isControllerHasAggro()) {
            this.aggroUpdatePuppetVisibility();
        }
    }

    /**
     * Automagically finds a new controller for the given monster from the chars
     * on the map it is from...
     *
     */
    public void aggroUpdateController() {
        MapleCharacter chrController = this.getActiveController();
        if (chrController != null && chrController.isAlive()) {
            if (chrController.isGM()) {
                return;
            }
            return;
        }

        MapleCharacter newController = getNextControllerCandidate();
        if (newController == null) {    // was a new controller found? (if not no one is on the map)
            return;
        }
        this.aggroSwitchController(newController, false);
    }

    /**
     * Finds a new controller for the given monster from the chars with deployed
     * puppet nearby on the map it is from...
     *
     */
    private void aggroUpdatePuppetController(MapleCharacter newController) {
        MapleCharacter chrController = this.getActiveController();
        boolean updateController = false;

        if (chrController != null && chrController.isAlive()) {
            if (isCharacterPuppetInVicinity(chrController)) {
                return;
            }
        } else {
            updateController = true;
        }

        if (newController == null || !isCharacterPuppetInVicinity(newController)) {
            MapleMonsterAggroCoordinator mmac = map.getAggroCoordinator();

            List<Integer> puppetOwners = mmac.getPuppetAggroList();
            List<Integer> toRemovePuppets = new LinkedList<>();

            for (Integer cid : puppetOwners) {
                MapleCharacter chr = map.getCharacterById(cid);

                if (chr != null) {
                    if (isCharacterPuppetInVicinity(chr)) {
                        newController = chr;
                        break;
                    }
                } else {
                    toRemovePuppets.add(cid);
                }
            }

            for (Integer cid : toRemovePuppets) {
                mmac.removePuppetAggro(cid);
            }

            if (newController == null) {    // was a new controller found? (if not there's no puppet nearby)
                if (updateController) {
                    aggroUpdateController();
                }

                return;
            }
        } else if (chrController == newController) {
            this.aggroUpdatePuppetVisibility();
        }

        this.aggroSwitchController(newController, this.isControllerHasAggro());
    }

    /**
     * Ensures controllability removal of the current player controller, and
     * fetches for any player on the map to start controlling in place.
     *
     */
    public void aggroRedirectController() {
        this.aggroRemoveController();   // don't care if new controller not found, at least remove current controller
        this.aggroUpdateController();
    }

    /**
     * Returns the current aggro status on the specified player, or null if the
     * specified player is currently not this mob's controller.
     *
     */
    public Boolean aggroMoveLifeUpdate(MapleCharacter player) {
        MapleCharacter chrController = getController();
        if (chrController != null && player.getId() == chrController.getId()) {
            boolean aggro = this.isControllerHasAggro();
            if (aggro) {
                this.setControllerKnowsAboutAggro(true);
            }
            return aggro;
        } else {
            return null;
        }
    }

    /**
     * Refreshes auto aggro for the player passed as parameter, does nothing if
     * there is already an active controller for this mob.
     *
     */
    public void aggroAutoAggroUpdate(MapleCharacter player) {
        MapleCharacter chrController = this.getActiveController();
        if (chrController == null) {
            this.aggroSwitchController(player, true);
        } else if (chrController.getId() == player.getId()) {
            this.setControllerHasAggro(true);
            aggroMonsterControl(player.getClient(), this, true);
        }
    }

    /**
     * Applied damage input for this mob, enough damage taken implies an aggro
     * target update for the attacker shortly.
     *
     * @param attacker
     * @param damage
     */
    public void aggroMonsterDamage(MapleCharacter attacker, long damage) {
        MapleMonsterAggroCoordinator mmac = this.getMapAggroCoordinator();
        mmac.addAggroDamage(this, attacker.getId(), damage);

        MapleCharacter chrController = this.getController();    // aggro based on DPS rather than first-come-first-served, now live after suggestions thanks to MedicOP, Thora, Vcoc
        if (chrController != attacker) {
            if (this.getMapAggroCoordinator().isLeadingCharacterAggro(this, attacker)) {
                this.aggroSwitchController(attacker, true);
            } else {
                this.setControllerHasAggro(true);
                this.aggroUpdatePuppetVisibility();
            }
        } else {
            this.setControllerHasAggro(true);
            this.aggroUpdatePuppetVisibility();
        }
    }

    private static void aggroMonsterControl(MapleClient c, MapleMonster mob, boolean immediateAggro) {
        c.announce(MaplePacketCreator.controlMonster(mob, false, immediateAggro));
    }

    private void aggroRefreshPuppetVisibility(MapleCharacter chrController, MapleSummon puppet) {
        // lame patch for client to redirect all aggro to the puppet

        List<MapleMonster> puppetControlled = new LinkedList<>();
        for (MapleMonster mob : chrController.getControlledMonsters()) {
            if (mob.isPuppetInVicinity(puppet)) {
                puppetControlled.add(mob);
            }
        }

        for (MapleMonster mob : puppetControlled) {
            chrController.announce(MaplePacketCreator.stopControllingMonster(mob.getObjectId()));
        }
        chrController.announce(MaplePacketCreator.removeSummon(puppet, false));

        MapleClient c = chrController.getClient();
        for (MapleMonster mob : puppetControlled) {
            aggroMonsterControl(c, mob, mob.isControllerKnowsAboutAggro());
        }
        chrController.announce(MaplePacketCreator.spawnSummon(puppet, false));
    }

    public void aggroUpdatePuppetVisibility() {
        if (!availablePuppetUpdate) {
            return;
        }

        availablePuppetUpdate = false;
        Runnable r = new Runnable() {

            @Override
            public void run() {
                try {
                    MapleCharacter chrController = MapleMonster.this.getActiveController();
                    if (chrController == null) {
                        return;
                    }

                    MapleStatEffect puppetEffect = chrController.getBuffEffect(MapleBuffStat.PUPPET);
                    if (puppetEffect != null) {
                        MapleSummon puppet = chrController.getSummonByKey(puppetEffect.getSourceId());

                        if (puppet != null && isPuppetInVicinity(puppet)) {
                            controllerHasPuppet = true;
                            aggroRefreshPuppetVisibility(chrController, puppet);
                            return;
                        }
                    }

                    if (controllerHasPuppet) {
                        controllerHasPuppet = false;

                        chrController.announce(MaplePacketCreator.stopControllingMonster(MapleMonster.this.getObjectId()));
                        aggroMonsterControl(chrController.getClient(), MapleMonster.this, MapleMonster.this.isControllerHasAggro());
                    }
                } finally {
                    availablePuppetUpdate = true;
                }
            }
        };

        // had to schedule this since mob wouldn't stick to puppet aggro who knows why
        this.getMap().getChannelServer().registerOverallAction(this.getMap().getId(), r, ServerConstants.UPDATE_INTERVAL);
    }

    /**
     * Clears all applied damage input for this mob, doesn't refresh target
     * aggro.
     *
     */
    public void aggroClearDamages() {
        this.getMapAggroCoordinator().removeAggroEntries(this);
    }

    /**
     * Clears this mob aggro on the current controller.
     *
     */
    public void aggroResetAggro() {
        aggroUpdateLock.lock();
        try {
            this.setControllerHasAggro(false);
            this.setControllerKnowsAboutAggro(false);
        } finally {
            aggroUpdateLock.unlock();
        }
    }

    public final int getRemoveAfter() {
        return stats.removeAfter();
    }

    public void dispose() {
        this.getMap().dismissRemoveAfter(this);
        disposeLocks();
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
        externalLock = externalLock.dispose();
        monsterLock = monsterLock.dispose();
        statiLock = statiLock.dispose();
        animationLock = animationLock.dispose();
    }

    public int getRank(int mid) {
        return getStats().getScale();
    }

    public int getRank(int mid, int channel) {
        return Rank.getMobRank(mid, channel);
    }

    public String getAttackPower(int ch) {
        return NumberFormat.getInstance().format((int) (Math.pow(this.getLevel(), 2) * this.getStats().getScale()) / 25);
    }

    public int getMinItemPower(int ch, MapleCharacter chr) {
        int value = this.getLevel() * this.getStats().getScale();
        return value;
    }

    public int getMaxItemPower(int ch, MapleCharacter chr) {
        return (int) (getMinItemPower(ch, chr) * 2.0);
    }

    public String MaxHP() {
        return NumberFormat.getInstance().format(this.getMaxHp());
    }
}
