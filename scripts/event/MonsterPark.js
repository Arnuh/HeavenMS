/*
 This file is part of the HeavenMS MapleStory Server
 Copyleft (L) 2016 - 2018 RonanLana
 
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

/**
 * @author: Ronan
 * @event: Zakum Battle
 */

importPackage(Packages.client);
importPackage(Packages.tools);
importPackage(Packages.server.life);


var recruitMap = 76102;
var entryMap = 76102;
var exitMap = 76102;
var eventMapId = 76101;

var eventTime = 480;     // 140 minutes

var lobbyRange = [0, 0];

function init() {

}

function setLobbyRange() {
    return lobbyRange;
}

function getEligibleParty(party) {      //selects, from the given party, the team that is allowed to attempt this event
}

function setup(player, scale) {
    var eim = em.newInstance("MonsterPark" + player.getName());
    eim.getMapInstance(eventMapId).toggledrops(false);
    eim.schedule("start", 10 * 1000);
    eim.createEventTimer(10 * 1000);
    eim.setIntProperty("stage", 9800000);
    eim.setIntProperty("basestage", 1);
    eim.setIntProperty("scale", scale);
	eim.setIntProperty("setting", scale);
    eim.setIntProperty("level", 1);
    eim.setExitMap(exitMap);
    return eim;
}

function start(eim) {
    if (eim.getIntProperty("scale") == 2) {
        eim.setIntProperty("level", 50);
    } else if (eim.getIntProperty("scale") == 3) {
        eim.setIntProperty("level", 100);
    } else if (eim.getIntProperty("scale") == 4) {
		eim.setIntProperty("scale", 3);
        eim.setIntProperty("level", 250);
    } else if (eim.getIntProperty("scale") == 5) {
		eim.setIntProperty("scale", 4);
        eim.setIntProperty("level", 500);
    }
    eim.dropMessage(5, "[Monster Park] Stage: " + eim.getIntProperty("basestage") + ". 20 " + MapleLifeFactory.getMonster(9800000, eim.getChannel()).getName() + " has appeared.");
    var map = eim.getMapInstance(eventMapId);
    for (var i = 1; i <= 20; i++) {
        map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterByLevelLink(9800000, eim.getIntProperty("level"), eim.getIntProperty("scale"), false), 15, new java.awt.Point(Randomizer.rand(-750, 900), -50));
    }
    eim.startEventTimer(eventTime * 60000);
}

function allMonstersDead(eim) {
    var id = eim.getIntProperty("stage");
    var map = eim.getMapInstance(eventMapId);
    eim.setIntProperty("basestage", eim.getIntProperty("basestage") + 1);
    eim.showClearEffect();
    eim.setIntProperty("stage", eim.getIntProperty("stage") + 1);
    var mobid = eim.getIntProperty("stage");
    if (mobid == 9800120) {
        if (eim.getIntProperty("setting") == 2) {
            eim.gainPartyItem(4001760, 1);
        } else if (eim.getIntProperty("setting") == 3) {
            eim.gainPartyItem(4001760, 5);
        } else if (eim.getIntProperty("setting") == 4) {
            eim.gainPartyItem(4001760, 25);
        } else if (eim.getIntProperty("setting") == 5) {
            eim.gainPartyItem(4001760, 100);
        }
        eim.victory(exitMap);
    } else if (mobid == 9800119) {
        eim.dropMessage(5, "[Monster Park] Final Stage: " + eim.getIntProperty("basestage") + ". The Knights of the Stronghold have appeared.");
        map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterByLevelLink(mobid, eim.getIntProperty("level"), eim.getIntProperty("scale") + 2, false), 15, new java.awt.Point(Randomizer.rand(-750, 900), -50));
        map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterByLevelLink(mobid + 1, eim.getIntProperty("level"), eim.getIntProperty("scale") + 2, false), 15, new java.awt.Point(Randomizer.rand(-750, 900), -50));
        map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterByLevelLink(mobid + 3, eim.getIntProperty("level"), eim.getIntProperty("scale") + 2, false), 15, new java.awt.Point(Randomizer.rand(-750, 900), -50));
        map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterByLevelLink(mobid + 4, eim.getIntProperty("level"), eim.getIntProperty("scale") + 2, false), 15, new java.awt.Point(Randomizer.rand(-750, 900), -50));
        map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterByLevelLink(mobid + 5, eim.getIntProperty("level"), eim.getIntProperty("scale") + 2, false), 15, new java.awt.Point(Randomizer.rand(-750, 900), -50));
    } else {
        if (eim.getMonsterRank(mobid) == 3) {
            eim.dropMessage(5, "[Monster Park] Boss Stage: " + eim.getIntProperty("basestage") + ". " + MapleLifeFactory.getMonster(mobid, eim.getChannel()).getName() + " has appeared.");
            map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterByLevelLink(mobid, eim.getIntProperty("level"), eim.getIntProperty("scale") + 1, false), 15, new java.awt.Point(Randomizer.rand(-750, 900), -50));
        } else {
            eim.dropMessage(5, "[Monster Park] Stage: " + eim.getIntProperty("basestage") + ". 20 " + MapleLifeFactory.getMonster(mobid, eim.getChannel()).getName() + " has appeared.");
            for (var i = 1; i <= 20; i++) {
                map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterByLevelLink(mobid, eim.getIntProperty("level"), eim.getIntProperty("scale"), false), 15, new java.awt.Point(Randomizer.rand(-750, 900), -50));
            }
        }
    }
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(eventMapId);
    player.changeMap(map, map.getPortal(0));
}

function scheduledTimeout(eim) {
    eim.exitParty(exitMap);
}

function playerUnregistered(eim, player) {
}

function playerExit(eim, player) {
    eim.exitPlayer(player, exitMap);
}

function playerLeft(eim, player) {
    eim.exitPlayer(player, exitMap);
}

function changedMap(eim, player, mapid) {
    if (mapid != eventMapId) {
        eim.exitPlayer(player, exitMap);
    }
}

function changedLeader(eim, leader) {
    eim.changeEventLeader(leader);
}

function playerDead(eim, player) {
}

function playerRevive(eim, player) { // player presses ok on the death pop up.
    //eim.exitPlayer(player, clearMap);
}

function playerDisconnected(eim, player) {
    eim.exitPlayer(player, exitMap);
}

function leftParty(eim, player) {
    eim.exitPlayer(player, exitMap);
}

function disbandParty(eim) {
    eim.exitParty(exitMap);
}

function monsterValue(eim, mobId) {
    if (eim.getMonsterRank(mobId) == 3) {
        eim.setIntProperty("level", eim.getIntProperty("level") + 1);
        eim.dropMessage(5, "[Monster Park] Monster level increase to: " + eim.getIntProperty("level"));
        //var amount = (MapleLifeFactory.getMonster(mobId, eim.getChannel()).getLevel() / 10) * eim.getChannel();
        //eim.gainPartySplitItem(4000313, amount);
    }
    return 1;
}

function end(eim) {
    eim.exitParty(exitMap);
}

function monsterKilled(mob, eim) {
}

function finish(eim) {
    eim.exitParty(exitMap);
}

function cancelSchedule() {
}

function dispose(eim) {
}

function afterSetup(eim) {
}
