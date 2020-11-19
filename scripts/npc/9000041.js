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
var status = 0;
var leaf = 4001126;
var amount = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            cm.sendYesNo("Would you like to exchange your Maple Leaves for Golden Maple Leaves?\r\n#kConversion rate is 100 Maple Leaves for 1 Golden Maple Leaves.\r\n#k(Make sure you have enough room in your ETC for the coins)\r\n#k\r\n#kYou currently have " + cm.getPlayer().getItemQuantity(leaf, false) + " Maple Leaves");
        } else if (status == 1) {
            if (cm.getPlayer().getItemQuantity(leaf, false) > 100) {
                cm.sendGetText("Hello#b #h ##k, How many Golden Maple Leaves would you like?\r\n#kYou can afford to buy " + Math.floor(cm.getPlayer().getItemQuantity(leaf, false) / 100) + " Golden Maple Leaves.\r\n#kMaximum amount of Golden Maple leaves per transaction is 500 (50000 Maple Leaves).\r\n\r\n");
            } else {
                cm.sendOk("Sorry, you currently don't have enough Maple Leaves.");
                cm.dispose();
            }
        } else if (status == 2) {
            amount = cm.getText();
            if (amount > 0 && amount <= 500) {
                cm.sendYesNo("We would like to confirm you want to buy " + amount + " Golden Maple Leaves with " + amount * 100 + " Maple Leaves.");
            } else {
                cm.sendOk("Sorry, \r\n#kMaximum amount of Golden Maple Leaves per transaction is 500 (50,000 Maple Leaves).");
                cm.dispose();
            }
        } else if (status == 3) {
            var total = amount * 100;
            if (total > 0) {
                if (cm.haveItem(leaf, total)) {
                    cm.gainItem(leaf, -total);
                    cm.gainItem(4000313, amount * 1);
                    cm.sendOk("Transaction Complete.\r\n#kYou have cashed in " + amount * 100 + " Maple Leaves for " + amount + " Golden Maple Leaves.");
                    cm.dispose();
                } else {
                    cm.sendOk("Sorry, you currently don't have enough Maple Leaves.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("Sorry, you currently don't have enough Maple Leaves.");
                cm.dispose();
            }
            cm.dispose();
        }
    }
}

