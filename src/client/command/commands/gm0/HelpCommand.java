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

public class HelpCommand extends Command {
    {
        setDescription("List of @ commands used by the players");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        c.getPlayer().yellowMessage("@bossHP - Shows HP value of all bosses on map that is tier 3 and greater.");
        c.getPlayer().yellowMessage("@nx, @nxtomesos, @nxtoexp - Uses nx for various things.");
        c.getPlayer().yellowMessage("@save - Saves characater.");
        c.getPlayer().yellowMessage("@hunt playername - allows you to assassinate another player for 5 stamina.");
        c.getPlayer().yellowMessage("@say - server wide chat system.");
        c.getPlayer().yellowMessage("@pos - shows what map you are on and coords.");
        c.getPlayer().yellowMessage("@monster - shows list of all monsters and their levels/id.");
        c.getPlayer().yellowMessage("@rates - displays server rates.");
        c.getPlayer().yellowMessage("@lost - guide for those who get lost and do not know where or what to do.");
        c.getPlayer().yellowMessage("@limit - shows the levels of monsters you can effectively damage/kill.");
        c.getPlayer().yellowMessage("@overpower - shows your true level when over level 250 and your overpower damage.");
        c.getPlayer().yellowMessage("@rank - shows rankings of players and their true levels.");
        c.getPlayer().yellowMessage("@raid - create or manage a raid group.");
        c.getPlayer().yellowMessage("@recharge - recharges all stars/bullets/arrows in your inventory.");
        c.getPlayer().yellowMessage("@donate - donates NX to targeted player.");
        c.getPlayer().yellowMessage("@stamina - displays a player's stamina.");
        c.getPlayer().yellowMessage("@bank - Players meso bank that holds unlimited mesos.");
        c.getPlayer().yellowMessage("@info - Shows your account id number used for donations.");
        c.getPlayer().yellowMessage("@mobinfo - Shows stats and items dropped by mob ID. Monster ID list is under discord Downloads..");
        c.getPlayer().yellowMessage("@music - Changes the music on your map. Only Applies to yourself. Music list is under discord Downloads.");
        c.getPlayer().yellowMessage("@who - Displays whos online-level-pwr-avg gear score. Shows upto 30 players at a time.");
        c.getPlayer().yellowMessage("@dispose - Unstucks your character and mouse.");
        c.getPlayer().yellowMessage("@customnpc - Shows a full list of custom npcs.");
        c.getPlayer().yellowMessage("@mp - Opens Maple Point shop to spend MP. MP can be bought or voted for.");
        c.getPlayer().yellowMessage("@gacha - Uses 10 MP to randomly obtain nx equips and weapons and chairs. Comes with bonus stats on NX equips based on your level.");
        c.getPlayer().yellowMessage("@expell - Usable only by event leader, command is used to expell players from instance.");
        c.getPlayer().yellowMessage("@vip - Temp disables/enabled VIP status for 10 mins or until relog.");
        c.getPlayer().yellowMessage("@exp - Temp disables/enabled Drops for more exp.");
        c.getPlayer().yellowMessage("@drops - Temp disables/enabled Exp for more drops.");
        c.getPlayer().yellowMessage("@panel - views current panel bonus stats.");
        c.getPlayer().yellowMessage("@mapinfo - displays current map info such as dark force.");
        c.getPlayer().yellowMessage("@overflow - toggles overflow equip system on/off.");
        c.getPlayer().yellowMessage("@equips - views extended stats on equips.");
    }
}
