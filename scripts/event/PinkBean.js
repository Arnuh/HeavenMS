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


var isPq = true;
var minPlayers = 6, maxPlayers = 30;
var minLevel = 120, maxLevel = 255;
var entryMap = 270050100;
var exitMap = 270050300;
var recruitMap = 270050000;
var clearMap = 270050300;

var eventMapId = 270050100;
var minMapId = 270050100;
var maxMapId = 270050100;

var eventTime = 140;     // 140 minutes

var lobbyRange = [0, 0];
var map = 0;

function init() {

}

function setLobbyRange() {
    return lobbyRange;
}

function getEligibleParty(party) {      //selects, from the given party, the team that is allowed to attempt this event
}

function setup(player, lobbyid) {
    var eim = em.newInstance("PinkBean" + player.getName());
    var map = eim.getMapInstance(eventMapId);
    eim.schedule("start", 10 * 1000);
    eim.createEventTimer(10 * 1000);
    eim.setIntProperty("finished", 0);
    return eim;
}

function start(eim) {
    eim.setIntProperty("wave", 0);
    var map = eim.getMapInstance(eventMapId);
    var mob1 = MapleLifeFactory.getMonster(8820002, eim.getChannel());
    map.spawnMonsterOnGroundBelow(mob1, new java.awt.Point(5, -50));
    var mob2 = MapleLifeFactory.getMonster(8820003, eim.getChannel());
    map.spawnMonsterOnGroundBelow(mob2, new java.awt.Point(5, -50));
    var mob3 = MapleLifeFactory.getMonster(8820004, eim.getChannel());
    map.spawnMonsterOnGroundBelow(mob3, new java.awt.Point(5, -50));
    var mob4 = MapleLifeFactory.getMonster(8820005, eim.getChannel());
    map.spawnMonsterOnGroundBelow(mob4, new java.awt.Point(5, -50));
    var mob5 = MapleLifeFactory.getMonster(8820006, eim.getChannel());
    map.spawnMonsterOnGroundBelow(mob5, new java.awt.Point(5, -50));
    var mob6 = MapleLifeFactory.getMonster(8820001, eim.getChannel());
    map.spawnMonsterOnGroundBelow(mob6, new java.awt.Point(0, -50));
    eim.startEventTimer(eventTime * 60000);
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
    //eim.exitPlayer(player, clearMap);
}

function playerDisconnected(eim, player) {
    eim.exitPlayer(player, clearMap);
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
}

function finish(eim) {
    eim.exitParty(exitMap);
}

function allMonstersDead(eim) {
    var map = eim.getMapInstance(eventMapId);
    eim.victory(exitMap);
}

function cancelSchedule() {
}

function dispose(eim) {
}

function afterSetup(eim) {
}
