//kaotic Tower level requirement
function enter(pi) {
    var base = pi.getPlayer().getMapId() - 90000;
    var level = (base * 10) + 10;
    if (pi.getPlayer().getTotalLevel() >= level) {
        pi.warp(pi.getPlayer().getMapId() + 1, pi.PortalName());
        return true;
    } else {
        pi.getPlayer().dropMessage(5, "Must reach minimum level of " + level + " to enter this portal.");
        return false;
    }
}