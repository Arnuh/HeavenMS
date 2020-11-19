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
package client.command.commands.gm6;

import client.MapleCharacter;
import client.MapleClient;
import client.command.Command;
import net.server.Server;
import tools.MaplePacketCreator;

public class JailCommand extends Command {

    {
        setDescription("Sends Maple Points to account ID. Command is @mp (acc id) (amount)");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        if (params.length < 1 || params[0] == null) {
            c.getPlayer().dropMessage(6, "Command is @donate playername amount.");
            return;
        }
        if (c.getPlayer().getLevel() >= 10) {
            if (c.getWorldServer().getPlayerStorage().getCharacterByName(params[0]) == null) {
                c.getPlayer().dropMessage(6, "Player you are sending nx to is invalid.");
                return;
            }
            MapleCharacter chr = c.getWorldServer().getPlayerStorage().getCharacterByName(params[0]);
            if (!chr.isLoggedinWorld()) {
                c.getPlayer().dropMessage(6, "Player you are sending nx to is offline.");
                return;
            }
            chr.gotoJail();
            Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, chr.getName() + "has been sent to jail."));
        } else {
            c.getPlayer().dropMessage(5, "Try again in a while... you must be level 10 or greater to use this command.");
        }
    }
}
