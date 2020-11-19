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
var slots = new Array(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11, -12, -13, -15, -16, -50, -112, -113, -115, -116);
var option = 0;

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
            var selStr = "";
            for (var i = 0; i < slots.length; i++) {
                if (cm.getEquippedbySlot(slots[i]) != null) {
                    selStr += "#L" + i + "# " + cm.getItemName(cm.getEquippedbySlot(slots[i]).getItemId()) + "#l\r\n\ ";
                }
            }

            if (cm.haveItem(2470000)) {
                cm.sendSimple("Which equip would you like to Hammer?\r\n\ " + selStr);
            } else {
                cm.sendOk("Sorry, you currently don't have any Golden Hammers.");
                cm.dispose();
            }
        } else if (status == 1) {
            option = slots[selection];
            cm.sendYesNo("Would you like to Upgrade " + cm.getItemName(cm.getEquippedbySlot(option).getItemId()) + "? The Cost for hammering is 1 Golden Hammer.");
        } else if (status == 2) {
            if (cm.getPlayer().upgradeEquipSlot(option)) {
                cm.gainItem(2470000, -1);
                cm.sendOk("Your Equip has been successfully hammered.");
            } else {
                cm.sendOk("Your Equip currently does not have a hammer slot available.");
            }
            cm.dispose();
        }
    }
}

