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




function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode < 0)
        cm.dispose();
    else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0 && mode == 1) {
            mPoint = cm.getPlayer().getCashShop().getCash(2);
            //cm.sendOk("Sorry, npc is temp closed");
            //cm.dispose();
            //(#b10% off#k) - sale tag
            cm.sendSimple("How would you like to spend your Maple Points?\r\n\You currently have " + mPoint + " Maple Points.\r\n\#L1#Exchange MP to Maple Point Items#l\r\n\#L3#Exchange Maple Point Items to MP#l\r\n\#L2#Exchange NX to MP#l\r\n\r\n\r\n\#L4#Buy Megalixier - 1 MP#l\r\n\#L5#Buy Weakened Totem - 1 MP#l\r\n\#L7#Buy Chaos Scroll - 1 MP#l\r\n\#L18#Buy Power Scrolls - 1 MP#l\r\n\#L19#Buy Super Power Scrolls 5 MP#l\r\n\#L20#Buy Random Skill Panel (Rank A) 10 MP#l\r\n\#L15#Buy Frenzy Totem (Perm) - 100 MP#l\r\n\#L16#Buy Guardian Totem (Perm) - 250 MP#l\r\n\#L17#Buy Raging Totem (Perm) - 500 MP#l\r\n\#L13#Buy VIP 1 MP (4 Hours)");
        } else if (status == 1) {
            option = selection;

            if (option == 1) {
                if (mPoint > 0) {
                    cm.sendGetText("Hello#b #h ##k, How many Maple Point Items would you like?\r\n\r\n#kMaple Point Item is worth 1 Maple Point.\r\n\#kYour currently have " + mPoint + " Maple Points that you can exchange for Maple Point Items.\r\n\r\n");
                } else {
                    cm.sendOk("Sorry, you dont seem to have any maple points.");
                    cm.dispose();
                }
            }
            if (option == 3) {
                if (cm.haveItem(4000814, 1)) {
                    cm.sendGetText("Hello#b #h ##k, How many Maple Point Items would you like to convert to Maple Points?\r\n\r\n#kMaple Point Item bag is worth 1 Maple Point.\r\n\#kYour currently have " + cm.getPlayer().getItemQuantity(4000814, false) + " Maple Point Items you can exchange for.\r\n\r\n");
                } else {
                    cm.sendOk("Sorry, you must have a Maple Point Item to use this option.");
                    cm.dispose();
                }
            }
            if (option == 2) {
                if (cm.getPlayer().getCashShop().getCash(1) >= 25000) {
                    cm.sendGetText("Hello#b #h ##k, How many Maple Points would you like to buy?\r\n\r\n#kEach Maple Point costs 25,000 NX.\r\n\#kYour currently have " + cm.getPlayer().getCashShop().getCash(1) + " NX.\r\n\r\n");
                } else {
                    cm.sendOk("Sorry, you need a minumum of 25,000 NX.");
                    cm.dispose();
                }
            }




            if (option == 4) {//2000012 megalixiers
                if (mPoint > 0) {
                    cm.sendGetText("Hello#b #h ##k, How many Megalixiers would you like?\r\n\r\n#kEach Megalixier is worth 1 Maple Point.\r\n\#kYour currently have " + mPoint + " Maple Points that you can exchange for.\r\n\r\n");
                } else {
                    cm.sendOk("Sorry, you dont seem to have any maple points.");
                    cm.dispose();
                }
            }
            if (option == 5) {//2003001 totem
                if (mPoint > 0) {
                    cm.sendGetText("Hello#b #h ##k, How many Weakened Totems would you like?\r\n\r\n#kEach Totem is worth 1 Maple Point.\r\n\#kYour currently have " + mPoint + " Maple Points that you can exchange for.\r\n\r\n");
                } else {
                    cm.sendOk("Sorry, you dont seem to have any maple points.");
                    cm.dispose();
                }
            }
            if (option == 6 || option == 9) {//buffs
                if (mPoint > 0) {
                    if (option == 6) {//buffs
                        if (mPoint >= 1) {
                            cm.sendYesNo("Would you like to purchace basic buffs? You will get following buffs applied to you:\r\n\#kSharp Eyes\r\n\#kHoly Symbol\r\n\#kHyper Body\r\n\#kInvincible\r\n\#Haste");
                        } else {
                            cm.sendOk("Sorry, you dont seem to have any maple points.");
                            cm.dispose();
                        }
                    }
                    if (option == 9) {//advbuffs
                        if (mPoint >= 5) {
                            cm.sendYesNo("Would you like to purchace basic buffs? You will get following buffs applied to you:\r\n\#kSharp Eyes\r\n\#kHoly Symbol\r\n\#kHyper Body\r\n\#kInvincible\r\n\#kHoly Shield\r\n\#kSpell Booster\r\n\#kMeso Up\r\n\#Haste\r\n\#Power Stance");
                        } else {
                            cm.sendOk("Sorry, you dont seem to have any maple points.");
                            cm.dispose();
                        }
                    }
                }
            }
            if (option == 7) {//2340000 white scroll
                if (mPoint > 0) {
                    cm.sendGetText("Hello#b #h ##k, How many chaos scrolls would you like?\r\n\r\n#kEach Scroll is worth 1 Maple Point.\r\n\#kYour currently have " + mPoint + " Maple Points that you can exchange for.\r\n\r\n");
                } else {
                    cm.sendOk("Sorry, you dont seem to have any maple points.");
                    cm.dispose();
                }
            }

            if (option == 13) {//vip
                if (mPoint > 0) {
                    cm.sendGetText("Hello#b #h ##k, How many hours of VIP would you like?\r\n\r\n#kEach Maple Point is worth 2 hours of VIP Service.\r\n\#kYour currently have " + mPoint + " Maple Points that you can exchange for.\r\n\r\n");
                } else {
                    cm.sendOk("Sorry, you dont seem to have any maple points.");
                    cm.dispose();
                }
            }

            if (option == 14) {//gach
                if (mPoint >= 10) {
                    cm.sendYesNo("Nx gacha gives 5 Random NX Equips and 1 Random NX Weapon.\r\n Do you confirm you want to spend 5 MP for this?");
                } else {
                    cm.sendOk("Sorry, you dont seem to have any maple points.");
                    cm.dispose();
                }
            }

            if (option == 10 || option == 11 || option == 12) {//item power
                if (option == 10) {
                    cost = 25;
                    power = 1000;
                }
                if (option == 11) {
                    cost = 100;
                    power = 5000;
                }
                if (option == 12) {
                    cost = 250;
                    power = 25000;
                }
                if (mPoint >= cost) {
                    cm.sendYesNo("Are you sure you want to spend " + cost + " MP to add " + power + " to your medal?");
                } else {
                    cm.sendOk("Sorry, you do not have enough maple points for this.");
                    cm.dispose();
                }
            }

            if (option == 15) {//frenzy totem
                cost = 100;//100 base
                if (mPoint >= cost) {
                    cm.sendYesNo("Are you sure you want to spend "+cost+" MP for Frenzy Totem (PERM)? This totem increases spawn amount and counts by 50%.");
                } else {
                    cm.sendOk("Sorry, you dont seem to have enough maple points.");
                    cm.dispose();
                }
            }

            if (option == 16) {//guardian totem
                cost = 250;//250 base
                if (mPoint >= cost) {
                    cm.sendYesNo("Are you sure you want to spend "+cost+" MP for Guardin Totem (PERM)? This totem reduces all damage by 50%.");
                } else {
                    cm.sendOk("Sorry, you dont seem to have enough maple points.");
                    cm.dispose();
                }
            }

            if (option == 17) {//raging totem
                cost = 500;//500 base
                if (mPoint >= cost) {
                    cm.sendYesNo("Are you sure you want to spend "+cost+" MP for Frenzy Totem (PERM)? This totem forces all monsters to spawn where totem is spawned.");
                } else {
                    cm.sendOk("Sorry, you dont seem to have enough maple points.");
                    cm.dispose();
                }
            }
            
            if (option == 18) {//2340000 power
                cost = 1;
                if (mPoint >= cost) {
                    cm.sendGetText("Hello#b #h ##k, How many Power Scrolls would you like?\r\n\r\n#kEach Scroll costs "+cost+" Maple Point.\r\n\#kYour currently have " + mPoint + " Maple Points that you can exchange for.\r\n\r\n");
                } else {
                    cm.sendOk("Sorry, you dont seem to have any maple points.");
                    cm.dispose();
                }
            }
            
            if (option == 19) {//2340000 super
                cost = 5;//5 base
                if (mPoint >= cost) {
                    cm.sendGetText("Hello#b #h ##k, How many Super Power Scrolls would you like?\r\n\r\n#kEach Scroll costs "+cost+" Maple Point.\r\n\#kYour currently have " + mPoint + " Maple Points that you can exchange for.\r\n\r\n");
                } else {
                    cm.sendOk("Sorry, you dont seem to have any maple points.");
                    cm.dispose();
                }
            }
			
            if (option == 20) {//skill panel
                cost = 10;//10 base
                if (mPoint >= cost) {
                    cm.sendYesNo("Are you sure you want to spend "+cost+" MP for Random Rank A Skill Panel?");
                } else {
                    cm.sendOk("Sorry, you dont seem to have enough maple points.");
                    cm.dispose();
                }
            }

        //cm.sendOk(text);
        //cm.dispose();
        } else if (status == 2) {
            if (option == 1) {
                amount = Number(cm.getText());
                if (mPoint >= amount) {
                    if (cm.canHold(4000814, amount)) {
                        cm.gainItem(4000814, amount);
                        cm.getPlayer().getCashShop().gainCash(2, -amount);
                        cm.sendOk("You have gained " + amount + " Maple Point Items.");
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
                if (cm.haveItem(4000814, amount)) {
                    cm.gainItem(4000814, -amount);
                    cm.getPlayer().getCashShop().gainCash(2, amount);
                    cm.sendOk("You have gained " + amount + " Maple Points.");
                    cm.dispose();
                } else {
                    cm.sendOk("Sorry, you do not have enough Maple Point Items for this.");
                    cm.dispose();
                }
            }


            if (option == 4) {
                amount = Number(cm.getText());
                if (mPoint >= amount) {
                    if (cm.canHold(2000012, amount)) {
                        cm.gainItem(2000012, amount);
                        cm.getPlayer().getCashShop().gainCash(2, -amount);
                        cm.sendOk("You have gained " + amount + " Megalixiers.");
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

            if (option == 5) {
                amount = Number(cm.getText());
                if (mPoint >= amount) {
                    if (cm.canHold(2003001, amount)) {
                        cm.gainItem(2003001, amount);
                        cm.getPlayer().getCashShop().gainCash(2, -amount);
                        cm.sendOk("You have gained " + amount + " Weakend Totems.");
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
			
            if (option == 2) {
                amount = Number(cm.getText());
                if (cm.getPlayer().getCashShop().getCash(1) >= amount * 25000) {
                    cm.getPlayer().getCashShop().gainCash(1, -(amount * 25000));
                    cm.getPlayer().getCashShop().gainCash(2, amount);
                    cm.sendOk("You have exchanged " + (amount * 25000) + "NX for " + amount + " Maple Points.");
                    cm.dispose();
                } else {
                    cm.sendOk("Sorry, you do not have enough NX for this.");
                    cm.dispose();
                }
            }

            if (option == 7) {
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

            if (option == 6 || option == 9) {//buffs
                if (option == 6) {//buffs
                    cm.getPlayer().getCashShop().gainCash(2, -1);
                    cm.getPlayer().basicBuff();
                    cm.sendOk("1 Maple point have been removed from your account. Enjoy your buffs.");
                    cm.dispose();
                }
                if (option == 9) {//advbuffs
                    cm.getPlayer().getCashShop().gainCash(2, -5);
                    cm.getPlayer().advancedBuff();
                    cm.sendOk("5 Maple points have been removed from your account. Enjoy your buffs.");
                    cm.dispose();
                }
            }

            if (option == 10 || option == 11 || option == 12) {
                if (mPoint >= cost) {
                    cm.getPlayer().getCashShop().gainCash(2, -cost);
                    cm.getPlayer().upgradeMedal(power);
                    cm.sendOk("Your Medal has been powered up.");
                } else {
                    cm.sendOk("Sorry, you do not have enough maple points for this.");
                    cm.dispose();
                }
            }
            if (option == 13) {
                amount = Number(cm.getText());
                if (mPoint >= amount && amount > 0) {
                    cm.sendYesNo("Do you confirm that you want to spend " + amount + " Maple Points on " + (amount * 4) + " hours of VIP? \r\n\.");
                } else {
                    cm.sendOk("Sorry, you do not have enough maple points for this.");
                    cm.dispose();
                }
            }
            if (option == 14) {
                if (cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.SETUP).getNumFreeSlot() >= 1 && cm.getPlayer().getInventory(Packages.client.inventory.MapleInventoryType.EQUIP).getNumFreeSlot() >= 6) {

                    var text = "You have recieved the following items:\r\n\r\n";
                    text += "NX Equips:\r\n";
                    for (var i = 0; i < 5; i++) {
                        var nx = cm.getRandomNx();
                        cm.gainItem(nx, 1);
                        text += "#i" + nx + "#";
                    }
                    text += "\r\n\r\nNX Weapon:\r\n";
                    var weapon = cm.getRandomNxWeapon();
                    cm.gainItem(weapon, 1);
                    text += "#i" + weapon + "#";

                    var chance = Randomizer.rand(1, 5);
                    if (chance == 1) {
                        text += "\r\n\r\nLegendary Chair:\r\n";
                        var chair = cm.getRandomChair();
                        cm.gainItem(chair, 1);
                        text += "#i" + chair + "#";
                    }

                    cm.getPlayer().getCashShop().gainCash(2, -10);
                    cm.sendOk(text);
                    cm.dispose();
                } else {
                    cm.sendOk("Not Enough space. Requires 6 free equip slots and 1 setup slot.");
                    cm.dispose();
                }
            }
            if (option == 15) {
                if (cm.canHold(2003000, 1)) {
                    cm.gainItem(2003000, 1);
                    cm.getPlayer().getCashShop().gainCash(2, -cost);
                    cm.sendOk("You have gained Frenzy Totem (PERM).");
                    cm.dispose();
                } else {
                    cm.sendOk("Sorry, you do not have enough room in your inventory for this.");
                    cm.dispose();
                }
            }
            if (option == 16) {
                if (cm.canHold(2003003, 1)) {
                    cm.gainItem(2003003, 1);
                    cm.getPlayer().getCashShop().gainCash(2, -cost);
                    cm.sendOk("You have gained Guarding Totem (PERM).");
                    cm.dispose();
                } else {
                    cm.sendOk("Sorry, you do not have enough room in your inventory for this.");
                    cm.dispose();
                }
            }
            if (option == 17) {
                if (cm.canHold(2003002, 1)) {
                    cm.gainItem(2003002, 1);
                    cm.getPlayer().getCashShop().gainCash(2, -cost);
                    cm.sendOk("You have gained Raging Totem (PERM).");
                    cm.dispose();
                } else {
                    cm.sendOk("Sorry, you do not have enough room in your inventory for this.");
                    cm.dispose();
                }
            }
            
            if (option == 18) {
                amount = Number(cm.getText());
                if (mPoint >= amount) {
                    if (cm.canHold(2049185, amount)) {
                        cm.gainItem(2049185, amount);
                        cm.getPlayer().getCashShop().gainCash(2, -(amount));
                        cm.sendOk("You have gained " + amount + " Power Scrolls.");
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
            
            if (option == 19) {
                amount = Number(cm.getText());
                if (mPoint >= (amount * cost)) {
                    if (cm.canHold(2049186, amount)) {
                        cm.gainItem(2049186, amount);
                        cm.getPlayer().getCashShop().gainCash(2, -(amount * cost));
                        cm.sendOk("You have gained " + amount + " Super Power Scrolls.");
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
			
            if (option == 20) {
                var item = Randomizer.rand(3072000, 3072006);
                if (cm.canHold(item, 1)) {
                    cm.gainItem(item, 1);
                    cm.getPlayer().getCashShop().gainCash(2, -(cost));
                    cm.sendOk("You have gained #i" + item + "#.");
                    cm.dispose();
                } else {
                    cm.sendOk("Sorry, you do not have enough room in your inventory for this.");
                    cm.dispose();
                }
            }
            
        } else if (status == 3) {
            if (option == 13) {
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