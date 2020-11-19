/* @author RonanLana */
var level = 140;
var minparty = 1;
var maxparty = 6;
var minraid = 1;
var maxraid = 40;

function enter(pi) {
    var em = pi.getEventManager("Vonleon");
    if (em != null) {
        if (pi.getPlayer().isGroup()) {
            if (pi.getPlayer().isLeader()) {
                var eli;
                var amount = 7500 + (2500 * pi.getPlayer().getChannel());
                var cost = 25 * pi.getPlayer().getChannel();
                if (pi.getPlayer().getStamina() >= cost) {
                    if (pi.getPlayer().getParty() != null) {
                        eli = em.getGearEligiblePartySrc(pi.getParty(), pi.getPlayer().getMapId(), amount, minparty, maxparty);
                    } else if (pi.getPlayer().getRaid() != null) {
                        eli = em.getGearEligibleRaidSrc(pi.getPlayer().getRaid(), pi.getPlayer().getMapId(), amount, minraid, maxraid);
                    } else {
                        pi.playerMessage(5, "Event has encountered an error");
                        return false;
                    }
                    if (eli.size() > 0) {
                        if (!em.startPlayerInstance(pi.getPlayer(), 1)) {
                            pi.playerMessage(5, "Someone is already attempting the PQ or your instance is currently being reseted. Try again in few seconds.");
                        } else {
                            pi.getPlayer().removeStamina(cost);
                            pi.playPortalSound();
                            return true;
                        }
                    } else {
                        pi.playerMessage(5, "You cannot start this party quest yet, because either your party is not in the range size, some of your party members are not eligible to attempt it or they are not in this map. Minimum requirements are: Gear Level " + amount + ", 1+ Raid members.");
                    }
                } else {
                    pi.playerMessage(5, "The leader of the party must have at least "+cost+" stamina or more to enter.");
                }
            } else {
                pi.playerMessage(5, "The leader of the party must be the to talk to me about joining the event.");
            }
        } else {
            pi.playerMessage(5, "Event is Party/Raid Mode. Requirements:");
            pi.playerMessage(5, "Party [Gear lvl=" + amount + ", minplayers=" + minparty + ", maxplayers=" + maxparty + "]");
            pi.playerMessage(5, "Raid [Gear lvl=" + amount + ", minplayers=" + minraid + ", maxplayers=" + maxraid + "]");
        }
    } else {
        pi.playerMessage(5, "Event has already started, Please wait.");
    }
    return false;
}
