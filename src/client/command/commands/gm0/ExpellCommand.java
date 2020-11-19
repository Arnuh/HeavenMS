/*
 This file is part of the HeavenMS MapleStory Server, commands OdinMS-based
 Copyleft (L) 2016 - 2018 RonanLana

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

/*
 @Author: Arthur L - Refactored command content into modules
 */
package client.command.commands.gm0;

import client.MapleCharacter;
import client.MapleClient;
import client.command.Command;

public class ExpellCommand extends Command {

    {
        setDescription("Expells a player from the event. Only usable by event leader.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        if (params.length < 1 || params[0] == null || params[1] == null) {
            c.getPlayer().dropMessage(6, "Command is @expell playername.");
            return;
        }
        if (c.getPlayer().getEventInstance() != null) {
            MapleCharacter chr = c.getWorldServer().getPlayerStorage().getCharacterByName(params[0]);
            if (chr == null) {
                c.getPlayer().dropMessage(6, "Player you are expelling is invalid.");
                return;
            }
            if (c.getPlayer() != chr && c.getPlayer().getEventInstance().isEventLeader(c.getPlayer())) {
                c.getPlayer().getEventInstance().exitPlayer(chr);
                c.getPlayer().dropMessage(6, "You have expelled " + chr.getName() + " from the instance.");
            } else {
                c.getPlayer().dropMessage(6, "This command is only usable by the event leader.");
            }
        } else {
            c.getPlayer().dropMessage(5, "Only usable inside an event instance.");
        }
    }
}
