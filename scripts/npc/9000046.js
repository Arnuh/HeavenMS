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
            for (var i = 0; i < 96; i++) {
                if (cm.getEquipbySlot(i) != null) {
					if (!cm.getEquipbySlot(i).isCash(cm.getEquipbySlot(i).getItemId())) {
						if (cm.getPlayer().getCanScroll(i)) {
							selStr += "#L" + i + "# " + cm.getItemName(cm.getEquipbySlot(i).getItemId()) + "#l\r\n\ ";
							slotcount += 1;
						}
					}
                }
            }
            if (slotcount > 0) {
                cm.sendSimple("Which equip would you like to Auto Scroll?\r\n\ " + selStr);
            } else {
                cm.sendOk("You currently do not have any equips to scroll.");
                cm.dispose();
            }
        } else if (status == 1) {
            slot = selection;
            if (cm.getEquipbySlot(slot) != null) {
                cm.sendYesNo("Would you like to Scroll " + cm.getItemName(cm.getEquipbySlot(slot).getItemId()) + "?");
            } else {
                cm.sendOk("Come back another day.");
                cm.dispose();
            }
        } else if (status == 2) {
            slotcount = 0;
            var selStr = "";
            for (var i = 0; i < 96; i++) {
                if (cm.getUsebySlot(i) != null) {
                    if (Math.floor(cm.getUsebySlot(i).getItemId() / 10000) == 204) {
                        selStr += "#L" + i + "# " + cm.getItemName(cm.getUsebySlot(i).getItemId()) + " ("+cm.getPlayer().getItemQuantity(cm.getUsebySlot(i).getItemId(), false)+"x)#l\r\n\ ";
                        slotcount += 1;
                    }
                }
            }
            if (slotcount > 0) {
                cm.sendSimple("Which Scroll would you like to use?\r\n\ " + selStr);
            } else {
                cm.sendOk("You currently do not have any scrolls.");
                cm.dispose();
            }
        } else if (status == 3) {
            scrollslot = selection;
            if (cm.getUsebySlot(scrollslot) != null) {
				scroll = cm.getUsebySlot(scrollslot).getItemId();
                cm.sendGetText("How many " + cm.getItemName(cm.getUsebySlot(scrollslot).getItemId()) + " would you like to use?\r\n\You currently have ("+cm.getPlayer().getItemQuantity(cm.getUsebySlot(scrollslot).getItemId(), false)+") scrolls to use.\r\n\There is a limit of 2500 Scrolls per transaction here.\r\n\r\n");
            } else {
                cm.sendOk("Come back another day.");
                cm.dispose();
            }
        } else if (status == 4) {
            amount = Number(cm.getText());
            if (amount > 0) {
                if (amount <= 2500) {
                    if (cm.haveItem(cm.getUsebySlot(scrollslot).getItemId(), amount)) {
                        cm.sendYesNo("Let's Confirm you would you like to Scroll " + cm.getItemName(cm.getEquipbySlot(slot).getItemId()) + " with " + amount + " " + cm.getItemName(cm.getUsebySlot(scrollslot).getItemId()) + "?\r\n");
                    } else {
                        cm.sendOk("I'm sorry. you don't you have enough scrolls.");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("I'm sorry. there's a limit of 250 scrolls per transaction.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("I'm sorry. you don't you have enough scrolls.");
                cm.dispose();
            }
        } else if (status == 5) {
            if (cm.getPlayer().getMeso() < (amount * 100000)) {
                cm.sendOk("I'm sorry. you don't you have enough Mesos.");
            } else {
                var scrolls = cm.getPlayer().autoScroll(amount, slot, scroll);
                if (scrolls > 0) {
                    cm.sendOk(scrolls + " Scrolls were consumed and the total fee for scrolling was "+(amount * 100000)+" mesos.");
                } else {
                    cm.sendOk("Your Equip currently does not have any upgrades available.");
                }
            }
            cm.dispose();
        }
    }
}

