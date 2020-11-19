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
package net.server.worker;

import client.MapleCharacter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.server.Server;
import net.server.world.World;
import tools.DatabaseConnection;

public class VoteWorker implements Runnable {

    @Override
    public void run() {
        World world = Server.getInstance().getWorld(0);
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM accounts WHERE `voted` > 0")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    for (MapleCharacter chr : world.getPlayerStorage().getAllCharacters()) {
                        if (chr.getAccountID() == rs.getInt("id")) {
                            chr.getCashShop().gainCash(2, rs.getInt("voted"));
                            chr.yellowMessage("Your pending " + rs.getInt("voted") + " vote points have been converted into " + rs.getInt("voted") + " Maple Points. Thank you for voting.");
                            try (PreparedStatement ps2 = con.prepareStatement("UPDATE accounts SET voted=0 WHERE id = ?")) {
                                ps2.setInt(1, rs.getInt("id"));
                                ps2.executeUpdate();
                            }
                            chr.saveCharToDBType(10);

                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println("Vote points processed " + ((System.currentTimeMillis() - timeToTake) / 1000.0) + " seconds");
    }
}
