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


var recruitMap = 270000000;
var entryMap = 270000000;
var exitMap = 270000000;
var eventMapId = 75100;
var channel = 1;

var eventTime = 30;     // 140 minutes

var lobbyRange = [0, 0];

function init() {

}

function setLobbyRange() {
    return lobbyRange;
}

function getEligibleParty(party) {      //selects, from the given party, the team that is allowed to attempt this event
}

function setup(player, lobbyid) {
    var eim = em.newInstance("Monster" + player.getName());
    eim.schedule("start", 10 * 1000);
    eim.createEventTimer(10 * 1000);
    eim.setExitMap(exitMap);
    eim.setIntProperty("basestage", 0);
    eim.setIntProperty("kills", 0);
    return eim;
}

function start(eim) {
    eim.setIntProperty("basestage", 1);
    eim.setIntProperty("level", eim.getAvgPlayerLevel());
    eim.dropMessage(5, "You have 30 minutes to kill as many monsters as you. More monsters you kill, the stronger corruption becomes.");
    eim.dropMessage(5, "Corruption level: " + eim.getIntProperty("level"));
    for (var i = 1; i <= 75; i++) {
        eim.getMapInstance(eventMapId).spawnMonsterWithEffect(MapleLifeFactory.getMonsterByLevel(8230067, eim.getIntProperty("level"), 3), 15, new java.awt.Point(Randomizer.rand(-700, 1600), Randomizer.rand(-1300, 80)));
        eim.getMapInstance(eventMapId).spawnMonsterWithEffect(MapleLifeFactory.getMonsterByLevel(8230068, eim.getIntProperty("level"), 3), 15, new java.awt.Point(Randomizer.rand(-700, 1600), Randomizer.rand(-1200, 80)));
    }
    eim.schedule("end", 10 * 60000);
    eim.createEventTimer(10 * 60000);

}

function end(eim) {
    eim.setIntProperty("basestage", 2);
    eim.dropMessage(5, "Your party has managed to elimate " + eim.getIntProperty("kills") + " Monsters.");
    eim.giveEventPlayersExpLevel(eim.getIntProperty("kills") * eim.getAvgPlayerLevel());
    eim.victory(exitMap);
}

function allMonstersDead(eim) {
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
    return 1;
}

function monsterKilled(mob, eim) {
    if (eim.getIntProperty("basestage") == 1) {
        eim.setIntProperty("kills", eim.getIntProperty("kills") + 1);
        eim.getMapInstance(eventMapId).spawnMonsterWithEffect(MapleLifeFactory.getMonsterByLevel(mob.getId(), eim.getIntProperty("level"), 3), 15, new java.awt.Point(Randomizer.rand(-700, 1600), Randomizer.rand(-1200, 80)));
    }
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
