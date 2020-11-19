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
var equip;
var equips;
var equiplist;
var options = 0;
var itemcount = 0;

function start() {
    status = 0;
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
            cm.dispose();
        }
        if (status == 1) {
            cm.sendSimple("Which system would you like to access?\r\n\#L7#Inventory#l\r\n\#L8#Overflow Storage#l");
        } else if (status == 2) {
            if (selection == 7) {
                cm.dispose();
                cm.openNpc(9010106, 3004038);
            }
            if (selection == 8) {
                if (!cm.getPlayer().getEquipOverflow().isEmpty()) {
                    itemcount = cm.getPlayer().getEquipOverflow().size();
                    if (itemcount > 750) {
                        cm.sendSimple("You currently have " + itemcount + " items in your overflow system. \r\nWhich option would you like to do?\r\n\#L1#View Items 1-250#l\r\n\#L2#View Items 251-500#l\r\n\#L3#View Items 501-750#l\r\n\#L4#View Items 751-1000#l\r\n\#L9#Recycle All#l");
                    } else if (itemcount > 500) {
                        cm.sendSimple("You currently have " + itemcount + " items in your overflow system. \r\nWhich option would you like to do?\r\n\#L1#View Items 1-250#l\r\n\#L2#View Items 251-500#l\r\n\#L3#View Items 501-750#l\r\n\#L9#Recycle All#l");
                    } else if (itemcount > 250) {
                        cm.sendSimple("You currently have " + itemcount + " items in your overflow system. \r\nWhich option would you like to do?\r\n\#L1#View Items 1-250#l\r\n\#L2#View Items 251-500#l\r\n\#L9#Recycle All#l");
                    } else {
                        cm.sendSimple("You currently have " + itemcount + " items in your overflow system. \r\nWhich option would you like to do?\r\n\#L1#View Items#l\r\n\#L9#Recycle All#l");
                    }
                } else {
                    cm.sendOk("You currently do not have any equips in overflow system.");
                    cm.dispose();
                }
            }
        } else if (status == 3) {
            options = selection;
            if (options == 1 || options == 2 || options == 3 || options == 4) {
                var indexStart = (options-1)*250;
                var indexEnd = indexStart+250;
                if (cm.getPlayer().getEquipOverflow().size() < indexEnd) {
                    equips = cm.getPlayer().getEquipOverflow().subList(indexStart, cm.getPlayer().getEquipOverflow().size());
                } else {
                    equips = cm.getPlayer().getEquipOverflow().subList(indexStart, indexEnd);
                }
                var selStr = "";
                for (var i = 0; i < equips.size(); i++) {
                    var curEquip = equips.get(i);
                    if (curEquip != null) {
                        selStr += "#L" + i + "# Item: "+ (i + (250 * (options-1)))+" #i"+curEquip.getItemId()+"# " + cm.getItemName(curEquip.getItemId()) + " (Power: "+curEquip.getPowerLevel()+") #l\r\n\ ";
                    } else {
                        break;
                    }
                }
                cm.sendSimple("Which equip would you like to view?\r\n\ " + selStr);
            }
            if (options == 9) {
                cm.sendYesNo("Do you confirm that you want to recycle all equips in overflow storage system?");
            }
        } else if (status == 4) {
            if (options == 1 || options == 2 || options == 3 || options == 4) {
                equip = equips.get(selection);
                cm.sendSimple( "#i"+equip.getItemId()+"# " + cm.getItemName(equip.getItemId()) + " (Power: "+equip.getPowerLevel()+") \r\nStats:\r\nStr: "+equip.getStr()+"\r\nDex: "+equip.getDex()+"\r\nInt: "+equip.getInt()+"\r\nLuk: "+equip.getLuk()+"\r\nWeapon Attack: "+equip.getWatk()+"\r\nMagic Attack: "+equip.getMatk()+"\r\nWeapon Defense: "+equip.getWdef()+"\r\nMagic Defense: "+equip.getMdef()+"\r\n\r\nWhich option would you like to do?\r\n\#L3#Withdrawl#l\r\n\#L4#Recycle#l");
            }
            if (options == 9) {
                if (power == 0) {
                    power = 1;
                }
                power = cm.getPlayer().recycle();
                cm.gainItem(4310059, power);
                cm.getPlayer().clearEquipOverflow();
                cm.sendOk("Thank You. All equips in overflow storage have been recycled for " + power + " Shadow Coins.");
                cm.dispose();
            }
			
        } else if (status == 5) {
            options = selection;
            if (options == 3) {
                if (cm.getPlayer().canHold(equip.getItemId())) {
                    cm.gainEquip(equip);
                    cm.getPlayer().removeEquipOverflow(equip);
                    cm.sendOk("Thank You. #i" + equip.getItemId() + "# was transfered to your inventory.");
                    cm.dispose();
                } else {
                    cm.sendOk("Please make sure you have enough space to hold this item");
                    cm.dispose();
                }
            }
            if (options == 4) {
                power = Math.round(equip.getPowerLevel() / 1000);
                if (power == 0) {
                    power = 1;
                }
                cm.sendYesNo("This equip is worth " + power + " Shadow Coins. Would you like to recycle this equip?");
            }			
        } else if (status == 6) {
            cm.gainItem(4310059, power);
            cm.getPlayer().removeEquipOverflow(equip);
            cm.sendOk("Thank You. Your equip has been recycled for " + power + " Shadow Coins.");
            cm.dispose();
        }
    }
}

