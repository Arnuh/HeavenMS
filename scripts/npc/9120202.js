var status = 0;

function start() {
    cm.sendYesNo("If you leave now, you won't be able to return. Are you sure you want to leave?");
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
        cm.warp(801040004, 1);
        cm.dispose();
    }
}




