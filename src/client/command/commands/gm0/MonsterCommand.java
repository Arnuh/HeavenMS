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
import java.text.NumberFormat;
import java.util.HashSet;
import server.life.MapleMonster;
import server.life.Rank;
import tools.Randomizer;

public class MonsterCommand extends Command {

    {
        setDescription("Shows what monsters are the map.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {

        MapleCharacter player = c.getPlayer();
        HashSet<Integer> aList = new HashSet<Integer>();
        c.getPlayer().yellowMessage("There is " + c.getPlayer().getMap().countMonsters() + " currently found on this map.");
        c.getPlayer().yellowMessage("RED = Out of Range - Yellow = Within Range to kill");
        c.getPlayer().yellowMessage("==========================================================================");
        for (final MapleMonster monster : player.getMap().getAllMonsters()) {
            if (monster != null && aList.add(monster.getId()) && monster.getHp() > 0) {
                if (monster.getId() != 1 && monster.getId() != 2 && monster.getId() != 3) {
                    double killerRange = (double) monster.getLevel() / (double) player.getTotalLevel();
                    long mobexp = (long) ((double) (killerRange) * (monster.getStats().getExp() * player.expBonus() * monster.getStatusExpMultiplier(player) * player.getExpRate()));
                    long basexp = (long) ((double) (monster.getStats().getExp() * player.expBonus() * monster.getStatusExpMultiplier(player) * player.getExpRate()));
                    String exp = NumberFormat.getInstance().format(basexp);
                    String exp2 = NumberFormat.getInstance().format(mobexp);
                    String exp3 = NumberFormat.getInstance().format((mobexp * 0.25));
                    int scale = monster.getStats().getScale();
                    String MaxHP = NumberFormat.getInstance().format(monster.getMaxHp());
                    String atk = NumberFormat.getInstance().format(Randomizer.MinMax(monster.getStats().getAtk(), 1, 99999));
                    String power = NumberFormat.getInstance().format(monster.getStats().getPower() * ((double) 1 + player.itemR));
                    String power2 = NumberFormat.getInstance().format(monster.getStats().getPower() * 2 * ((double) 1 + player.itemR));
                    c.getPlayer().yellowMessage("Monster: " + monster.getName() + " - ID: " + monster.getId() + " - Level: " + monster.getLevel() + " - Tier: " + scale + " - Ch: " + monster.getChannel());
                    c.getPlayer().yellowMessage("Max HP: " + MaxHP + " - Damage: " + atk + " - Power: " + power);
                    c.getPlayer().yellowMessage("Attack Power: " + NumberFormat.getInstance().format((int) monster.getStats().getDef()) + " - Damage Reduction: " + ((int) (player.getDefense(monster.getStats().getDef()) * 100)) + "%");
                    c.getPlayer().yellowMessage("Exp: " + exp + " - Pyr Exp: " + exp2 + " (" + (int) (killerRange * 100) + "%) - Leech Exp: " + exp3 + " (25%)");
                    if (player.getDrops()) {
                        c.getPlayer().yellowMessage("Item Power: " + power + " - " + power2 + " - Drops: Enabled");
                    } else {
                        c.getPlayer().yellowMessage("Item Power: " + power + " - " + power2 + " - Drops: Disabled");
                    }
                    c.getPlayer().yellowMessage("---------------------------------------------------------------------------------------------------------------------------------");
                } else {
                    c.getPlayer().dropMessage(monster.getName() + " Belongs to: " + monster.getStats().getOwner().getName());
                }
            }
        }
        aList.clear();
    }
}
