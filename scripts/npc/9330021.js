importPackage(Packages.net.server.world);
var status = 0;
var sel = 0;
/*
 #L0 = raid - member - list;
 L1 = leave - cm.sendYesNo;
 L2 = disband - cm.sendYesNo;
 L3 = invite - cm.sendGetText("");
 L4 = kick - cm.sendGetText("");
 L6 = close 
 */


function start() {
    cm.sendYesNo("Would you like to goto Multi-player Zone?");
}



function action(mode, type, selection) {
    status++;
    if (mode != 1) {
        if (mode == 0 && type != 1) {
            status -= 2;
        } else if (type == 1 || (mode == -1 && type != 1)) {
            if (mode == 0) {
                cm.sendOk("Hmm... I guess you still have things to do here?");
            }
            cm.dispose();
            return;
        }
    }
    if (status == 1) {
        cm.warp(80000, 0);
		cm.dispose();
    } else {
        cm.dispose();
    }
}