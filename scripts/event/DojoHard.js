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


var recruitMap = 925020001;
var entryMap = 925020001;
var exitMap = 925020001;

var eventMapId = 925020002;

var startMobId = 9305200;
var endMobId = 9305239;

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
    var eim = em.newInstance("DojoNormal" + player.getName());
    eim.getMapInstance(eventMapId).toggledrops(false);
    eim.getMapInstance(eventMapId).toggleconsume(false);
    eim.schedule("start", 1000);
    eim.setIntProperty("stage", 1);
    eim.setIntProperty("startmob", startMobId);
    eim.setIntProperty("endmob", endMobId);
	eim.setExitMap(exitMap);
    return eim;
}

function playerEntry(eim, player) {
    player.changeMap(eim.getMapInstance(eventMapId), 0);
    player.dropMessage(6, "[Dojo] Event will begin in few seconds. Players: " + eim.getPlayers());

}

//event --------------------------------------------------------------------

function start(eim) {
    eim.changeMusic("BgmCustom/TheStageIsSet");
    var map = eim.getMapInstance(eventMapId);
    var mob = MapleLifeFactory.getMonsterRankLink(startMobId, eim.getChannel(), 4, false);
    map.spawnMonsterWithEffect(mob, 15, new java.awt.Point(Randomizer.rand(-250, 250), 0));
    eim.dropMessage(6, "[Dojo] Battle has begun. Good Luck!!!");
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
    eim.showClearEffect();
    var map = eim.getMapInstance(eventMapId);
    map.broadcastMessage(MaplePacketCreator.playSound("Dojang/clear"));
    var amount = (eim.getIntProperty("stage") * 10) * eim.getChannel();
    if (eim.getdojorate()) {
        amount *= 2;
    }
    eim.getDojoPoints(amount);
    eim.setIntProperty("stage", eim.getIntProperty("stage") + 1);
    if (mobId >= eim.getIntProperty("startmob") && mobId < eim.getIntProperty("endmob")) {
        map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterRankLink(mobId + 1, eim.getChannel(), 4, false), 15, new java.awt.Point(Randomizer.rand(-250, 250), 0));
    }
    if (mobId == eim.getIntProperty("endmob")) {
        eim.setIntProperty("finished", 1);
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

