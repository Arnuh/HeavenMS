/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>
 
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation version 3 as published by
 the Free Software Foundation. You may not use, modify or distribute
 this program under any other version of the GNU Affero General Public
 License.
 
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.
 
 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
var status = -1;
var stage = 0;
var jobs = new Array(100, 200, 300, 400, 500, 900);

function start() {
    if (cm.getJobId() == 0) {
        stage = 1;
        cm.sendYesNo("So, you'd like to take on your first job?");
    } else {
        stage = 2;
        cm.sendYesNo("Are you ready to leave?");
    }
}

function action(mode, type, selection) {
    status++;
    if (mode != 1) {
        if (mode == 0) {
            cm.sendNext("If you wish to experience what it's like to be a Bowman, come see me again.");
        }
        cm.dispose();
        return;
    }
    if (status == 0) {
        if (stage == 1) {
                stage = 3;
                cm.sendSimple("Which job would you like to advance to?\r\n\#L0# Warrior #l\r\n\#L1# Mage #l\r\n\#L2# Bowman #l\r\n\#L3# Thief #l\r\n\#L4# Pirate #l\r\n");
        } else if (stage == 2) {
            cm.warp(100000000, 0);
            cm.dispose();
        }
    } else if (status == 1) {
        if (stage == 3) {
            cm.resetStats();
            cm.getPlayer().gainMeso(250000);
            cm.getPlayer().maxSlots();
            cm.gainEquip(1002419, 25);//beta
            if (cm.getPlayer().getSkillLevel(12) > 0) {
                cm.getPlayer().gainBonusAP(cm.getPlayer().getSkillLevel(12) * 25);
            }
            cm.gainEquip(1142249, 50);//medal
            if (selection == 0) {
                cm.warp(102000003, 0);
                cm.getPlayer().gainlevel(10 - cm.getPlayer().getLevel());
                cm.changeJobById(jobs[selection]);
            } else if (selection == 1) {
                cm.warp(101000003, 0);
                cm.getPlayer().gainlevel(8 - cm.getPlayer().getLevel());
                cm.changeJobById(jobs[selection]);
                cm.getPlayer().gainlevel(2);
            } else if (selection == 2) {
                cm.warp(100000201, 0);
                cm.getPlayer().gainlevel(10 - cm.getPlayer().getLevel());
                cm.changeJobById(jobs[selection]);
            } else if (selection == 3) {
                cm.warp(103000003, 0);
                cm.getPlayer().gainlevel(10 - cm.getPlayer().getLevel());
                cm.changeJobById(jobs[selection]);
            } else if (selection == 4) {
                cm.warp(120000101, 0);
                cm.getPlayer().gainlevel(10 - cm.getPlayer().getLevel());
                cm.changeJobById(jobs[selection]);
            }
            
            cm.getPlayer().yellowMessage("Now go and seek out a nearby shop to get prepared for this insanely kaotic world.");
        }
        cm.dispose();
    }
}