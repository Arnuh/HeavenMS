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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import server.life.MapleMonster;

public class BossCommand extends Command {

    {
        setDescription("Shows the maps boss Hp and stats.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {

        DecimalFormat df = new DecimalFormat("###.####");
        if (!c.getPlayer().getMap().getAllBosses().isEmpty()) {
            for (MapleMonster monster : c.getPlayer().getMap().getAllBosses()) {
                int scale = monster.getStats().getScale();
                double percent = (double) (100 * ((double) monster.getHp() / (double) monster.getMaxHp()));
                String bar = "[";
                for (int i = 0; i < 100; i++) {
                    bar += i < (int) (percent) ? "|" : ".";
                }
                bar += "]";
                String MaxHP = NumberFormat.getInstance().format(monster.getMaxHp());
                String CurHP = NumberFormat.getInstance().format(monster.getHp());
                c.getPlayer().yellowMessage(monster.getName() + " ID: " + monster.getId() + " - Level: " + monster.getStats().getLevel() + " - Tier: " + scale + ".");
                c.getPlayer().yellowMessage("Max HP: " + MaxHP + " / Current HP: " + CurHP + ".");
                c.getPlayer().yellowMessage("HP: " + bar + " " + df.format(percent) + "% HP.");
            }
        } else {
            c.getPlayer().yellowMessage("There is no bosses equal or greater than tier 3 found on this map.");
        }
    }
}
