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
/*
MiniDungeon - Pirate
*/ 

var level = 10;
var minparty = 1;
var maxparty = 6;
var minraid = 1;
var maxraid = 40;
var event = "MD_event_treasure";

var baseid = 251010402;
var dungeonid = 251010410;
var dungeons = 30;

function enter(pi) {
    if (pi.getMapId() == baseid) {
        var em = pi.getEventManager(event);
        if (em != null) {
            if (pi.getPlayer().isGroup()) {
                if (pi.getPlayer().isLeader()) {
                    var eli;
                    if (pi.getPlayer().getParty() != null) {
                        eli = em.getEligiblePartySrc(pi.getParty(), pi.getPlayer().getMapId(), level, 255, minparty, maxparty);
                    } else if (pi.getPlayer().getRaid() != null) {
                        eli = em.getEligibleRaidSrc(pi.getPlayer().getRaid(), pi.getPlayer().getMapId(), level, 255, minraid, maxraid);
                    } else {
                        pi.playerMessage(5, "Event has encountered an error");
                        return false;
                    }
                    if (eli.size() > 0) {
                        if (!em.startPlayerInstance(pi.getPlayer(), 1)) {
                            pi.playerMessage(5, "Someone is already attempting the PQ or your instance is currently being reseted. Try again in few seconds.");
                        } else {
                            pi.playPortalSound();
                            return true;
                        }
                    } else {
                        pi.playerMessage(5, "You cannot start this party quest yet, because either your party is not in the range size, some of your party members are not eligible to attempt it or they are not in this map. Minimum requirements are: Level 160+, 1+ Raid members.");
                    }
                } else {
                    pi.playerMessage(5, "The leader of the party must be the to talk to me about joining the event.");
                }
            } else {
            if (!em.startPlayerInstance(pi.getPlayer(), 1)) {
                pi.playerMessage(5, "Someone is already attempting the PQ or your instance is currently being reseted. Try again in few seconds.");
            } else {
                pi.playPortalSound();
                return true;
            }
            }
        } else {
            pi.playerMessage(5, "Event has already started, Please wait.");
        }
        return false;
    } else {
        pi.playPortalSound();
        pi.warp(baseid, "MD00");
        return true;
    }
}