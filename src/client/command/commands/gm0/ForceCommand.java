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
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import server.MapleItemInformationProvider;
import server.maps.MapleMap;
import java.util.Random;
import tools.MaplePacketCreator;

public class ForceCommand extends Command {
    public void execute(MapleClient c, String[] params) {
        int value = Integer.parseInt(params[0]);
        short[] types = {-1,-2,-3,-4,-5,-6,-7,-8,-9,-10,-17,-50};
        int rnd = new Random().nextInt(types.length);
        if (c.getPlayer().isAlive()) {
            if (c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((short) rnd) != null) {
                if (value > 0) {
                    final MapleItemInformationProvider li = MapleItemInformationProvider.getInstance();
                    final MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIPPED);
                    Item eq = equip.getItem((short) rnd);    
                    for(int i=1;i<value;i++) {
                        Item scrolled = li.scrollEquipWithId(eq, 2049100, false, 0, false, c.getPlayer(), false);
                        c.getPlayer().forceUpdateItem(scrolled);
                    }
                    c.getPlayer().dropMessage(6, "Your power level has been increased. You spent " + value + " chaos scrolls.");
                    c.getPlayer().announce(MaplePacketCreator.showEquipmentLevelUp());
                }
            }
        }
    }
}
