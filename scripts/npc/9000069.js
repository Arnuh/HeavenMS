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
 cm.getPlayer().addMP( amount);
 
 */

importPackage(Packages.client);
importPackage(Packages.tools);
importPackage(Packages.server.life);

var status;
var option = 0;
var item = 0;
var mPoint = 0;
var amount = 0;
var slots = new Array(-1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11, -12, -13, -15, -16, -50, -112, -113, -115, -116);
var power = 0;
var cost = 0;
var rewards = [5520000, 5370000, 1702099, 1702220, 1702190, 1372035, 1372036, 1372037, 1372038, 1372039, 1372040, 1372041, 1372042, 1382045, 1382046, 1382047, 1382048, 1382049, 1382050, 1382051, 1382052, 1302098, 1302099, 1302100, 1302101, 2070001, 2070002, 2070003, 2070004, 2070005, 2070006, 2070007, 5000054, 5000100, 1812000, 1812001, 1812002, 1812003, 1812004, 2010000];



function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode < 0) {
        cm.dispose();
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0 && mode == 1) {
            mPoint = cm.getPlayer().getCashShop().getCash(2);
            //cm.sendOk("Sorry, npc is temp closed");
            //cm.dispose();
            //(#b10% off#k) - sale tag
            cm.sendSimple("How would you like to spend your Maple Points?\r\n\You currently have " + mPoint + " Maple Points.\r\n\#L1#Buy Chaos Scroll - 1 MP#l\r\n\#L2#Exchange Monster Coin for MP#l\r\n\#L3#Exchange MP for VIP#l\r\n\#L4#Exchange MP for SOK\r\n\#L5#MP Gachapon");
        } else if (status == 1) {
            option = selection;

            if (option == 1) {//2340000 white scroll
                if (mPoint > 0) {
                    cm.sendGetText("Hello#b #h ##k, How many Chaos Scrolls would you like?\r\n\r\n#kEach Scroll is worth 1 Maple Point.\r\n\#kYour currently have " + mPoint + " Maple Points that you can exchange for.\r\n\r\n");
                } else {
                    cm.sendOk("Sorry, you don\'t have enough maple points.");
                    cm.dispose();
                }
            }
            if (option == 2) {
                if (mPoint > -1) {
					cm.sendGetText("Hello#b #h ##k, How many Monster coin(s) would you like to trade in?\r\n\r\n#k1000 Each Monster coin is worth 1 Maple Point.\r\n\r\n");
                } else {
                    cm.sendOk("Something went wrong. :/");
                    cm.dispose();
                }
            }
			if (option == 3) {
                if (mPoint > -1) {
                    cm.sendGetText("Hello#b #h ##k, How many hours of VIP would you like?\r\n\r\n#kEach Maple Point is worth 4 hours of VIP Service, which gives you 2x EXP and DROP rates. This stacks with all other bonuses.\r\n\#kYour currently have " + mPoint + " Maple Points that you can exchange for.\r\n\r\n");
                } else {
                    cm.sendOk("Something went wrong. :/");
                    cm.dispose();
                }
            }
			if (option == 4) {
                if (mPoint > -1) {
					cm.sendGetText("Hello#b #h ##k, How many Scissors Of Karma would you like?\r\n\r\n#k1 SOK is is worth 10 Maple Points.\r\n\r\n");
                } else {
                    cm.sendOk("Something went wrong. :/");
                    cm.dispose();
                }
			}
			if (option == 5) {
				if (mPoint > -1) {
                cm.sendYesNo("Give me 10 MP and I will give you a random reward in exchange. Possible rewards are:\r\ #i5520000# #i5370000# #i1702099# #i1702220# #i1702190# #i1372035# #i1372036# #i1372037# #i1372038# #i1372039# #i1372040# #i1372041# #i1372042# #i1382045# #i1382046# #i1382047# #i1382048# #i1382049# #i1382050# #i1382051# #i1382052# #i1302098# #i1302099# #i1302100# #i1302101# #i2070001# #i2070002# #i2070003# #i2070004# #i2070005# #i2070006# #i2070007# #i5000054# #i5000100# #i1812000# #i1812001# #i1812002# #i1812003# #i1812004# #i2010000#");
            } else {
                cm.sendOk("Sorry, you don\'t have enough maple points.");
                cm.dispose();
            }
		}

        //cm.sendOk(text);
        //cm.dispose();
        } else if (status == 2) {
			if (option == 1) {
                amount = Number(cm.getText());
                if (mPoint >= amount) {
                    if (cm.canHold(2049100, amount)) {
                        cm.gainItem(2049100, amount);
                        cm.getPlayer().getCashShop().gainCash(2, -amount);
                        cm.sendOk("You have gained " + amount + " Chaos Scrolls.");
                        cm.dispose();
                    } else {
                        cm.sendOk("Sorry, you do not have enough room in your inventory for this.");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("Sorry, you do not have enough maple points for this.");
                    cm.dispose();
                }
			}
            if (option == 3) {
                amount = Number(cm.getText());
                if (mPoint >= amount && amount > 0) {
                    cm.sendYesNo("Do you confirm that you want to spend " + amount + " Maple Points on " + (amount * 4) + " hours of VIP? \r\n\.");
                } else {
                    cm.sendOk("Sorry, you do not have enough maple points for this.");
                    cm.dispose();
                }
            }
				if (option == 2) {
                amount = Number(cm.getText());
                if (cm.haveItem(4310000, amount)) {
                    cm.gainItem(4310000, -amount);
                    cm.getPlayer().getCashShop().gainCash(2, amount / 1000);
                    cm.sendOk("You have gained " + amount / 1000 + " Maple Points.");
                    cm.dispose();
                } else {
                    cm.sendOk("Sorry, you do not have enough Monster Coin for this.");
                    cm.dispose();
                }
			}
				if (option == 4) {
                amount = Number(cm.getText());
                if (mPoint >= amount * 10 && amount > 0 && cm.canHold(5520000, amount)) {
                    cm.gainItem(5520000, amount);
                    cm.getPlayer().getCashShop().gainCash(2, -(amount * 10));
                    cm.sendOk("You have gained " + amount + " Scissors Of Karma.");
                    cm.dispose();
                } else {
                    cm.sendOk("Sorry, you do not have enough MP for this or your inventory is full.");
                    cm.dispose();
                }
			}
			if (option == 5) {
				randomNum = Randomizer.rand(0, rewards.length - 1);
				itemDec = rewards[randomNum];
                if (mPoint >= 10 && cm.canHold(itemDec, 1)) {
                    cm.gainItem(itemDec, 1);
                    cm.getPlayer().getCashShop().gainCash(2, -10);
                    cm.sendOk("You have gained a #i"+itemDec+"#");
                    cm.dispose();
                } else {
                    cm.sendOk("Sorry, you do not have enough MP for this or your inventory is full.");
                    cm.dispose();
                }
			}

            
        } else if (status == 3) {
            if (option == 3) {
                cm.getPlayer().getCashShop().gainCash(2, -(amount));
                cm.getPlayer().addVip(amount * 4);
                cm.sendOk("You have successfull added " + (amount * 4) + " hours of vip to your account");
                cm.dispose();
            }
            cm.dispose();
		} else {
            cm.sendOk("Have a good day.");
            cm.dispose();
				}
			}
		}