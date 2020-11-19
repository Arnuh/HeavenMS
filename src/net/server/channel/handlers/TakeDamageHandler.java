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

import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import client.Skill;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.GameConstants;
import constants.ItemConstants;
import constants.ServerConstants;
import constants.skills.Aran;

import java.awt.Point;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

import net.AbstractMaplePacketHandler;
import client.inventory.manipulator.MapleInventoryManipulator;
import constants.skills.Archer;
import constants.skills.Cleric;
import constants.skills.Magician;
import constants.skills.Paladin;
import net.server.Server;
import server.MapleStatEffect;
import server.life.MapleLifeFactory;
import server.life.MapleLifeFactory.loseItem;
import server.life.MapleMonster;
import server.life.MobAttackInfo;
import server.life.MobAttackInfoFactory;
import server.life.MobSkill;
import server.life.MobSkillFactory;
import server.life.Rank;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import tools.FilePrinter;
import tools.MaplePacketCreator;
import tools.Randomizer;
import tools.data.input.SeekableLittleEndianAccessor;

public final class TakeDamageHandler extends AbstractMaplePacketHandler {

    public boolean bomb(int id) {
        switch (id) {
            case 8240107:
            case 8240108:
            case 8240109:
            case 8500003:
            case 8500004:
            case 8240200:
            case 8240201:
            case 8240202:
            case 8240203:
            case 8240204:
            case 8240205:
            case 8240206:
            case 8240207:
            case 9833641:
            case 9833642:
            case 9833643:
            case 9833661:
            case 8240126:
            case 8240100:
            case 8240101:
            case 8240102:
            case 8880315:
            case 8880317:
            case 8880319:
                return true;
            default:
                return false;
        }
    }

    @Override
    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        List<MapleCharacter> banishPlayers = new ArrayList<>();

        MapleCharacter chr = c.getPlayer();
        slea.readInt();
        byte damagefrom = slea.readByte();
        slea.readByte(); //Element
        int damage = slea.readInt();
        int base = 0;
        int monsterid = 0;
        int mpattack = 0;
        double scale = 0.0;

        int oid = 0, monsteridfrom = 0, pgmr = 0, direction = 0;
        int pos_x = 0, pos_y = 0, fake = 0;
        boolean is_pgmr = false, is_pg = true, is_deadly = false;
        MapleMonster attacker = null;
        final MapleMap map = chr.getMap();
        if (damagefrom != -3 && damagefrom != -4) {
            monsteridfrom = slea.readInt();
            oid = slea.readInt();
            if (bomb(monsteridfrom)) {
                int dam = MapleLifeFactory.getBombDamage(monsteridfrom);
                damage = dam;
            }
            try {
                MapleMapObject mmo = map.getMapObject(oid);
                if (mmo instanceof MapleMonster) {

                    boolean damagefrommob = true;
                    attacker = (MapleMonster) mmo;
                    if (attacker.getId() != monsteridfrom) {
                        attacker = null;
                    }
                    if (attacker != null) {
                        if ((chr.counter >= 5 || damage > 0) && !chr.isHidden()) {
                            if (damagefrommob) { // -1 = melee. 0 = magic/skill
                                double defscale = chr.getDefense(attacker.getStats().getDef());
                                double atkpower = attacker.getStats().getAtk();
                                int dam = (int) (atkpower - (atkpower * defscale));
                                int finaldamage = Randomizer.random(dam * 95, dam * 105) / 100;
                                damage = Randomizer.MinMax(finaldamage, 1, 99999);
                            }
                        }
                    }
                }

                if (attacker != null) {
                    if (attacker.isBuffed(MonsterStatus.NEUTRALISE)) {
                        return;
                    }

                    List<loseItem> loseItems;
                    if (damage > 0) {
                        loseItems = attacker.getStats().loseItem();
                        if (loseItems != null) {
                            if (chr.getBuffEffect(MapleBuffStat.AURA) == null) {
                                MapleInventoryType type;
                                final int playerpos = chr.getPosition().x;
                                byte d = 1;
                                Point pos = new Point(0, chr.getPosition().y);
                                for (loseItem loseItem : loseItems) {
                                    type = ItemConstants.getInventoryType(loseItem.getId());

                                    int dropCount = 0;
                                    for (byte b = 0; b < loseItem.getX(); b++) {
                                        if (Randomizer.nextInt(100) < loseItem.getChance()) {
                                            dropCount += 1;
                                        }
                                    }

                                    if (dropCount > 0) {
                                        int qty;

                                        MapleInventory inv = chr.getInventory(type);
                                        inv.lockInventory();
                                        try {
                                            qty = Math.min(chr.countItem(loseItem.getId()), dropCount);
                                            MapleInventoryManipulator.removeById(c, type, loseItem.getId(), qty, false, false);
                                        } finally {
                                            inv.unlockInventory();
                                        }

                                        if (loseItem.getId() == 4031868) {
                                            chr.updateAriantScore();
                                        }

                                        for (byte b = 0; b < qty; b++) {
                                            pos.x = (int) (playerpos + ((d % 2 == 0) ? (25 * (d + 1) / 2) : -(25 * (d / 2))));
                                            map.spawnItemDrop(chr, chr, new Item(loseItem.getId(), (short) 0, (short) 1), map.calcDropPos(pos, chr.getPosition()), true, true);
                                            d++;
                                        }
                                    }
                                }
                            }
                            map.removeMapObject(attacker);
                        }
                    }
                }
            } catch (ClassCastException e) {
                FilePrinter.printError(FilePrinter.EXCEPTION_CAUGHT, "Attacker is not a mob-type, rather is a " + map.getMapObject(oid).getClass().getName() + " entity.");
                return;
            }

            direction = slea.readByte();

        }
        if (attacker != null) {
            if (damage > 0) {
                chr.counter = 0;
            } else {
                chr.counter += 1;
            }

            if (chr.counter > 10 && damage == 0) {
                chr.gotoJail();
                Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, chr.getName() + "has been sent to jail for using godmode."));
            }
        }
        if (damagefrom != -1 && damagefrom != -2 && attacker != null) {
            MobAttackInfo attackInfo = MobAttackInfoFactory.getMobAttackInfo(attacker, damagefrom);
            if (attackInfo != null) {
                if (attackInfo.isDeadlyAttack()) {
                    is_deadly = true;
                }
                mpattack += attackInfo.getMpBurn();
                MobSkill mobSkill = MobSkillFactory.getMobSkill(attackInfo.getDiseaseSkill(), attackInfo.getDiseaseLevel());
                if (mobSkill != null && damage > 0) {
                    mobSkill.applyEffect(chr, attacker, false, banishPlayers);
                }

                attacker.setMp(attacker.getMp() - attackInfo.getMpCon());
                if (chr.getBuffedValue(MapleBuffStat.MANA_REFLECTION) != null && damage > 0 && !attacker.isBoss()) {
                    int jobid = chr.getJob().getId();
                    if (jobid == 212 || jobid == 222 || jobid == 232) {
                        int id = jobid * 10000 + 1002;
                        Skill manaReflectSkill = SkillFactory.getSkill(id);
                        if (chr.isBuffFrom(MapleBuffStat.MANA_REFLECTION, manaReflectSkill) && chr.getSkillLevel(manaReflectSkill) > 0 && manaReflectSkill.getEffect(chr.getSkillLevel(manaReflectSkill)).makeChanceResult()) {
                            int bouncedamage = (damage * manaReflectSkill.getEffect(chr.getSkillLevel(manaReflectSkill)).getX() / 100);
                            if (bouncedamage > attacker.getMaxHp() / 5) {
                                int hp = (int) (attacker.getMaxHp() > Integer.MAX_VALUE ? Integer.MAX_VALUE : attacker.getMaxHp());
                                bouncedamage = hp / 5;
                            }
                            map.damageMonster(chr, attacker, bouncedamage);
                            map.broadcastMessage(chr, MaplePacketCreator.damageMonster(oid, bouncedamage), true);
                            chr.announce(MaplePacketCreator.showOwnBuffEffect(id, 5));
                            map.broadcastMessage(chr, MaplePacketCreator.showBuffeffect(chr.getId(), id, 5), false);
                        }
                    }
                }
            }
        }

        if (damage == -1) {
            fake = 4020002 + (chr.getJob().getId() / 10 - 40) * 100000;
        }

        //in dojo player cannot use pot, so deadly attacks should be turned off as well
        if (is_deadly && chr.getMap().isDojoMap() && !ServerConstants.USE_DEADLY_DOJO) {
            damage = 0;
            mpattack = 0;
        }

        if (damage > 0 && !chr.isHidden() && !is_deadly) {
            if (attacker != null) {
                if (damagefrom == -1) {
                    if (chr.getBuffedValue(MapleBuffStat.POWERGUARD) != null) { // PG works on bosses, but only at half of the rate.
                        int bouncedamage = (int) (damage * (chr.getBuffedValue(MapleBuffStat.POWERGUARD).doubleValue() / (attacker.isBoss() ? 200 : 100)));
                        bouncedamage = Math.min(bouncedamage, (attacker.getLevel() * chr.getTotalLevel()) * chr.getTotalLevel());
                        damage -= bouncedamage;
                        map.damageMonster(chr, attacker, bouncedamage);
                        map.broadcastMessage(chr, MaplePacketCreator.damageMonster(oid, bouncedamage), false, true);
                        attacker.aggroMonsterDamage(chr, bouncedamage);
                    }
                    MapleStatEffect bPressure = chr.getBuffEffect(MapleBuffStat.COMBO_BARRIER);
                    if (bPressure != null) {
                        Skill skill = SkillFactory.getSkill(Aran.BODY_PRESSURE);
                        if (!attacker.alreadyBuffedStats().contains(MonsterStatus.NEUTRALISE)) {
                            if (!attacker.isBoss() && bPressure.makeChanceResult()) {
                                attacker.applyStatus(chr, new MonsterStatusEffect(Collections.singletonMap(MonsterStatus.NEUTRALISE, 1), skill, null, false), false, (bPressure.getDuration() / 10) * 2, false);
                            }
                        }
                    }
                }

                MapleStatEffect cBarrier = chr.getBuffEffect(MapleBuffStat.COMBO_BARRIER);  // thanks BHB for noticing Combo Barrier buff not working
                if (cBarrier != null) {
                    damage *= (cBarrier.getX() / 1000.0);
                }
            }
            if (damagefrom != -3 && damagefrom != -4) {
                int achilles = 0;
                Skill achilles1 = null;
                int jobid = chr.getJob().getId();
                if (jobid < 200 && jobid % 10 == 2) {
                    achilles1 = SkillFactory.getSkill(jobid * 10000 + (jobid == 112 ? 4 : 5));
                    achilles = chr.getSkillLevel(achilles1);
                }
                if (achilles != 0 && achilles1 != null) {
                    damage *= (achilles1.getEffect(achilles).getX() / 1000.0);
                }
            }

            Integer mesoguard = chr.getBuffedValue(MapleBuffStat.MESOGUARD);
            if (mesoguard != null) {
                damage = Math.round(damage / 2);
                int mesoloss = (int) (damage * (mesoguard.doubleValue() / 100.0));
                if (chr.getMeso() < mesoloss) {
                    chr.gainMeso(-chr.getMeso(), false);
                    chr.cancelBuffStats(MapleBuffStat.MESOGUARD);
                } else {
                    chr.gainMeso(-mesoloss, false);
                }
                //chr.addMPHP(-damage, -mpattack);
            }

            //shield system
            if (chr.getBuffedValue(MapleBuffStat.INVINCIBLE) != null) {
                double value = (chr.getBuffedValue(MapleBuffStat.INVINCIBLE).doubleValue() / 100);
                damage = (int) (damage - (damage * value));
            }
            if (chr.getBuffedValue(MapleBuffStat.MAGIC_GUARD) != null) {
                double value = ((double) chr.getSkillLevel(Magician.MAGIC_GUARD) / 100);
                damage = (int) (damage - (damage * value));
            }
            if (chr.getSkillLevel(Paladin.ACHILLES) > 0) {
                double value = (chr.getSkillLevel(Paladin.ACHILLES) / 100);
                damage = (int) (damage - (damage * value));
            }
            if (chr.getMap().getGuardTotem()) {
                damage = (int) (damage / 2);
            }
            Randomizer.Min(damage, 1);
            int mploss = Randomizer.Max(Math.min(damage, chr.getMp()), 32000);
            int hploss = Randomizer.Max(Math.min(damage - mploss, chr.getHp()), 32000);
            if (!chr.getTested()) {
                if (!chr.isHidden() && !chr.isGM() && chr.isAlive()) {
                    chr.addMPHP(-hploss, -mploss);
                    chr.announce(MaplePacketCreator.showOwnDamage(damage));
                    map.broadcastMessage(chr, MaplePacketCreator.damagePlayer(damagefrom, monsteridfrom, chr.getId(), damage, fake, direction, is_pgmr, pgmr, is_pg, oid, pos_x, pos_y), false);
                } else {
                    map.broadcastGMMessage(chr, MaplePacketCreator.damagePlayer(damagefrom, monsteridfrom, chr.getId(), damage, fake, direction, is_pgmr, pgmr, is_pg, oid, pos_x, pos_y), false);
                }
            }
        }
        if (is_deadly && !chr.isGM() && !chr.getTested() && chr.isAlive()) {
            chr.updateHpMp(1, 1);
        }

        if (GameConstants.isDojo(map.getId())) {
            chr.setDojoEnergy(chr.getDojoEnergy() + ServerConstants.DOJO_ENERGY_DMG);
            c.announce(MaplePacketCreator.getEnergy("energy", chr.getDojoEnergy()));
        }

        for (MapleCharacter player : banishPlayers) {  // chill, if this list ever gets non-empty an attacker does exist, trust me :)
            //player.changeMapBanish(attacker.getBanish().getMap(), attacker.getBanish().getPortal(), attacker.getBanish().getMsg());
        }
    }
}
