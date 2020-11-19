function start() {
    cm.sendYesNo("Would you like to max out all your skills?");
}

function action(mode, type, selection) {
		cm.maxMastery();
	    cm.dispose();
}