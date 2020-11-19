var status = 0;
var amount = 0;
var cost = 1;
var option = 0;
var ticket = 2049100;
var ticketname;
var ticketamount;
var item;

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
            if (cm.haveItem(ticket, 1)) {
                cm.sendSimple("You currently have #b" + ticketamount + " " + ticketname + "#k. \r\nWhich option would you like to spend your chaos scrolls on?\r\n\#L11# #i"+2049180+"# Warrior Class Scroll (Cost: 100x Scrolls)#l\r\n\#L12# #i"+2049181+"# Mage Class Scroll (Cost: 100x Scrolls)#l\r\n\#L13# #i"+2049182+"# Bowman Class Scroll (Cost: 100x Scrolls)#l\r\n\#L14# #i"+2049183+"# Thief Class Scroll (Cost: 100x Scrolls)#l\r\n\#L15# #i"+2049184+"# Pirate Class Scroll (Cost: 100x Scrolls)#l\r\n\#L16# #i"+2049185+"# Power Scroll (Cost: 1000x Scrolls)#l\r\n\#L17# #i"+2049116+"# Black Scroll (Cost: 5000x Scrolls)#l\r\n\#L18# #i"+2049186+"# Super Power Scroll (Cost: 10,000x Scrolls)#l");
            } else {
                cm.sendOk("You have currently have no " + ticketname + ". Come back and talk to me when you have some.");
                cm.dispose();
            }
        } else if (status == 1) {
            option = selection;
            if (option == 11 || option == 12 || option == 13 || option == 14 || option == 15) { //class scrolls
                cost = 100;
                if (option == 11) { //class scroll
                    item = 2049180;    
                }
                if (option == 12) { //class scroll
                    item = 2049181;    
                }
                if (option == 13) { //class scroll
                    item = 2049182;    
                }
                if (option == 14) { //class scroll
                    item = 2049183;    
                }
                if (option == 15) { //class scroll
                    item = 2049184;    
                }
            }
            if (option == 16) { //power scroll
                cost = 1000;
                item = 2049185;
            }
            if (option == 17) { //black scroll
                cost = 5000;
                item = 2049116;  
            }
            if (option == 18) { //super scroll
                cost = 10000;
                item = 2049186; 
            }
            if (cm.haveItem(ticket, cost)) {
                cm.sendGetText("How many #i"+item+"# "+cm.getItemName(item)+" would you like to buy?\r\n\Each #i"+item+"# costs "+cost+" Chaos Scrolls.\r\n\You currently have " + ticketamount + " chaos scrolls to exchange.\r\n\r\n");
            } else {
                cm.sendOk("Please make sure you have enough Shaos Scrolls. Minimun Cost is " + cost);
                cm.dispose();
            }
        } else if (status == 2) {
            amount = Number(cm.getText());
            if (amount > 0 && amount < 50000) {
                if (cm.haveItem(ticket, amount * cost)) {
                    if (cm.getPlayer().canHold(item, amount)) {
                        cm.gainItem(item, amount);
                        cm.gainItem(ticket, -(amount * cost));
                        cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " Chaos Scrolls for " + amount + " "+cm.getItemName(item)+"s.");
                    } else {
                        cm.sendOk("Please make sure you have enough space to hold this item");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("Please make sure you have enough Chaos Scrolls.");
                }
            } else {
                cm.sendOk("Please make sure you have enough Chaos Scrolls.");
            }
            cm.dispose();
        } else {
            cm.dispose();
        }
    }
}



    