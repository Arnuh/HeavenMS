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

var level = 10;
var minparty = 1;
var maxparty = 6;
var minraid = 1;
var maxraid = 40;

function enter(pi) {
    var em = pi.getEventManager("victorDoor");
    if (em != null) {
		var cost = 25;
        if (pi.getPlayer().isGroup()) {
            if (pi.getPlayer().isLeader()) {
                var amount = 1000 + (500 * pi.getPlayer().getChannel());
                if (pi.getPlayer().getStamina() >= cost) {
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
                            pi.getPlayer().removeStamina(cost);
                            pi.playPortalSound();
                            return true;
                        }
                    } else {
                        pi.playerMessage(5, "You cannot start this party quest yet, because either your party is not in the range size, some of your party members are not eligible to attempt it or they are not in this map. Minimum requirements are: Level 160+, 1+ Raid members.");
                    }
                } else {
                    pi.playerMessage(5, "The leader of the party must have at least "+cost+" stamina or more to enter.");
                }
            } else {
                pi.playerMessage(5, "The leader of the party must be the to talk to me about joining the event.");
            }
        } else {
			if (pi.getPlayer().getStamina() >= cost) {
				if (!em.startPlayerInstance(pi.getPlayer(), 1)) {
					pi.playerMessage(5, "Someone is already attempting the PQ or your instance is currently being reseted. Try again in few seconds.");
					return false;
				} else {
					pi.getPlayer().removeStamina(cost);
					pi.playPortalSound();
					return true;
				}
			} else {
                pi.playerMessage(5, "The leader of the party must have at least "+cost+" stamina or more to enter.");
				return false;
            }
        }
    } else {
        pi.playerMessage(5, "Event has already started, Please wait.");
    }
    return false;
}