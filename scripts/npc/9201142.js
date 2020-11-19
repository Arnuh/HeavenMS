importPackage(Packages.client);
importPackage(Packages.tools);
importPackage(Packages.server.life);

var status = 0;
var item;
var amount = 0;
var cost = 1;
var option = 0;
var ticket = 4031203;
var ticketname;
var ticketamount;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && type > 0) {
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            ticketname = cm.getItemName(ticket);
            ticketamount = cm.getPlayer().getItemQuantity(ticket, false);
            if (cm.haveItem(ticket, 1000)) {
                cm.sendYesNo("Give me 1000 Candies and I will give you a random reward in exchange. \r\nPossible rewards are:\r\n #i2049100# #i2049180# #i2049181# #i2049182# #i2049183# #i2049184# #i2049185# #i2049186# #i2340000# #i4000814# ");
            } else {
                cm.sendOk("You have currently have no " + ticketname + ". Come back and talk to me when you have some.");
                cm.dispose();
            }
        } else if (status == 1) {
            var choice = Randomizer.rand(0, 10);
            if (choice == 0) {
                item = 2049100;
                amount = 100;
            }
            if (choice == 1) {
                item = 2049180;
                amount = 25;
            }
            if (choice == 2) {
                item = 2049181;
                amount = 25;
            }
            if (choice == 3) {
                item = 2049182;
                amount = 25;
            }
            if (choice == 4) {
                item = 2049183;
                amount = 25;
            }
            if (choice == 5) {
                item = 2049184;
                amount = 25;
            }
            if (choice == 6) {
                item = 2049185;
                amount = 10;
            }
            if (choice == 7) {
                item = 2049185;
                amount = 10;
            }
            if (choice == 8) {
                item = 2049186;
                amount = 5;
            }
            if (choice == 9) {
                item = 2340000;
                amount = 25;
            }
            if (choice == 10) {
                item = 4000814;
                amount = 5;
            }
            if (cm.getPlayer().canHold(item, amount)) {
                cm.gainItem(item, amount);
                cm.gainItem(ticket, -1000);
                cm.sendOk("Thank you. You have exchanged 1000 Halloween Candies for " + amount + " #i"+item+"#.");
            } else {
                cm.sendOk("Please make sure you have enough space to hold this item");
                cm.dispose();
            }
        } else {
            cm.dispose();
        }
    }
}



    