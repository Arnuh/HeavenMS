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

public class giveNXCommand extends Command {

    {
        setDescription("Donates NX to targeted player.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        if (params.length < 1 || params[0] == null || params[1] == null) {
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
            int value = 0;
            try {
                value = Integer.parseInt(params[1]);
            } catch (NumberFormatException e) {
                c.getPlayer().dropMessage(6, "You can only use numbers.");
                return;
            }
            if (value <= c.getPlayer().getCashShop().getCash(1) && value > 0 && c.getPlayer() != chr) {
                c.getPlayer().getCashShop().gainCash(1, -value);
                chr.getCashShop().gainCash(1, value);
                c.getPlayer().dropMessage(6, "You gifted " + value + " NX to " + chr.getName() + ".");
                chr.dropMessage(6, c.getPlayer().getName() + " has gifted " + value + " NX to you.");
                c.getPlayer().saveCharToDBType(10);
                chr.saveCharToDBType(10);
            } else {
                c.getPlayer().dropMessage(6, "Not Enough NX.");
            }
        } else {
            c.getPlayer().dropMessage(5, "Try again in a while... you must be level 10 or greater to use this command.");
        }
    }
}
