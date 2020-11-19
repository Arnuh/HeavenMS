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

import client.MapleCharacter;
import client.MapleClient;
import client.MapleDisease;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.ItemConstants;
import constants.ServerConstants;
import net.AbstractMaplePacketHandler;
import client.inventory.manipulator.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleStatEffect;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMiniDungeonInfo;
import server.maps.MapleTotem;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

/**
 * @author Matze
 */
public final class UseItemHandler extends AbstractMaplePacketHandler {

    @Override
    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        MapleCharacter chr = c.getPlayer();

        if (!chr.isAlive() || !chr.getMap().getConsume()) {
            c.announce(MaplePacketCreator.enableActions());
            return;
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        slea.readInt();
        short slot = slea.readShort();
        int itemId = slea.readInt();
        Item toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);
        if (toUse != null && toUse.getQuantity() > 0 && toUse.getItemId() == itemId) {
            if (itemId == 2470000) {
                chr.dropMessage("You must take this hammer to Nana in Ludibrium.");
                c.announce(MaplePacketCreator.enableActions());
                return;
            }
            if (itemId == 2022035) {
                chr.gainBonusAP(1);
                chr.dropMessage("1 AP has been added.");
                remove(c, slot);
                return;
            }
            if (itemId == 2003000 || itemId == 2003001) {
                long cooldown = 1000 * 10;
                long curr = System.currentTimeMillis();
                if (curr - c.getPlayer().getsavecooldown() >= cooldown) {
                    if (MapleMiniDungeonInfo.isDungeonMap(chr.getMapId()) || chr.getEventInstance() == null) {
                        //MapleTotem newTotem = new MapleTotem(chr.getId());
                        //chr.getMap().getTotem().compareAndSet(null, newTotem);
                        if (chr.getMap().getTotemSpawned()) {
                            chr.getMap().removeTotem();
                        }
                        MapleMonster totemMonster = MapleLifeFactory.getMonsterOwner(1, chr);
                        chr.getMap().spawnMonsterWithEffect(totemMonster, 112, chr.getPosition(), chr);
                        chr.getMap().setLocation(totemMonster.getPosition());
                        chr.getMap().setFrenzyTotem(true);
                        chr.getMap().totem = totemMonster;
                        chr.getMap().broadcastStringMessage(6, "Frezny Totem has been summoned and spawn has increased 50% while the totem is alive.");
                        c.getPlayer().setsavecooldown(System.currentTimeMillis());
                        if (itemId == 2003001) {
                            remove(c, slot);
                        }
                    } else {
                        chr.dropMessage("Frenzy Totem cannot be summoned while inside an instance.");
                    }
                } else {
                    c.getPlayer().dropMessage(6, "Time until next available frenzy totem summon " + ((long) (cooldown - (curr - c.getPlayer().getsavecooldown())) / 1000) + " seconds.");
                }
                c.announce(MaplePacketCreator.enableActions());
                return;
            }
            if (itemId == 2003002) {
                long cooldown = 1000 * 10;
                long curr = System.currentTimeMillis();
                if (curr - c.getPlayer().getsavecooldown() >= cooldown) {
                    if (chr.getEventInstance() == null) {
                        //MapleTotem newTotem = new MapleTotem(chr.getId());
                        //chr.getMap().getTotem().compareAndSet(null, newTotem);
                        if (chr.getMap().getTotemSpawned()) {
                            chr.getMap().removeTotem();
                        }
                        MapleMonster totemMonster = MapleLifeFactory.getMonsterOwner(2, chr);
                        chr.getMap().spawnMonsterWithEffect(totemMonster, 113, chr.getPosition(), chr);
                        chr.getMap().setLocation(totemMonster.getPosition());
                        chr.getMap().setRagingTotem(true);
                        chr.getMap().setOwner(chr);
                        chr.getMap().totem = totemMonster;
                        chr.getMap().broadcastStringMessage(6, "Raging Totem has been summoned and all monsters will spawn to the location of the totem while the totem is alive.");
                        c.getPlayer().setsavecooldown(System.currentTimeMillis());
                    } else {
                        chr.dropMessage("Raging Totem cannot be summoned while inside an instance.");
                    }
                } else {
                    c.getPlayer().dropMessage(6, "Time until next available Raging Totem summon " + ((long) (cooldown - (curr - c.getPlayer().getsavecooldown())) / 1000) + " seconds.");
                }
                c.announce(MaplePacketCreator.enableActions());
                return;
            }
            if (itemId == 2003003 || itemId == 2003004) {
                long cooldown = 1000 * 10;
                long curr = System.currentTimeMillis();
                if (curr - c.getPlayer().getsavecooldown() >= cooldown) {
                    //MapleTotem newTotem = new MapleTotem(chr.getId());
                    //chr.getMap().getTotem().compareAndSet(null, newTotem);
                    if (chr.getMap().getTotemSpawned()) {
                        chr.getMap().removeTotem();
                    }
                    MapleMonster totemMonster = MapleLifeFactory.getMonsterOwner(3, chr);
                    chr.getMap().spawnMonsterWithEffect(totemMonster, 114, chr.getPosition(), chr);
                    chr.getMap().setLocation(totemMonster.getPosition());
                    chr.getMap().setGuardTotem(true);
                    chr.getMap().totem = totemMonster;
                    chr.getMap().broadcastStringMessage(6, "Guardian Totem has been summoned and all damage is reduced by 50% while the totem is alive.");
                    c.getPlayer().setsavecooldown(System.currentTimeMillis());
                    if (itemId == 2003004) {
                        remove(c, slot);
                    }
                } else {
                    c.getPlayer().dropMessage(6, "Time until next available Guardian Totem summon " + ((long) (cooldown - (curr - c.getPlayer().getsavecooldown())) / 1000) + " seconds.");
                }
                c.announce(MaplePacketCreator.enableActions());
                return;
            }
            if (itemId == 2000012) {
                if (chr.getStamina() < chr.getTotalLevel()) {
                    ii.getItemEffect(toUse.getItemId()).applyTo(chr);
                    chr.restoreStam();
                    remove(c, slot);
                } else {
                    chr.dropMessage("Your stamina is currently full.");
                    c.announce(MaplePacketCreator.enableActions());
                }
                return;
            }
            if (itemId == 2022178 || itemId == 2050004) {
                chr.dispelDebuffs();
                remove(c, slot);
                return;
            } else if (itemId == 2050001) {
                chr.dispelDebuff(MapleDisease.DARKNESS);
                remove(c, slot);
                return;
            } else if (itemId == 2050002) {
                chr.dispelDebuff(MapleDisease.WEAKEN);
                chr.dispelDebuff(MapleDisease.SLOW);
                remove(c, slot);
                return;
            } else if (itemId == 2050003) {
                chr.dispelDebuff(MapleDisease.SEAL);
                chr.dispelDebuff(MapleDisease.CURSE);
                remove(c, slot);
                return;
            } else if (ItemConstants.isTownScroll(itemId)) {
                int banMap = chr.getMapId();
                int banSp = chr.getMap().findClosestPlayerSpawnpoint(chr.getPosition()).getId();
                long banTime = currentServerTime();

                if (ii.getItemEffect(toUse.getItemId()).applyTo(chr)) {
                    if (ServerConstants.USE_BANISHABLE_TOWN_SCROLL) {
                        chr.setBanishPlayerData(banMap, banSp, banTime);
                    }

                    remove(c, slot);
                }
                return;
            } else if (ItemConstants.isAntibanishScroll(itemId)) {
                if (ii.getItemEffect(toUse.getItemId()).applyTo(chr)) {
                    remove(c, slot);
                } else {
                    chr.dropMessage(5, "You cannot recover from a banish state at the moment.");
                }
                return;
            }

            remove(c, slot);

            if (toUse.getItemId() != 2022153) {
                ii.getItemEffect(toUse.getItemId()).applyTo(chr);
            } else {
                MapleStatEffect mse = ii.getItemEffect(toUse.getItemId());
                for (MapleCharacter player : chr.getMap().getCharacters()) {
                    mse.applyTo(player);
                }
            }
        }
    }

    private void remove(MapleClient c, short slot) {
        MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
        c.announce(MaplePacketCreator.enableActions());
    }
}
