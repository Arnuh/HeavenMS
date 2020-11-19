var status = 0;
var amount = 0;
var cost = 1;
var option = 0;
var ticket = 4001760;
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
            if (cm.haveItem(ticket, 1)) {
                cm.sendSimple("You currently have #b" + ticketamount + "#k " + ticketname + ". \r\nWhich option would you like to spend your tickets on?\r\n\#L11# Golden Maple Leaves#l\r\n\#L12# Gach Tickets#l\r\n\#L13# AP#l\r\n\#L16# Megalixers#l\r\n\#L17# Super Power Scrolls#l");
            } else {
                cm.sendOk("You have currently have no " + ticketname + ". Come back and talk to me when you have some.");
                cm.dispose();
            }
        } else if (status == 1) {
            option = selection;
            if (option == 11) { //GML
                cost = 5;
                cm.sendGetText("How many Golden Maple Leaves would you like to buy?\r\n\Each GML costs 5 tickets.\r\n\You currently have " + ticketamount + " tickets to spend.\r\n\r\n");
            }
            if (option == 12) { //Gach
                cm.sendGetText("How many Gachapon Tickets would you like to buy?\r\n\Each ticket ticket gives 2 Gachapon Tickets.\r\n\You currently have " + ticketamount + " tickets to spend.\r\n\r\n");
            }
            if (option == 13) { //AP
                cm.sendGetText("How much AP would you like to buy?\r\n\Each ticket gives 5 AP.\r\n\You currently have " + ticketamount + " tickets to spend.\r\n\r\n");
            }
            if (option == 14) { //NX
                cm.sendGetText("How NX would you like to buy?\r\n\Each ticket gives 100 NX.\r\n\You currently have " + ticketamount + " tickets to spend.\r\n\r\n");
            }
            if (option == 15) { //Hammers
                cost = 250;
                cm.sendGetText("How many Golden Hammers would you like to buy?\r\n\Each Hammer costs 250 tickets.\r\n\You currently have " + ticketamount + " tickets to spend.\r\n\r\n");
            }
            if (option == 16) {
                cost = 50;
                cm.sendGetText("How many Megalixers would you like to buy?\r\n\Each elixer costs 50 tickets.\r\n\You currently have " + ticketamount + " tickets to spend.\r\n\r\n");
            }
            if (option == 17) {
                cost = 100;
                cm.sendGetText("How many Super Power Scrolls would you like to buy?\r\n\Each scroll costs 100 tickets.\r\n\You currently have " + ticketamount + " tickets to spend.\r\n\r\n");
            }
        } else if (status == 2) {
            amount = Number(cm.getText());
            if (amount > 0 && amount < 1000000) {
                if (cm.haveItem(ticket, amount * cost)) {
                    if (option == 11) { //GML
                        if (cm.getPlayer().canHold(4000313, amount)) {
                            cm.gainItem(4000313, amount);
                            cm.gainItem(ticket, -(amount * cost));
                            cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " tickets for " + amount + " Golden Maple Leaves.");
                        }
                    }
                    if (option == 12) { //Gach
                        if (cm.getPlayer().canHold(5220000, amount * 2)) {
                            cm.gainItem(5220000, amount * 2);
                            cm.gainItem(ticket, -(amount * cost));
                            cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " tickets for " + (amount * 2) + " Gachapon Tickets.");

                        }
                    }
                    if (option == 13) { //AP
                        cm.getPlayer().gainBonusAP(amount * 5);
                        cm.gainItem(ticket, -(amount * cost));
                        cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " tickets for " + (amount * 5) + " AP.");

                    }
                    if (option == 14) { //NX
                        cm.getPlayer().getCashShop().gainCash(1, amount * 100);
                        cm.gainItem(ticket, -(amount * cost));
                        cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " tickets for " + (amount * 100) + " NX.");
                    }
                    if (option == 15) { //Hammers
                        if (cm.getPlayer().canHold(2470000, amount)) {
                            cm.gainItem(2470000, amount);
                            cm.gainItem(ticket, -(amount * cost));
                            cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " tickets for " + amount + " Golden Hammers.");

                        }
                    }
                    if (option == 16) { //elixers
                        if (cm.getPlayer().canHold(2000012, amount)) {
                            cm.gainItem(2000012, amount);
                            cm.gainItem(ticket, -(amount * cost));
                            cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " tickets for " + amount + " Megalixers.");
                        }
                    }
                    if (option == 17) { //scrolls
                        if (cm.getPlayer().canHold(2049186, amount)) {
                            cm.gainItem(2049186, amount);
                            cm.gainItem(ticket, -(amount * cost));
                            cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " tickets for " + amount + " Super Power Scrolls.");
                        }
                    }
                } else {
                    cm.sendOk("Please make sure you have enough Monster Tickets.");
                }
            } else {
                cm.sendOk("Please make sure you have enough Monster Tickets.");
            }
            cm.dispose();
        } else {
            cm.dispose();
        }
    }
}



    