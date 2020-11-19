/*
 Credits go to Travis of DeanMS ( xKillsAlotx on RaGEZONE)
 Item Exchanger for scrolls
 
 Modified by SharpAceX (Alan) for MapleSolaxia
 */

importPackage(Packages.tools);

var leaf = 4310000;
var receive = 0;
var max = 0;
var currency = 1;
var coin = 0;
var amount = 0;
var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    status++;
    if (mode != 1) {
        if (mode == 0)
            cm.sendNext("I am the fairy for you.");
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendYesNo("Would you like a new pet?");
    } else if (status == 1) {
        if (!cm.haveItem(5000054)) {
            cm.gainItem(5000054);
            cm.sendOk("Enjoy your new pet.");
        } else {
            cm.sendOk("You already own a pet.");
        }
        cm.dispose();
    }
}