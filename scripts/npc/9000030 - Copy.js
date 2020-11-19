/*
Credits go to Travis of DeanMS ( xKillsAlotx on RaGEZONE)
Item Exchanger for scrolls

Modified by SharpAceX (Alan) for MapleSolaxia
*/

importPackage(Packages.tools);

var leaf = 4310000;
var receive = 0;
var max = 0;
var currency = 1;
var coin = 0;
var amount = 0;
var status = 0;

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
        if (mode == 1){
            status++;
		} else {
            status--;
		}
		if (status == 0) {
			if (cm.getPlayer().getLevel() >= 10){
			    cm.sendSimple("Which would you like to do?\r\n#k#L1# Open shop#l\r\n\#L2# Turn in monster Coins/pitch#l");
			} else {
				cm.sendOk("You must be level 10 or greater to use this feature.");
				cm.dispose();
			}
	    } else if (status == 1) {
			if (selection == 1) {
				cm.openShopNPC(10075);
				cm.dispose();
			} else if (selection == 2) {
				currency = cm.getLevel();
				max = 9999;
				coin = 1;
				cm.sendGetText("Hello#b #h ##k, you currently have #b#c4310000# monster Coins.\r\n#kHow much would you like to use? \r\nThere is max of 9999 Coins per transaction.\r\n\r\n ");
			} else {
				cm.dispose();
			}
		} else if (status == 2) {
			receive = cm.getText();
			if (receive <= max && receive > 100) {
				if (((receive <= cm.getPlayer().getCashShop().getCash(1)) && (coin == 0)) || (cm.haveItem(leaf, receive) && (coin == 1))) {
					cm.sendSimple("Which selection would you like to pick from?#k \r\nWhat would you like to do?\r\n#k#L5# Trade for Exp?#l\r\n#L6# Trade for Mesos?#l\r\n#L7# Trade for Fame?#l");
				} else {
					cm.sendOk("Sorry, you currently dont have enough currency for this amount.");
					cm.dispose();
				}
			} else {
				cm.sendOk("Sorry, you currently dont have enough currency for this amount. You need Min of 100 or max of " + max + " Currency.");
				cm.dispose();
			}
		} else if (status == 3) {
			if (coin == 0) {
				cm.getPlayer().getCashShop().gainCash(1, -receive);
				amount = receive;
			} else if (coin == 1) {
				cm.gainItem(leaf, -receive);
				amount = receive;
			}
			if (selection == 5) {
				amount *= 10;
				cm.getPlayer().gainExp(amount);
				cm.getPlayer().announce(MaplePacketCreator.earnTitleMessage("+" + amount + " Exp"));
				cm.sendOk("You gained " + amount + " Exp!");
				cm.dispose();
			} else if (selection == 6) {
				amount *= 100;
				cm.getPlayer().gainMeso(amount);
				cm.getPlayer().announce(MaplePacketCreator.earnTitleMessage("+" + amount + " Mesos"));
				cm.sendOk("You gained " + amount + " Mesos!");
				cm.dispose();
			} else if (selection == 7) {
				amount /= 100;
				cm.getPlayer().gainFame(amount);
				cm.getPlayer().announce(MaplePacketCreator.earnTitleMessage("+" + amount + " Fame"));
				cm.sendOk("You gained " + amount + " Fame!");
				cm.dispose();
			} else {
				cm.sendOk("Come back later!");
				cm.dispose();
			}
		}
	}
}