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


var recruitMap = 970030000;
var entryMap = 970030000;
var exitMap = 970030000;

var eventMapId = 78000;

var startMobId = 9305100;
var endMobId = 9305139;

var minPlayers = 3, maxPlayers = 6;
var minLevel = 140, maxLevel = 255;

var eventTime = 60; //30 minutes
var lobbyRange = [0, 8];


function getEligibleParty(party) {      //selects, from the given party, the team that is allowed to attempt this event
    return eligible;
}

//setup --------------------------------------------------------------------

function setLobbyRange() {
    return lobbyRange;
}

function init() {
}

function setup(player, diff) {
    var eim = em.newInstance("EasyBossPQ" + player.getName());
    eim.getMapInstance(eventMapId).toggledrops(false);
    eim.setIntProperty("scale", diff);
    eim.schedule("start", 10 * 1000);
    eim.createEventTimer(10 * 1000);
    eim.setIntProperty("finished", 0);
    eim.setExitMap(exitMap);
    return eim;
}

function playerEntry(eim, player) {
    player.changeMap(eim.getMapInstance(eventMapId), 0);
    player.dropMessage(6, "[Easy Boss PQ] Event will begin in 10 seconds. Players: " + eim.getPlayers());
}

//event --------------------------------------------------------------------

function start(eim) {
    eim.changeMusic("BgmCustom/EDM");
    var map = eim.getMapInstance(eventMapId);
    var mob = MapleLifeFactory.getMonsterRankLink(startMobId, eim.getChannel(), 2, false);
    map.spawnMonsterWithEffect(mob, 15, new java.awt.Point(Randomizer.rand(-380, 1300), -10));
    eim.startEventTimer(eventTime * 60000);
    eim.schedule("bomb", 0);
}

function bomb(eim) {
    if (eim.getIntProperty("finished") < 1) {
        eim.getMapInstance(eventMapId).spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterLink(8240108, eim.getChannel(), false), new java.awt.Point(Randomizer.rand(-700, 1600), -10));
        eim.schedule("bomb", 250);
    }
}

function finish(eim) {
    eim.exitParty(exitMap);
}

//timer ending --------------------------------------------------------------------
function scheduledTimeout(eim) {
    eim.exitParty(exitMap);
}

//monsters--------------------------------------------------------------------

function monsterKilled(mob, eim) {

}

function monsterValue(eim, mobId) {
    var map = eim.getMapInstance(eventMapId);
    if (mobId >= startMobId && mobId < endMobId) {
        eim.getMapInstance(eventMapId).spawnMonsterWithEffect(MapleLifeFactory.getMonsterRankLink(mobId + 1, eim.getChannel(), 2, false), 15, new java.awt.Point(Randomizer.rand(-380, 1300), -10));
    }
    if (mobId == endMobId) {
        eim.setIntProperty("finished", 1);
        eim.gainPartyItem(4000313, 10);
        eim.victory(exitMap);
    }
    return 1;
}

function allMonstersDead(eim) {
}

//player leave --------------------------------------------------------------------

function playerUnregistered(eim, player) {
}

function playerExit(eim, player) {
    eim.exitPlayer(player, exitMap);
}

function playerDisconnected(eim, player) {
    eim.exitPlayer(player, exitMap);
}

function clear(eim) {
}

function changedMap(eim, player, mapid) {
    if (mapid != eventMapId) {
        eim.exitPlayer(player, exitMap);
    }
}

function playerRevive(eim, player) { // player presses ok on the death pop up.
    eim.exitPlayer(player, exitMap);
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

