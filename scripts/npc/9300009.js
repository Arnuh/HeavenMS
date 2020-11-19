var status = 0;

function start() {
    if (cm.getPlayer().getParty() != null) {
        if (cm.getPlayer().isLeader()) {
            cm.sendSimple("Which boss difficulty would your group want to take on?\r\n\#L1# Easy (5 Stamina)#l\r\n\#L2# Normal (10 Stamina)#l\r\n\#L3# Hard (25 Stamina)#l\r\n\#L4# Ultimate (50 Stamina)#l\r\n");
        } else {
            cm.sendOk("The leader of the party must be the to talk to me about joining the event.");
            cm.dispose();
        }
    } else {
        cm.sendOk("Event is Party Mode Only.");
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
        var minlevel = 0;
        var em = null;

        if (selection == 1) {
            em = cm.getEventManager("EasyBossPQ");
            cost = 5;
        } else if (selection == 2) {
            em = cm.getEventManager("SuperBossPQ");
            cost = 10;
        } else if (selection == 3) {
            em = cm.getEventManager("MegaBossPQ");
            cost = 25;
        } else if (selection == 4) {
            em = cm.getEventManager("UltimateBossPQ");
            cost = 50;
        }
        if (cm.getPlayer().itemlevel() >= minlevel) {
            if (em != null) {
                var eli;
                if (cm.getPlayer().getParty() != null) {
                    eli = em.getGearEligiblePartySrc(cm.getParty(), cm.getPlayer().getMapId(), minlevel, 1, 6);
                } else if (cm.getPlayer().getRaid() != null) {
                    eli = em.getGearEligibleRaidSrc(cm.getPlayer().getRaid(), cm.getPlayer().getMapId(), minlevel, 1, 40);
                } else {
                    cm.sendOk("Event has encountered an error");
                    cm.dispose();
                }
                if (eli.size() > 0) {
                    if (cm.getPlayer().getStamina() >= cost) {
                        if (!em.startPlayerInstance(cm.getPlayer(), 1)) {
                            cm.sendOk("Someone is already attempting the PQ or your instance is currently being reseted. Try again in few seconds.");
                        } else {
                            cm.getPlayer().removeStamina(cost);
                        }
                    } else {
                        cm.sendOk("The leader of the group must have " + cost + " Stamina to Enter.");
                    }
                } else {
                    cm.sendOk("You cannot start this instance yet, because either your party is not in the range size, some of your group members are not eligible to attempt it or they are not in this map. Minimum requirements are: Level " + minlevel + " +.");
                }
            } else {
                cm.sendOk("Event has already started, Please wait.");
                cm.dispose();
            }
        } else {
            cm.sendOk("Event is Party/Raid Mode Only.");
            cm.dispose();
        }
    }
    cm.dispose();
}



    