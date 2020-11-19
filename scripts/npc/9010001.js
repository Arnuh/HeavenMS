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
var leaf = 4310000;
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
            cm.sendYesNo("Would you like to cash in your NX for Monster Coins?\r\n#kConversion rate is 10 NX for 1 Coin.\r\n#k(Make sure you have enough room in your ETC for the coins)\r\n#k\r\n#kYou currently have " + cm.getPlayer().getCashShop().getCash(1) + " NX");
        } else if (status == 1) {
            if (cm.getPlayer().getCashShop().getCash(1) >= 10) {
                cm.sendGetText("Hello#b #h ##k, How many Monster Coins would you like?\r\n#kYou currently can afford to buy " + Math.floor(cm.getPlayer().getCashShop().getCash(1) / 10) + " NX.\r\n#kMaximum amount of Monster coins per transaction is 25,000 (250,000 NX).\r\n\r\n");
            } else {
                cm.sendOk("Sorry, you currently don't have enough NX or Exceeded the maximum that I can exchange.");
                cm.dispose();
            }
        } else if (status == 2) {
            amount = cm.getText();
            if (amount > 0 && amount <= 25000) {
                cm.sendYesNo("We would like to confirm you want to buy " + amount + " Monster Coins with " + amount * 10 + " NX.");
            } else {
                cm.sendOk("Sorry, \r\n#kMaximum amount of Monster coins per transaction is 25,000 (250,000 NX).");
                cm.dispose();
            }
        } else if (status == 3) {
            if (cm.getPlayer().getCashShop().getCash(1) >= amount * 10) {
                cm.getPlayer().getCashShop().gainCash(1, -amount * 10);
                cm.gainItem(leaf, amount);
                cm.sendOk("Transaction Complete.\r\n#kYou have cashed in " + amount * 10 + " NX for " + amount + " Monster Coins.");
                cm.dispose();
            } else {
                cm.sendOk("Sorry, you currently don't have enough NX.");
                cm.dispose();
            }
            cm.dispose();
        }
    }
}

