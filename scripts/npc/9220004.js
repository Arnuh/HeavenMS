/** 
 Happy - Happy ville 
 By Ronan
 **/
var status = -1;
function start() {
    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status == 0 && mode == 0) {
            cm.sendOk("Talk to me again when you want to.");
            cm.dispose();
        }
        if (mode == 1)
            status++;
        else
            status--;

        if (status == 0) {
            if (cm.getMap().getMonsters().size() < 1) {  //reactor as a monster? wtf
                cm.sendSimple("#b<Raid Quest: Happyville>#k\r\nA raid is nothing but many people joining up in an attempt to defeat extremely powerful creatures. Here is no different. Everyone can take part in defeating the spawned creature. What will you do?\r\n#b\r\n#L0#Snowman - Normal.\r\n#L1#Snowman - Hard.\r\n#L3#Leave.#k");
            } else {
                cm.sendSimple("#b<Raid Quest: Happyville>#k\r\nA raid is nothing but many people joining up in an attempt to defeat extremely powerful creatures. Here is no different. Everyone can take part in defeating the spawned creature. What will you do?\r\n#L3#Leave.\r\n#L4#Reset.#k");
            }
        } else if (status == 1) {
            if (selection == 0) {
                cm.getMap().spawnMonsterOnGroundBelow(9500317, 1700, 80);//9500317
            } else if (selection == 1) {
                cm.getMap().spawnMonsterOnGroundBelow(9500532, 1700, 80);//9500317
            } else if (selection == 3) {
                cm.warp(209000000, 0);
            } else if (selection == 4) {
                cm.getMap().clearMapObjects();
            }
            cm.dispose();
        }
    }
} 