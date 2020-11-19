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
 * @event: Vs Dunas
 */

importPackage(Packages.client);
importPackage(Packages.tools);
importPackage(Packages.server.life);

var isPq = true;
var minPlayers = 2, maxPlayers = 6;
var minLevel = 150, maxLevel = 255;

var exitMap = 80000;
var recruitMap = 80000;
var clearMap = 80000;

var entryMap = 280030000;
var eventMapId = 280030000;

var BossId = 9400632;
var map = 0;


var eventTime = 240;     // 10 minutes

var lobbyRange = [0, 0];

function init() {

}

function setLobbyRange() {
    return lobbyRange;
}

function getEligibleParty(party) {      //selects, from the given party, the team that is allowed to attempt this event
}

function setup(player, scale) {
    var eim = em.newInstance("MP_Zakum" + player.getName());
    var map = eim.getMapInstance(eventMapId);
    map.removeReactors();
    eim.schedule("start", 10 * 1000);
    eim.createEventTimer(10 * 1000);
    eim.setExitMap(exitMap);
    eim.setIntProperty("start", 0);
    eim.setIntProperty("scale", scale);
    return eim;
}

function start(eim) {
    eim.setIntProperty("start", 1);
    eim.getMapInstance(eventMapId).spawnZakum(eim.getChannel(), eim.getIntProperty("scale"));
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
    var map = eim.getMapInstance(eventMapId);
    if (mobId == 8800000) {
        map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(8800001, eim.getChannel(), eim.getIntProperty("scale"), false), new java.awt.Point(30, -220));
    }
    if (mobId == 8800001) {
        map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(8800002, eim.getChannel(), eim.getIntProperty("scale"), false), new java.awt.Point(30, -220));
    }
    if (mobId == 8800002) {
        eim.victory(exitMap);
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

function allMonstersDead(eim) {
}

function cancelSchedule() {
}

function dispose(eim) {
}

function afterSetup(eim) {
}
