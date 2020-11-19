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
var amount = 0;

function start() {
    amount = cm.getPlayer().getTotalLevel() * 10;
    if (cm.getEquipbySlot(1) != null) {
        cm.sendYesNo("Would you like to reset " + cm.getItemName(cm.getEquipbySlot(1).getItemId()) + " at the cost of " + amount + " Golden Maple Leaves? Please make sure the equip you wish to reset is in the first equip slot and 2nd slot is empty. Note: The equip you are reseting will be gone forever and new equip replaces it clean version that is scaled to your level multiplied by Random 1x-2x.");
    } else {
        cm.sendOk("Sorry. make sure there is an equip in slot 1.");
        cm.dispose();
    }
}

function action(mode, type, selection) {
    status++;
    if (mode != 1) {
        if (mode == 0 && type != 1) {
            status -= 2;
        } else if (type == 1 || (mode == -1 && type != 1)) {
            if (mode == 0) {
                cm.sendOk("Hmm... I guess you still have things to do here?");
            }
            cm.dispose();
            return;
        }
    }
    if (status == 1) {
        if (cm.haveItem(leaf, amount)) {
            cm.gainItem(leaf, -amount);
			var power = (cm.getPlayer().getTotalLevel() * cm.getPlayer().getTotalLevel()) / 10;
            cm.resetItem(1, (power * cm.getPlayer().lootDropChance()));
            cm.sendOk("Your Equip has been successfully reseted based on your level.");
            cm.dispose();
        } else {
            cm.sendOk("Sorry. You need a minimum of " + amount + " Golden Maple Leaves.");
            cm.dispose();
        }
        cm.dispose();
    }
}

