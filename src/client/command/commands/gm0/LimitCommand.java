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

public class LimitCommand extends Command {

    {
        setDescription("@limit - displays limits of what monsters level you can effectivly kill.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        MapleCharacter chr = c.getPlayer();
        if (chr.getMapId() != 925020002) {
            if (!chr.isBeginnerJob()) {
                int minlevel = (chr.getTotalLevel() + (int) (chr.getTotalLevel() * 0.25) + 5);
                int minlevel2 = (chr.getTotalLevel() + ((int) ((chr.getTotalLevel() * 0.25) + 5) * 2));
                int minlevel3 = (chr.getTotalLevel() + ((int) ((chr.getTotalLevel() * 0.25) + 5) * 3));
                int minlevel4 = (chr.getTotalLevel() + ((int) ((chr.getTotalLevel() * 0.25) + 5) * 4));
                chr.dropMessage(6, "Max level of Tier 1 Monster you can kill is " + minlevel + ".");
                chr.dropMessage(6, "Max level of Tier 2 Boss you can kill is " + minlevel2 + ".");
                chr.dropMessage(6, "Max level of Tier 3 Super Boss you can kill is " + minlevel3 + ".");
                chr.dropMessage(6, "Max level of Tier 4 Mega Boss you can kill is " + minlevel4 + ".");
            } else {
                chr.dropMessage(6, "For use level 10 and above");
            }
        } else {
            chr.dropMessage(6, "Limits do not apply here");
        }
    }
}
