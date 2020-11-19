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
package scripting.portal;

import client.MapleCharacter;
import client.MapleClient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import scripting.AbstractPlayerInteraction;
import server.MaplePortal;
import server.quest.MapleQuest;
import tools.DatabaseConnection;
import tools.MaplePacketCreator;

public class PortalPlayerInteraction extends AbstractPlayerInteraction {

    private MaplePortal portal;

    public PortalPlayerInteraction(MapleClient c, MaplePortal portal) {
        super(c);
        this.portal = portal;
    }

    public MaplePortal getPortal() {
        return portal;
    }

    public boolean hasLevel30Character() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT `level` FROM `characters` WHERE accountid = ?");
            ps.setInt(1, getPlayer().getAccountID());
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("level") >= 30) {
                    rs.close();
                    ps.close();
                    return true;
                }
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {

                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return getPlayer().getLevel() >= 30;
    }

    public void blockPortal() {
        c.getPlayer().blockPortal(getPortal().getScriptName());
    }

    public void unblockPortal() {
        c.getPlayer().unblockPortal(getPortal().getScriptName());
    }

    public void playPortalSound() {
        c.announce(MaplePacketCreator.playPortalSound());
    }

    public String PortalName() {
        return portal.getTarget();
    }

    public int PortalMap() {
        return portal.getTargetMapId();
    }

    public void warp(int map, int portal) {
        playPortalSound();
        c.getPlayer().changeMap(map, portal);
    }

    public int getMinlevel() {
        switch (getPortal().getTargetMapId()) {
            case 98000://temple ruins - ark
                return 160;
            case 271000000://future hene/ksh
            case 82000://LKC
            case 91000://passage of time
                return 200;
            case 273010000://future perion
                return 250;
            case 241000000://krit
            case 86000://RA
                return 300;
            case 240090000://stone colosus
                return 350;
            case 310010010://edelstin
                return 500;
            case 310040000://edelstin outside caves
                return 550;
            case 400000001://pantheon
            case 310070100://black heaven
                return 700;
            case 401000002://helisium
                return 750;
            case 402000000://black market
                return 800;
            case 401050001://tyrant castle
            case 402000300://behind blackmarket
                return 850;
            case 402000501://santuary
            case 402000600://verdel
                return 900;
            case 402000521://san-temple
            case 402000631://verdel-temple
                return 950;
            case 450001003://arc river - ext town
                return 1000;
            case 450001200://cave enterance - ext town
                return 1050;
            case 450014010://reverse-city
                return 1100;
            case 450014100://reverse-city upper
                return 1125;
            case 450014140://reverse-city lower
                return 1150;
            case 450001250://chuchu
                return 1200;
            case 450002016://chuchu - whale
                return 1250;
            case 450015020://yumyum
                return 1300;
            case 450015150://yumyum - middle
                return 1325;
            case 450015190://yumyum - dark
                return 1350;
            case 450003100://lach - nightmarket
                return 1425;
            case 450003400://Lach - East
                return 1450;
            case 450003500://Lach Clocktower
                return 1475;
            case 450005010://Arcana
                return 1500;
            case 450005200://Arc - deep forest
                return 1525;
            case 450005430://arc - east lagoon
            case 450005410://are - west lagoon
                return 1550;
            case 450005500://arc - lagoon
                return 1575;
            case 450006010://morass
                return 1600;
            case 450006130://tuffet town
                return 1625;
            case 450006140://tuffet - upper area
                return 1650;
            case 450006200://tuffet underground
                return 1700;
            case 450006300://tuffet underground part 2
                return 1725;
            case 450006400://destoryed tuffet
                return 1750;
            case 450007000://estera
                return 1800;
            case 450007050://living sea
                return 1825;
            case 450007110://Mirrior 
                return 1850;
            case 450007140://Mirrior 2
                return 1875;
            case 450007220://temple 2
                return 1925;
            case 450007230://temple 3
                return 1950;
            case 450007240://temple 4 - will
                return 1975;
        }

        return 0;
    }
}
