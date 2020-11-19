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
 * @Author Ronan
 * 3rd Job Event - Magician
 **/
importPackage(Packages.client);
importPackage(Packages.tools);
importPackage(Packages.server.life);

var entryMap = 910000000;
var exitMap = 910000000;

var eventMapId = 76300;

var eventTime = 60; //60 minutes

var lobbyRange = [0, 8];
var mobid = 9840000;

function setLobbyRange() {
    return lobbyRange;
}

function init() {
}

function setup(player, diff) {
    var eim = em.newInstance("Dragon_Train" + player.getName());
    eim.getMapInstance(eventMapId).toggledrops(false);
	mobid = 9840000 + (player.getLevel() / 10);
	player.dropMessage(6, "[Training] Mob ID:" + mobid);
	eim.schedule("start", 1000);
    return eim;
}

function start(eim) {
	eim.startEventTimer(eventTime * 60 * 1000);
	var map = eim.getMapInstance(eventMapId);
    for (var i = 1; i <= 50; i++) {
        map.spawnMonsterWithEffect(MapleLifeFactory.getMonster(mobid, eim.getChannel()), 15, new java.awt.Point(Randomizer.rand(-400, 1300), Randomizer.rand(-1000, -250)));
    }
}

function playerEntry(eim, player) {
    player.changeMap(eim.getMapInstance(eventMapId), 0);
    player.dropMessage(6, "[Training] You have 60 minutes to kill as many golden dragons as you can.");
}

function playerUnregistered(eim, player) {
}

function playerExit(eim, player) {
    eim.exitPlayer(player, exitMap);
}

function scheduledTimeout(eim) {
    eim.exitParty(exitMap);
}

function playerDisconnected(eim, player) {
    eim.exitPlayer(player, exitMap);
}

function clear(eim) {
    eim.exitParty(exitMap);
}

function changedMap(eim, player, mapid) {
    if (mapid != eventMapId) {
        eim.exitPlayer(player, exitMap);
    }
}

function monsterValue(eim, mobId) {
    var map = eim.getMapInstance(eventMapId);
    map.spawnMonsterWithEffect(MapleLifeFactory.getMonster(mobId, eim.getChannel()), 15, new java.awt.Point(Randomizer.rand(-400, 1300), Randomizer.rand(-1000, -250)));
    return 1;
}

function monsterKilled(mob, eim) {
}

function allMonstersDead(eim) {
}

function cancelSchedule() {
}

function dispose() {
}

function end(eim) {
    eim.exitParty(exitMap);
}
// ---------- FILLER FUNCTIONS ----------

function disbandParty(eim, player) {
}

function afterSetup(eim) {
}

function changedLeader(eim, leader) {
}

function leftParty(eim, player) {
    eim.exitPlayer(player, exitMap);
}

function clearPQ(eim) {
}

