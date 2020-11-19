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
package net.server.channel.handlers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.AbstractMaplePacketHandler;
import server.MapleStatEffect;
import server.TimerManager;
import server.life.Element;
import server.life.ElementalEffectiveness;
import server.life.MapleMonster;
import server.life.MobSkill;
import server.life.MobSkillFactory;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.Randomizer;
import tools.data.input.LittleEndianAccessor;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleJob;
import client.Skill;
import client.SkillFactory;
import client.autoban.AutobanFactory;
import client.autoban.AutobanManager;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import constants.ServerConstants;
import constants.skills.Aran;
import constants.skills.Archer;
import constants.skills.Assassin;
import constants.skills.Bandit;
import constants.skills.Bishop;
import constants.skills.BlazeWizard;
import constants.skills.Bowmaster;
import constants.skills.Brawler;
import constants.skills.ChiefBandit;
import constants.skills.Cleric;
import constants.skills.Corsair;
import constants.skills.Crossbowman;
import constants.skills.Crusader;
import constants.skills.DawnWarrior;
import constants.skills.DragonKnight;
import constants.skills.Evan;
import constants.skills.FPArchMage;
import constants.skills.FPMage;
import constants.skills.FPWizard;
import constants.skills.Fighter;
import constants.skills.Gunslinger;
import constants.skills.Hermit;
import constants.skills.Hero;
import constants.skills.Hunter;
import constants.skills.ILArchMage;
import constants.skills.ILMage;
import constants.skills.Marauder;
import constants.skills.Marksman;
import constants.skills.NightLord;
import constants.skills.NightWalker;
import constants.skills.Outlaw;
import constants.skills.Page;
import constants.skills.Paladin;
import constants.skills.Ranger;
import constants.skills.Rogue;
import constants.skills.Shadower;
import constants.skills.Sniper;
import constants.skills.Spearman;
import constants.skills.ThunderBreaker;
import constants.skills.WhiteKnight;
import constants.skills.WindArcher;
import java.text.NumberFormat;
import net.server.Server;
import scripting.AbstractPlayerInteraction;
import scripting.npc.NPCScriptManager;

public abstract class AbstractDealDamageHandler extends AbstractMaplePacketHandler {

    public int limit = 0;

    public static class AttackInfo {

        public int numAttacked, numDamage, numAttackedAndDamage, skill, skilllevel, stance, direction, rangedirection, charge, display, critical;
        public Map<Integer, List<Long>> allDamage;
        public boolean ranged, magic;
        public int speed = 4;
        public Point position = new Point();

        public MapleStatEffect getAttackEffect(MapleCharacter chr, Skill theSkill) {
            Skill mySkill = theSkill;
            if (mySkill == null) {
                mySkill = SkillFactory.getSkill(GameConstants.getHiddenSkill(skill));
            }

            int skillLevel = chr.getSkillLevel(mySkill);
            if (skillLevel == 0 && GameConstants.isPqSkillMap(chr.getMapId()) && GameConstants.isPqSkill(mySkill.getId())) {
                skillLevel = 1;
            }

            if (skillLevel == 0 || skillLevel < 0) {
                return null;
            }
            if (display > 320) { //Hmm
                if (!mySkill.getAction()) {
                    AutobanFactory.FAST_ATTACK.autoban(chr, "WZ Edit; adding action to a skill: " + display);
                    return null;
                }
            }
            return mySkill.getEffect(skillLevel);
        }
    }

    protected synchronized void applyAttack(AttackInfo attack, final MapleCharacter player, int attackCount) {
        Skill theSkill = null;
        MapleStatEffect attackEffect = null;
        final int job = player.getJob().getId();
        try {
            if (player.isBanned()) {
                return;
            }
            if (attack.skill != 0) {
                theSkill = SkillFactory.getSkill(GameConstants.getHiddenSkill(attack.skill)); //returns back the skill id if its not a hidden skill so we are gucci
                attackEffect = attack.getAttackEffect(player, theSkill);
                if (attackEffect == null) {
                    player.announce(MaplePacketCreator.enableActions());
                    return;
                }

                int mobCount = attackEffect.getMobCount();
                if (attack.skill != Cleric.HEAL) {
                    if (player.isAlive()) {
                        if (attack.skill == NightWalker.POISON_BOMB) {// Poison Bomb
                            attackEffect.applyTo(player, new Point(attack.position.x, attack.position.y));
                        } else if (attack.skill != Aran.BODY_PRESSURE) {// prevent BP refreshing
                            attackEffect.applyTo(player);

                            if (attack.skill == DawnWarrior.FINAL_ATTACK || attack.skill == Page.FINAL_ATTACK_BW || attack.skill == Page.FINAL_ATTACK_SWORD || attack.skill == Fighter.FINAL_ATTACK_SWORD
                                    || attack.skill == Fighter.FINAL_ATTACK_AXE || attack.skill == Spearman.FINAL_ATTACK_SPEAR || attack.skill == Spearman.FINAL_ATTACK_POLEARM || attack.skill == WindArcher.FINAL_ATTACK
                                    || attack.skill == DawnWarrior.FINAL_ATTACK || attack.skill == Hunter.FINAL_ATTACK || attack.skill == Crossbowman.FINAL_ATTACK) {

                                mobCount = 15;//:(
                            } else if (attack.skill == Aran.HIDDEN_FULL_DOUBLE || attack.skill == Aran.HIDDEN_FULL_TRIPLE || attack.skill == Aran.HIDDEN_OVER_DOUBLE || attack.skill == Aran.HIDDEN_OVER_TRIPLE) {
                                mobCount = 12;
                            }
                        }
                    } else {
                        player.announce(MaplePacketCreator.enableActions());
                    }
                }

                if (attack.numAttacked > mobCount) {
                    AutobanFactory.MOB_COUNT.autoban(player, "Skill: " + attack.skill + "; Count: " + attack.numAttacked + " Max: " + attackEffect.getMobCount());
                    return;
                }
            }
            if (!player.isAlive()) {
                return;
            }

            //WTF IS THIS F3,1
            /*if (attackCount != attack.numDamage && attack.skill != ChiefBandit.MESO_EXPLOSION && attack.skill != NightWalker.VAMPIRE && attack.skill != WindArcher.WIND_SHOT && attack.skill != Aran.COMBO_SMASH && attack.skill != Aran.COMBO_FENRIR && attack.skill != Aran.COMBO_TEMPEST && attack.skill != NightLord.NINJA_AMBUSH && attack.skill != Shadower.NINJA_AMBUSH) {
            return;
            }*/
            long totDamage = 0;
            final MapleMap map = player.getMap();

            if (attack.skill == ChiefBandit.MESO_EXPLOSION) {
                int delay = 0;
                for (Integer oned : attack.allDamage.keySet()) {
                    MapleMapObject mapobject = map.getMapObject(oned.intValue());
                    if (mapobject != null && mapobject.getType() == MapleMapObjectType.ITEM) {
                        final MapleMapItem mapitem = (MapleMapItem) mapobject;
                        if (mapitem.getMeso() == 0) { //Maybe it is possible some how?
                            return;
                        }

                        mapitem.lockItem();
                        try {
                            if (mapitem.isPickedUp()) {
                                return;
                            }
                            TimerManager.getInstance().schedule(new Runnable() {

                                @Override
                                public void run() {
                                    mapitem.lockItem();
                                    try {
                                        if (mapitem.isPickedUp()) {
                                            return;
                                        }
                                        map.pickItemDrop(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 4, 0), mapitem);
                                    } finally {
                                        mapitem.unlockItem();
                                    }
                                }
                            }, delay);
                            delay += 100;
                        } finally {
                            mapitem.unlockItem();
                        }
                    } else if (mapobject != null && mapobject.getType() != MapleMapObjectType.MONSTER) {
                        return;
                    }
                }
            }
            for (Integer oned : attack.allDamage.keySet()) {
                final MapleMonster monster = map.getMonsterByOid(oned.intValue());
                if (monster != null) {
                    long totDamageToOneMonster = 0;
                    List<Long> onedList = attack.allDamage.get(oned);

                    if (attack.magic) { // thanks BHB, Alex (CanIGetaPR) for noticing no immunity status check here
                        if (monster.isBuffed(MonsterStatus.MAGIC_IMMUNITY)) {
                            Collections.fill(onedList, (long) 1);
                        }
                    } else {
                        if (monster.isBuffed(MonsterStatus.WEAPON_IMMUNITY)) {
                            Collections.fill(onedList, (long) 1);
                        }
                    }

                    for (Long eachd : onedList) {
                        if (eachd < 0) {
                            eachd += Integer.MAX_VALUE;
                        }
                        totDamageToOneMonster += eachd;
                    }
                    totDamage += totDamageToOneMonster;
                    monster.aggroMonsterDamage(player, totDamageToOneMonster);
                    if (player.getBuffedValue(MapleBuffStat.PICKPOCKET) != null && (attack.skill == 0 || attack.skill == Rogue.DOUBLE_STAB || attack.skill == Bandit.SAVAGE_BLOW || attack.skill == ChiefBandit.ASSAULTER || attack.skill == Shadower.ASSASSINATE || attack.skill == Shadower.TAUNT || attack.skill == Shadower.BOOMERANG_STEP)) {
                        Skill pickpocket = SkillFactory.getSkill(ChiefBandit.PICKPOCKET);
                        int picklv = (player.isGM()) ? pickpocket.getMaxLevel() : player.getSkillLevel(pickpocket);
                        if (picklv > 0) {
                            int delay = 0;
                            final int maxmeso = player.getBuffedValue(MapleBuffStat.PICKPOCKET).intValue();
                            for (Long eachd : onedList) {
                                eachd += Integer.MAX_VALUE;

                                if (pickpocket.getEffect(picklv).makeChanceResult()) {
                                    final Long eachdf;
                                    if (eachd < 0) {
                                        eachdf = eachd + Integer.MAX_VALUE;
                                    } else {
                                        eachdf = eachd;
                                    }

                                    TimerManager.getInstance().schedule(new Runnable() {

                                        @Override
                                        public void run() {
                                            player.getMap().spawnMesoDrop(Math.min((int) Math.max(((double) eachdf / (double) 20000) * (double) maxmeso, (double) 1), maxmeso), new Point((int) (monster.getPosition().getX() + Randomizer.nextInt(100) - 50), (int) (monster.getPosition().getY())), monster, player, true, (byte) 2);
                                        }
                                    }, delay);
                                    delay += 100;
                                }
                            }
                        }

                    } else if (attack.skill == Marauder.ENERGY_DRAIN || attack.skill == ThunderBreaker.ENERGY_DRAIN || attack.skill == NightWalker.VAMPIRE || attack.skill == Assassin.DRAIN) {
                        player.addHP((int) Math.min(monster.getMaxHp() > Integer.MAX_VALUE ? Integer.MAX_VALUE : monster.getMaxHp(), Math.min((int) ((double) totDamage * (double) SkillFactory.getSkill(attack.skill).getEffect(player.getSkillLevel(SkillFactory.getSkill(attack.skill))).getX() / 100.0), player.getCurrentMaxHp() / 2)));
                    } else if (attack.skill == Bandit.STEAL) {
                        player.announce(MaplePacketCreator.earnTitleMessage("Immune to Steal"));
                    } else if (attack.skill == FPArchMage.FIRE_DEMON) {
                        monster.setTempEffectiveness(Element.ICE, ElementalEffectiveness.WEAK, SkillFactory.getSkill(FPArchMage.FIRE_DEMON).getEffect(player.getSkillLevel(SkillFactory.getSkill(FPArchMage.FIRE_DEMON))).getDuration() * 1000);
                    } else if (attack.skill == ILArchMage.ICE_DEMON) {
                        monster.setTempEffectiveness(Element.FIRE, ElementalEffectiveness.WEAK, SkillFactory.getSkill(ILArchMage.ICE_DEMON).getEffect(player.getSkillLevel(SkillFactory.getSkill(ILArchMage.ICE_DEMON))).getDuration() * 1000);
                    } else if (attack.skill == Outlaw.HOMING_BEACON || attack.skill == Corsair.BULLSEYE) {
                        MapleStatEffect beacon = SkillFactory.getSkill(attack.skill).getEffect(player.getSkillLevel(attack.skill));
                        beacon.applyBeaconBuff(player, monster.getObjectId());
                    } else if (attack.skill == Outlaw.FLAME_THROWER) {
                        if (!monster.isBoss()) {
                            Skill type = SkillFactory.getSkill(Outlaw.FLAME_THROWER);
                            if (player.getSkillLevel(type) > 0) {
                                MapleStatEffect DoT = type.getEffect(player.getSkillLevel(type));
                                MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.POISON, 1), type, null, false);
                                monster.applyStatus(player, monsterStatusEffect, true, DoT.getDuration(), false);
                            }
                        }
                    }

                    if (player.isAran()) {
                        if (player.getBuffedValue(MapleBuffStat.WK_CHARGE) != null) {
                            Skill snowCharge = SkillFactory.getSkill(Aran.SNOW_CHARGE);
                            if (totDamageToOneMonster > 0) {
                                MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.SPEED, snowCharge.getEffect(player.getSkillLevel(snowCharge)).getX()), snowCharge, null, false);
                                monster.applyStatus(player, monsterStatusEffect, false, snowCharge.getEffect(player.getSkillLevel(snowCharge)).getY() * 1000);
                            }
                        }
                    }
                    if (player.getBuffedValue(MapleBuffStat.HAMSTRING) != null) {
                        Skill hamstring = SkillFactory.getSkill(Bowmaster.HAMSTRING);
                        if (hamstring.getEffect(player.getSkillLevel(hamstring)).makeChanceResult()) {
                            MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.SPEED, hamstring.getEffect(player.getSkillLevel(hamstring)).getX()), hamstring, null, false);
                            monster.applyStatus(player, monsterStatusEffect, false, hamstring.getEffect(player.getSkillLevel(hamstring)).getY() * 1000);
                        }
                    }
                    if (player.getBuffedValue(MapleBuffStat.SLOW) != null) {
                        Skill slow = SkillFactory.getSkill(Evan.SLOW);
                        if (slow.getEffect(player.getSkillLevel(slow)).makeChanceResult()) {
                            MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.SPEED, slow.getEffect(player.getSkillLevel(slow)).getX()), slow, null, false);
                            monster.applyStatus(player, monsterStatusEffect, false, slow.getEffect(player.getSkillLevel(slow)).getY() * 60 * 1000);
                        }
                    }
                    if (player.getBuffedValue(MapleBuffStat.BLIND) != null) {
                        Skill blind = SkillFactory.getSkill(Marksman.BLIND);
                        if (blind.getEffect(player.getSkillLevel(blind)).makeChanceResult()) {
                            MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.ACC, blind.getEffect(player.getSkillLevel(blind)).getX()), blind, null, false);
                            monster.applyStatus(player, monsterStatusEffect, false, blind.getEffect(player.getSkillLevel(blind)).getY() * 1000);
                        }
                    }
                    if (job == 121 || job == 122) {
                        for (int charge = 1211005; charge < 1211007; charge++) {
                            Skill chargeSkill = SkillFactory.getSkill(charge);
                            if (player.isBuffFrom(MapleBuffStat.WK_CHARGE, chargeSkill)) {
                                if (totDamageToOneMonster > 0) {
                                    if (charge == WhiteKnight.BW_ICE_CHARGE || charge == WhiteKnight.SWORD_ICE_CHARGE) {
                                        monster.setTempEffectiveness(Element.ICE, ElementalEffectiveness.WEAK, chargeSkill.getEffect(player.getSkillLevel(chargeSkill)).getY() * 1000);
                                        break;
                                    }
                                    if (charge == WhiteKnight.BW_FIRE_CHARGE || charge == WhiteKnight.SWORD_FIRE_CHARGE) {
                                        monster.setTempEffectiveness(Element.FIRE, ElementalEffectiveness.WEAK, chargeSkill.getEffect(player.getSkillLevel(chargeSkill)).getY() * 1000);
                                        break;
                                    }
                                }
                            }
                        }
                        if (job == 122) {
                            for (int charge = 1221003; charge < 1221004; charge++) {
                                Skill chargeSkill = SkillFactory.getSkill(charge);
                                if (player.isBuffFrom(MapleBuffStat.WK_CHARGE, chargeSkill)) {
                                    if (totDamageToOneMonster > 0) {
                                        monster.setTempEffectiveness(Element.HOLY, ElementalEffectiveness.WEAK, chargeSkill.getEffect(player.getSkillLevel(chargeSkill)).getY() * 1000);
                                        break;
                                    }
                                }
                            }
                        }
                    } else if (player.getBuffedValue(MapleBuffStat.COMBO_DRAIN) != null) {
                        Skill skill;
                        if (player.getBuffedValue(MapleBuffStat.COMBO_DRAIN) != null) {
                            skill = SkillFactory.getSkill(21100005);
                            player.addHP((int) ((totDamage * skill.getEffect(player.getSkillLevel(skill)).getX()) / 100));
                        }
                    } else if (job == 412 || job == 422 || job == 1411) {
                        Skill type = SkillFactory.getSkill(player.getJob().getId() == 412 ? 4120005 : (player.getJob().getId() == 1411 ? 14110004 : 4220005));
                        if (player.getSkillLevel(type) > 0) {
                            MapleStatEffect venomEffect = type.getEffect(player.getSkillLevel(type));
                            for (int i = 0; i < attackCount; i++) {
                                if (venomEffect.makeChanceResult()) {
                                    if (monster.getVenomMulti() < 3) {
                                        monster.setVenomMulti((monster.getVenomMulti() + 1));
                                        MonsterStatusEffect monsterStatusEffect = new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.POISON, 1), type, null, false);
                                        monster.applyStatus(player, monsterStatusEffect, false, venomEffect.getDuration(), true);
                                    }
                                }
                            }
                        }
                    } else if (job >= 311 && job <= 322) {
                        if (!monster.isBoss()) {
                            Skill mortalBlow;
                            if (job == 311 || job == 312) {
                                mortalBlow = SkillFactory.getSkill(Ranger.MORTAL_BLOW);
                            } else {
                                mortalBlow = SkillFactory.getSkill(Sniper.MORTAL_BLOW);
                            }
                            if (player.getSkillLevel(mortalBlow) > 0) {
                                MapleStatEffect mortal = mortalBlow.getEffect(player.getSkillLevel(mortalBlow));
                                if (monster.getHp() <= (monster.getStats().getHp() * mortal.getX()) / 100) {
                                    if (Randomizer.rand(1, 100) <= mortal.getY()) {
                                        monster.getMap().killMonster(monster, player, true);
                                    }
                                }
                            }
                        }
                    }
                    if (attack.skill != 0) {
                        if (attackEffect.getFixDamage() != -1) {
                            if (totDamageToOneMonster != attackEffect.getFixDamage() && totDamageToOneMonster != 0) {
                                AutobanFactory.FIX_DAMAGE.autoban(player, String.valueOf(totDamageToOneMonster) + " damage");
                            }

                            int threeSnailsId = player.getJobType() * 10000000 + 1000;
                            if (attack.skill == threeSnailsId) {
                                if (ServerConstants.USE_ULTRA_THREE_SNAILS) {
                                    int skillLv = player.getSkillLevel(threeSnailsId);

                                    if (skillLv > 0) {
                                        AbstractPlayerInteraction api = player.getAbstractPlayerInteraction();

                                        int shellId;
                                        switch (skillLv) {
                                            case 1:
                                                shellId = 4000019;
                                                break;

                                            case 2:
                                                shellId = 4000000;
                                                break;

                                            default:
                                                shellId = 4000016;
                                        }

                                        if (api.haveItem(shellId, 1)) {
                                            api.gainItem(shellId, (short) -1, false);
                                            totDamageToOneMonster *= player.getTotalLevel();
                                        } else {
                                            player.dropMessage(5, "You have ran out of shells to activate the hidden power of Three Snails.");
                                        }
                                    } else {
                                        totDamageToOneMonster = 0;
                                    }
                                }
                            }
                        }
                    }
                    if (totDamageToOneMonster > 0 && attackEffect != null) {
                        Map<MonsterStatus, Integer> attackEffectStati = attackEffect.getMonsterStati();
                        if (!attackEffectStati.isEmpty()) {
                            if (attackEffect.makeChanceResult()) {
                                monster.applyStatus(player, new MonsterStatusEffect(attackEffectStati, theSkill, null, false), attackEffect.isPoison(), attackEffect.getDuration());
                            }
                        }
                    }
                    if (attack.skill == Paladin.HEAVENS_HAMMER) {
                        damageMonsterWithSkill(player, map, monster, (int) ((monster.getHp() > Integer.MAX_VALUE ? Integer.MAX_VALUE : monster.getHp()) - 1), attack.skill, 1777);
                    } else if (attack.skill == Aran.COMBO_TEMPEST) {
                        int TmpDmg = (player.calculateMaxBaseDamage(player.getTotalWatk()) * (SkillFactory.getSkill(Aran.COMBO_TEMPEST).getEffect(player.getSkillLevel(SkillFactory.getSkill(Aran.COMBO_TEMPEST))).getDamage() / 100));
                        damageMonsterWithSkill(player, map, monster, (int) (Math.floor(Math.random() * (TmpDmg / 5) + TmpDmg * .8)), attack.skill, 0);
                    } else {
                        if (attack.skill == Aran.BODY_PRESSURE) {
                            map.broadcastMessage(MaplePacketCreator.damageMonster(monster.getObjectId(), totDamageToOneMonster));
                        }

                        map.damageMonster(player, monster, totDamageToOneMonster);
                    }
                    if (monster.isBuffed(MonsterStatus.WEAPON_REFLECT)) {
                        List<Pair<Integer, Integer>> mobSkills = monster.getSkills();

                        for (Pair<Integer, Integer> ms : mobSkills) {
                            if (ms.left == 145) {
                                MobSkill toUse = MobSkillFactory.getMobSkill(ms.left, ms.right);
                                player.addHP(-toUse.getX());
                                map.broadcastMessage(player, MaplePacketCreator.damagePlayer(0, monster.getId(), player.getId(), toUse.getX(), 0, 0, false, 0, true, monster.getObjectId(), 0, 0), true);
                            }
                        }
                    }
                    if (monster.isBuffed(MonsterStatus.MAGIC_REFLECT)) {
                        List<Pair<Integer, Integer>> mobSkills = monster.getSkills();

                        for (Pair<Integer, Integer> ms : mobSkills) {
                            if (ms.left == 145) {
                                MobSkill toUse = MobSkillFactory.getMobSkill(ms.left, ms.right);
                                player.addMP(-toUse.getY());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void damageMonsterWithSkill(final MapleCharacter attacker, final MapleMap map, final MapleMonster monster, final int damage, int skillid, int fixedTime) {
        int animationTime;

        if (fixedTime == 0) {
            animationTime = SkillFactory.getSkill(skillid).getAnimationTime();
        } else {
            animationTime = fixedTime;
        }
        if (animationTime > 0) { // be sure to only use LIMITED ATTACKS with animation time here
            TimerManager.getInstance().schedule(new Runnable() {

                @Override
                public void run() {
                    map.broadcastMessage(MaplePacketCreator.damageMonster(monster.getObjectId(), damage), monster.getPosition());
                    map.damageMonster(attacker, monster, damage);
                }
            }, animationTime);
        } else {
            map.broadcastMessage(MaplePacketCreator.damageMonster(monster.getObjectId(), damage), monster.getPosition());
            map.damageMonster(attacker, monster, damage);
        }
    }

    protected AttackInfo parseDamage(LittleEndianAccessor lea, MapleCharacter chr, boolean ranged, boolean magic) {
        AttackInfo ret = new AttackInfo();
        if (chr.getTested()) {
            return ret;
        }
        if (System.currentTimeMillis() < chr.getAtkCooldown()) {
            limit += 1;
            if (limit > 25) {
                chr.gotoJail();
                Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, chr.getName() + " has been sent to jail due to speed attack hacking."));
            }
        } else {
            limit = 0;
        }
        chr.setAtkCooldown(System.currentTimeMillis() + 10);

        //2C 00 00 01 91 A1 12 00 A5 57 62 FC E2 75 99 10 00 47 80 01 04 01 C6 CC 02 DD FF 5F 00
        lea.readByte();
        ret.numAttackedAndDamage = lea.readByte();
        ret.numAttacked = (ret.numAttackedAndDamage >>> 4) & 0xF; // was 4 kaotic AB | AA BB
        ret.numDamage = ret.numAttackedAndDamage & 0xF;
        ret.allDamage = new HashMap<>();
        ret.skill = lea.readInt();
        ret.ranged = ranged;
        ret.magic = magic;

        if (ret.skill > 0) {
            ret.skilllevel = chr.getSkillLevel(ret.skill);
            if (ret.skilllevel == 0 && GameConstants.isPqSkillMap(chr.getMapId()) && GameConstants.isPqSkill(ret.skill)) {
                ret.skilllevel = 1;
            }
        }

        if (ret.skill == Evan.ICE_BREATH || ret.skill == Evan.FIRE_BREATH || ret.skill == FPArchMage.BIG_BANG || ret.skill == ILArchMage.BIG_BANG || ret.skill == Bishop.BIG_BANG || ret.skill == Gunslinger.GRENADE || ret.skill == Brawler.CORKSCREW_BLOW || ret.skill == ThunderBreaker.CORKSCREW_BLOW || ret.skill == NightWalker.POISON_BOMB) {
            ret.charge = lea.readInt();
        } else {
            ret.charge = 0;
        }

        lea.skip(8);
        ret.display = lea.readByte();
        ret.direction = lea.readByte();
        ret.stance = lea.readByte();
        if (ret.skill == ChiefBandit.MESO_EXPLOSION) {
            if (ret.numAttackedAndDamage == 0) {
                lea.skip(10);
                int bullets = lea.readByte();
                for (int j = 0; j < bullets; j++) {
                    int mesoid = lea.readInt();
                    lea.skip(1);
                    ret.allDamage.put(Integer.valueOf(mesoid), null);
                }
                return ret;
            } else {
                lea.skip(6);
            }
            for (int i = 0; i < ret.numAttacked + 1; i++) {
                int oid = lea.readInt();
                if (i < ret.numAttacked) {
                    lea.skip(12);
                    int bullets = lea.readByte();
                    List<Long> allDamageNumbers = new ArrayList<>();
                    for (int j = 0; j < bullets; j++) {
                        int damage = lea.readInt();
                        allDamageNumbers.add(Long.valueOf(damage));
                    }
                    ret.allDamage.put(Integer.valueOf(oid), allDamageNumbers);
                    lea.skip(4);
                } else {
                    int bullets = lea.readByte();
                    for (int j = 0; j < bullets; j++) {
                        int mesoid = lea.readInt();
                        lea.skip(1);
                        ret.allDamage.put(Integer.valueOf(mesoid), null);
                    }
                }
            }
            return ret;
        }
        if (ranged) {
            //ret.critical = lea.readByte();
            lea.readByte();
            ret.speed = lea.readByte();
            lea.readByte();
            ret.rangedirection = lea.readByte();
            lea.skip(7);

            if (ret.skill == Bowmaster.HURRICANE || ret.skill == Marksman.PIERCING_ARROW || ret.skill == Corsair.RAPID_FIRE || ret.skill == WindArcher.HURRICANE) {
                lea.skip(4);
            }
        } else {
            //ret.critical = lea.readByte();
            lea.readByte();
            ret.speed = lea.readByte();
            lea.skip(4);
        }

        // Find the base damage to base futher calculations on.
        // Several skills have their own formula in this section.
        if (ret.skill != 0) {
            if (magic) {
                if (ret.skill == Cleric.HEAL) {
                    ret.speed = 7;
                }
            }
        }

        long damage = 0;
        long totaldamage = 0;
        if (chr.getTotalLevel() >= 250) {
            totaldamage = (long) (chr.getStats() * (1 + chr.damR));
            if (SkillFactory.getSkill(ret.skill) != null) {
                if (chr.isMage()) {
                    totaldamage *= ret.getAttackEffect(chr, SkillFactory.getSkill(ret.skill)).getMagic();
                } else {
                    totaldamage *= ret.getAttackEffect(chr, SkillFactory.getSkill(ret.skill)).getDamage();
                }
            }
        }
        long bossdamage = totaldamage;
        long attacks = 0;
        for (int i = 0; i < ret.numAttacked; i++) {
            int oid = lea.readInt();
            MapleMonster monster = chr.getMap().getMonsterByOid(oid);
            if (monster != null) {
                if (monster.getStats().getScale() >= 4 && totaldamage > 0) {
                    totaldamage = (long) (bossdamage * ((double) (1 + chr.bossR)));
                }
            }

            //short tDelay = lea.readShort();
            lea.skip(14);
            List<Long> allDamageNumbers = new ArrayList<>();

            if (ret.skill != 0) {
                if (ret.skill == Hermit.SHADOW_MESO) {
                    if (monster != null) {
                        monster.debuffMob(Hermit.SHADOW_MESO);
                    }
                }
            }
            for (int j = 0; j < ret.numDamage; j++) {
                damage = lea.readInt();
                if (monster != null) {
                    int power = monster.getStats().getPower();
                    if (monster.getId() >= 9990000 && monster.getId() <= 9990017) {//target dummies
                        damage = 0;
                        totaldamage = 0;
                    }

                    if (totaldamage > 1) {
                        attacks += 1;
                    }
                }
                //finaldamage = totaldamage / scale;

                allDamageNumbers.add((long) (totaldamage + damage));
            }
            if (ret.skill != Corsair.RAPID_FIRE || ret.skill != Aran.HIDDEN_FULL_DOUBLE || ret.skill != Aran.HIDDEN_FULL_TRIPLE || ret.skill != Aran.HIDDEN_OVER_DOUBLE || ret.skill != Aran.HIDDEN_OVER_TRIPLE) {
                lea.skip(4);
            }
            ret.allDamage.put(Integer.valueOf(oid), allDamageNumbers);

        }
        if (attacks > 0) {
            String dam = NumberFormat.getInstance().format(totaldamage + damage);
            chr.announce(MaplePacketCreator.earnTitleMessage("Overpower Damage: " + dam + " (x" + attacks + ")"));
        }

        if (ret.skill == NightWalker.POISON_BOMB) { // Poison Bomb
            lea.skip(4);
            ret.position.setLocation(lea.readShort(), lea.readShort());
        }
        return ret;
    }

    private static int rand(int l, int u) {
        return (int) ((Math.random() * (u - l + 1)) + l);
    }
}
