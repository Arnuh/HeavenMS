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
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import net.server.Server;
import net.server.world.World;
import server.MapleItemInformationProvider;

public class OnlineCommand extends Command {

    //private static final short[] slots = {-1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11, -12, -13, -15, -16, -17, -49, -50};
    @Override
    public void execute(MapleClient c, String[] params) {
        int maxcount = 1;
        for (World w : Server.getInstance().getWorlds()) {
            c.getPlayer().yellowMessage("Current Players Online: " + w.getPlayerStorage().getAllCharacters().size());
            c.getPlayer().yellowMessage("-----------------------------------------------------------------------------");
            if (!w.getPlayerStorage().getAllCharacters().isEmpty()) {
                for (final MapleCharacter chr : w.getPlayerStorage().getAllCharacters()) {
                    if (!chr.isGM()) {
                        if (maxcount < 30) {
                            if (!c.getPlayer().isGM()) {
                                c.getPlayer().yellowMessage("[Player: " + chr.getName() + "] [Total Level: " + chr.getTotalLevel() + "]");
                            } else {
                                c.getPlayer().yellowMessage("[Player: " + chr.getName() + "] [Total Level: " + chr.getTotalLevel() + "] [Medal Power: " + "]");
                            }
                            maxcount += 1;
                        }
                    }
                    if (c.getPlayer().isGM()) {
                        if (chr.isGM()) {
                            c.getPlayer().dropMessage("[GM: " + chr.getName() + "] [Total Level: " + chr.getTotalLevel() + "]");
                        } else {

                        }
                    }
                }
            }
        }
    }
}
