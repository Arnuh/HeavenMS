var status = 0;

function start() {
    cm.sendYesNo("Do you have what it takes to take on the Grand Snowman?");
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
        cm.warp(209080000, 0);
        cm.dispose();
    }
}