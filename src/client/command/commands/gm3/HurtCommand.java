package client.command.commands.gm3;

import client.command.Command;
import client.MapleClient;
import client.MapleCharacter;
import net.server.Server;
import tools.MaplePacketCreator;

public class HurtCommand extends Command {
    {
        setDescription("");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        MapleCharacter player = c.getPlayer();
        MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(params[0]);
        int stamina = player.getStamina();
        if(stamina < 5) {
            player.message("This action requires 5 stamina.");
            return;
        }
        if (victim != null) {
            player.removeStamina(5);
            victim.updateHp(0);
            Server.getInstance().broadcastMessage(c.getWorld(), MaplePacketCreator.serverNotice(6, "[RIP]: " + victim + " has been assassinated by " + player + "."));
        } else {
            player.message("Player '" + params[0] + "' could not be found.");
        }
    }
}
