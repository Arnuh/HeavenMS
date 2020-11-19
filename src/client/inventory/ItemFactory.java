/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package client.inventory;

import client.MapleCharacter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import net.server.audit.locks.MonitoredLockType;
import net.server.audit.locks.factory.MonitoredReentrantLockFactory;
import tools.DatabaseConnection;
import tools.Pair;

/**
 *
 * @author Flav
 */
public enum ItemFactory {

    INVENTORY(1, false),
    STORAGE(2, true),
    CASH_EXPLORER(3, true),
    CASH_CYGNUS(4, true),
    CASH_ARAN(5, true),
    MERCHANT(6, false),
    CASH_OVERALL(7, true),
    MARRIAGE_GIFTS(8, false),
    DUEY(9, false);
    private final int value;
    private final boolean account;

    private static final int lockCount = 400;
    private static final Lock locks[] = new Lock[lockCount];  // thanks Masterrulax for pointing out a bottleneck issue here
    private final Lock invLock = MonitoredReentrantLockFactory.createLock(MonitoredLockType.INVENTORY);

    static {
        for (int i = 0; i < lockCount; i++) {
            locks[i] = MonitoredReentrantLockFactory.createLock(MonitoredLockType.ITEM, true);
        }
    }

    private ItemFactory(int value, boolean account) {
        this.value = value;
        this.account = account;
    }

    public int getValue() {
        return value;
    }

    public void lockItem() {
        invLock.lock();
    }

    public void unlockItem() {
        invLock.unlock();
    }

    public List<Pair<Item, MapleInventoryType>> loadItems(int id, boolean login) throws SQLException {
        if (value != 6) {
            return loadItemsCommon(id, login);
        } else {
            return loadItemsMerchant(id, login);
        }
    }

    public void saveItems(List<Pair<Item, MapleInventoryType>> items, int id, Connection con) throws SQLException {
        saveItems(items, null, id, con);
    }

    public void saveItems(List<Pair<Item, MapleInventoryType>> items, List<Integer> bundlesList, int id, Connection con) throws SQLException {
        // thanks Arufonsu, MedicOP, BHB for pointing a "synchronized" bottleneck here

        if (value != 6) {
            saveItemsCommon(items, id, con);
        } else {
            saveItemsMerchant(items, bundlesList, id, con);
        }
    }

    private static Equip loadEquipFromResultSet(ResultSet rs) throws SQLException {
        Equip equip = new Equip(rs.getInt("itemid"), (short) rs.getInt("position"));
        equip.setOwner(rs.getString("owner"));
        equip.setQuantity(rs.getInt("quantity"));
        equip.setAcc((short) rs.getInt("acc"));
        equip.setAvoid((short) rs.getInt("avoid"));
        equip.setDex(rs.getInt("dex"));
        equip.setHands((short) rs.getInt("hands"));
        equip.setHp((short) rs.getInt("hp"));
        equip.setInt(rs.getInt("int"));
        equip.setJump((short) rs.getInt("jump"));
        equip.setVicious((short) rs.getInt("vicious"));
        equip.setFlag((byte) rs.getInt("flag"));
        equip.setLuk(rs.getInt("luk"));
        equip.setMatk(rs.getInt("matk"));
        equip.setMdef(rs.getInt("mdef"));
        equip.setMp((short) rs.getInt("mp"));
        equip.setSpeed((short) rs.getInt("speed"));
        equip.setStr(rs.getInt("str"));
        equip.setWatk(rs.getInt("watk"));
        equip.setWdef(rs.getInt("wdef"));
        equip.setUpgradeSlots((byte) rs.getInt("upgradeslots"));
        equip.setLevel((short) rs.getShort("level"));
        equip.setItemExp(rs.getInt("itemexp"));
        equip.setItemLevel(rs.getByte("itemlevel"));
        equip.setExpiration(rs.getLong("expiration"));
        equip.setGiftFrom(rs.getString("giftFrom"));
        equip.setRingId(rs.getInt("ringid"));
        equip.setPowerLevel(rs.getInt("powerlevel"));

        return equip;
    }

    public static List<Pair<Item, Integer>> loadEquippedItems(int id, boolean isAccount, boolean login) throws SQLException {
        List<Pair<Item, Integer>> items = new ArrayList<>();

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ");
        query.append("(SELECT id, accountid FROM characters) AS accountterm ");
        query.append("RIGHT JOIN ");
        query.append("(SELECT * FROM (`inventoryitems` LEFT JOIN `inventoryequipment` USING(`inventoryitemid`))) AS equipterm");
        query.append(" ON accountterm.id=equipterm.characterid ");
        query.append("WHERE accountterm.`");
        query.append(isAccount ? "accountid" : "characterid");
        query.append("` = ?");
        query.append(login ? " AND `inventorytype` = " + MapleInventoryType.EQUIPPED.getType() : "");

        try (Connection con = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(query.toString())) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Integer cid = rs.getInt("characterid");
                        items.add(new Pair<Item, Integer>(loadEquipFromResultSet(rs), cid));
                    }
                }
            }
        }

        return items;
    }

    private List<Pair<Item, MapleInventoryType>> loadItemsCommon(int id, boolean login) throws SQLException {
        List<Pair<Item, MapleInventoryType>> items = new ArrayList<>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = DatabaseConnection.getConnection();
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM `inventoryitems` LEFT JOIN `inventoryequipment` USING(`inventoryitemid`) WHERE `type` = ? AND `");
            query.append(account ? "accountid" : "characterid").append("` = ?");

            if (login) {
                query.append(" AND `inventorytype` = ").append(MapleInventoryType.EQUIPPED.getType());
            }

            ps = con.prepareStatement(query.toString());
            ps.setInt(1, value);
            ps.setInt(2, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                MapleInventoryType mit = MapleInventoryType.getByType(rs.getByte("inventorytype"));

                if (mit.equals(MapleInventoryType.EQUIP) || mit.equals(MapleInventoryType.EQUIPPED)) {
                    items.add(new Pair<Item, MapleInventoryType>(loadEquipFromResultSet(rs), mit));
                } else {
                    Item item = new Item(rs.getInt("itemid"), (byte) rs.getInt("position"), rs.getInt("quantity"), rs.getInt("petid"));
                    item.setOwner(rs.getString("owner"));
                    item.setExpiration(rs.getLong("expiration"));
                    item.setGiftFrom(rs.getString("giftFrom"));
                    item.setFlag((byte) rs.getInt("flag"));
                    items.add(new Pair<>(item, mit));
                }
            }

            rs.close();
            ps.close();
            con.close();
        } finally {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
        }
        return items;
    }

    private void saveItemsCommon(List<Pair<Item, MapleInventoryType>> items, int id, Connection con) throws SQLException {
        Lock lock = locks[id % lockCount];
        lock.lock();
        try {
            StringBuilder query = new StringBuilder();
            query.append("DELETE `inventoryitems`, `inventoryequipment` FROM `inventoryitems` LEFT JOIN `inventoryequipment` USING(`inventoryitemid`) WHERE `type` = ? AND `");
            query.append(account ? "accountid" : "characterid").append("` = ?");
            try (PreparedStatement ps = con.prepareStatement(query.toString())) {
                ps.setLong(1, value);
                ps.setInt(2, id);
                ps.executeUpdate();
                ps.close();
            }
            //try (PreparedStatement ps = con.prepareStatement("DELETE FROM inventoryequipment WHERE inventoryitemid NOT IN (SELECT inventoryitemid FROM inventoryitems)")) {
            //    ps.executeUpdate();
           // }
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO `inventoryitems` VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

                if (!items.isEmpty()) {
                    for (Pair<Item, MapleInventoryType> pair : items) {
                        Item item = pair.getLeft();
                        MapleInventoryType mit = pair.getRight();
                        ps.setLong(1, value);
                        ps.setString(2, account ? null : String.valueOf(id));
                        ps.setString(3, account ? String.valueOf(id) : null);
                        ps.setInt(4, item.getItemId());
                        ps.setInt(5, mit.getType());
                        ps.setInt(6, item.getPosition());
                        ps.setInt(7, item.getQuantity());
                        ps.setString(8, item.getOwner());
                        ps.setInt(9, item.getPetId());
                        ps.setInt(10, item.getFlag());
                        ps.setLong(11, item.getExpiration());
                        ps.setString(12, item.getGiftFrom());
                        ps.executeUpdate();
                        if (mit.equals(MapleInventoryType.EQUIP) || mit.equals(MapleInventoryType.EQUIPPED)) {
                            try (PreparedStatement pse = con.prepareStatement("INSERT INTO `inventoryequipment` VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                                try (ResultSet rs = ps.getGeneratedKeys()) {

                                    if (!rs.next()) {
                                        throw new RuntimeException("Inserting item failed.");
                                    }

                                    pse.setLong(1, rs.getLong(1));
                                }

                                Equip equip = (Equip) item;
                                pse.setInt(2, equip.getItemId());
                                pse.setInt(3, id);
                                pse.setInt(4, equip.getUpgradeSlots());
                                pse.setInt(5, equip.getLevel());
                                pse.setInt(6, equip.getStr());
                                pse.setInt(7, equip.getDex());
                                pse.setInt(8, equip.getInt());
                                pse.setInt(9, equip.getLuk());
                                pse.setInt(10, equip.getHp());
                                pse.setInt(11, equip.getMp());
                                pse.setInt(12, equip.getWatk());
                                pse.setInt(13, equip.getMatk());
                                pse.setInt(14, equip.getWdef());
                                pse.setInt(15, equip.getMdef());
                                pse.setInt(16, equip.getAcc());
                                pse.setInt(17, equip.getAvoid());
                                pse.setInt(18, equip.getHands());
                                pse.setInt(19, equip.getSpeed());
                                pse.setInt(20, equip.getJump());
                                pse.setInt(21, 0);
                                pse.setInt(22, equip.getVicious());
                                pse.setInt(23, equip.getItemLevel());
                                pse.setInt(24, equip.getItemExp());
                                pse.setInt(25, equip.getRingId());
                                pse.setInt(26, equip.getPowerLevel());
                                pse.executeUpdate();
                            }
                        }
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void saveItemsCommonAll(Collection<MapleCharacter> chars, Connection con) throws SQLException {

        long timeToTake = System.currentTimeMillis();
        //Thread.dumpStack();

        //Lock lock = locks[id % lockCount];
        //lock.lock();
        invLock.lock();
        try {
            StringBuilder query = new StringBuilder();
            query.append("DELETE `inventoryitems`, `inventoryequipment` FROM `inventoryitems` LEFT JOIN `inventoryequipment` USING(`inventoryitemid`) WHERE `type` = ? AND `");
            query.append(account ? "accountid" : "characterid").append("` = ?");
            try (PreparedStatement ps = con.prepareStatement(query.toString())) {
                for (MapleCharacter chrs : chars) {
                    ps.setLong(1, value);
                    ps.setInt(2, chrs.getId());
                    ps.executeUpdate();
                }
            }
            //System.out.println("All Player items deleted completed in " + ((System.currentTimeMillis() - timeToTake) / 1000.0) + " seconds");

            try (PreparedStatement ps = con.prepareStatement("INSERT INTO `inventoryitems` VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                for (MapleCharacter chrs : chars) {
                    List<Pair<Item, MapleInventoryType>> itemsWithType = new ArrayList<>();
                    for (MapleInventory iv : chrs.getInventory()) {
                        for (Item item : iv.list()) {
                            itemsWithType.add(new Pair<>(item, iv.getType()));
                        }
                    }

                    if (!itemsWithType.isEmpty()) {
                        for (Pair<Item, MapleInventoryType> pair : itemsWithType) {
                            Item item = pair.getLeft();
                            MapleInventoryType mit = pair.getRight();
                            ps.setLong(1, value);
                            ps.setString(2, account ? null : String.valueOf(chrs.getId()));
                            ps.setString(3, account ? String.valueOf(chrs.getId()) : null);
                            ps.setInt(4, item.getItemId());
                            ps.setInt(5, mit.getType());
                            ps.setInt(6, item.getPosition());
                            ps.setInt(7, item.getQuantity());
                            ps.setString(8, item.getOwner());
                            ps.setInt(9, item.getPetId());
                            ps.setInt(10, item.getFlag());
                            ps.setLong(11, item.getExpiration());
                            ps.setString(12, item.getGiftFrom());
                            ps.executeUpdate();
                            if (mit.equals(MapleInventoryType.EQUIP) || mit.equals(MapleInventoryType.EQUIPPED)) {
                                try (PreparedStatement pse = con.prepareStatement("INSERT INTO `inventoryequipment` VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                                    try (ResultSet rs = ps.getGeneratedKeys()) {

                                        if (!rs.next()) {
                                            throw new RuntimeException("Inserting item failed.");
                                        }

                                        pse.setLong(1, rs.getLong(1));
                                    }

                                    Equip equip = (Equip) item;
                                    pse.setInt(2, equip.getItemId());
                                    pse.setInt(3, chrs.getId());
                                    pse.setInt(4, equip.getUpgradeSlots());
                                    pse.setInt(5, equip.getLevel());
                                    pse.setInt(6, equip.getStr());
                                    pse.setInt(7, equip.getDex());
                                    pse.setInt(8, equip.getInt());
                                    pse.setInt(9, equip.getLuk());
                                    pse.setInt(10, equip.getHp());
                                    pse.setInt(11, equip.getMp());
                                    pse.setInt(12, equip.getWatk());
                                    pse.setInt(13, equip.getMatk());
                                    pse.setInt(14, equip.getWdef());
                                    pse.setInt(15, equip.getMdef());
                                    pse.setInt(16, equip.getAcc());
                                    pse.setInt(17, equip.getAvoid());
                                    pse.setInt(18, equip.getHands());
                                    pse.setInt(19, equip.getSpeed());
                                    pse.setInt(20, equip.getJump());
                                    pse.setInt(21, 0);
                                    pse.setInt(22, equip.getVicious());
                                    pse.setInt(23, equip.getItemLevel());
                                    pse.setInt(24, equip.getItemExp());
                                    pse.setInt(25, equip.getRingId());
                                    pse.setInt(26, equip.getPowerLevel());
                                    pse.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }
            //System.out.println("All Player items saved completed in " + ((System.currentTimeMillis() - timeToTake) / 1000.0) + " seconds");
        } finally {
            invLock.unlock();
        }
    }

    private List<Pair<Item, MapleInventoryType>> loadItemsMerchant(int id, boolean login) throws SQLException {
        List<Pair<Item, MapleInventoryType>> items = new ArrayList<>();

        PreparedStatement ps = null, ps2 = null;
        ResultSet rs = null, rs2 = null;
        Connection con = DatabaseConnection.getConnection();
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM `inventoryitems` LEFT JOIN `inventoryequipment` USING(`inventoryitemid`) WHERE `type` = ? AND `");
            query.append(account ? "accountid" : "characterid").append("` = ?");

            if (login) {
                query.append(" AND `inventorytype` = ").append(MapleInventoryType.EQUIPPED.getType());
            }

            ps = con.prepareStatement(query.toString());
            ps.setInt(1, value);
            ps.setInt(2, id);
            rs = ps.executeQuery();

            while (rs.next()) {
                ps2 = con.prepareStatement("SELECT `bundles` FROM `inventorymerchant` WHERE `inventoryitemid` = ?");
                ps2.setLong(1, rs.getLong("inventoryitemid"));
                rs2 = ps2.executeQuery();

                short bundles = 0;
                if (rs2.next()) {
                    bundles = rs2.getShort("bundles");
                }

                MapleInventoryType mit = MapleInventoryType.getByType(rs.getByte("inventorytype"));

                if (mit.equals(MapleInventoryType.EQUIP) || mit.equals(MapleInventoryType.EQUIPPED)) {
                    items.add(new Pair<Item, MapleInventoryType>(loadEquipFromResultSet(rs), mit));
                } else {
                    if (bundles > 0) {
                        Item item = new Item(rs.getInt("itemid"), (byte) rs.getInt("position"), (bundles * rs.getInt("quantity")), rs.getInt("petid"));
                        item.setOwner(rs.getString("owner"));
                        item.setExpiration(rs.getLong("expiration"));
                        item.setGiftFrom(rs.getString("giftFrom"));
                        item.setFlag((byte) rs.getInt("flag"));
                        items.add(new Pair<>(item, mit));
                    }
                }

                rs2.close();
                ps2.close();
            }

            rs.close();
            ps.close();
            con.close();
        } finally {
            if (rs2 != null && !rs2.isClosed()) {
                rs2.close();
            }
            if (ps2 != null && !ps2.isClosed()) {
                ps2.close();
            }
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
            if (con != null && !con.isClosed()) {
                con.close();
            }
        }
        return items;
    }

    private void saveItemsMerchant(List<Pair<Item, MapleInventoryType>> items, List<Integer> bundlesList, int id, Connection con) throws SQLException {
        PreparedStatement ps = null;
        PreparedStatement pse = null;
        ResultSet rs = null;

        Lock lock = locks[id % lockCount];
        lock.lock();
        try {
            ps = con.prepareStatement("DELETE FROM `inventorymerchant` WHERE `characterid` = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();

            StringBuilder query = new StringBuilder();
            query.append("DELETE `inventoryitems`, `inventoryequipment` FROM `inventoryitems` LEFT JOIN `inventoryequipment` USING(`inventoryitemid`) WHERE `type` = ? AND `");
            query.append(account ? "accountid" : "characterid").append("` = ?");
            ps = con.prepareStatement(query.toString());
            ps.setLong(1, value);
            ps.setInt(2, id);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement("INSERT INTO `inventoryitems` VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            if (!items.isEmpty()) {
                int i = 0;
                for (Pair<Item, MapleInventoryType> pair : items) {
                    Item item = pair.getLeft();
                    Integer bundles = bundlesList.get(i);
                    MapleInventoryType mit = pair.getRight();
                    i++;

                    ps.setLong(1, value);
                    ps.setString(2, account ? null : String.valueOf(id));
                    ps.setString(3, account ? String.valueOf(id) : null);
                    ps.setInt(4, item.getItemId());
                    ps.setInt(5, mit.getType());
                    ps.setInt(6, item.getPosition());
                    ps.setInt(7, item.getQuantity());
                    ps.setString(8, item.getOwner());
                    ps.setInt(9, item.getPetId());
                    ps.setInt(10, item.getFlag());
                    ps.setLong(11, item.getExpiration());
                    ps.setString(12, item.getGiftFrom());
                    ps.executeUpdate();

                    rs = ps.getGeneratedKeys();
                    if (!rs.next()) {
                        throw new RuntimeException("Inserting item failed.");
                    }

                    int genKey = rs.getInt(1);
                    rs.close();

                    pse = con.prepareStatement("INSERT INTO `inventorymerchant` VALUES (DEFAULT, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                    pse.setInt(1, genKey);
                    pse.setInt(2, id);
                    pse.setInt(3, bundles);
                    pse.executeUpdate();
                    pse.close();

                    if (mit.equals(MapleInventoryType.EQUIP) || mit.equals(MapleInventoryType.EQUIPPED)) {
                        pse = con.prepareStatement("INSERT INTO `inventoryequipment` VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        pse.setLong(1, genKey);

                        Equip equip = (Equip) item;
                        pse.setInt(2, equip.getUpgradeSlots());
                        pse.setInt(3, equip.getLevel());
                        pse.setInt(4, equip.getStr());
                        pse.setInt(5, equip.getDex());
                        pse.setInt(6, equip.getInt());
                        pse.setInt(7, equip.getLuk());
                        pse.setInt(8, equip.getHp());
                        pse.setInt(9, equip.getMp());
                        pse.setInt(10, equip.getWatk());
                        pse.setInt(11, equip.getMatk());
                        pse.setInt(12, equip.getWdef());
                        pse.setInt(13, equip.getMdef());
                        pse.setInt(14, equip.getAcc());
                        pse.setInt(15, equip.getAvoid());
                        pse.setInt(16, equip.getHands());
                        pse.setInt(17, equip.getSpeed());
                        pse.setInt(18, equip.getJump());
                        pse.setInt(19, 0);
                        pse.setInt(20, equip.getVicious());
                        pse.setInt(21, equip.getItemLevel());
                        pse.setInt(22, equip.getItemExp());
                        pse.setInt(23, equip.getRingId());
                        pse.setInt(24, equip.getPowerLevel());
                        pse.executeUpdate();

                        pse.close();
                    }
                }
            }

            ps.close();
        } finally {
            if (ps != null && !ps.isClosed()) {
                ps.close();
            }
            if (pse != null && !pse.isClosed()) {
                pse.close();
            }
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }

            lock.unlock();
        }
    }
}
