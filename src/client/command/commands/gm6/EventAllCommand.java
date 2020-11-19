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

import client.command.Command;
import client.MapleClient;
import net.server.Server;
import tools.MaplePacketCreator;

public class EventAllCommand extends Command {

    {
        setDescription("");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        if (params.length < 1 || params[0] == null) {
            return;
        }
        int toggle = Integer.parseInt(params[0]);
        if (toggle == 1) {
            Server.getInstance().setToggleevent(false);
            Server.getInstance().setexprate(true);
            Server.getInstance().setdroprate(true);
            Server.getInstance().setscrollrate(true);
            Server.getInstance().setquestrate(true);
            Server.getInstance().setdojorate(true);
            Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "All Fevers have been activated."));
        }
        if (toggle == 0) {
            Server.getInstance().setToggleevent(true);
            Server.getInstance().setexprate(false);
            Server.getInstance().setdroprate(false);
            Server.getInstance().setscrollrate(false);
            Server.getInstance().setquestrate(false);
            Server.getInstance().setdojorate(false);
            Server.getInstance().broadcastMessage(MaplePacketCreator.serverNotice(6, "All Fevers have been de-activated."));
        }
    }
}
