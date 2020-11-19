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
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import java.util.ArrayList;
import java.util.List;
import server.MapleItemInformationProvider;

public class PowerCommand extends Command {

    //private static final short[] slots = {-1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11, -12, -13, -15, -16, -17, -49, -50};

    @Override
    public void execute(MapleClient c, String[] params) {
        int powerlevel = 0;
        for (Item it : c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).list()) {
            if (c.getPlayer().isEquip(it.getPosition())) {
                final MapleItemInformationProvider li = MapleItemInformationProvider.getInstance();
                Equip equip = (Equip) it;
                powerlevel += equip.getPowerLevel();
                c.getPlayer().yellowMessage(li.getName(it.getItemId()) + " has Item Level: " + equip.getPowerLevel());
            }
        }
        c.getPlayer().yellowMessage("Your current Total Power Level is " + powerlevel);
        c.getPlayer().yellowMessage("Your current Average Power Level is " + (powerlevel / c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).list().size()));
    }
}
