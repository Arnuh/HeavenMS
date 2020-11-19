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
package client.command.commands.gm6;

import client.MapleCharacter;
import client.command.Command;
import client.MapleClient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.server.Server;
import net.server.channel.Channel;
import net.server.world.World;
import tools.DatabaseConnection;
import tools.MaplePacketCreator;

public class setVIPCommand extends Command {

    {
        setDescription("");
    }

    @Override
    public void execute(MapleClient c, String[] params) {
        int accountid = Integer.parseInt(params[0]);
        int days = Integer.parseInt(params[1]);
        long duration = days * 60000 * 60 * 24;
        try (Connection con = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("UPDATE characters SET gm = 1 WHERE accountid = ?")) {
                ps.setInt(1, accountid);
                ps.executeUpdate();
            }
            try(PreparedStatement ps = con.prepareStatement("UPDATE accounts SET vip = ? WHERE id = ?")) {
                ps.setLong(1, duration);
                ps.setInt(2, accountid);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        for(World world : Server.getInstance().getWorlds()) {
            for(MapleCharacter cur : world.getPlayerStorage().getAllCharacters()) {
                if(cur.getAccountID() == accountid) {
                    cur.setVipDuration(duration);
                    cur.setLoginTime();
                    cur.setGMLevel(1);
                    cur.vipEndTask();
                    break;
                }
            }
        }
        c.getPlayer().dropMessage(6, "Account ID: " + accountid + " vip added for " + days + " days");
    }
}
