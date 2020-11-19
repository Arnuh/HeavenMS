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
var slot = 0;
var scroll = 0;
var scrollslot = 0;
var amount = 0;
var slotcount = 0;
var power = 0;

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
            for (var i = 0; i < 80; i++) {
                if (cm.getEquipbySlot(i) != null) {
                    if (cm.getEquipbySlot(i).getPowerLevel() >= 1000) {
                        selStr += "#L" + i + "# " + cm.getItemName(cm.getEquipbySlot(i).getItemId()) + " (Power: "+cm.getEquipbySlot(i).getPowerLevel()+") #l\r\n\ ";
                        slotcount += 1;
                    }
                }
            }
            if (slotcount > 0) {
                cm.sendSimple("Which equip would you like to recycle?\r\n\ " + selStr);
            } else {
                cm.sendOk("You currently do not have any equips to recycle. Equips must have minium of 1000 power to exchange.");
                cm.dispose();
            }
        } else if (status == 1) {
            slot = selection;
            if (cm.getEquipbySlot(slot) != null) {
                power = Math.round(cm.getEquipbySlot(slot).getPowerLevel() / 1000);
                cm.sendYesNo("This equip is worth " + power + " Shadow Coins. Would you like to recycle this equip?");
            } else {
                cm.sendOk("Come back another day.");
                cm.dispose();
            }
        } else if (status == 2) {
            cm.removeEquipFromItemSlot(slot);
            cm.gainItem(4310059, power);
            cm.sendOk("Thank You. You equip was recycled for "+power+" Shadow Coins.");
            cm.dispose();
        }
    }
}

