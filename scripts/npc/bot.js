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
/* NPC Base
 Map Name (Map ID)
 Extra NPC info.
 */
importPackage(Packages.tools);

var status;
var option = 0;
var random = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode < 0) {
        cm.getPlayer().setTested(false);
        cm.getPlayer().updateHpMp(0);
        cm.dispose();
    } else {
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            cm.getPlayer().setTested(true);
            cm.sendNext("Hello#b #h ##k, Welcome to anti-bot script. If you pass this test you will be rewarded with 1 Maple Point. All you have to do a solve a simple 4 Digit Code.", 1);
        } else if (status == 1) {
            random = Randomizer.rand(1000, 9999);
            cm.sendGetText("Please enter following 4 digit code " + random + " to be rewarded.\r\n\r\n", 1);
        } else if (status == 2) {
            amount = Number(cm.getText());
            if (amount == random) {
                cm.getPlayer().setTested(false);
                cm.getPlayer().getCashShop().gainCash(2, 1);
                cm.sendOk("You have passed the bot test. You have gained 1 Maple Point", 1);
            } else {
                cm.getPlayer().setTested(false);
                cm.getPlayer().updateHpMp(0);
            }
            cm.dispose();
        }
    }
}