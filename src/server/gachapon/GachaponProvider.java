/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.gachapon;

import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.ItemConstants;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import server.MapleItemInformationProvider;
import tools.DatabaseConnection;
import tools.FilePrinter;
import tools.Randomizer;

/**
 *
 * @author David
 */
public class GachaponProvider {

    private static final Map<Integer, List<GachaponEntry>> entries = new HashMap<>();

    private static void loadFromDB() {
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM gach_data");
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int townId = rs.getInt("gachid");
                List<GachaponEntry> items = entries.get(townId);
                if (items == null) {
                    items = new LinkedList<>();
                    entries.put(townId, items);
                }
                GachaponEntry itemEntry = new GachaponEntry(rs.getInt("itemid"), rs.getInt("chance"), rs.getInt("minimum_quantity"), rs.getInt("maximum_quantity"));
                items.add(itemEntry);
            }
        } catch (SQLException e) {
            FilePrinter.printError(FilePrinter.SQL_EXCEPTION, e, "Failed to get DB connection.");
        }
    }

    public static void reloadFromDB() {
        synchronized (entries) {
            entries.clear();
            loadFromDB();
        }
    }

    public static List<Item> getRewards(int townId, int cost, int count) {
        List<GachaponEntry> townEntries;
        synchronized (entries) {
            townEntries = entries.get(townId);
            if (townEntries == null) {
                reloadFromDB();
                townEntries = entries.get(townId);
                if (townEntries == null) {
                    throw new IllegalArgumentException("Unknown Town/NPC ID provided to GachaponProvider.getRewards()");
                }
            }
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        List<Item> rewards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            GachaponEntry reward = townEntries.get(Randomizer.nextInt(townEntries.size()));
            if (ItemConstants.getInventoryType(reward.itemId) == MapleInventoryType.EQUIP) {
                rewards.add((Item) ii.randomizeStatsbyMobLevel((Equip) ii.getEquipById(reward.itemId), cost));
            } else {
                rewards.add(new Item(reward.itemId, (short) 0, (short) (Randomizer.randomMinMax(reward.Minimum, reward.Maximum + Randomizer.nextInt(10))), -1));
            }
        }
        return rewards;
    }

    public static List<Integer> getAllRewards(int townId) {
        List<GachaponEntry> townEntries;
        synchronized (entries) {
            townEntries = entries.get(townId);
            if (townEntries == null) {
                reloadFromDB();
                townEntries = entries.get(townId);
                if (townEntries == null) {
                    throw new IllegalArgumentException("Unknown Town/NPC ID provided to GachaponProvider.getRewards()");
                }
            }
        }
        List<Integer> ret = new ArrayList<>();
        for (GachaponEntry cur : townEntries) {
            ret.add(cur.itemId);
        }
        return ret;
    }

}
