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
import scripting.event.EventInstanceManager;
import scripting.event.EventManager;
import tools.MaplePacketCreator;

public class MusicCommand extends Command {

    {
        setDescription("@music full-song-name. Example is @music BgmCustom/TheStageIsSet.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        if (params.length < 1 || params[0] == null) {
            c.getPlayer().dropMessage(6, "Command is @music full-song-name. Example is @music BgmCustom/TheStageIsSet.");
            return;
        }
        String text = params[0].toString();
        c.announce(MaplePacketCreator.musicChange(text));
        c.getPlayer().dropMessage(6, "Music is now set to " + text);
    }
}
