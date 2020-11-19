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
/* 9000021 - Gaga
 BossRushPQ recruiter
 @author Ronan
 */

var status;
var option;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode < 0) {
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
            if (cm.getPlayer().getMapId() == 450001005) {
                if (cm.getPlayer().getTotalLevel() >= 1400) {
                    option = 1;
                    cm.sendYesNo("Need a ride over to Lachelen City?");
                } else {
                    cm.sendOk("You need to be a miniuim of lvl 1400 to take this ride.");
                    cm.dispose();
                }
            }
            if (cm.getPlayer().getMapId() == 450003010) {
                option = 2;
                cm.sendYesNo("Need a ride back to Nameless Town?");
            }
        } else if (status == 1) {
            if (option == 1) {
                cm.warp(450003010);
            }
            if (option == 2) {
                cm.warp(450001005);
            }
            cm.dispose();
        }
    }
}