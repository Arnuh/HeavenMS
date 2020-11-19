var status = 0;

function start() {
    if (cm.getPlayer().getRaid() == null && cm.getPlayer().getParty() == null) {
        if ((cm.getPlayer().getLevel() == 70)) {
            cm.sendYesNo("Fight the first of many challenges on your path to the top?");
        } else {
            cm.sendYesNo("You need to be level 70 to enter.");
            cm.dispose();
        }
    } else {
        cm.sendOk("you must leave or disband your party or raid in order to enter.");
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
        var em = cm.getEventManager("3rd_job");
        if (em != null) {
            if (!em.startPlayerInstance(cm.getPlayer(), 1)) {
                cm.sendOk("Please report this error to local gm in discord with screenshot.");
            }
        }
    }
    cm.dispose();
}