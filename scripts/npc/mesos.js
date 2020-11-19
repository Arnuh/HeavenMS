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
/* Author: Xterminator
 NPC Name: 		Shanks
 Map(s): 		Maple Road : Southperry (60000)
 Description: 		Brings you to Victoria Island
 */
var status = 0;
var bank = 0;
var select = 0;
var amount = 0;

function start() {
    bank = cm.getPlayer().getBank();
    cm.sendSimple("Welcome to Kaotic Bank. How can we help you? \r\n#k Your current balance is " + cm.convertNumber(bank) + " Mesos.\r\n\r\n\#L1# Withdrawl Mesos #l\r\n\#L2# Deposit Mesos #l\r\n\#L3# Withdrawl Mesos as Gold Bags #l\r\n\#L4# Deposit Gold Bags into you Bank #l\r\n");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1) {
        if (mode == 0 && type != 1) {
            status -= 2;
        } else if (type == 1 || (mode == -1 && type != 1)) {
            if (mode == 0) {
                //cm.sendOk("Hmm... I guess you still have things to do here?");
            } else {
                cm.dispose();
                return;
            }
        }
    }
    if (status == 1) {
        select = selection;
        if (select == 1) {
			if (bank > 0) {
				cm.sendGetText("Hello#b #h ##k, How much mesos would you like to withdrawl today?\r\n#kMaximum Withdrawl limit is 2 Billion Mesos.\r\n\r\n#kYour current balance in your bank is " + cm.convertNumber(bank) + ".\r\n\r\n");
			} else {
				cm.sendOk("Sorry, your bank is currently empty.");
				cm.dispose();
			}
        } else if (select == 2) {
            cm.sendGetText("Hello#b #h ##k, How much mesos would you like to deposit today?\r\n\r\n#kYour current balance is " + cm.convertNumber(bank) + ".\r\n#kYour currently have " + cm.convertNumber(cm.getPlayer().getMeso()) + " in your wallet.\r\n\r\n");
        } else if (select == 3) {
			if (bank > 0) {
				cm.sendGetText("Hello#b #h ##k, How many meso bags would you like to withdrawl today?\r\n\r\n#kEach meso bag is worth 1 billion mesos.\r\n#kYour current balance in your bank is " + cm.convertNumber(bank) + ".\r\n\r\n");
			} else {
				cm.sendOk("Sorry, your bank is currently empty.");
				cm.dispose();
			}
        } else if (select == 4) {
            cm.sendGetText("Hello#b #h ##k, How many meso bags would you like to deposit today?\r\n\r\n#kEach meso bag is worth 1 billion mesos.\r\n\#kYour currently have " + cm.getPlayer().getItemQuantity(4310001, false) + " meso bags you can deposite.\r\n\r\n");
        }
    } else if (status == 2) {
        amount = Number(cm.getText());
        if (amount > 0) {
            if (select == 1) {//withdrawl
				if (amount <= 2100000000) {
					if (bank >= amount) {
						if (cm.getPlayer().canHoldMeso(amount)) {
							cm.sendYesNo("Do you confirm this trasaction?");
						} else {
							cm.sendOk("Sorry, you can't carry this much.");
							cm.dispose();
						}
					} else {
						cm.sendOk("Sorry, you do not have enough mesos in your bank.");
						cm.dispose();
					}
				} else {
					cm.sendOk("Sorry, you cannot withdrawl more than 2,000,000,000 at time.");
					cm.dispose();
				}
            } else if (select == 2) {//depo
				if (amount <= 2100000000) {
					if (cm.getPlayer().getMeso() >= amount) {
						cm.sendYesNo("Do you confirm this trasaction?");
					} else {
						cm.sendOk("Sorry, no cheaty doddles here.");
						cm.dispose();
					}
				} else {
					cm.sendOk("Sorry, no cheaty doddles here.");
					cm.dispose();
				}
            } else if (select == 3) {
                if (bank >= (amount * 1000000000)) {
                    cm.sendYesNo("Do you confirm this trasaction?");
                } else {
                    cm.sendOk("Sorry, no cheaty doddles here.");
                    cm.dispose();
                }
            } else if (select == 4) {
                if (cm.haveItem(4310001, amount)) {
                    cm.sendYesNo("Do you confirm this trasaction?");
                } else {
                    cm.sendOk("Sorry, no cheaty doddles here.");
                    cm.dispose();
                }
            }
        } else {
            cm.sendOk("Sorry, no cheaty doddles here.");
            cm.dispose();
        }
    } else if (status == 3) {
        if (select == 1) {
            cm.getPlayer().gainMeso(amount);
            cm.getPlayer().updateBank(-amount);
            cm.sendOk("You have successfully withdrawn " + amount + " mesos from your bank. Remaining balance is " + cm.getPlayer().getBank() + " Mesos.");
            cm.dispose();
        } else if (select == 2) {
            cm.getPlayer().gainMeso(-amount);
            cm.getPlayer().updateBank(amount);
            cm.sendOk("You have successfully deposited " + amount + " mesos into your bank. New balance is " + cm.getPlayer().getBank() + " Mesos.");
            cm.dispose();
        } else if (select == 3) {
            cm.getPlayer().updateBank(-(amount * 1000000000));
            cm.gainItem(4310001, amount);
            cm.sendOk("You have successfully withdrawn " + amount + " meso bags from your bank. New balance is " + cm.getPlayer().getBank() + " Mesos.");
            cm.dispose();
        } else if (select == 4) {
            cm.getPlayer().updateBank(amount * 1000000000);
            cm.gainItem(4310001, -amount);
            cm.sendOk("You have successfully deposited " + amount + " meso bags into your bank. New balance is " + cm.getPlayer().getBank() + " Mesos.");
            cm.dispose();
        }
    } else {
        cm.dispose();
    }
}

