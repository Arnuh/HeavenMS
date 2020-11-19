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
var entryMap = 450007250;
var exitMap = 450007240;

var minMapId = 450007250;
var maxMapId = 450007250;

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
    var eim = em.newInstance("will_boss" + player.getName());
    eim.getMapInstance(450004250).toggleconsume(false);
    eim.schedule("start", 10 * 1000);
    eim.createEventTimer(10 * 1000);
    eim.setIntProperty("stage", 0);
    return eim;
}

function start(eim) {
    eim.dropMessage(6, "[Event] All consumables have been disabled!");
    eim.setIntProperty("stage", 1);
    eim.getMapInstance(entryMap).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880303, eim.getChannel()), new java.awt.Point(0, 240), false);
    eim.startEventTimer(120 * 60000);
    eim.schedule("boss", 10000);
    eim.schedule("boss1", 60000);
}

function boss(eim) {
    if (eim.getIntProperty("stage") == 1) {
        eim.getMapInstance(entryMap).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880305, eim.getChannel()), new java.awt.Point(Randomizer.rand(-800, 800), 240));
        eim.schedule("boss", Randomizer.rand(20000, 40000));
    }
}

function boss1(eim) {
    if (eim.getIntProperty("stage") == 1) {
        eim.getMapInstance(entryMap).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8880306, eim.getChannel()), new java.awt.Point(Randomizer.rand(-800, 800), 240));
        eim.schedule("boss1", 300000);
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
    if (mob.getId() == 8880302) {
        eim.setIntProperty("stage", 2);
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
