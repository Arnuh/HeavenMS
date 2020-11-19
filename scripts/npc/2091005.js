var status = 0;
var amount = 0;
var cost = 0;
var option = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && type > 0) {
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            if (cm.getPlayer().getStamina() > 0) {
                if (cm.getPlayer().getRaid() != null) {
                    cm.sendOk("Dojo is Solo/Party Mode Only.");
                    cm.dispose();
                } else {
                    if (cm.getPlayer().getParty() != null) {
                        if (cm.getPlayer().isLeader()) {
                            cm.sendSimple("You currently have #b" + cm.numberWithCommas(cm.getPlayer().getStamina()) + "#k out of #b" + cm.numberWithCommas(cm.getPlayer().getTotalLevel()) + "#k Stamina available. \r\n\r\nWhich difficulty would you like to take on?\r\n\#L1# Normal (" + (1 * cm.getPlayer().getChannel()) + " Stamina) #l\r\n\#L2# Hard (" + (5 * cm.getPlayer().getChannel()) + " Stamina)#l\r\n\#L3# Ultimate (" + (25 * cm.getPlayer().getChannel()) + " Stamina)#l\r\n\#L4# Unlimited (" + (5 * cm.getPlayer().getChannel()) + " Stamina)#l\r\n\r\n\r\nWhich other options can I help you with?\r\n\#L8# Rankings#l\r\n\#L9# Shop#l");
                        } else {
                            cm.sendSimple("You currently have #b" + cm.numberWithCommas(cm.getPlayer().getStamina()) + "#k out of #b" + cm.numberWithCommas(cm.getPlayer().getTotalLevel()) + "#k Stamina available. \r\n\r\n\r\nWhich other options can I help you with?\r\n\#L8# Rankings#l\r\n\#L9# Shop#l");
                        }
                    } else {
                        cm.sendSimple("You currently have #b" + cm.numberWithCommas(cm.getPlayer().getStamina()) + "#k out of #b" + cm.numberWithCommas(cm.getPlayer().getTotalLevel()) + "#k Stamina available. \r\n\r\nWhich difficulty would you like to take on?\r\n\#L1# Normal (" + (1 * cm.getPlayer().getChannel()) + " Stamina) #l\r\n\#L2# Hard (" + (5 * cm.getPlayer().getChannel()) + " Stamina)#l\r\n\#L3# Ultimate (" + (25 * cm.getPlayer().getChannel()) + " Stamina)#l\r\n\#L4# Unlimited (" + (5 * cm.getPlayer().getChannel()) + " Stamina)#l\r\n\r\n\r\nWhich other options can I help you with?\r\n\#L8# Rankings#l\r\n\#L9# Shop#l");
                    }
                }
            } else {
                cm.sendOk("You have currently used all your stamina for the day.");
                cm.dispose();
            }
        } else if (status == 1) {
            if (selection >= 1 && selection <= 6) {
                var em = null;
                if (selection == 1) {
                    em = cm.getEventManager("DojoNormal");
                    cost = 0;
                } else if (selection == 2) {
                    em = cm.getEventManager("DojoHard");
                    cost = 1 * cm.getPlayer().getChannel();
                } else if (selection == 3) {
                    em = cm.getEventManager("DojoUltimate");
                    cost = 10 * cm.getPlayer().getChannel();
                } else if (selection == 4) {
                    em = cm.getEventManager("DojoUnlimited_Normal");
                    cost = 2 * cm.getPlayer().getChannel();
                }
                if (em != null) {
                    if (cm.getPlayer().getParty() != null) {
                        var eli;
                        if (cm.getPlayer().getParty() != null) {
                            eli = em.getGearEligiblePartySrc(cm.getParty(), cm.getPlayer().getMapId(), cost, 1, 6);
                        } else if (cm.getPlayer().getRaid() != null) {
                            eli = em.getGearEligibleRaidSrc(cm.getPlayer().getRaid(), cm.getPlayer().getMapId(), cost, 1, 40);
                        } else {
                            cm.sendOk("Event has encountered an error");
                            cm.dispose();
                        }
                        if (eli.size() > 0) {
                            if (cm.getPlayer().getStamina() >= cost) {
                                if (!em.startPlayerInstance(cm.getPlayer(), 1)) {
                                    cm.sendOk("Someone is already attempting the PQ or your instance is currently being reseted. Try again in few seconds.");
                                    cm.dispose();
                                } else {
                                    cm.getPlayer().removeStamina(cost);
                                }
                            } else {
                                cm.sendOk("You currently do not have enough stamina to take on this battle.");
                                cm.dispose();
                            }
                        }

                    } else {
                        if (cm.getPlayer().getStamina() >= cost) {
                            if (!em.startPlayerInstance(cm.getPlayer(), 1)) {
                                cm.sendOk("Someone is already attempting the PQ or your instance is currently being reseted. Try again in few seconds.");
                                cm.dispose();
                            } else {
                                cm.getPlayer().removeStamina(cost);
                            }
                        } else {
                            cm.sendOk("You currently do not have enough stamina to take on this battle.");
                            cm.dispose();
                        }
                    }
                } else {
                    cm.sendOk("Event has already started, Please wait.");
                    cm.dispose();
                }
            }
            if (selection == 8) {
                cm.displayDojoRanks();
                cm.dispose();
            }
            if (selection == 9) {
                cm.sendSimple("You currently have #b" + cm.numberWithCommas(cm.getPlayer().getDojoPoints()) + "#k Dojo Points. \r\n\r\nWhich would you like to spend your dojo points on?\r\n\#L10# Dojo Gloves#l\r\n\#L12# Gach Tickets#l\r\n\#L13# AP#l\r\n\#L15# Golden Hammers#l\r\n\#L16# Restore Stamina#l");
            }
        } else if (status == 2) {
            if (selection == 10) { //Gloves
                cm.sendSimple("You currently have #b" + cm.numberWithCommas(cm.getPlayer().getDojoPoints()) + "#k Dojo Points. \r\n\r\nWhich glove would you like? Gloves scaling power is Players Total level * (Tier * Tier)\r\n\#L20# Dojo Gloves Tier 1 - 25,000 Points#l\r\n\#L21# Dojo Gloves Tier 2 - 100,000 Points#l\r\n\#L22# Dojo Gloves Tier 3 - 500,000 Points#l\r\n\#L23# Dojo Gloves Tier 4 - 1,000,000 Points#l\r\n\#L24# Dojo Gloves Tier 5 - 5,000,000 Points#l\r\n\#L25# Dojo Gloves Tier 6 - 25,000,000 Points#l");
            }
            if (selection == 11) { //GML
                cm.sendSimple("You currently have #b" + cm.numberWithCommas(cm.getPlayer().getDojoPoints()) + "#k Dojo Points. \r\n\r\nHow many Golden Maple Leaves would you like?\r\n\#L30# 10 Gmls - 10,000 Points#l\r\n\#L31# 100 Gmls - 100,000 Points#l\r\n\#L32# 250 Gmls - 250,000 Points#l\r\n\#L33# 1000 Gmls - 1,000,000 Points#l\r\n\#L34# 2500 Gmls - 2,500,000 Points#l\r\n\#L35# 5000 Gmls - 5,000,000 Points#l");
            }
            if (selection == 12) { //Gach
                cm.sendSimple("You currently have #b" + cm.numberWithCommas(cm.getPlayer().getDojoPoints()) + "#k Dojo Points. \r\n\r\nHow many Gach Tickets would you like?\r\n\#L40# 10 Gach Tickets - 10,000 Points#l\r\n\#L41# 25 Gach Tickets - 25,000 Points#l\r\n\#L42# 50 Gach Tickets - 50,000 Points#l\r\n\#L43# 100 Gach Tickets - 100,000 Points#l\r\n\#L44# 250 Gach Tickets - 250,000 Points#l\r\n\#L45# 1000 Gach Tickets - 1,000,000 Points#l");
            }
            if (selection == 13) { //AP
                cm.sendSimple("You currently have #b" + cm.numberWithCommas(cm.getPlayer().getDojoPoints()) + "#k Dojo Points. \r\n\r\nHow much AP would you like?\r\n\#L50# 25 AP - 25,000 Points#l\r\n\#L51# 100 AP - 100,000 Points#l\r\n\#L52# 250 AP - 250,000 Points#l\r\n\#L53# 1,000 AP - 1,000,000 Points#l\r\n\#L54# 2,500 AP - 2,500,000 Points#l\r\n\#L55# 5,000 AP - 5,000,000 Points#l");
            }
            if (selection == 14) { //NX
                cm.sendSimple("You currently have #b" + cm.numberWithCommas(cm.getPlayer().getDojoPoints()) + "#k Dojo Points. \r\n\r\nHow much NX would you like?\r\n\#L60# 1,000 NX - 10,000 Points#l\r\n\#L61# 5,000 NX - 50,000 Points#l\r\n\#L62# 10,000 NX - 100,000 Points#l\r\n\#L63# 25,000 NX - 250,000 Points#l\r\n\#L64# 50,000 NX - 500,000 Points#l\r\n\#L65# 100,000 NX - 1,000,000 Points#l");
            }
            if (selection == 15) { //Hammers
                cm.sendSimple("You currently have #b" + cm.numberWithCommas(cm.getPlayer().getDojoPoints()) + "#k Dojo Points. \r\n\r\nHow many golden hammers would you like?\r\n\#L70# 1 Gold Hammer - 250,000 Points#l\r\n\#L71# 5 Golden Hammers - 1,250,000 Points#l\r\n\#L72# 10 Golden Hammers - 2,500,000 Points#l\r\n\#L73# 25 Golden Hammers - 6,250,000 Points#l\r\n\#L74# 50 Golden Hammers - 12,500,000 Points#l\r\n\#L75# 100 Golden Hammers - 25,000,000 Points#l");
            }
            if (selection == 16) {
                if (cm.getPlayer().getDojoPoints() >= 25000) {
                    option = 80;
                    cm.sendYesNo("Cost to restore all your stamina is 25,000 Dojo Points. Would you like to use this");
                } else {
                    cm.sendOk("Please make sure you have 25,000 or more Dojo Points.");
                    cm.dispose();
                }
            }
        } else if (status == 3) {
            if (selection >= 20 && selection <= 29) {
                switch (selection) {
                    case selection = 20:
                        amount = 25000;
                        power = 250;
                        break;
                    case selection = 21:
                        amount = 100000;
                        power = 500;
                        break;
                    case selection = 22:
                        amount = 500000;
                        power = 1000;
                        break;
                    case selection = 23:
                        amount = 1000000;
                        power = 2500;
                        break;
                    case selection = 24:
                        amount = 5000000;
                        power = 5000;
                        break;
                    case selection = 25:
                        amount = 25000000;
                        power = 10000;
                        break;
                }
                if (cm.getPlayer().getDojoPoints() >= amount && cm.getPlayer().canHold(1082392)) {
                    cm.getPlayer().setDojoPoints(cm.getPlayer().getDojoPoints() - amount);
                    var tier = (selection - 20) + 1;
                    cm.gainRareEquip(cm.getPlayer(), 1082392, power, tier);
                    cm.getPlayer().dropMessage(6, cm.numberWithCommas(amount) + " Dojo Points has been removed from your character.");
                    cm.dispose();
                } else {
                    cm.sendOk("Please make sure you have enough Dojo Points and enough space in inventory.");
                    cm.dispose();
                }
            }
            if (selection >= 30 && selection <= 39) {
                switch (selection) {
                    case selection = 30:
                        amount = 10;
                        break;
                    case selection = 31:
                        amount = 100;
                        break;
                    case selection = 32:
                        amount = 250;
                        break;
                    case selection = 33:
                        amount = 1000;
                        break;
                    case selection = 34:
                        amount = 2500;
                        break;
                    case selection = 35:
                        amount = 5000;
                        break;
                }
                if (cm.getPlayer().getDojoPoints() >= amount * 1000 && cm.getPlayer().canHold(4000313, amount)) {
                    cm.getPlayer().setDojoPoints(cm.getPlayer().getDojoPoints() - (amount * 1000));
                    cm.gainItem(4000313, amount);
                    cm.getPlayer().dropMessage(6, cm.numberWithCommas(amount * 1000) + " Dojo Points has been removed from your character.");
                    cm.dispose();
                } else {
                    cm.sendOk("Please make sure you have enough Dojo Points and enough space in inventory.");
                    cm.dispose();
                }
            }
            if (selection >= 40 && selection <= 49) {
                switch (selection) {
                    case selection = 40:
                        amount = 10;
                        break;
                    case selection = 41:
                        amount = 25;
                        break;
                    case selection = 42:
                        amount = 50;
                        break;
                    case selection = 43:
                        amount = 100;
                        break;
                    case selection = 44:
                        amount = 250;
                        break;
                    case selection = 45:
                        amount = 1000;
                        break;
                }
                if (cm.getPlayer().getDojoPoints() >= amount * 1000 && cm.getPlayer().canHold(5220000, amount)) {
                    cm.getPlayer().setDojoPoints(cm.getPlayer().getDojoPoints() - (amount * 1000));
                    cm.gainItem(5220000, amount);
                    cm.getPlayer().dropMessage(6, cm.numberWithCommas(amount * 1000) + " Dojo Points has been removed from your character.");
                    cm.dispose();
                } else {
                    cm.sendOk("Please make sure you have enough Dojo Points and enough space in inventory.");
                    cm.dispose();
                }
            }
            if (selection >= 50 && selection <= 59) {
                switch (selection) {
                    case selection = 50:
                        amount = 25;
                        break;
                    case selection = 51:
                        amount = 100;
                        break;
                    case selection = 52:
                        amount = 250;
                        break;
                    case selection = 53:
                        amount = 1000;
                        break;
                    case selection = 54:
                        amount = 2500;
                        break;
                    case selection = 55:
                        amount = 5000;
                        break;
                }
                if (cm.getPlayer().getDojoPoints() >= amount * 1000) {
                    cm.getPlayer().setDojoPoints(cm.getPlayer().getDojoPoints() - (amount * 1000));
                    cm.getPlayer().gainBonusAP(amount);
                    cm.getPlayer().dropMessage(6, "Gained " + cm.numberWithCommas(amount) + " AP");
                    cm.getPlayer().dropMessage(6, cm.numberWithCommas(amount * 1000) + " Dojo Points has been removed from your character.");
                    cm.dispose();
                } else {
                    cm.sendOk("Please make sure you have enough Dojo Points.");
                    cm.dispose();
                }
            }
            if (selection >= 60 && selection <= 69) {
                switch (selection) {
                    case selection = 60:
                        amount = 1000;
                        break;
                    case selection = 61:
                        amount = 5000;
                        break;
                    case selection = 62:
                        amount = 10000;
                        break;
                    case selection = 63:
                        amount = 25000;
                        break;
                    case selection = 64:
                        amount = 50000;
                        break;
                    case selection = 65:
                        amount = 100000;
                        break;
                }
                if (cm.getPlayer().getDojoPoints() >= amount * 10) {
                    cm.getPlayer().setDojoPoints(cm.getPlayer().getDojoPoints() - (amount * 10));
                    cm.getPlayer().getCashShop().gainCash(1, amount);
                    cm.getPlayer().dropMessage(6, "Gained " + cm.numberWithCommas(amount) + " NX");
                    cm.getPlayer().dropMessage(6, cm.numberWithCommas(amount * 10) + " Dojo Points has been removed from your character.");
                    cm.dispose();
                } else {
                    cm.sendOk("Please make sure you have enough Dojo Points.");
                    cm.dispose();
                }
            }
            if (selection >= 70 && selection <= 79) {
                switch (selection) {
                    case selection = 70:
                        amount = 1;
                        break;
                    case selection = 71:
                        amount = 5;
                        break;
                    case selection = 72:
                        amount = 10;
                        break;
                    case selection = 73:
                        amount = 25;
                        break;
                    case selection = 74:
                        amount = 50;
                        break;
                    case selection = 75:
                        amount = 100;
                        break;
                }
                if (cm.getPlayer().getDojoPoints() >= amount * 250000 && cm.getPlayer().canHold(2470000, amount)) {
                    cm.getPlayer().setDojoPoints(cm.getPlayer().getDojoPoints() - (amount * 250000));
                    cm.gainItem(2470000, amount);
                    cm.getPlayer().dropMessage(6, cm.numberWithCommas(amount * 250000) + " Dojo Points has been removed from your character.");
                    cm.dispose();
                } else {
                    cm.sendOk("Please make sure you have enough Dojo Points and enough space in inventory.");
                    cm.dispose();
                }
            }
            if (option == 80) {
				if (cm.getPlayer().canHold(2000012, 1)) {
					cm.getPlayer().setDojoPoints(cm.getPlayer().getDojoPoints() - (25000));
					cm.gainItem(2000012, amount);
					cm.getPlayer().dropMessage(6, cm.numberWithCommas(25000) + " Dojo Points has been removed from your character and you have gained a Megalixer.");
					cm.dispose();
				} else {
                    cm.sendOk("Please make sure you have enough Dojo Points and enough space in USE inventory.");
                    cm.dispose();
                }
            }
        }
    }

    //cm.dispose();
}



    