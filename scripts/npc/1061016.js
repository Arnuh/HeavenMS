var status = -1;
var itemids = Array(2040728, 2040729, 2040730, 2040731, 2040732, 2040733, 2040734, 2040735, 2040736, 2040737, 2040738, 2040739);

var leaf = 4001261;
var receive = 0;
var amount = 0;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode != 1) {
		cm.dispose();
		return;
	}
	status++;
	if (status == 0) {
		if (cm.haveItem(4001261)) {
		    cm.sendGetText("Hello#b #h ##k, you currently have " + cm.haveItem(4001261) + " Balrog's Leathers.\r\n#kHow many would you like to use? \r\n\r\n ");
		} else {
			cm.sendOk("Please come see me when you have some Balrog Leather.");
			cm.dispose();
		}
	} else if (status == 1) {
		receive = cm.getText();
		if (receive > 0) {
			cm.sendSimple("Hello, #h0#. I can exchange your Balrog Leathers.\r\n\r\n#r#L1#Redeem items#l#k");
		} else {
			cm.sendOk("Sorry, you currently dont have enough currency for this amount.");
			cm.dispose();
		}
	} else if (status == 2) {
		var selStr = "Well, okay. These are what you can redeem...\r\n\r\n#b";
		for (var i = 0; i < itemids.length; i++) {
			selStr += "#L" + i + "##i" + itemids[i] + "##z" + itemids[i] + "##l\r\n";
		}
		cm.sendSimple(selStr);
	} else if (status == 3) {
		if (!cm.canHold(itemids[selection], 1)) {
			cm.sendOk("Please make room");
		} else if (!cm.haveItem(leaf, receive)) {
			cm.sendOk("You don't have enough leathers.");
		} else {
			cm.gainItem(4001261, -receive);
			cm.gainItem(itemids[selection], receive);
			cm.sendOk("Thank you for your redemption");
		}
		cm.dispose();
	}
}