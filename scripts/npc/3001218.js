var status = 0;
var amount = 0;
var cost = 1;
var option = 0;
var ticket = 4310059;
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
                cm.sendSimple("You currently have #b" + ticketamount + "#k " + ticketname + ". \r\nWhich option would you like to spend your shadow coins on?\r\n\#L11# Golden Maple Leaves (Cost: 5x Coins)#l\r\n\#L13# AP (Cost: 10x Coins)#l\r\n\#L16# Megalixers (Cost: 250x Coins)#l\r\n\#L19# Meso Bags (Cost: 1,000x Coins)#l\r\n\#L17# Super Power Scrolls (Cost: 2,500x Coins)#l");
            } else {
                cm.sendOk("You have currently have no " + ticketname + ". Come back and talk to me when you have some.");
                cm.dispose();
            }
        } else if (status == 1) {
            option = selection;
            if (option == 11) { //GML
                cost = 5;
                if (cm.haveItem(ticket, cost)) {
                    cm.sendGetText("How many Golden Maple Leaves would you like to buy?\r\n\Each GML costs 5 shadow coins.\r\n\You currently have " + ticketamount + " shadow coins to spend.\r\n\r\n");
                } else {
                    cm.sendOk("Please make sure you have enough Monster shadow coins. Minimun Cost is " + cost);
                    cm.dispose();
                }
            }
            if (option == 13) { //AP
				cost = 10;
				if (cm.haveItem(ticket, cost)) {
					cm.sendGetText("How much AP would you like to buy?\r\n\10 shadow coins gives 1 AP.\r\n\You currently have " + ticketamount + " shadow coins to spend.\r\n\r\n");
				} else {
                    cm.sendOk("Please make sure you have enough Monster shadow coins. Minimun Cost is " + cost);
                    cm.dispose();
                }
            }
            if (option == 14) { //NX
                cm.sendGetText("How NX would you like to buy?\r\n\Each shadow coin gives 25 NX.\r\n\You currently have " + ticketamount + " shadow coins to spend.\r\n\r\n");
            }
            if (option == 15) { //Hammers
                cost = 5000;
                if (cm.haveItem(ticket, cost)) {
                    cm.sendGetText("How many Golden Hammers would you like to buy?\r\n\Each Hammer costs 5000 shadow coins.\r\n\You currently have " + ticketamount + " shadow coins to spend.\r\n\r\n");
                } else {
                    cm.sendOk("Please make sure you have enough Monster shadow coins. Minimun Cost is " + cost);
                    cm.dispose();
                }
            }
            if (option == 16) {
                cost = 250;
                if (cm.haveItem(ticket, cost)) {
                    cm.sendGetText("How many Megalixers would you like to buy?\r\n\Each elixer costs 250 shadow coins.\r\n\You currently have " + ticketamount + " shadow coins to spend.\r\n\r\n");
                } else {
                    cm.sendOk("Please make sure you have enough Monster shadow coins. Minimun Cost is " + cost);
                    cm.dispose();
                }
            }
            if (option == 17) {
                cost = 2500;
                if (cm.haveItem(ticket, cost)) {
                    cm.sendGetText("How many Super Power Scrolls would you like to buy?\r\n\Each scroll costs 2,500 shadow coins.\r\n\You currently have " + ticketamount + " shadow coins to spend.\r\n\r\n");
                } else {
                    cm.sendOk("Please make sure you have enough Monster shadow coins. Minimun Cost is " + cost);
                    cm.dispose();
                }
            }
            if (option == 18) {
                cost = 2500;
                if (cm.haveItem(ticket, cost)) {
                    cm.sendGetText("How many Maple Point Items would you like to buy?\r\n\Each MP costs 2,500 shadow coins.\r\n\You currently have " + ticketamount + " shadow coins to spend.\r\n\r\n");
                } else {
                    cm.sendOk("Please make sure you have enough Monster shadow coins. Minimun Cost is " + cost);
                    cm.dispose();
                }
            }
            if (option == 19) {
                cost = 1000;
                if (cm.haveItem(ticket, cost)) {
                    cm.sendGetText("How many Meso Bags would you like to buy?\r\n\Each MP costs 1,000 shadow coins.\r\n\You currently have " + ticketamount + " shadow coins to spend.\r\n\r\n");
                } else {
                    cm.sendOk("Please make sure you have enough Monster shadow coins. Minimun Cost is " + cost);
                    cm.dispose();
                }
            }
        } else if (status == 2) {
            amount = Number(cm.getText());
            if (amount > 0 && amount < 50000) {
                if (cm.haveItem(ticket, amount * cost)) {
                    if (option == 11) { //GML
                        if (cm.getPlayer().canHold(4000313, amount)) {
                            cm.gainItem(4000313, amount);
                            cm.gainItem(ticket, -(amount * cost));
                            cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " shadow coins for " + amount + " Golden Maple Leaves.");
                        } else {
                            cm.sendOk("Please make sure you have enough space to hold this item");
                            cm.dispose();
                        }
                    }
                    if (option == 13) { //AP
                        cm.getPlayer().gainBonusAP(amount);
                        cm.gainItem(ticket, -(amount * cost));
                        cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " shadow coins for " + (amount) + " AP.");

                    }
                    if (option == 14) { //NX
                        cm.getPlayer().getCashShop().gainCash(1, amount * 25);
                        cm.gainItem(ticket, -(amount * cost));
                        cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " shadow coins for " + (amount * 25) + " NX.");
                    }
                    if (option == 15) { //Hammers
                        if (cm.getPlayer().canHold(2470000, amount)) {
                            cm.gainItem(2470000, amount);
                            cm.gainItem(ticket, -(amount * cost));
                            cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " shadow coins for " + amount + " Golden Hammers.");

                        } else {
                            cm.sendOk("Please make sure you have enough space to hold this item");
                            cm.dispose();
                        }
                    }
                    if (option == 16) { //elixers
                        if (cm.getPlayer().canHold(2000012, amount)) {
                            cm.gainItem(2000012, amount);
                            cm.gainItem(ticket, -(amount * cost));
                            cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " shadow coins for " + amount + " Megalixers.");
                        } else {
                            cm.sendOk("Please make sure you have enough space to hold this item");
                            cm.dispose();
                        }
                    }
                    if (option == 17) { //scrolls
                        if (cm.getPlayer().canHold(2049186, amount)) {
                            cm.gainItem(2049186, amount);
                            cm.gainItem(ticket, -(amount * cost));
                            cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " shadow coins for " + amount + " Super Power Scrolls.");
                        } else {
                            cm.sendOk("Please make sure you have enough space to hold this item");
                            cm.dispose();
                        }
                    }
                    if (option == 18) { //MP
                        if (cm.getPlayer().canHold(4000814, amount)) {
                            cm.gainItem(4000814, amount);
                            cm.gainItem(ticket, -(amount * cost));
                            cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " shadow coins for " + amount + " Maple Point Item.");
                        } else {
                            cm.sendOk("Please make sure you have enough space to hold this item");
                            cm.dispose();
                        }
                    }
                    if (option == 19) { //Bag
                        if (cm.getPlayer().canHold(4310001, amount)) {
                            cm.gainItem(4310001, amount);
                            cm.gainItem(ticket, -(amount * cost));
                            cm.sendOk("Thank you. You have exchanged " + (amount * cost) + " shadow coins for " + amount + " Meso Bags.");
                        } else {
                            cm.sendOk("Please make sure you have enough space to hold this item");
                            cm.dispose();
                        }
                    }
                } else {
                    cm.sendOk("Please make sure you have enough Monster shadow coins.");
                }
            } else {
                cm.sendOk("Please make sure you have enough Monster shadow coins.");
            }
            cm.dispose();
        } else {
            cm.dispose();
        }
    }
}



    