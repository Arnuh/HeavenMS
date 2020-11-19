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

var monsters = new Array(8610005, 8610006, 8610007, 8610008, 8610009);
var elites = new Array(8610010, 8610011, 8610012, 8610013, 8610014);
var bosses = new Array(8850003, 8850001, 8850004, 8850002, 8850000);

var recruitMap = 80000;
var entryMap = 80000;
var exitMap = 80000;


var eventMapId = 81600;

var eventTime = 480; //60 minutes

var lobbyRange = [0, 8];

function setLobbyRange() {
    return lobbyRange;
}

function init() {
}

function setup(player, scale) {
    var eim = em.newInstance("MP_Empress" + player.getName());
    eim.setIntProperty("finish", 0);
    eim.schedule("start", 10 * 1000);
    eim.createEventTimer(10 * 1000);
    eim.setExitMap(exitMap);
    eim.setIntProperty("scale", scale);
    return eim;
}

function getEligibleParty(party) {      //selects, from the given party, the team that is allowed to attempt this event

}

function playerEntry(eim, player) {
    player.changeMap(eim.getMapInstance(81600), 0);
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

function playerRevive(eim, player) { // player presses ok on the death pop up.

}

function monsterKilled(mob, eim) {
    var map = eim.getMapInstance(81600);
    if (mob.getId() == 8850011) {
        eim.setIntProperty("finish", 1);
        eim.setExitMap(exitMap);
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

function start(eim) {
    eim.startEventTimer(eventTime * 60000);
    var map = eim.getMapInstance(81600);
    //knights
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(8850000, eim.getChannel(), eim.getIntProperty("scale"), false), new java.awt.Point(Randomizer.rand(20, 1280), -25), false);
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(8850001, eim.getChannel(), eim.getIntProperty("scale"), false), new java.awt.Point(Randomizer.rand(20, 1280), -25), false);
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(8850002, eim.getChannel(), eim.getIntProperty("scale"), false), new java.awt.Point(Randomizer.rand(20, 1280), -25), false);
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(8850003, eim.getChannel(), eim.getIntProperty("scale"), false), new java.awt.Point(Randomizer.rand(20, 1280), -25), false);
    map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(8850004, eim.getChannel(), eim.getIntProperty("scale"), false), new java.awt.Point(Randomizer.rand(20, 1280), -25), false);
    //empress
    map.spawnMonsterWithEffect(MapleLifeFactory.getMonsterCustomRank(8850011, eim.getChannel(), eim.getIntProperty("scale"), false), 15, new java.awt.Point(440, -65));
    eim.schedule("boss", Randomizer.rand(45, 90) * 1000);
    eim.schedule("mob", Randomizer.rand(30, 60) * 1000);



}

function boss(eim) {
    var map = eim.getMapInstance(81600);
    if (eim.getIntProperty("finish") < 1) {
        var random = Randomizer.rand(0, 4);
        var monster = bosses[random];
        for (var i = 1; i <= Randomizer.rand(2, 4); i++) {
            map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(monster, eim.getChannel(), eim.getIntProperty("scale") - 2), new java.awt.Point(Randomizer.rand(20, 1280), -25), false);
        }
        eim.schedule("boss", Randomizer.rand(120, 300) * 1000);
        eim.dropMessage(6, "[Empress] Come forth my greatest knights!!!");
    }
}

function mob(eim) {
    var map = eim.getMapInstance(81600);
    if (eim.getIntProperty("finish") < 1) {
        var random = Randomizer.rand(0, 4);
        var monster = elites[random];
        for (var i = 1; i <= Randomizer.rand(10, 20); i++) {
            map.spawnMonsterOnGroundBelow(MapleLifeFactory.getMonsterCustomRank(monster, eim.getChannel(), eim.getIntProperty("scale") - 1), new java.awt.Point(Randomizer.rand(20, 1280), -25), false);
        }
        eim.schedule("mob", Randomizer.rand(30, 120) * 1000);
        eim.dropMessage(6, "[Empress] Come forth my knights!!!");
    }
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

