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
var entryMap = 450004150;
var exitMap = 450003600;

var minMapId = 450004150;
var maxMapId = 450004250;

var eventTime = 240;     // 10 minutes
var monsters = new Array(8240100, 8240101, 8240102);

var lobbyRange = [0, 0];
var x = new Array(716, 163, 324, 1116, 727, 796);
var y = new Array(-490, 550, -855, -619, -490, -194);

function init() {

}

function setLobbyRange() {
    return lobbyRange;
}

function getEligibleParty(party) {      //selects, from the given party, the team that is allowed to attempt this event
}

function setup(player, lobbyid) {
    var eim = em.newInstance("lucid_boss" + player.getName());
    eim.getMapInstance(450004250).toggleconsume(false);
    eim.schedule("start", 10 * 1000);
    eim.createEventTimer(10 * 1000);
    eim.setIntProperty("stage", 0);
    return eim;
}

function start(eim) {
    eim.dropMessage(6, "[Event] All consumables have been disabled!");
    eim.setIntProperty("stage", 1);
    eim.getMapInstance(450004150).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880158, eim.getChannel()), new java.awt.Point(1000, 0), false);
    eim.getMapInstance(450004150).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880140, eim.getChannel()), new java.awt.Point(1000, 0), false);
    eim.getMapInstance(450004250).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880150, eim.getChannel()), new java.awt.Point(605, -50));
    eim.startEventTimer(60 * 60000);
    eim.schedule("boss", 30000);
    eim.schedule("boss1", 1000);
}

function boss(eim) {
    if (eim.getIntProperty("stage") == 1) {
        eim.getMapInstance(450004150).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880166, eim.getChannel()), new java.awt.Point(Randomizer.rand(100, 1900), 0));
        eim.schedule("boss", 20000);
    }
    if (eim.getIntProperty("stage") == 2) {
        var i = Randomizer.rand(0, 5);
        eim.getMapInstance(450004250).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880166, eim.getChannel()), new java.awt.Point(x[i], y[i]));
        eim.schedule("boss", 30000);
    }
}

function boss1(eim) {
    if (eim.getIntProperty("stage") == 1) {
        eim.getMapInstance(450004150).spawnMonsterOnGround(MapleLifeFactory.getMonster(8880165, eim.getChannel()), new java.awt.Point(Randomizer.rand(100, 1900), -100));
        eim.schedule("boss1", Randomizer.rand(500, 1500));
    }
    if (eim.getIntProperty("stage") == 2) {
        eim.getMapInstance(450004250).spawnMonsterOnGround(MapleLifeFactory.getMonster(8880165, eim.getChannel()), new java.awt.Point(Randomizer.rand(0, 1300), Randomizer.rand(-1150, -50)));
        eim.schedule("boss1", Randomizer.rand(500, 1500));
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
    if (mob.getId() == 8880140) {
        eim.warpEventTeam(450004250);
        eim.setIntProperty("stage", 2);
    }
    if (mob.getId() == 8950102) {
        eim.setIntProperty("stage", 3);
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
