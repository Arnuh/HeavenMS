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

public class ToggleVIPCommand extends Command {

    {
        setDescription("Toggles vip status on/off. Has 10 min cooldown between switching.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        if (c.getPlayer().getVIPTime() > 0) {
            long cooldown = 10 * 60 * 1000;
            long curr = System.currentTimeMillis();
            if (curr - c.getPlayer().getsavecooldown() >= cooldown) {
                c.getPlayer().toggleVIP();
                c.getPlayer().setsavecooldown(System.currentTimeMillis());
            } else {
                c.getPlayer().dropMessage(6, "Time until next available @save " + ((long) (cooldown - (curr - c.getPlayer().getsavecooldown())) / 1000) + " seconds.");
            }
        }

    }
}
