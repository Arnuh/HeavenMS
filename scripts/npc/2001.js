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
/* Author: Xterminator
 NPC Name: 		Shanks
 Map(s): 		Maple Road : Southperry (60000)
 Description: 		Brings you to Victoria Island
 */
var status = 0;

function start() {
    cm.sendYesNo("Welcome newcomer. In this server everything has changed such as items, monsters, events, and even super high level zones. As you play, leveling isn't always the way to get stronger. Scrolling and replacing items is the go to here. Monsters scale based on their Level, tier, and Channel ID. Items are scaled based on monster stats, and gachapon scales with player level. If you get lost, commands like @customnpc, @help, and @lost can help. If none of these work simply use @say or the discord to ask for help. Are you ready to play???");
        
}

function action(mode, type, selection) {
	if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 1) {
        cm.warp(5001, 0);
        cm.dispose();
    } else {
        cm.dispose();
    }

}

