function enter(pi) {
    if (pi.isQuestCompleted(2331)) {
        pi.openNpc(1300013);
        return false;
    }

    if (pi.isQuestCompleted(2333) && pi.isQuestStarted(2331) && !pi.hasItem(4001318)) {
        pi.getPlayer().message("Lost the Royal Seal, eh? Worry not! Kevin's code here to save your hide.");
        if (pi.canHold(4001318)) {
            pi.gainItem(4001318, 1);
        }
        else {
            pi.getPlayer().message("Hey, how do you plan to hold this Seal when your inventory is full?");
        }
    }

    if (pi.isQuestCompleted(2333)) {
        pi.playPortalSound();
        pi.warp(106021600, 1);
        return true;
    } else if (pi.isQuestStarted(2332) && pi.hasItem(4032388)) {
        if (pi.getPlayer().getParty() != null) {
            pi.message("This portal is only for solo kills.");
            return false;
        } else {
            var em = pi.getEventManager("MK_PrimeMinister");
            if (em.startPlayerInstance(pi.getPlayer(), 1)) {
                pi.forceCompleteQuest(2332, 1300002);
                pi.getPlayer().message("You've found the princess!");
                pi.giveCharacterExp(4400, pi.getPlayer());
                pi.playPortalSound();
                return true;
            } else {
                pi.message("Another party is already challenging the boss in this channel.");
                return false;
            }
        }
    } else if (pi.isQuestStarted(2333) || (pi.isQuestCompleted(2332) && !pi.isQuestStarted(2333))) {
        if (pi.getPlayer().getParty() != null) {
            pi.message("This portal is only for solo kills.");
            return false;
        } else {
            var em = pi.getEventManager("MK_PrimeMinister");
            if (em.startPlayerInstance(pi.getPlayer(), 1)) {
                pi.playPortalSound();
                return true;
            } else {
                pi.message("Another party is already challenging the boss in this channel.");
                return false;
            }
        }
    } else {
        pi.getPlayer().message("The door seems to be locked. Perhaps I can find a key to open it...");
        return false;
    }
}