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

import client.MapleClient;
import client.command.Command;
import constants.ServerConstants;
import net.server.Server;
import tools.MaplePacketCreator;

public class SayCommand extends Command {

    {
        setDescription("@say message - ETC @say Hi.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        String type;
        if (c.getPlayer().isGM()) {
            type = "[GM]-[" + c.getPlayer().getName() + "] ";
        } else {
            type = "[" + c.getPlayer().getName() + "] ";
        }
        Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, type + joinStringFrom(params, 0)));
    }
}
