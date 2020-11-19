var status = 0;
var leaf = 4006000;
var stage = 0;
var amount = 0;
var jobs = new Array(0, 112, 122, 132, 212, 222, 232, 312, 322, 412, 422, 512, 522);

function start() {
	if (cm.getPlayer().getLevelCap() < 1000) {
		cm.sendYesNo("Do you confirm that you want to spend 250,000,000 Mesos to expand your level cap by 25 levels? Maximum Cap I can sell is upto 1000, anything over 1000 will be set to 1000");
	} else {
		cm.sendOk("Sorry, you are already at max level cap that I can offer. Please go see Molto in Arcane River.");
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
            if (cm.getPlayer().getMeso() >= 250000000) {
				cm.getPlayer().gainMeso(-250000000);
				cm.getPlayer().addLevelCap(25);
				if (cm.getPlayer().getLevelCap() > 1000) {
					cm.getPlayer().setLevelCap(1000);
				}
				cm.sendOk("Your level has been increased by 25 levels. Your level cap is now set to " + cm.getPlayer().getLevelCap() + ".");
				cm.dispose();
			} else {
				cm.sendOk("Sorry, you do not have enough mesos for these levels.");
				cm.dispose();
			}
        } else {
            cm.dispose();
        }
    }
}



    