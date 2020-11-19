var status = 0;

function start() {
    if (cm.getPlayer().isGroup()) {
        if (cm.getPlayer().isLeader()) {
             cm.sendYesNo("You wish to overthrow power of Will?");
		} else {
            cm.sendOk("The leader of the party must be the to talk to me about joining the event.");
            cm.dispose();
        }
    } else {
        cm.sendOk("Event is Party/Raid Mode Only.");
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
        var cost = 1000;
        var minlevel = 0;
        var em = null;

        em = cm.getEventManager("will_boss");
        if (cm.getPlayer().isGroup()) {
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
            if (cm.getPlayer().getStamina() >= cost) {
                if (!em.startPlayerInstance(cm.getPlayer(), 1)) {
                    cm.sendOk("Someone is already attempting the PQ or your instance is currently being reseted. Try again in few seconds.");
                } else {
                    cm.getPlayer().removeStamina(cost);
                }
            } else {
                cm.sendOk("The leader of the group must have " + cost + " Stamina to Enter.");
            }
        }
    }
    cm.dispose();
}



    