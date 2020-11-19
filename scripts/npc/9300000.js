var status = 0;

function start() {
    if (cm.getPlayer().isGroup()) {
        if (cm.getPlayer().isLeader()) {
            if (cm.getPlayer().getStamina() >= 100) {
                cm.sendYesNo("Would you like to take on the Grand Cygnus Empress? Stamina Cost is 100.");
            } else {
                cm.sendOk("The leader of the party must have 10 or more stamina to compete.");
                cm.dispose();
            }
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
        var em = cm.getEventManager("Tower_Battle");
        if (em != null) {
            var eli;
            if (cm.getPlayer().getParty() != null) {
                eli = em.getGearEligiblePartySrc(cm.getParty(), cm.getPlayer().getMapId(), 2500, 1, 6);
            } else if (cm.getPlayer().getRaid() != null) {
                eli = em.getGearEligibleRaidSrc(cm.getPlayer().getRaid(), cm.getPlayer().getMapId(), 2500, 1, 40);
            } else {
                cm.sendOk("Event has encountered an error");
                cm.dispose();
            }
            if (eli.size() > 0) {
                if (cm.getPlayer().getStamina() >= 100) {
                    if (!em.startPlayerInstance(cm.getPlayer(), 1)) {
                        cm.sendOk("Someone is already attempting the PQ or your instance is currently being reseted. Try again in few seconds.");
                    } else {
                        cm.getPlayer().removeStamina(100);
                    }
                } else {
                    cm.sendOk("The leader of the party doesn't have enough stamina to Enter.");
                }
            } else {
                cm.sendOk("You cannot start this party quest yet, because either your party is not in the range size, some of your party members are not eligible to attempt it or they are not in this map. Minimum requirements are: Gear Level 2500+");
            }
        } else {
            cm.sendOk("Event has already started, Please wait.");
        }
        cm.dispose();
    }
}