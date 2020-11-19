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
 * @event: Vs Bergamot
*/

importPackage(Packages.client);
importPackage(Packages.tools);
importPackage(Packages.server.life);

var isPq = true;
var minPlayers = 2, maxPlayers = 6;
var minLevel = 150, maxLevel = 255;
var entryMap = 450006450;
var exitMap = 450006440;

var minMapId = 450006450;
var maxMapId = 450006450;

var eventTime = 30;     // 10 minutes

var lobbyRange = [0, 0];

function init() {

}

function setLobbyRange() {
    return lobbyRange;
}

function getEligibleParty(party) {      //selects, from the given party, the team that is allowed to attempt this event
}

function setup(player, lobbyid) {
    var eim = em.newInstance("ark2_battle" + player.getName());
    var map = eim.getMapInstance(entryMap);
	map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterByLevel(8860000, 1800, 7), new java.awt.Point(-1220, -275));
    eim.startEventTimer(eventTime * 60000);
	eim.schedule("waves", 10 * 1000);
	eim.setIntProperty("finish", 0);
    return eim;
}

function waves(eim) {
    var map = eim.getMapInstance(eventMapId);
    var count = eim.getMapInstance(eventMapId).getSpawnedMonstersOnMap();
    if (eim.getIntProperty("finish") < 1) {
        if (count < 80) {
            for (var i = 1; i <= 10; i++) {
                map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterByLevel(8860000, 1800, 5), 15, new java.awt.Point(Randomizer.rand(-2500, -300), -500));
            }
            for (var i = 1; i <= 5; i++) {
                map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterByLevel(8860000, 1800, 6), 15, new java.awt.Point(Randomizer.rand(-2500, -300), -500));
            }
        }
        eim.schedule("waves", Randomizer.rand(5, 20) * 1000);
    }
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(entryMap);
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
    if (mapid < minMapId || mapid > maxMapId) {
        eim.exitPlayer(player, exitMap);
    }
}

function changedLeader(eim, leader) {
    eim.changeEventLeader(leader);
}

function playerDead(eim, player) {
}

function playerRevive(eim, player) { // player presses ok on the death pop up.
    eim.exitPlayer(player, exitMap);
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
    return 1;
}

function end(eim) {
    eim.exitParty(exitMap);
}

function monsterKilled(mob, eim) {
	var map = eim.getMapInstance(eventMapId);
    if (mob.getId() == 8860000) {
        eim.setIntProperty("finish", 1);
        eim.victory(exitMap);
    }
}

function finish(eim) {
    eim.exitParty(exitMap);
}

function allMonstersDead(eim) {
    eim.victory(exitMap);
}

function cancelSchedule() {
}

function dispose(eim) {
}

function afterSetup(eim) {
}
