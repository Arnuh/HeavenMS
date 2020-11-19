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

public class CustomnpcCommand extends Command {

    {
        setDescription("List of @ npcs used in the server");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        c.getPlayer().yellowMessage("List of Custom Npcs:");
        c.getPlayer().yellowMessage("Cody in henesys lets you change your chars looks such as Hair, Skin, Eyes, ETC.");
        c.getPlayer().yellowMessage("Inkwell in freemarket sells powerful rare items for monster coins.");
        c.getPlayer().yellowMessage("Nana(H) in Henesys resets All AP.");
        c.getPlayer().yellowMessage("Mia in Henesys allows access to powerful VIP training room.");
        c.getPlayer().yellowMessage("Ria in Ellinia gives free pet snail.");
        c.getPlayer().yellowMessage("Nana(E) in Ellinia lets you apply Golden Maple Leaves to boost the power of your kaotic Medal.");
        c.getPlayer().yellowMessage("Gaga lets you take on party quest called monster park or Boss PQs.");
        c.getPlayer().yellowMessage("Dimensional Mirror takes you to various towns based on player's level to unlock each town. Each trip consumes 10 stamina.");
        c.getPlayer().yellowMessage("Maple Donation Box lets you convert maple leaves to golden maple leaves.");
        c.getPlayer().yellowMessage("Santa converts 10 10%s into 1 single 100% gm scroll.");
        c.getPlayer().yellowMessage("Fredrick in Free Market sells 30% scrolls for monster coins.");
        c.getPlayer().yellowMessage("Nana(O) in Orbis lets you resets your items at cost of your player total level in GMLs. Items reseted, scale 1x to 5x(Legendary) of your total level.");
        c.getPlayer().yellowMessage("?? guy in free market to access special raid boss zone.");
    }
}
