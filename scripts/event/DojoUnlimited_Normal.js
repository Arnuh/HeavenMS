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


var recruitMap = 925020001;
var entryMap = 925020001;
var exitMap = 925020001;

var eventMapId = 925020002;
var startlevel;

var eventTime = 480;     // 140 minutes

var lobbyRange = [0, 0];

function init() {

}

function setLobbyRange() {
    return lobbyRange;
}

function getEligibleParty(party) {      //selects, from the given party, the team that is allowed to attempt this event
}

function setup(player, lobbyid) {
    var eim = em.newInstance("DojoUnlimited_Normal" + player.getName());
    eim.getMapInstance(eventMapId).toggledrops(false);
    eim.getMapInstance(eventMapId).setUnlimited(true);
    eim.getMapInstance(eventMapId).toggleconsume(false);
    eim.schedule("start", 1000);
    eim.setIntProperty("basestage", 1);
    eim.setIntProperty("level", 10);
	eim.setExitMap(exitMap);
    return eim;
}

function start(eim) {
    eim.changeMusic("BgmCustom/TheStageIsSet");
    var map = eim.getMapInstance(eventMapId);
    var mobid = Randomizer.rand(9305100, 9305139);
    eim.dropMessage(5, "[Dojo] Round: " + eim.getIntProperty("basestage") + ". " + MapleLifeFactory.getMonster(mobid, 1).getName() + " has appeared.");
    map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterByLevelByChanLink(mobid, eim.getIntProperty("level"), 4, eim.getChannel(), true, false), 15, new java.awt.Point(Randomizer.rand(-250, 250), 0));
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

function end(eim) {
    eim.exitParty(exitMap);
}

function monsterKilled(mob, eim) {
	if (mob.getId() >= 9305100 && mob.getId() <= 9305139) {
		var map = eim.getMapInstance(eventMapId);
		eim.showClearEffect();
		eim.getDojoPoints(eim.getIntProperty("basestage") * (eim.getChannel() * eim.getChannel()));
		eim.setIntProperty("basestage", eim.getIntProperty("basestage") + 1);
		eim.setIntProperty("level", eim.getIntProperty("level") + 1);
		var mobid = Randomizer.rand(9305100, 9305139);
		eim.dropMessage(5, "[Dojo] Round: " + eim.getIntProperty("basestage") + ". " + MapleLifeFactory.getMonster(mobid, 1).getName() + " has appeared.");
		map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterByLevelByChanLink(mobid, eim.getIntProperty("level"), 4, eim.getChannel(), true, false), 15, new java.awt.Point(Randomizer.rand(-250, 250), 0));
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
