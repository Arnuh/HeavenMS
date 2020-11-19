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

var bossMap = 86044;
var exitMap = 86043;
var boss = 8930100;

var eventTime = 480; //60 minutes

var lobbyRange = [0, 8];

function setLobbyRange() {
    return lobbyRange;
}

function init() {
}

function setup(player, scale) {
    var eim = em.newInstance("TimeVellum" + player.getName());
    eim.schedule("start", 10000);
    eim.createEventTimer(10 * 1000);
    eim.setIntProperty("finish", 0);
    return eim;
}

function getEligibleParty(party) {      //selects, from the given party, the team that is allowed to attempt this event

}

function playerEntry(eim, player) {
    player.changeMap(eim.getMapInstance(bossMap), 0);
}

function start(eim) {
    var map = eim.getMapInstance(bossMap);
    eim.startEventTimer(eventTime * 60000);
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(8930100, eim.getChannel(), 6, false), new java.awt.Point(1000, 380));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(8930101, eim.getChannel(), 5, false), new java.awt.Point(1200, 380));
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(8930101, eim.getChannel(), 5, false), new java.awt.Point(800, 380));
    eim.schedule("anni", Randomizer.rand(30, 60) * 1000);
}

function anni(eim) {
    var map = eim.getMapInstance(bossMap);
    if (eim.getIntProperty("finish") < 1) {
        map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(8930101, eim.getChannel(), 5, false), new java.awt.Point(Randomizer.rand(800, 1200), 380));
        eim.dropMessage(6, "[Vellum] You cannot stop me!!!");
        eim.schedule("anni", Randomizer.rand(60, 300) * 1000);
    }
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
    if (mapid != bossMap) {
        eim.exitPlayer(player, exitMap);
    }
}

function playerRevive(eim, player) { // player presses ok on the death pop up.
    eim.exitPlayer(player, exitMap);
}

function monsterKilled(mob, eim) {
    var map = eim.getMapInstance(bossMap);
    if (mob.getId() == 8930100) {
        eim.setIntProperty("finish", 1);
        eim.victory(exitMap);
    }
}

function monsterValue(eim, mobId) {
    return 1;
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
}

function clearPQ(eim) {
}

