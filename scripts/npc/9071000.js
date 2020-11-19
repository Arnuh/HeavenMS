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
            cm.sendOk("Welcome to my Grand Monster Park. The portals to the right of me are where my monsters are kept. Complete event to win my amazing monster tickets. You can echange my tickets with Mary.");
            cm.dispose();
        } else {
            cm.dispose();
        }
    }
}



    