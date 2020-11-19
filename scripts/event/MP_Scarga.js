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

var entryMap = 551030200;
var eventMapId = 551030200;

var BossId = 9400632;

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
    var eim = em.newInstance("MP_Scarga" + player.getName());
    var map = eim.getMapInstance(eventMapId);
    map.removeReactors();
    eim.schedule("start", 10 * 1000);
    eim.createEventTimer(10 * 1000);
    eim.setIntProperty("start", 0);
    eim.setExitMap(exitMap);
    eim.setIntProperty("scale", scale);
    eim.setIntProperty("kills", 0);
    return eim;
}

function start(eim) {
    var map = eim.getMapInstance(eventMapId);
    eim.setIntProperty("start", 1);
    map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterCustomRank(9420544, eim.getChannel(), eim.getIntProperty("scale"), false), 15, new java.awt.Point(-516, 640));
    map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterCustomRank(9420549, eim.getChannel(), eim.getIntProperty("scale"), false), 15, new java.awt.Point(-223, 640));

    eim.startEventTimer(eventTime * 60000);
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
    if ((mobId == 9420544) || (mobId == 9420549)) {
        eim.setIntProperty("kills", eim.getIntProperty("kills") + 1);
    }
    if (eim.getIntProperty("kills") == 2) {
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
