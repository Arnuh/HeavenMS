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
/*      Author: Xterminator, Moogra
	NPC Name: 		First Eos Rock
	Map(s): 		Ludibrium : Eos Tower 100th Floor (221024400)
	Description: 		Brings you to 71st Floor
*/

var status = 0;
var em = null;

function start() {
	cm.warp(221022900, "go00");
	cm.dispose();
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
		cm.gainItem(4001020, -1);
        cm.warp(221022900, "go00");
	}
    cm.dispose();
}