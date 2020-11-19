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
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

public class InfoCommand extends Command {

    @Override
    public void execute(MapleClient c, String[] params) {
        c.getPlayer().dropMessage("Your account ID is : " + c.getPlayer().getAccountID() + ". Use this ID when donating along with your char name.");
        c.getPlayer().dropMessage("Your account B-Day is : 0000-00-00. Use this when selling NX items in player shops.");
        c.getPlayer().dropMessage("Your account has : " + c.getPlayer().getCashShop().getCash(1) + " NX. Use @nx for uses.");
        c.getPlayer().dropMessage("Your account has : " + c.getPlayer().getCashShop().getCash(2) + " Maple Points. Used for @mp (Maple Point Shop)");
        c.getPlayer().getVIPDuration();
    }
}
