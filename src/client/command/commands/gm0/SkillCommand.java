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

public class SkillCommand extends Command {

    {
        setDescription("Shows the maps boss Hp and stats.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        MapleCharacter player = c.getPlayer();
        //1st job
        if (player.getJobId() == 100) {
            c.getPlayer().yellowMessage("Power Strike hits single target 4 times.");
            c.getPlayer().yellowMessage("Slash Blast hits 6 monsters 2 times.");
        }
        if (player.getJobId() == 200) {
            c.getPlayer().yellowMessage("Magic Guard reduces ALL damage by %skilllevel.");
        }
        if (player.getJobId() == 300) {
            c.getPlayer().yellowMessage("Critical Shot 100% crit-rate at +250% base-damage.");
            c.getPlayer().yellowMessage("Eye of Amazon has extended range.");
        }
        if (player.getJobId() == 400) {
            c.getPlayer().yellowMessage("Keen Eyes has extended range on throwing stars.");
            c.getPlayer().yellowMessage("Double Stab hits 8 mobs 2 times.");
            c.getPlayer().yellowMessage("Lucky Seven deal 4 times to single target.");
        }
        if (player.getJobId() == 500) {
            c.getPlayer().yellowMessage("No changes made..");
        }
        //2nd job
        if (player.getJobId() == 110 || player.getJobId() == 120 || player.getJobId() == 130) {
            c.getPlayer().yellowMessage("Booster gives FASTEST weapon speed on ALL weapons.");
        }
        if (player.getJobId() == 210) {
            c.getPlayer().yellowMessage("Meditation gives +2500 Matk - useless with star potions.");
            c.getPlayer().yellowMessage("Fire Arrow hits single target 6 times.");
        }
        if (player.getJobId() == 220) {
            c.getPlayer().yellowMessage("Meditation gives +2500 Matk - useless with star potions.");
            c.getPlayer().yellowMessage("Cold Beam hits single target 4 times.");
            c.getPlayer().yellowMessage("Thunder Bolt hits 6 targets 2 times.");
        }
        if (player.getJobId() == 230) {
            c.getPlayer().yellowMessage("Invincible reduces all damage by 40%.");
            c.getPlayer().yellowMessage("Heal targets 15 monsters and Heals MP.");
            c.getPlayer().yellowMessage("Holy Arrow hits single target 6 times.");
        }
        if (player.getJobId() == 310) {
            c.getPlayer().yellowMessage("Booster gives FASTEST weapon speed on ALL weapons.");
            c.getPlayer().yellowMessage("Arrow Bomb hits 6 monsters 2 times.");
        }
        if (player.getJobId() == 320) {
            c.getPlayer().yellowMessage("Booster gives FASTEST weapon speed on ALL weapons.");
            c.getPlayer().yellowMessage("Iron Arrow hits 6 monsters 4 times.");
        }
        if (player.getJobId() == 410) {
            c.getPlayer().yellowMessage("Claw Mastery extended star counts.");
            c.getPlayer().yellowMessage("Critical Throw 100% crit-rate and +250% Base-damage.");
            c.getPlayer().yellowMessage("Booster gives FASTEST weapon speed on ALL weapons.");
        }
        if (player.getJobId() == 420) {
            c.getPlayer().yellowMessage("Booster gives FASTEST weapon speed on ALL weapons.");
            c.getPlayer().yellowMessage("Haste gives +180 speed +20 jump");
            c.getPlayer().yellowMessage("Savage Blow deals 12 attacks to single monster. Max Level is 50.");
        }
        if (player.getJobId() == 510) {
            c.getPlayer().yellowMessage("Booster gives FASTEST weapon speed on ALL weapons.");
            c.getPlayer().yellowMessage("Power Strike hits single target 4 times.");
            c.getPlayer().yellowMessage("Slash Blast hits 6 monsters 2 times.");
        }
        if (player.getJobId() == 520) {
            c.getPlayer().yellowMessage("Booster gives FASTEST weapon speed on ALL weapons.");
            c.getPlayer().yellowMessage("Power Strike hits single target 4 times.");
            c.getPlayer().yellowMessage("Slash Blast hits 6 monsters 2 times.");
        }
        //3rd job changes
    }
}
