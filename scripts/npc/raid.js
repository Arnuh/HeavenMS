importPackage(Packages.net.server.world);
var status = 0;
var sel = 0;
/*
 #L0 = raid - member - list;
 L1 = leave - cm.sendYesNo;
 L2 = disband - cm.sendYesNo;
 L3 = invite - cm.sendGetText("");
 L4 = kick - cm.sendGetText("");
 L6 = close 
 */


function start() {
    if (cm.getPlayer().getParty() != null) {
        cm.sendOk("You must leave or disband your party before creating a raid.");
        cm.dispose();
    } else {
        if (cm.getPlayer().getRaid() == null) {
            if (MapleRaid.invited(cm.getPlayer())) {
                cm.sendSimple("Raid Menu#l\r\n\#L5#Accept Invite#l\r\n\#L6#Reject Invite");
            } else {
                cm.sendYesNo("Would you like to create a raid?");
            }
        } else {
            if (cm.getPlayer().isRaidLeader()) {
                cm.sendSimple("Raid Menu#l\r\n\#L0#Raid Members#l\r\n\#L3#Invite Member#l\r\n\#L4#Kick Memeber#l\r\n\#L2#Disband Raid#l\r\n\#L8#Change Leader#l\r\n\#L7#Close");
            } else {
                cm.sendSimple("Raid Menu#l\r\n\#L0#Raid Members#l\r\n\#L1#Leave Raid#l\r\n\#L7#Close");
            }
        }
    }
}



function action(mode, type, selection) {
    status++;
    if (mode != 1) {
        if (mode == 0 && type != 1) {
            status -= 2;
        } else if (type == 1 || (mode == -1 && type != 1)) {
            if (mode == 0) {
                cm.sendOk("Hmm... I guess you still have things to do here?");
            }
            cm.dispose();
            return;
        }
    }
    if (status == 1) {
        sel = selection;
        if (selection == -1) {
            if (cm.getPlayer().getRaid() == null) {
                cm.getPlayer().createRaid();
                cm.sendOk("You have successfully created a raid.");
            } else {
                cm.sendOk("Sorry you cannot create a raid right now.");
            }
            cm.dispose();
        } else if (selection == 0) {
            if (cm.getPlayer().getRaid() != null) {
                var selStr = "";
                var chars = cm.getPlayer().getRaid().getMembers();
                for (var i = 0; i < chars.size(); i++) {
                    var chr = chars.get(i);
                    if (chr.isRaidLeader()) {
                        selStr += "#b[Leader] " + chr.getName() + " - " + chr.getJobName() + "#k\r\n";
                    } else {
                        selStr += "#g" + chr.getName() + " - " + chr.getJobName() + "#k\r\n";
                    }
                }
                cm.sendOk("Current raid members?\r\n\ " + selStr);
            } else {
                cm.sendOk("Error with showing raid members.");
            }
            cm.dispose();
        } else if (selection == 1) {
            cm.sendYesNo("Would you like to leave the raid?");
        } else if (selection == 2) {
            cm.sendYesNo("Would you like to disband the raid?");
        } else if (selection == 3) {
            cm.sendGetText("Who would you like to invite?");
        } else if (selection == 4) {
            cm.sendGetText("Who would you like to remove from the raid?");
        } else if (selection == 5) {
            cm.sendYesNo("Would you like to accept the invite from raid?");
        } else if (selection == 6) {
            cm.sendYesNo("Would you like to reject the invite from raid?");
        } else if (selection == 7) {
            cm.sendOk("Hmm... I guess you still have things to do here?");
            cm.dispose();
        } else if (selection == 8) {
            cm.sendGetText("Who would you like to make leader?");
        }

    } else if (status == 2) {
        if (sel == 1) {
            cm.getPlayer().getRaid().leaveRaid(cm.getPlayer());
            cm.sendOk("You have left the raid.");
            cm.dispose();
        }
        if (sel == 2) {
            cm.getPlayer().getRaid().disbandRaidByLeader(cm.getPlayer());
            cm.sendOk("Raid has been disbanded.");
            cm.dispose();
        }
        if (sel == 3) {
            var member = cm.getPlayerByName(cm.getText());
            if (member != null) {
                if (member.getParty() == null) {
                    if (member.getRaid() == null) {
                        cm.getPlayer().getRaid().invite(cm.getPlayer(), member);
                        cm.dispose();
                    } else {
                        cm.sendOk("Player: " + cm.getText() + " is already in another raid party.");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("Player: " + cm.getText() + " is currently in a party.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("Player: " + cm.getText() + " is not online or does not exist.");
                cm.dispose();
            }
        }
        if (sel == 4) {
            var member = cm.getPlayerByName(cm.getText());
            if (member != null) {
                if (member.getRaid() == cm.getPlayer().getRaid()) {
                    cm.getPlayer().getRaid().removeMember(cm.getPlayer(), member);
                    cm.dispose();
                } else {
                    cm.sendOk("Player: " + cm.getText() + " is already in another raid party.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("Player: " + cm.getText() + " is not online or does not exist.");
                cm.dispose();
            }
        }
        if (sel == 5) {
            MapleRaid.handleinvite(cm.getPlayer(), true);
            cm.sendOk("You have successfully join the raid.");
            cm.dispose();
        }
        if (sel == 6) {
            MapleRaid.handleinvite(cm.getPlayer(), false);
            cm.sendOk("You have successfully rejected the raid invite.");
            cm.dispose();
        }
        if (sel == 8) {
            var member = cm.getPlayerByName(cm.getText());
            if (member != null) {
                if (member.getRaid() == cm.getPlayer().getRaid()) {
                    cm.getPlayer().getRaid().setLeader(member);
                    cm.dispose();
                } else {
                    cm.sendOk("Player: " + cm.getText() + " is already in another raid party.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("Player: " + cm.getText() + " is not online or does not exist.");
                cm.dispose();
            }
        }
    } else {
        cm.dispose();
    }
}