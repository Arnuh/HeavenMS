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
var leaf = 4000313;

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
            cm.sendYesNo("Would you like to reset " + cm.getItemName(cm.getEquipbySlot(1).getItemId()) + "? If not please place the equip you wish to reset in the first equip slot. Note: The equip you are reseting will be gone forever and new equip replaces it clean with new powers.");
        } else if (status == 1) {
            if (cm.haveItem(leaf)) {
                cm.sendGetText("Hello#b #h ##k, you currently have #b#c4000313# Golden Maple Leaves.\r\n#kHow many would you like to use on " + cm.getItemName(cm.getEquipbySlot(1).getItemId()) + "? \r\nThere is max of 9999 GML's per transaction. Each GML counts as 1 level Power towards the equip.\r\n\r\n ");
            } else {
                cm.sendOk("Sorry, you currently dont have any Golden Maple Leaves.");
                cm.dispose();
            }
        } else if (status == 2) {
            amount = cm.getText();
            if (amount <= 9999 && amount > 0) {
                if (cm.haveItem(leaf, amount)) {
                    cm.gainItem(leaf, -amount);
                    cm.resetItem(1, amount);
                    cm.sendOk("You Equip has been successfully reseted to its new power.");
                    cm.dispose();
                } else {
                    cm.sendOk("Sorry, you need blessing of the fairy at or greater than level 12. You can obtain this by leveling a GM job char to level 120. Please speak to Shanks at the docks at level 10.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("Sorry, you need blessing of the fairy at or greater than level 12. You can obtain this by leveling a GM job char to level 120. Please speak to Shanks at the docks at level 10.");
                cm.dispose();
            }
            cm.dispose();
        }
    }
}

