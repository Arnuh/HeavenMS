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

public class ClassCommand extends Command {

    {
        setDescription("Shows info about the class you play.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        MapleCharacter player = c.getPlayer();
        if (player.isWarrior()) {
            c.getPlayer().yellowMessage("Warrior Class Builds:");
            c.getPlayer().yellowMessage("First job - Aim for slash blast early on followed with Max HP.");
            c.getPlayer().yellowMessage("Second Job - Aim for booster right away then mastery for all weapons.");
            c.getPlayer().yellowMessage("Dragon Knights: Aim for one crusher then furies then Power Crash followed by beserker for extra damage on 4th job.");
            c.getPlayer().yellowMessage("White Knights: Aim for all charged blasts then Magic Crash followed with Blast on 4th job to boss");
            c.getPlayer().yellowMessage("Crusaders: Aim for Coma/Panic then max combo followed with Brandish on 4th job");
        }
        if (player.isMage()) {
            c.getPlayer().yellowMessage("Mage Class Builds:");
            c.getPlayer().yellowMessage("First job - Aim for max claw then magic guard.");
            c.getPlayer().yellowMessage("Second job - Aim for Meditate.");
            c.getPlayer().yellowMessage("Fire Mage: Aim for anything for second job.");
            c.getPlayer().yellowMessage("Fire Mage: Aim for anything for second job.");
            c.getPlayer().yellowMessage("White Knights: Aim for all charged blasts then Magic Crash followed with Blast on 4th job to boss");
            c.getPlayer().yellowMessage("Crusaders: Aim for Coma/Panic then max combo followed with Brandish on 4th job");
        }
        if (player.getLevel() >= 30 && player.getLevel() < 70) {
            c.getPlayer().yellowMessage("Maple Tips for levels 30-70");
            c.getPlayer().yellowMessage("After you make your first job advance, alot of options start to open up.");
            c.getPlayer().yellowMessage("A massive boost to your gear and damage is head to xmas area and speak to Mrs.Clause. Make sure you buy a mitten first from rudi.");
            c.getPlayer().yellowMessage("You should be familiar with mini-dungeons by now, use them to level faster.");
            c.getPlayer().yellowMessage("Maple weapons drop from ALL monster at rare chance and scale to monsters level. Higher the level better the stats.");
            c.getPlayer().yellowMessage("Look for maps that ate strong to your job to level quickly to lvl 70.");
        }
        if (player.getLevel() >= 70 && player.getLevel() < 100) {
            c.getPlayer().yellowMessage("Maple Tips for levels 70-100");
            c.getPlayer().yellowMessage("Now that your third job, alot of power will come quickly based on your job. Focus on damage skills followed by passive.");
            c.getPlayer().yellowMessage("As you farm you may have noticed you have been collecting Golden Maple Leaves. These are used for BossPQ by talking to GAGA.");
            c.getPlayer().yellowMessage("WIth good damage and gear you may want to test your skills at Easy Boss PQ.");
            c.getPlayer().yellowMessage("This is stage of the game where you want to really start working on scrolling gear. Scrolled gear is massive as you can scroll 250 times per item.");
            c.getPlayer().yellowMessage("With decent damage you may want to try to kill some area bosses for hidden bonus loots they drop. Every area boss has them.");
        }
        if (player.getLevel() >= 100 && player.getLevel() < 120) {
            c.getPlayer().yellowMessage("Maple Tips for levels 100-120");
            c.getPlayer().yellowMessage("You should be nearing your 4th job advancement, the 2 bosses you need for advancement have 100 Million HP.");
            c.getPlayer().yellowMessage("You should also pay a visit to Neo Toyko located in Leafre, the mobs are weak high levels with good equip drops.");
            c.getPlayer().yellowMessage("You may want to pay a visit to Haunted House area and try and kill some headless horsemen for bonus equips.");
            c.getPlayer().yellowMessage("You may want to start mass stocking on monster coins as they are needed for 4th job skills. 1000 to learn skill, 2500 for lvl 20 and 5000 for lvl 30.");
        }
        if (player.getLevel() > 120 && player.getLevel() < 140) {
            c.getPlayer().yellowMessage("Maple Tips for levels 120-140");
            c.getPlayer().yellowMessage("Now that your finally 4th Job, you will find your skill books in a shop in henesys or leafre, the guy's name is Abdula.");
            c.getPlayer().yellowMessage("You should be strong enough to start working on Temple of Time Quest line.");
            c.getPlayer().yellowMessage("This is level at which NORMAL boss pq is unlocked. Good for power leveling your lucky guy medal");
        }
        if (player.getLevel() >= 140 && player.getLevel() < 160) {
            c.getPlayer().yellowMessage("Maple Tips for levels 140-160");
            c.getPlayer().yellowMessage("Lion King Castle is now open to your level which yeilds a MASSIVE boost in equips and damage.");
            c.getPlayer().yellowMessage("The Boss, located in showa, is open and drops Cash Shop Rings with massive stats.");
            c.getPlayer().yellowMessage("You may want to complete papu quests in ludi for raid as he drops powerful earrings.");
            c.getPlayer().yellowMessage("Painus is good kill at this level, if you have a belt on killing him yields massive bonus to belt stats.");
        }
        if (player.getLevel() >= 160 && player.getLevel() < 180) {
            c.getPlayer().yellowMessage("Maple Tips for levels 160-180");
            c.getPlayer().yellowMessage("Temple Ruins is now unlocked for your level. Great place to farm GML's and Black scrolls.");
            c.getPlayer().yellowMessage("Neo Toyko Bosses are unlocked at this level. They drop Tyrants and Abs Rings.");
        }
        if (player.getLevel() >= 180 && player.getLevel() < 200) {
            c.getPlayer().yellowMessage("Maple Tips for levels 180-200");
            c.getPlayer().yellowMessage("Knights Stronghold is now unlocked for your level. This is VERY Strong area.");
        }
        if (player.getLevel() >= 200 && player.getLevel() < 250) {
            c.getPlayer().yellowMessage("Maple Tips for levels 200-250");
            c.getPlayer().yellowMessage("Sub-Job change is now unlocked. Speak to monk at Tot to switch jobs. Cost 2500 magic rocks to change.");
            c.getPlayer().yellowMessage("Hard Boss PQ unlocked, you may want to bring a team to try and clear it.");
        }
        if (player.getLevel() >= 250) {
            c.getPlayer().yellowMessage("Maple Tips for levels 250");
            c.getPlayer().yellowMessage("Full job switching is unlocked, Speak to the monk at Temple of Time. Cost 2500 magic rocks to change.");
            c.getPlayer().yellowMessage("Ultimate Pink Bean is unlocked");
            c.getPlayer().yellowMessage("Ultimate Boss PQ is unlocked");
            c.getPlayer().yellowMessage("Infnite Overleveling is unlocked");
        }
    }
}
