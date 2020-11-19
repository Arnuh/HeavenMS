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

public class StamCommand extends Command {

    {
        setDescription("Displays your ur current and max stamina.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        c.getPlayer().yellowMessage("Your Current/Max Stamina is: " + c.getPlayer().getStamina() + ":" + c.getPlayer().getTotalLevel() + ".");
        c.getPlayer().yellowMessage("Stamina is recovered through leveling or every 24 hours. Elixers drop from any mob at very low chance. Elixers can also be obtained from gachapon.");
    }
}
