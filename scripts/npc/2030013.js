/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.
 
 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*Adobis
 *
 *@author Alan (SharpAceX)
 *@author Ronan
 */
var status = 0;

var level = 120;
var minparty = 1;
var maxparty = 6;
var minraid = 1;
var maxraid = 40;

function start() {
    if (cm.haveItem(4001017)) {
        cm.sendYesNo("Do you wish to take on Zakum? Stamina cost is ("+10 * cm.getPlayer().getChannel()+" Stamina)");
    } else {
        cm.sendOk("Leader must have an Eye of Fire to enter.");
        cm.dispose();
    }
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.dispose();
            return;
        }
        status--;
    }
    if (status == 1) {
        var em = cm.getEventManager("ZakumBattle");
        if (em != null) {
            if (cm.getPlayer().isGroup()) {
                if (cm.getPlayer().isLeader()) {
                    var cost = 10 * cm.getPlayer().getChannel();
                    if (cm.getPlayer().getStamina() >= cost) {
                        var eli = 0;
                        if (cm.getPlayer().getParty() != null) {
                            eli = em.getEligiblePartySrc(cm.getPlayer().getParty(), cm.getPlayer().getMapId(), level, 255, minparty, maxparty);
                        } else if (cm.getPlayer().getRaid() != null) {
                            eli = em.getEligibleRaidSrc(cm.getPlayer().getRaid(), cm.getPlayer().getMapId(), level, 255, minraid, maxraid);
                        } else {
                            cm.sendOk("Event has encountered an error");
                        }
                        if (eli.size() > 0) {
                            if (!em.startPlayerInstance(cm.getPlayer(), 1)) {
                                cm.sendOk("Someone is already attempting the PQ or your instance is currently being reseted. Try again in few seconds.");
                            } else {
                                pi.getPlayer().removeStamina(cost);
                            }
                        } else {
                            cm.sendOk("You cannot start this party quest yet, because either your party is not in the range size, some of your party members are not eligible to attempt it or they are not in this map. Minimum requirements are: Level 10+, 1+ Raid members.");
                        }
                    } else {
                        cm.sendOk("The leader of the party must have at least "+cost+" stamina or more to enter.");
                    }
                } else {
                    cm.sendOk("The leader of the party must be the to talk to me about joining the event.");
                }
            } else {
                cm.sendOk("Event is Party/Raid Mode. Requirements: \r\n#kParty[minlvl=" + level + ", minplayers=" + minparty + ", maxplayers=" + maxparty + "] \r\n#kRaid[minlvl=" + level + ", minplayers=" + minraid + ", maxplayers=" + maxraid + "]");
            }
        } else {
            cm.sendOk("Event has already started, Please wait.");
        }
    }
    cm.dispose();
}
