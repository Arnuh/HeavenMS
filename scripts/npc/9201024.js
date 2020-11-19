/*
 Credits go to Travis of DeanMS ( xKillsAlotx on RaGEZONE)
 Item Exchanger for scrolls
 
 Modified by SharpAceX (Alan) for MapleSolaxia
 */

importPackage(Packages.tools);

var leaf = 4000313;
var amount = 0;
var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            if (cm.haveItem(leaf, 5) && cm.getPlayer().getMedalEquip()) {
                cm.sendGetText("Hello#b #h ##k, you currently have #b#c4000313# Golden Maple Leaves#k.\r\nEach GML Grants 25 power.\r\nHow much power would you like to add?\r\n\r\n ");
            } else {
                cm.sendOk("Sorry, you currently dont have any Golden Maple Leaves or do not have your medal equipped.");
                cm.dispose();
            }
        } else if (status == 1) {
            amount = cm.getText();
            if (amount > 0) {
                if (cm.haveItem(leaf, (amount))) {
                    cm.gainItem(leaf, -(amount));
                    cm.getPlayer().upgradeMedal(amount * 25);
                    cm.sendOk("You gained " + (amount * 25) + " Power on your medal!");
                    cm.dispose();
                } else {
                    cm.sendOk("Sorry, you currently dont have enough Golden Maple Leaves for this amount.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("Sorry, no cheaty here.");
                cm.dispose();
            }
        }
    }
}