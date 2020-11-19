var status = 0;
var groupsize = 0;
var item = 4310001;

function start() {
    if (!cm.getPlayer().isGroup()) {
        cm.sendSimple("Which mode would you like to take on?\r\n\#L1# Easy (1 Meso Bag)#l\r\n\#L2# Normal (2 Meso Bags)#l\r\n\#L3# Hard (5 Meso Bags)#l\r\n\#L4# Ultimate (25 Meso Bags)#l\r\n");
    } else {
        cm.sendOk("Event is Solo Mode Only.");
        cm.dispose();
    }
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.dispose();
            return;
        }
        status--;
    }
    if (status == 1) {
		var cost = 0;
        var scale = 0;
        var em = cm.getEventManager("Monster2");

        if (selection == 1) {
            cost = 1;
			scale = 2;
        } else if (selection == 2) {
            cost = 2;
			scale = 3;
        } else if (selection == 3) {
            cost = 5;
			scale = 4;
        } else if (selection == 4) {
            cost = 25;
			scale = 5;
        }
        
        if (em != null) {
            if (cm.haveItem(item, cost)) {
                if (!em.startPlayerInstance(cm.getPlayer(), scale)) {
                    cm.sendOk("Someone is already attempting the PQ or your instance is currently being reseted. Try again in few seconds.");
				} else {
					cm.gainItem(item, -cost);
                }
            } else {
                cm.sendOk("you currently do not have enough #i"+item+"# Enter. You can get these items from @bank.");
            }
        } else {
            cm.sendOk("Event has already started, Please wait.");
        }
        cm.dispose();
    }
}