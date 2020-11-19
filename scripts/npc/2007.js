/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>
 
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
/* Author: Xterminator
 NPC Name: 		Shanks
 Map(s): 		Maple Road : Southperry (60000)
 Description: 		Brings you to Victoria Island
 */
var status = 0;

function start() {
    cm.sendSimple("Which option would you like to know more about?\r\n\#L1# Jobs #l\r\n\#L2# Monsters #l\r\n\#L3# Items #l\r\n\#L4# Scrolls #l\r\n\#L5# Custom Npcs #l\r\n\#L6# Custom Events #l\r\n\#L7# Damage System #l\r\n\#L8# Level System #l\r\n\#L9# Gacha System #l\r\n\#L10# Limit System #l\r\n");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1) {
        if (mode == 0 && type != 1) {
            status -= 2;
        } else if (type == 1 || (mode == -1 && type != 1)) {
            if (mode == 0) {
                cm.sendOk("Hmm... I guess you still have things to do here?");
            }
            cm.dispose();
            return;
        }
    }
    if (status == 1) {
        if (selection == 1) {
            cm.sendNext("Jobs: \r\n\r\n\      Job advancements are GMS-Like. When you reach the level of that job 30,70,120 you stay at that level until you job adv. At level 200, you can unlock sub-job switch which allows you to change jobs within your class. At lvl 250, you unlock FULL job change to play any job u want. Different combos of skills will unlock new potentials. GM job is mainly used for farming equips and scrolls with its passive x2 drop bonus stacks with events.");
        } else if (selection == 2) {
            cm.sendNext("Monsters: \r\n\r\n\      Monsters are VERY customed when it comes to rates,HP, and EXP. Monsters comes in several tiers ranging normal-tier 1 to extreme-tier 6. Tier 1 is normal mobs, Tier 2 is area bosses and high level zones, Tier 3 is super bosses such as Papu, Tier 4 is mega bosses such as pianus, zakum, The Boss, Tier 5 is ultra boss such as Empress, Magnus, Baby Lotus, Tier 6 is Black Mage-coming soon. Monster Stats work as follows. ALl mobs are equal in terms of HP and EXP. Hp is based on Level ^ Tier (Level*level). Exp is based on Mobs Level*level * Number of players online. Drops is every 10 levels = 1x.");
        } else if (selection == 3) {
            cm.sendNext("Items: \r\n\r\n\      Items dropped by monsters are based on their tier and level. For example normal lvl 120 monster will drop an item with power of 120. For a tiered bosses, Tier 2 boss = 2x Power, Tier 3 boss = 3x Power, Tier 4 boss = 4x Power, Tier 5 boss = 5x Power, Tier 6 boss = 6x Power. Stronger the monster's level and tier Better the drops. Also drop rates increase with Tier/level. Certain bosses drop hidden bonus rewards.");
        } else if (selection == 4) {
            cm.sendNext("Scrolling: \r\n\r\n\      Scrolling items is EXTREMELY vital. Fully scroll lvl 30 can out perform lvl 180 with nothing. Each item comes with 1 slot, that slot is consumed once an item has reach +250. Chaos scrolls yield 0-5 stats on avg per scroll, only found in gacha. Black Scroll consumes the slot and grants 0-250 base on remaining levels found in starting zone and in Inkwell for 2500 coins. Example would be if said item is +27 the black will grant 0-228 stats.");
        } else if (selection == 5) {
            cm.sendNext("Custom Npcs: \r\n\r\n\      Custom Npcs are a thing here that help provide alot new and exiciting content. Here's a list of them.\r\n\r\n\ Mia(Henesys) handles VIP Training Room.\r\n\ Nana(Henesys) handles Stat reseting\r\n\ Cody handles Hair/Skin/Eye looks \r\n\ Inkwell Custom shop for monster coins\r\n\ Fredrick(FM) handles NX Items\r\n\ Gaga handles Monster Park/BossPQ\r\n\ Tia(Perion) handles NX->Monster Coins\r\n\ Nana(Perion) tells locations of 30% scrolls\r\n\ Donation Box handles Leaves->GML\r\n\ Nana(Orbis) handles item reset with GML\r\n\ Dimensional Mirrior handles Fast Travel\r\n\ Mrs.Clause handles Snowman Raid\r\n\ Santa handles 10% scrolls -> 100% scrolls\r\n\ Nana(Ellinia) handles Medal Level up\r\n\ Ria(Ellinia) Gives free snail pet PERM\r\n\ Abadula(Henesys/Leafre) Sells skill books\r\n\ Fa Hai(ToT) Job Switcher for level 200+\r\n\ Nana(Ludi) Upgrades equips with GML");
        } else if (selection == 6) {
            cm.sendNext("Custom Events: \r\n\r\n\      Custom events are a way to help bring more challenging aspects to the game. Here is a list of them.\r\n\r\n\ Golden Pigs - when killed drops ALOT of GML\r\n\ Monster Park - Talk to Gaga - level 120+\r\n\ Boss PQ Easy/Normal/Hard/Ultmiate - talk to gaga\r\n\ Ultimate PinkBean - Finish ToT quest line lvl 250\r\n\ Kaotic Tower - FM - Leveling Tower with no drops\r\n\ VIP Training room - 25,000 NX - Henesys");
        } else if (selection == 7) {
            cm.sendNext("Damage System: \r\n\r\n\      Monsters deal REAL damage shown on msg top middle of screen and red combo numbers to the right of screen. Damage does get reduced with Defense and Magic Defense properly. Higher tier monster ignore defense more and deal more damage.");
        } else if (selection == 8) {
            cm.sendNext("Level System: \r\n\r\n\      New level system works as follows. Max real level is 250, Overlevel is infinite. Total level is real level + overlevel. Items in the server are based on your Total Level along with monster levels. Exp gain works as follows: moblevel / chrtotallevel. If a monster is level 120 and your lvl 100, you will get a 20% boost in exp gain. If a monster is lvl 50 and your level is 100, you will gain 50% of the total exp.");
        } else if (selection == 9) {
            cm.sendNext("Gacha System: \r\n\r\n\      Gachapon now is gambling machine. Sometimes you may get back many items or no items. Higher your level TOTAL level better the rewards. Higher levels will yield higher stats on equips or increase amount of usable items.");
        } else if (selection == 10) {
            cm.sendNext("Limit System: \r\n\r\n\      Mob level limit system. Use @limit to view your limits for monsters. Monsters over the limit will block all damage dealt to themselves. This effect applies per mob attacked.");
        }
    } else {
        cm.sendOk("Hmm... I guess you still have things to do here?");
        cm.dispose();
    }
}

