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
/* NPC Base
 Map Name (Map ID)
 Extra NPC info.
 */

var status;
var option = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode < 0)
        cm.dispose();
    else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0 && mode == 1) {
            cm.sendSimple("Which nx would u like to see?\r\n\#L1#Items1-100#l\r\n\#L2#Items101-200#l\r\n\#L3#201-300#l\r\n\#L4#301-400#l\r\n\#L5#401-500#l\r\n\#L6#501-600#l\r\n\#L7#601-700#l");
        } else if (status == 1) {
            var text = "These are the Monster's Stats on this channel.\r\n";
            
            for (var i = 1100000; i < 1110000; i++) {
                if (cm.itemExists(i)) {
                    text += "#i" + i + "#";
                }
            }
            
            cm.sendOk(text);
            cm.dispose();
        }
    }
}