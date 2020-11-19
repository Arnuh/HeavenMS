var status = 0;
var groupsize = 0;
var item = 4000814;

function start() {
    if (cm.getPlayer().getParty() != null) {
        if (cm.getPlayer().isLeader()) {
            groupsize = cm.getPlayer().allMembersonSameMap().size();
            if (groupsize == 6) {
                if (cm.getPlayer().partyQuestCheck(3522)) {
                    if (cm.haveItem(item, 25)) {
                        cm.sendYesNo("Does your party want to take on corruption of time?");
                    } else {
                        cm.sendOk("This event requires 25 #i"+item+"# to enter. You can get these items from @mp.");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("One or more players has not yet completed the Temple of Time Quest line.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("This event requires full party of 6 members to join.");
                cm.dispose();
            }
        } else {
            cm.sendOk("The leader of the party must be the to talk to me about joining the event.");
            cm.dispose();
        }
    } else {
        if (cm.getPlayer().isGM()) {
            cm.sendYesNo("Does your party want to take on corruption of time?");
        } else  {
            //cm.sendOk("Event is Temp closed.");
            cm.sendOk("Event is Party Mode Only.");
            cm.dispose();
        }
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
        var em = cm.getEventManager("Monster");
        if (em != null) {
            var eli;
            if (cm.getPlayer().getParty() != null) {
                eli = em.getGearEligiblePartySrc(cm.getParty(), cm.getPlayer().getMapId(), 0, 1, 6);
            } else if (cm.getPlayer().getRaid() != null) {
                eli = em.getGearEligibleRaidSrc(cm.getPlayer().getRaid(), cm.getPlayer().getMapId(), 0, 1, 40);
            } else {
                cm.sendOk("Event has encountered an error");
                cm.dispose();
            }
            if (!em.startPlayerInstance(cm.getPlayer(), 1)) {
                cm.sendOk("Someone is already attempting the PQ or your instance is currently being reseted. Try again in few seconds.");
                cm.dispose();
            } else {
                cm.gainItem(item, -25);
                cm.dispose();
            }
        } else {
            cm.sendOk("Event has already started, Please wait.");
            cm.dispose();
        }
    } else {
        cm.dispose();
    }
}



    