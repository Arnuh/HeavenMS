var status = 0;

function start() {
    if (cm.getPlayer().isGroup()) {
        if (cm.getPlayer().isLeader()) {
            cm.sendSimple("Welcome to Easy MP Boss PQ. Which boss would your group want to take on?\r\n\Each Battle costs 50 Stamina\r\n\#L1# Pianus#l\r\n\#L2# Papulatus#l\r\n\#L3# Zakum#l\r\n\#L4# Horntail#l\r\n\#L5# Scarga/Targa#l\r\n");
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
        var cost = 50;
        var minlevel = 0;
        var em = null;

        if (selection == 1) {
            em = cm.getEventManager("MP_Pianus");
        } else if (selection == 2) {
            em = cm.getEventManager("MP_Papulatus");
        } else if (selection == 3) {
            em = cm.getEventManager("MP_Zakum");
        } else if (selection == 4) {
            em = cm.getEventManager("MP_Horntail");
        } else if (selection == 5) {
            em = cm.getEventManager("MP_Scarga");
        } else if (selection == 6) {
            em = cm.getEventManager("MP_BigBoss");
        } else if (selection == 7) {
            em = cm.getEventManager("MP_PinkBean");
        } else if (selection == 8) {
            em = cm.getEventManager("MP_Vonleon");
        } else if (selection == 9) {
            em = cm.getEventManager("MP_Ark");
        } else if (selection == 10) {
            em = cm.getEventManager("MP_Empress");
        } else if (selection == 11) {
            em = cm.getEventManager("MP_BlackMage");
        }
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
                        if (!em.startPlayerInstance(cm.getPlayer(), 3)) {
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
                if (!em.startPlayerInstance(cm.getPlayer(), 2)) {
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



    