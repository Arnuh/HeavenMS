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
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.inventory.manipulator.MapleInventoryManipulator;
import server.MapleTrade;
import server.maps.MaplePlayerShop;
import server.maps.MaplePlayerShopItem;
import tools.MaplePacketCreator;

public class TradeCommand1 extends Command {

    {
        setDescription("@cash slotid while in trade window");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        if (params.length < 1 || params[0] == null || params[1] == null || params[2] == null) {
            return;
        }

        MapleCharacter player = c.getPlayer();
        MaplePlayerShop shop = player.getPlayerShop();
        if (shop != null && shop.isOwner(player)) {
            if (!shop.isOpen()) {
                short value = (short) Integer.parseInt(params[1]);
                if (value > 0) {
                    int price = Integer.parseInt(params[2]);
                    if (price > 0 && price <= Integer.MAX_VALUE) {
                        Item item = player.getInventory(MapleInventoryType.EQUIP).getItem((short) value);
                        if (item != null) {
                            Item tradeItem = item.copy();
                            MaplePlayerShopItem shopItem = new MaplePlayerShopItem(tradeItem, 1, price);
                            if (!shop.addItem(shopItem)) {
                                c.getPlayer().dropMessage(6, "Error with adding shop item");
                            } else {
                                MapleInventoryType ivType = MapleInventoryType.getByType(tradeItem.getItemType());
                                MapleInventoryManipulator.removeFromSlot(c, ivType, value, 1, true);
                                c.announce(MaplePacketCreator.getPlayerShopItemUpdate(shop));
                            }
                        } else {
                            c.getPlayer().dropMessage(6, "Invalid Item from slot");
                        }
                    } else {
                        c.getPlayer().dropMessage(6, "Invalid Price");
                    }
                } else {
                    c.getPlayer().dropMessage(6, "Invalid Slot");
                }
            }
        }
    }
}
