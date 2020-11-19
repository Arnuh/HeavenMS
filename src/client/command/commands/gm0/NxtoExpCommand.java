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

public class NxtoExpCommand extends Command {

    {
        setDescription("Converts 1 nx into 10 EXP.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        if (params.length < 1 || params[0] == null) {
            c.getPlayer().dropMessage(6, "Command is @nxtoexp ####.");
            return;
        }
        if (c.getPlayer().getLevel() >= 10) {
            int value = 0;
            try {
                value = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                c.getPlayer().dropMessage(6, "You can only use numbers.");
                return;
            }
            if (value <= c.getPlayer().getCashShop().getCash(1) && value > 0) {
                c.getPlayer().getCashShop().gainCash(1, -value);
                
                c.getPlayer().gainExp(value * 10);
                c.getPlayer().dropMessage(6, "You traded in " + value + " NX for " + c.getPlayer().getLevel() * value + " Exp.");
                c.getPlayer().saveCharToDBType(0);
                c.getPlayer().saveCharToDBType(10);
                
            } else {
                c.getPlayer().dropMessage(6, "Not Enough NX.");
            }
        } else {
            c.getPlayer().dropMessage(5, "Try again in a while... you must be level 10 or greater to use this command.");
        }
    }
}
