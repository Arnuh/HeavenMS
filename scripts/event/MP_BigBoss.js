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

var entryMap = 801040100;
var eventMapId = 801040100;

var BossId = 9400300;

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
    var eim = em.newInstance("MP_BigBoss" + player.getName());
    var map = eim.getMapInstance(eventMapId);
    map.removeReactors();
    map.removeAllMonsters();
    eim.setExitMap(exitMap);
    eim.schedule("start", 10 * 1000);
    eim.schedule("bomb", 10 * 1000);
    eim.createEventTimer(10 * 1000);
    eim.setIntProperty("finish", 0);
    eim.setIntProperty("scale", scale);
    return eim;
}

function start(eim) {
    var map = eim.getMapInstance(eventMapId);
    eim.dropMessage(6, "Map instance: " + eim);
    map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterCustomRank(9400300, eim.getChannel(), (eim.getIntProperty("scale")), false), 15, new java.awt.Point(729, 160));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(9400112, eim.getChannel(), (eim.getIntProperty("scale") - 2), false), new java.awt.Point(Randomizer.rand(-700, 800), 160), false);
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(9400112, eim.getChannel(), (eim.getIntProperty("scale") - 2), false), new java.awt.Point(Randomizer.rand(-700, 800), 160), false);
    eim.startEventTimer(eventTime * 60000);
    eim.schedule("anni", Randomizer.rand(10, 30) * 1000);
}

function anni(eim) {
    var map = eim.getMapInstance(eventMapId);
    if (eim.getIntProperty("finish") < 1) {
        map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(9400112, eim.getChannel(), (eim.getIntProperty("scale") - 2), false), new java.awt.Point(Randomizer.rand(-700, 800), 160), false);
        map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(9400112, eim.getChannel(), (eim.getIntProperty("scale") - 2), false), new java.awt.Point(Randomizer.rand(-700, 800), 160), false);
        eim.dropMessage(6, "[The Boss] Body Guards get them!!!");
        eim.schedule("anni", Randomizer.rand(20, 40) * 1000);
    }
}

function bomb(eim) {
    if (eim.getIntProperty("finish") < 1) {
        eim.getMapInstance(eventMapId).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterLink(8240108, eim.getChannel(), false), new java.awt.Point(Randomizer.rand(-700, 800), -10));
        eim.schedule("bomb", 1000);
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
    return 1;
}

function end(eim) {
    eim.exitParty(exitMap);
}

function monsterKilled(mob, eim) {
    var map = eim.getMapInstance(eventMapId);
    if (mob.getId() == BossId) {
        eim.setIntProperty("finish", 1);
        eim.victory(exitMap);
    }
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
