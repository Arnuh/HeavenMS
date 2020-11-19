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
import client.SkillFactory;
import client.command.Command;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class OverpowerCommand extends Command {

    {
        setDescription("Shows your full details on your level and overpower damage.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        DecimalFormat df = new DecimalFormat("###.##");
        MapleCharacter chr = c.getPlayer();
        c.getPlayer().yellowMessage("Your Level is " + c.getPlayer().getLevel());
        if (c.getPlayer().getOverLevel() > 0) {
            c.getPlayer().yellowMessage("Your Overlevel is " + c.getPlayer().getOverLevel());
            c.getPlayer().yellowMessage("Your Total Level is " + c.getPlayer().getTotalLevel());
        }
        c.getPlayer().yellowMessage("Your Level Cap is " + c.getPlayer().getLevelCap());
        if (c.getPlayer().getLevel() >= 200) {
            double percent = (double) (100 * ((double) c.getPlayer().getOverExp() / (double) c.getPlayer().getExpLevel()));
            c.getPlayer().yellowMessage("EXP: " + (NumberFormat.getInstance().format(c.getPlayer().getOverExp())) + " / " + (NumberFormat.getInstance().format(c.getPlayer().getExpLevel())) + " (" + df.format(percent) + "%)");
        }
        c.getPlayer().yellowMessage("Your Total Defense Power: " + chr.getTotalDef());
        if (c.getPlayer().getTotalLevel() >= 250) {
            c.getPlayer().yellowMessage("Your All Stats Power Bonus: " + chr.getBaseStats());
            c.getPlayer().yellowMessage("Your Overlevel Bonus: +" + chr.getOverLevel() + "%.");
            c.getPlayer().yellowMessage("Your Overpower Damage per Line is " + (NumberFormat.getInstance().format(chr.getStats())) + " multipled by damage stat of the skill.");
        }
    }
}
