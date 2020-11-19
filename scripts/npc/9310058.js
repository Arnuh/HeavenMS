
var scroll10 = new Array(0, 2040005, 2040402, 2040602, 2040505, 2040805, 2040902, 2043002, 2043102, 2043202, 2043302, 2043702, 2043802, 2044002, 2044102, 2044202, 2044302, 2044402, 2044502, 2044602, 2044702);
var scroll100 = new Array(0, 2040007, 2040403, 2040603, 2040506, 2040807, 2040903, 2043003, 2043103, 2043203, 2043303, 2043703, 2043803, 2044003, 2044103, 2044203, 2044303, 2044403, 2044503, 2044603, 2044703);


var status = 0;
var em = null;
var value;
var receive = 0;
var scroll;

function start() {
    cm.sendSimple("Which scroll would you like to upgrade to? Each 100% scroll costs 10x 10% scrolls of that kind. Min of 1 and Max of 100x 100% scrolls at a time.\r\n\
	#L1# Scroll for Helmet for HP 100% #l\r\n\
	#L2# Scroll for Topwear Defense 100% #l\r\n\
	#L3# Scroll for Bottomwear Defense 100% #l\r\n\
	#L4# Scroll for Overall Dex 100% #l\r\n\
	#L5# Scroll for Gloves for Attack 100% #l\r\n\
	#L6# Scroll for Shield for DEF 100% #l\r\n\
	#L7# Scroll for One-Handed Sword for ATT 100% #l\r\n\
	#L8# Scroll for One-Handed Axe for ATT 100% #l\r\n\
	#L9# Scroll for One-Handed BW for ATT 100% #l\r\n\
	#L10# Scroll for Dagger for ATT 100% #l\r\n\
	#L11# Scroll for Wand for Magic Att 100% #l\r\n\
	#L12# Scroll for Staff for Magic Att 100% #l\r\n\
	#L13# Scroll for Two-handed Sword for ATT 100% #l\r\n\
	#L14# Scroll for Two-handed Axe for ATT 100% #l\r\n\
	#L15# Scroll for Two-handed BW for ATT 100% #l\r\n\
	#L16# Scroll for Spear for ATT 100% #l\r\n\
	#L17# Scroll for Pole Arm for ATT 100% #l\r\n\
	#L18# Scroll for Bow for ATT 100% #l\r\n\
	#L19# Scroll for Crossbow for ATT 100% #l\r\n\
	#L20# Scroll for Claw for ATT 100% #l\r\n\
	");
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
        value = selection;
        scroll = scroll100[value];
        cm.sendGetText("Hello#b #h ##k, how many " + cm.getItemName(scroll100[value]) + " scrolls would you like?\r\n\r\n ");
    } else if (status == 2) {
        receive = cm.getText();
        if (receive <= 100 && receive > 0) {
            if (cm.haveItem(scroll10[value], receive * 10) && cm.getPlayer().canHold(scroll100[value], receive)) {
                cm.gainItem(scroll10[value], -receive * 10);
                cm.gainItem(scroll100[value], receive);
                cm.dispose();
            } else {
                cm.sendOk("You do not have enough scrolls to cash in or you dont have enough space to hold more scrolls.");
                cm.dispose();
            }
        } else {
            cm.sendOk("You can't cheat me. now go away and stop trying to cash in too many scrolls at once.");
            cm.dispose();
        }
    }
}