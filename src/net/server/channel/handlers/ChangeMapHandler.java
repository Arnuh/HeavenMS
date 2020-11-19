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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

import net.AbstractMaplePacketHandler;
import client.MapleCharacter;
import client.MapleClient;
import client.inventory.MapleInventoryType;
import client.inventory.manipulator.MapleInventoryManipulator;
import server.MaplePortal;
import server.MapleTrade;
import server.maps.MapleMap;
import tools.FilePrinter;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public final class ChangeMapHandler extends AbstractMaplePacketHandler {

    @Override
    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        MapleCharacter chr = c.getPlayer();

        if (chr.isChangingMaps() || chr.isBanned()) {
            if (chr.isChangingMaps()) {
                FilePrinter.printError(FilePrinter.PORTAL_STUCK + chr.getName() + ".txt", "Player " + chr.getName() + " got stuck when changing maps. Timestamp: " + Calendar.getInstance().getTime().toString() + " Last visited mapids: " + chr.getLastVisitedMapids());
            }

            c.announce(MaplePacketCreator.enableActions());
            return;
        }
        if (chr.getTrade() != null) {
            MapleTrade.cancelTrade(chr, MapleTrade.TradeResult.UNSUCCESSFUL_ANOTHER_MAP);
        }
        if (slea.available() == 0) { //Cash Shop :)
            if (!chr.getCashShop().isOpened()) {
                c.disconnect(false, false);
                return;
            }
            String[] socket = c.getChannelServer().getIP().split(":");
            chr.getCashShop().open(false);

            c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
            chr.setSessionTransitionState();
            try {
                c.announce(MaplePacketCreator.getChannelChange(c, InetAddress.getByName(socket[0]), Integer.parseInt(socket[1])));
            } catch (UnknownHostException ex) {
                ex.printStackTrace();
            }
        } else {
            if (chr.getCashShop().isOpened()) {
                c.disconnect(false, false);
                return;
            }
            try {
                slea.readByte(); // 1 = from dying 0 = regular portals
                int targetid = slea.readInt();
                String startwp = slea.readMapleAsciiString();
                MaplePortal portal = chr.getMap().getPortal(startwp);
                slea.readByte();
                boolean wheel = slea.readShort() > 0;

                if (targetid != -1) {
                    if (!chr.isAlive()) {
                        MapleMap map = chr.getMap();
                        if (wheel && chr.haveItemWithId(5510000, false)) {
                            // thanks lucasziron for showing revivePlayer() also being triggered by Wheel

                            MapleInventoryManipulator.removeById(c, MapleInventoryType.CASH, 5510000, 1, true, false);
                            chr.announce(MaplePacketCreator.showWheelsLeft(chr.getItemQuantity(5510000, false)));

                            chr.updateHp(50);
                            chr.changeMap(map, map.findClosestPlayerSpawnpoint(chr.getPosition()));
                        } else {
                            if (chr.getEventInstance() == null) {
                                if (map.getReturnMapId() == -1 || map.getReturnMapId() == 999999999) {
                                    chr.respawn(chr.getMapId());
                                } else {
                                    chr.respawn(map.getReturnMapId());
                                }
                            } else {
                                if (chr.getLives() < 1) {
                                    if (chr.getEventInstance().getExitMap() == -1) {
                                        if (map.getReturnMapId() == -1 || map.getReturnMapId() == 999999999) {
                                            chr.getEventInstance().revivePlayer(chr, 100000000); // force to henesys if no return id is found
                                        } else {
                                            chr.getEventInstance().revivePlayer(chr, map.getReturnMapId());
                                        }
                                    } else {
                                        chr.getEventInstance().revivePlayer(chr, chr.getEventInstance().getExitMap());
                                    }
                                } else {
                                    chr.respawn(chr.getMapId());
                                    chr.setLives(chr.getLives() - 1);
                                    if (chr.getLives() > 0) {
                                        c.announce(MaplePacketCreator.earnTitleMessage("You have " + chr.getLives() + " lives remaining."));
                                    } else {
                                        c.announce(MaplePacketCreator.earnTitleMessage("You have no remaining lives left. Be careful!"));
                                    }
                                }
                            }
                        }
                    } else {
                        if (chr.isGM()) {
                            MapleMap to = chr.getWarpMap(targetid);
                            chr.changeMap(to, to.getPortal(0));
                        } else {
                            final int divi = chr.getMapId() / 100;
                            boolean warp = false;
                            if (divi == 0) {
                                if (targetid == 10000) {
                                    warp = true;
                                }
                            } else if (divi == 20100) {
                                if (targetid == 104000000) {
                                    c.announce(MaplePacketCreator.lockUI(false));
                                    c.announce(MaplePacketCreator.disableUI(false));
                                    warp = true;
                                }
                            } else if (divi == 9130401) { // Only allow warp if player is already in Intro map, or else = hack
                                if (targetid == 130000000 || targetid / 100 == 9130401) { // Cygnus introduction
                                    warp = true;
                                }
                            } else if (divi == 9140900) { // Aran Introduction
                                if (targetid == 914090011 || targetid == 914090012 || targetid == 914090013 || targetid == 140090000) {
                                    warp = true;
                                }
                            } else if (divi / 10 == 1020) { // Adventurer movie clip Intro
                                if (targetid == 1020000) {
                                    warp = true;
                                }
                            } else if (divi / 10 >= 980040 && divi / 10 <= 980045) {
                                if (targetid == 980040000) {
                                    warp = true;
                                }
                            }
                            if (warp) {
                                final MapleMap to = chr.getWarpMap(targetid);
                                chr.changeMap(to, to.getPortal(0));
                            }
                        }
                    }
                }

                if (portal != null && !portal.getPortalStatus()) {
                    c.announce(MaplePacketCreator.blockedMessage(1));
                    c.announce(MaplePacketCreator.enableActions());
                    return;
                }

                if (chr.getMapId() == 109040004) {
                    chr.getFitness().resetTimes();
                } else if (chr.getMapId() == 109030003 || chr.getMapId() == 109030103) {
                    chr.getOla().resetTimes();
                }

                if (portal != null) {
                    if (portal.getPosition().distanceSq(chr.getPosition()) > 400000) {
                        c.announce(MaplePacketCreator.enableActions());
                        return;
                    }
                    //MapleMap to = chr.getWarpMap(targetid);
                    //chr.changeMap(to, to.getPortal(0));
                    portal.enterPortal(c);
                } else {
                    c.announce(MaplePacketCreator.enableActions());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
