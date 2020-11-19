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

public class LostCommand extends Command {

    {
        setDescription("Shows the maps boss Hp and stats.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        MapleCharacter player = c.getPlayer();
        if (player.getTotalLevel() < 10) {
            c.getPlayer().yellowMessage("Maple Tips for levels 1-10");
            c.getPlayer().yellowMessage("Welcome to Kaotic Maple.");
            c.getPlayer().yellowMessage("Your first task is to collect 100 coins then talk to shanks for 1st job advance.");
            c.getPlayer().yellowMessage("Skills like nimble feet and recovery are boosted on this server so make sure you don't pass them up.");
            c.getPlayer().yellowMessage("The starter zone also has only Black Scrolls dropped in game.");
        }
        if (player.getTotalLevel() >= 10 && player.getTotalLevel() < 30) {
            c.getPlayer().yellowMessage("Maple Tips for levels 10-30");
            c.getPlayer().yellowMessage("Make sure you always wear your kaotic medal as it levels up with you gaining more stats.");
            c.getPlayer().yellowMessage("Now that you on main land. Free Market is open to you where you can buy RARE items from inkwell with monster coins/Pitch ior buy NX from SHOP button.");
            c.getPlayer().yellowMessage("Good Tip: Going to haunted house zombies in chimney drop alot of moonrocks worth alot of mesos.");
            c.getPlayer().yellowMessage("Good Tip: Dimensional Mirrior is good way to travel fast to major cities.");
            c.getPlayer().yellowMessage("Good Tip: Doing Quests in all Kerning, Henesys, Ellinia, Perion is quick way to level to 30.");
        }
        if (player.getTotalLevel() >= 30 && player.getTotalLevel() < 70) {
            c.getPlayer().yellowMessage("Maple Tips for levels 30-70");
            c.getPlayer().yellowMessage("After you make your first job advance, alot of options start to open up.");
            c.getPlayer().yellowMessage("Mini-Dungeons (bright portals) These instanced maps are party Boosted for max of 6 players.");
            c.getPlayer().yellowMessage("Maple weapons drop from ALL monster at rare chance and scale to monsters level. Higher the level better the stats.");
            c.getPlayer().yellowMessage("Look for maps that are strong to your job to level quickly to lvl 70.");
        }
        if (player.getTotalLevel() >= 70 && player.getTotalLevel() < 100) {
            c.getPlayer().yellowMessage("Maple Tips for levels 70-100");
            c.getPlayer().yellowMessage("Now that your third job, alot of power will come quickly based on your job. Focus on damage skills followed by passive.");
            c.getPlayer().yellowMessage("As you farm you may have noticed you have been collecting Golden Maple Leaves. These are used for BossPQ by talking to GAGA.");
            c.getPlayer().yellowMessage("WIth good damage and gear you may want to test your skills at Easy Boss PQ.");
            c.getPlayer().yellowMessage("This is stage of the game where you want to really start working on scrolling gear. Scrolled gear is massive as you can scroll 250 times per item.");
            c.getPlayer().yellowMessage("With decent damage you may want to try to kill some area bosses for hidden bonus loots they drop. Every area boss has them.");
        }
        if (player.getTotalLevel() >= 100 && player.getTotalLevel() < 120) {
            c.getPlayer().yellowMessage("Maple Tips for levels 100-120");
            c.getPlayer().yellowMessage("You should be nearing your 4th job advancement, the 2 bosses you need for advancement have 100 Million HP.");
            c.getPlayer().yellowMessage("You should also pay a visit to Neo Toyko located in Leafre, the mobs are weak high levels with good equip drops.");
            c.getPlayer().yellowMessage("You may want to pay a visit to Haunted House area and try and kill some headless horsemen for bonus equips.");
        }
        if (player.getTotalLevel() > 120 && player.getTotalLevel() < 140) {
            c.getPlayer().yellowMessage("Maple Tips for levels 120-140");
            c.getPlayer().yellowMessage("Now that your finally 4th Job, you will find your skill books in a shop in henesys or leafre, the guy's name is Abdula.");
            c.getPlayer().yellowMessage("You should be strong enough to start working on Temple of Time Quest line for overpower damage.");
        }
        if (player.getTotalLevel() >= 140 && player.getTotalLevel() < 160) {
            c.getPlayer().yellowMessage("Maple Tips for levels 160+");
            c.getPlayer().yellowMessage("Lion King Castle is now open to your level which yeilds a MASSIVE boost in equips and damage.");
            c.getPlayer().yellowMessage("The Boss, located in showa, is open and drops Cash Shop Rings with massive stats.");
            c.getPlayer().yellowMessage("You may want to complete papu quests in ludi for raid as he drops powerful earrings.");
            c.getPlayer().yellowMessage("Painus is good kill at this level, if you have a belt on killing him yields massive bonus to belt stats.");
        }
        if (player.getTotalLevel() >= 160 && player.getTotalLevel() < 200) {
            c.getPlayer().yellowMessage("Maple Tips for levels 160-200");
            c.getPlayer().yellowMessage("Temple Ruins is now unlocked for your level. Great place to farm GML's and Black scrolls.");
            c.getPlayer().yellowMessage("Neo Toyko Bosses are unlocked at this level. They drop Tyrants and Abs Rings.");
            c.getPlayer().yellowMessage("Middle door in Temple of Time has worth while training zones. Check it out.");
        }
        if (player.getTotalLevel() >= 200 && player.getTotalLevel() < 300) {
            c.getPlayer().yellowMessage("Maple Tips for levels 200-300");
            c.getPlayer().yellowMessage("Future Henesys/Perion zone is open in Warped passage of time.");
        }
        if (player.getTotalLevel() >= 300 && player.getTotalLevel() < 400) {
            c.getPlayer().yellowMessage("Maple Tips for levels 300-400");
            c.getPlayer().yellowMessage("Kritas is now unlocked in warped passage of time.");
            c.getPlayer().yellowMessage("Root Abyss is now unlocked in warped passage of time.");
        }

        if (player.getTotalLevel() >= 400 && player.getTotalLevel() < 500) {
            c.getPlayer().yellowMessage("Maple Tips for levels 400-500");
            c.getPlayer().yellowMessage("World Tree linked to Root Abyss is now unlocked.");
            c.getPlayer().yellowMessage("Stone Colosus in Warp passage of time is now unlocked.");
        }

        if (player.getTotalLevel() >= 500 && player.getTotalLevel() < 1000) {
            c.getPlayer().yellowMessage("Maple Tips for levels 500-1000");
            c.getPlayer().yellowMessage("Edelstin is unlocked in warped passge of time.");
            c.getPlayer().yellowMessage("Pantheon is unlocked in warped passge of time at level 700.");
            c.getPlayer().yellowMessage("Black Market is unlocked in warped passge of time at level 800.");
        }
        if (player.getTotalLevel() >= 1000) {
            c.getPlayer().yellowMessage("Maple Tips for levels 1000+");
            c.getPlayer().yellowMessage("First door in Temple of Time is open to Arcane River zone.");
        }
    }
}
