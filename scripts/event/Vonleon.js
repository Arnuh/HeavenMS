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

var exitMap = 82100;
var eventMapId = 82101;

var eventTime = 240;     // 140 minutes

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
    var eim = em.newInstance("Vonleon" + player.getName());
    map = eim.getMapInstance(eventMapId);
    eim.schedule("start", 10 * 1000);
    eim.createEventTimer(10 * 1000);
    eim.setIntProperty("finished", 0);
    return eim;
}

function start(eim) {
    var mob = MapleLifeFactory.getMonster(8840000, eim.getChannel());
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(1500, -70));
    eim.startEventTimer(eventTime * 60000);
    eim.schedule("bomb", 60000);
	eim.schedule("anni", Randomizer.rand(60, 300) * 1000);
}

function bomb(eim) {
    if (eim.getIntProperty("finished") < 1) {
        if (map.getSpawnedMonstersOnMap() < 20) {
            for (var i = 1; i <= 20; i++) {
                map.spawnMonsterOnGround(MapleLifeFactory.getMonster(8510200, eim.getChannel()), new java.awt.Point(Randomizer.rand(-650, 2500), -70));
            }
            eim.schedule("bomb", 60000);
        }
    }
}

function anni(eim) {
    if (eim.getIntProperty("finished") < 1) {
        map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonster(8210011, eim.getChannel()), new java.awt.Point(Randomizer.rand(-650, 2500), -70), false);
		eim.dropMessage(6, "[Von] Kill them my servant!!!");
        eim.schedule("anni", Randomizer.rand(60, 300) * 1000);
    }
}

function playerEntry(eim, player) {
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

function end(eim) {
    eim.exitParty(exitMap);
}

function monsterKilled(mob, eim) {
    if (mob.getId() == 8840000) {
        eim.setIntProperty("finished", 1);
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
