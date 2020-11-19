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
 
 
 cm.gainItem(4310001, amount);
 cm.getPlayer().getCashShop().gainCash(2, amount);
 
 */

var status;
var option = 0;
var item = 0;
var mPoint = 0;
var amount = 0;
var slots = new Array(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11, -12, -13, -15, -16, -50, -112, -113, -115, -116);
var power = 0;
var cost = 0;




function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode < 0)
        cm.dispose();
    else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0 && mode == 1) {
            cm.sendGetText("Hello#b #h ##k, How many #i2049186# would you like to craft?\r\n\ You will need the following items:\r\n\r\n#k25x of Each: #i2049180##i2049181##i2049182##i2049183##i2049184#, 1x #i4310001#.\r\n\r\n");
        } else if (status == 1) {
            amount = Number(cm.getText());
            if (amount > 0) {
                if (cm.haveItem(2049180, 25 * amount) && cm.haveItem(2049181, 25 * amount) && cm.haveItem(2049182, 25 * amount) && cm.haveItem(2049183, 25 * amount) && cm.haveItem(2049184, 25 * amount) && cm.haveItem(4310001, amount)) {
                    cm.gainItem(2049180, -(amount * 25));
                    cm.gainItem(2049181, -(amount * 25));
                    cm.gainItem(2049182, -(amount * 25));
                    cm.gainItem(2049183, -(amount * 25));
                    cm.gainItem(2049184, -(amount * 25));
                    cm.gainItem(4310001, -(amount));


                    cm.gainItem(2049186, amount);
                    cm.sendOk(amount + " Super Power Scrolls have been crafted for you. Thank You.");
                    cm.dispose();

                } else {
                    cm.sendOk("Sorry, you dont seem to have all of the items needed.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("Sorry, no cheaty cheaty here.");
                cm.dispose();
            }
        } else {
            cm.sendOk("Have a good day.");
            cm.dispose();
        }
    }
}