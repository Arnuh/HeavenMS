var status = 0;
var leaf = 4006000;
var stage = 0;
var amount = 0;
var jobs = new Array(0, 112, 122, 132, 212, 222, 232, 312, 322, 412, 422, 512, 522);

function start() {
	if (cm.getPlayer().getRemainingSp() > 0) {
    cm.sendGetText("Hello#b #h ##k, How many SP Points do you want to exchange for AP?\r\n#kYou current SP amount is " + cm.getPlayer().getRemainingSp() + ".\r\n#kEach SP is worth 5 AP.\r\n#k\r\n\r\n");
	} else {
		cm.sendOk("Sorry, you do not have enough SP to exchange.");
                cm.dispose();
	}
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
            status--;
        }
        if (status == 1) {
            amount = Number(cm.getText());
            if (amount > 0) {
                if (amount <= cm.getPlayer().getRemainingSp()) {
                    cm.sendYesNo("Do you confirm that you want to exchange " + amount + " SP for " + (amount * 5) + " AP?");
                } else {
                    cm.sendOk("You dont have enough SP to make this trade.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("Sorry, you dont seem to know what your doing.");
                cm.dispose();
            }
        } else if (status == 2) {
            cm.getPlayer().gainBonusAP(amount * 5);
			cm.getPlayer().gainSP(-amount);
            cm.sendOk("You have exchanged " + amount + " SP for " + (amount * 5) + "AP.");
            cm.dispose();
        } else {

            cm.dispose();
        }
    }
}



    