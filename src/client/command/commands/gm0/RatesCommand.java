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
import net.server.Server;

public class RatesCommand extends Command {

    {
        setDescription("@rates - displays World and your exp/drop rates.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        MapleCharacter player = c.getPlayer();
        if (Server.getInstance().getexprate()) {
            player.dropMessage(6, "[(Fever) World Exp Rate] " +(int) (player.getExpRate() * 100) + "%");
        } else {
            player.dropMessage(6, "[World Exp Rate] " + (int) (player.getExpRate() * 100) + "%");
        }
        if (Server.getInstance().getdroprate()) {
            player.dropMessage(6, "[(Fever) World Drop Rate] " + (int) (player.getDropRate() * 100) + "%");
        } else {
            player.dropMessage(6, "[World Drop Rate] " + (int) (player.getDropRate() * 100) + "%");
        }
        if (Server.getInstance().getscrollrate()) {
            player.dropMessage(6, "[(Fever) World Scroll Chance Rate] 2x");
        } else {
            player.dropMessage(6, "[World Scroll Chance Rate] 1x");
        }
        if (Server.getInstance().getquestrate()) {
            player.dropMessage(6, "[(Fever) World Quest Exp Rate] 2x");
        } else {
            player.dropMessage(6, "[World Quest Exp Rate] 1x");
        }
        if (Server.getInstance().getdojorate()) {
            player.dropMessage(6, "[(Fever) World Dojo Rate] 2x");
        } else {
            player.dropMessage(6, "[World Dojo Rate] 1x");
        }
    }
}
