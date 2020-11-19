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
            cm.sendGetNumber("Welcome to Monster Bestiary. By entering the ID of the monster, you can see its full stats such as HP, Level, Tier, and Drops. This info is also based on what channel you are on.\r\n\r\nPlease enter in the id of monster you wish to look up.\r\n\r\n");
        } else if (status == 1) {
            var id = selection;
            var channel = cm.getPlayer().getChannel();
            var monster = cm.getMonster(id, channel);
            if (monster != null) {
                var text = "These are the Monster's Stats on this channel.\r\n";
                text += "Monster Name: " + monster.getName() + " \r\n";
                text += "Monster Max HP: " + monster.MaxHP() + " \r\n";
                text += "Monster Level: " + monster.getLevel() + " \r\n";
                text += "Monster Rank: " + monster.getRank(monster.getId()) + " \r\n";
                text += "Monster Attack Power: " + monster.getAttackPower(channel) + " \r\n";
                text += "Monster Item Power: Common(" + monster.getMinItemPower(channel, cm.getPlayer()) + ") - Legendary(" + monster.getMaxItemPower(channel, cm.getPlayer()) + ")\r\n\r\n";
                var rewards = cm.getMonsterDrops(monster);
                if (!rewards.isEmpty()) {
                    text += "The following items can be obtained from this Monster :\r\n\r\n";
                    var iter = rewards.iterator();
                    while (iter.hasNext()) {
                        var i = iter.next();
                        text += "#i" + i + "#";
                    }
                    cm.sendOk(text);
                } else {
                    cm.sendOk(text + "#rThis Monster has no drops.#k");
                }
            } else {
                cm.sendOk("#rThis Monster does not exist.#k");
            }
            cm.dispose();
        }
    }
}