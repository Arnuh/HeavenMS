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
 * @npc: Abdula
 * @map: Multiple towns on Maplestory
 * @func: Job Skill / Mastery Book Drop Announcer
 */

var status = 0;
var selected = 0;
var skillbook = [], masterybook = [], table = [];

function start() {
    if (cm.getPlayer().isWarrior()) {
        cm.openShopNPC(12000);
    } else if (cm.getPlayer().isMage()) {
        cm.openShopNPC(12001);
    } else if (cm.getPlayer().isBowman()) {
        cm.openShopNPC(12002);
    } else if (cm.getPlayer().isThief()) {
        cm.openShopNPC(12003);
    } else if (cm.getPlayer().isPirate()) {
        cm.openShopNPC(12004);
    } else {
        cm.sendOk("I sell Mastery Skill Books to those who need them.");
    }
    cm.dispose();
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.dispose();
            return;
        }
        status--;
    }
}
