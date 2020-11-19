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
 @Author: Ubaware
 */
package client.command.commands.gm3;
import client.command.Command;
import client.MapleClient;

import java.util.Calendar;

import client.MapleCharacter;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import client.inventory.manipulator.MapleInventoryManipulator;
import constants.ItemConstants;
import server.MapleItemInformationProvider;

public class ItemCommand extends Command {

    {
        setDescription("");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        MapleCharacter player = c.getPlayer();

        if (params.length < 2) {
            player.yellowMessage("Syntax: !give <player> <itemid> [quantity] [expiration]");
            return;
        }

        MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(params[0]);
        if (victim == null) {
            player.dropMessage(5, "Player not found.");
            return;
        }

        int itemId = Integer.parseInt(params[1]);
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

        if (ii.getName(itemId) == null) {
            player.yellowMessage("Item id '" + params[1] + "' does not exist.");
            return;
        }

        long expiration = -1;
        int quantity = 1;
        if (params.length >= 3) {
            quantity = Integer.parseInt(params[2]);
        }
        if (params.length >= 4) {
            expiration = Long.parseLong(params[3]);
        }

        if (quantity < 0) { //take items
            int oldQuantity = victim.getAbstractPlayerInteraction().getItemQuantity(itemId);
            if (-quantity > oldQuantity) {
                quantity = (short) -oldQuantity;
            }
            if (ItemConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                quantity = -1;
            }
            if (oldQuantity > 0) {
                victim.getAbstractPlayerInteraction().gainItem(itemId, quantity, false);
                player.dropMessage("Successfully took " + -quantity + " " + ii.getName(itemId) + " from " + victim.getName());
            } else {
                player.dropMessage(5, victim.getName() + " doesn't have any " + ii.getName(itemId));
            }
            return;
        }

        if (ItemConstants.isPet(itemId)) {
            if (params.length >= 3) {   // thanks to istreety & TacoBell
                quantity = 1;
                long time = Math.max(1, Integer.parseInt(params[1]));
                if (params.length <= 3 || !params[3].equals("m")) {
                    time *= 60 * 24;
                }
                long petExpiration = System.currentTimeMillis() + (time * 60 * 1000);
                int petid = MaplePet.createPet(itemId);

                if (MapleInventoryManipulator.addById(victim.getClient(), itemId, quantity, player.getName(), petid, petExpiration)) {
                    player.dropMessage("Successfully gave " + quantity + " " + ii.getName(itemId) + " to " + victim.getName() + ".");
                } else {
                    player.dropMessage(5, victim.getName() + "'s inventory is full.");
                }
                return;
            } else {
                player.yellowMessage("Pet Syntax: !give <player> <itemid> <expiration>");
                return;
            }
        }

        /*short flag = 0;
         if(player.gmLevel() < 3) {
         flag |= ItemConstants.ACCOUNT_SHARING;
         flag |= ItemConstants.UNTRADEABLE;
         }*/
        if (expiration >= 0) {
            if (expiration == 0) {
                Calendar toExpire = Calendar.getInstance();
                toExpire.add(Calendar.DAY_OF_MONTH, 1);
                toExpire.add(Calendar.MINUTE, 1);
                toExpire.set(Calendar.HOUR_OF_DAY, 0);
                toExpire.set(Calendar.MINUTE, 0);
                toExpire.set(Calendar.SECOND, 0);
                toExpire.set(Calendar.MILLISECOND, 0);
                expiration = toExpire.getTimeInMillis() - 60000;
                if (expiration < 0) {
                    player.dropMessage(5, "Unknown error calculating expiration date.");
                    return;
                }
            } else {
                expiration = System.currentTimeMillis() + (expiration * 60000);
            }
        }

        if (MapleInventoryManipulator.addById(victim.getClient(), itemId, quantity, null, -1, (byte) 0, expiration)) {
            player.dropMessage("Successfully gave " + quantity + " " + ii.getName(itemId) + " to " + victim.getName() + ".");
        } else {
            player.dropMessage(5, victim.getName() + "'s inventory is full.");
        }
    }
}
