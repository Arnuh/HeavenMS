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
var entryMap = 350060160;
var exitMap = 350060300;

var minMapId = 350060160;
var maxMapId = 350060200;

var eventTime = 240;     // 10 minutes
var monsters = new Array(8240100, 8240101, 8240102);

var lobbyRange = [0, 0];

function init() {

}

function setLobbyRange() {
    return lobbyRange;
}

function getEligibleParty(party) {      //selects, from the given party, the team that is allowed to attempt this event
}

function setup(player, lobbyid) {
    var eim = em.newInstance("blackHeaven_boss" + player.getName());
    eim.schedule("start", 10 * 1000);
    eim.createEventTimer(10 * 1000);
	eim.setIntProperty("stage", 0);
    return eim;
}

function start(eim) {
	eim.setIntProperty("stage", 1);
    eim.getMapInstance(350060160).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8240103, eim.getChannel()), new java.awt.Point(0, -20), false);
	eim.getMapInstance(350060180).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8240104, eim.getChannel()), new java.awt.Point(0, -20), false);
	eim.getMapInstance(350060200).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8950102, eim.getChannel()), new java.awt.Point(0, -20));
    eim.startEventTimer(240 * 60000);
	eim.schedule("boss", 1000);
}

function boss(eim) {
	if (eim.getIntProperty("stage") == 1) {
		var map = eim.getMapInstance(350060160);
        map.spawnMonsterOnGround(MapleLifeFactory.getMonster(9833661, eim.getChannel()), new java.awt.Point(-512, -280));
		map.spawnMonsterOnGround(MapleLifeFactory.getMonster(9833661, eim.getChannel()), new java.awt.Point(512, -280));
		map.spawnMonsterOnGround(MapleLifeFactory.getMonster(9833661, eim.getChannel()), new java.awt.Point(-430, -440));
		map.spawnMonsterOnGround(MapleLifeFactory.getMonster(9833661, eim.getChannel()), new java.awt.Point(430, -440));
		map.spawnMonsterOnGround(MapleLifeFactory.getMonster(9833661, eim.getChannel()), new java.awt.Point(0, -256));
        eim.schedule("boss", 2500);
    }
	if (eim.getIntProperty("stage") == 2) {
        eim.getMapInstance(350060200).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8240200, eim.getChannel()), new java.awt.Point(Randomizer.rand(-600, 600), -20));
        eim.schedule("boss", Randomizer.rand(100, 250));
    }
}

function boss1(eim) {
    if (eim.getIntProperty("stage") == 3) {
        eim.getMapInstance(350060200).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8240200, eim.getChannel()), new java.awt.Point(Randomizer.rand(-600, 600), -20));
        eim.schedule("boss1", Randomizer.rand(250, 500));
    }
}

function boss2(eim) {
    if (eim.getIntProperty("stage") == 3) {
        eim.getMapInstance(350060200).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8240201, eim.getChannel()), new java.awt.Point(Randomizer.rand(-600, 600), -20));
        eim.schedule("boss2", Randomizer.rand(1000, 2500));
    }
}

function boss3(eim) {
    if (eim.getIntProperty("stage") == 3) {
        eim.getMapInstance(350060200).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8240202, eim.getChannel()), new java.awt.Point(Randomizer.rand(-600, 600), -20));
        eim.schedule("boss3", Randomizer.rand(5000, 7500));
    }
}

function boss4(eim) {
    if (eim.getIntProperty("stage") == 3) {
        eim.getMapInstance(350060200).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8240126, eim.getChannel()), new java.awt.Point(Randomizer.rand(-600, 600), -20));
        eim.schedule("boss4", Randomizer.rand(10000, 20000));
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
    if (mob.getId() == 8240103) {
		eim.warpEventTeam(350060180);
		eim.setIntProperty("stage", 2);
    }
    if (mob.getId() == 8240104) {
		eim.warpEventTeam(350060200);
		eim.setIntProperty("stage", 3);
		eim.schedule("boss1", 1000);
		eim.schedule("boss2", 5000);
		eim.schedule("boss3", 10000);
		eim.schedule("boss4", 30000);
    }
    if (mob.getId() == 8950102) {
        eim.setIntProperty("stage", 4);
        eim.setExitMap(exitMap);
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
