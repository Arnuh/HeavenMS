function enter(pi) {
    var eim = pi.getEventInstance();
    if (eim != null) {
        eim.stopEventTimer();
        eim.dispose();
    }

    pi.playPortalSound();
    pi.warp(220000000, 0);
    return true;
}