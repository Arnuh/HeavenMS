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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import scripting.event.EventInstanceManager;
import scripting.event.EventManager;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMap;
import tools.Randomizer;

public class SpawnCommand extends Command {

    {
        setDescription("@spawn monstername - ETC @spawn snail.");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        EventManager em = c.getEventManager("Tower_Train");
        MapleCharacter player = c.getPlayer();
        boolean requirements = true;
        if (!c.getPlayer().isGM()) {
            if (!player.getMap().getTrain()) {
                player.dropMessage(5, "You do not meet the requirements to use this command.");
                player.dropMessage(5, "Must be on VIP training map to use this command.");
                return;
            }
            if (player.getParty() == null) {
                player.dropMessage(5, "You do not meet the requirements to use this command.");
                player.dropMessage(5, "Must be in a party of 1 or more to use this command.");
                return;
            }
            if (player.getStamina() < player.getTotalLevel()) {
                player.dropMessage(5, player.getName() + " does not meet the requirements.");
                player.dropMessage(5, player.getName() + " must have at least " + player.getTotalLevel() + " Stamina to enable this command.");
                return;
            }
        }
        if (requirements) {
            if (params.length < 1) {
                player.dropMessage(5, "Please do @spawn <ID>");
            } else {
                if (player.isGM() || player.getEventInstance().getIntProperty("locked") == 0) {
                    int mobId = 0;
                    try {
                        mobId = Integer.parseInt(params[0]);
                    } catch (NumberFormatException e) {
                        player.dropMessage(5, "You can only spawns monsters with their ID not Names.");
                        return;
                    }
                    if (MapleLifeFactory.getMonster(mobId, player.getClient().getChannel()) != null) {
                        MapleMonster mob = MapleLifeFactory.getMonster(mobId, player.getClient().getChannel());
                        int maxlevel = player.getTotalLevel() + ((int) ((player.getTotalLevel() * 0.25) + 5) * mob.getStats().getScale());
                        if (player.isGM() || mob.getLevel() <= maxlevel) {
                            if (player.isGM() || !MapleLifeFactory.getMonster(mobId, player.getClient().getChannel()).getStats().isBoss()) {
                                if (c.getPlayer().isGM()) {
                                    MapleMap map = player.getMap();
                                    map.spawnMonsterWithEffect(MapleLifeFactory.getMonster(mobId, player.getClient().getChannel()), 112, player.getPosition(), player);
                                } else {
                                    MapleMap map = player.getEventInstance().getMapInstance(player.getMap().getId());
                                    player.getEventInstance().setIntProperty("locked", 1);
                                    try {
                                        player.getEventInstance().invokeScriptFunction("start", player.getEventInstance());
                                    } catch (ScriptException | NoSuchMethodException ex) {
                                        Logger.getLogger(EventManager.class.getName()).log(Level.SEVERE, "Event '" + em.getName() + "' does not implement start function.", ex);
                                    }
                                    player.removeStamina(player.getTotalLevel());
                                    for (int i = 0; i < 50; i++) {
                                        map.spawnMonsterWithEffect(MapleLifeFactory.getMonster(mobId, player.getClient().getChannel()), 15, new java.awt.Point(Randomizer.rand(-400, 1300), -25));
                                    }
                                }
                            } else {
                                player.dropMessage(5, "You cant spawn any bosses.");
                            }
                        } else {
                            player.dropMessage(5, "This monster is too strong you.");
                        }
                    } else {
                        player.dropMessage(5, "Error try a different ID.");
                    }
                } else {
                    player.dropMessage(5, "You cant spawn any more monsters.");
                }
            }
        } else {
            player.dropMessage(5, "You do not meet the requirements to use this command.");
            player.dropMessage(5, "Must ");
        }
    }
}
