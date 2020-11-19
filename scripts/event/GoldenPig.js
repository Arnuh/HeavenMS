/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>
 
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
 -- Odin JavaScript --------------------------------------------------------------------------------
 Bamboo Warrior Spawner
 -- Edited by --------------------------------------------------------------------------------------
 Ronan (based on xQuasar's King Clang spawner)
 
 **/

importPackage(Packages.client);
importPackage(Packages.tools);
importPackage(Packages.server.life);

var random = 0;
var towns = new Array(100000004, 100040003, 100040106, 101020004, 101020008, 101030103, 101030108, 101030405, 101040003, 102020300, 103000202, 103000105, 103030200, 104010001, 104040002, 105040305, 105050300, 105070002, 105090310, 105090700, 106000140, 106010106, 107000403, 130010200, 140020200, 200010300, 200050000, 211040000, 211040900, 211041700, 220010200, 220010900, 220020200, 220030100, 220060201, 220070301, 221021200, 221023200, 221024200, 221030301, 221040300, 222010102, 230020300, 230040300, 240010600, 240030300, 240040521, 250010303, 250010700, 251010200, 251010402, 261020300, 261010103, 300010200, 300020100, 300020100, 610010102, 682010202, 600020300, 800020101, 801020000);

function init() {
    scheduleNew();
}

function scheduleNew() {
    setupTask = em.schedule("start", Randomizer.rand(30, 60) * 60000);    //spawns upon server start. Each 3 hours an server event checks if boss exists, if not spawns it instantly.
}

function cancelSchedule() {
    if (setupTask != null) {
        setupTask.cancel(true);
    }
}

function start() {
    var random = Randomizer.nextInt(towns.length);
    var town = towns[random];
    var mapObj = em.getChannelServer().getMapFactory().getMap(town);
    var mobid = Randomizer.rand(9302021, 9302038);
    var mobObj = MapleLifeFactory.getMonster(mobid, mapObj.getChannelId());
    var mapName = mapObj.getStreetName();
    mapObj.getRandomSpawnPoint(mobObj);
    em.BroadcastChMsg("[EVENT] A Golden Pig has been spotted around " + mapName + "! Go find him for a massive reward.");
    setupTask = em.schedule("start", Randomizer.rand(30, 60) * 60000);
}

function startspawn() {

}