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
package client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.Semaphore;
import tools.DatabaseConnection;
import tools.MaplePacketCreator;
import net.server.audit.locks.MonitoredLockType;
import net.server.audit.locks.factory.MonitoredReentrantLockFactory;

public final class MonsterBook {

    private static final Semaphore semaphore = new Semaphore(10);

    private int specialCard = 0;
    private int normalCard = 0;
    private int bookLevel = 1;
    private Map<Integer, Integer> cards = new LinkedHashMap<>();
    private Lock lock = MonitoredReentrantLockFactory.createLock(MonitoredLockType.BOOK);

    private Set<Entry<Integer, Integer>> getCardSet() {
        lock.lock();
        try {
            return Collections.unmodifiableSet(cards.entrySet());
        } finally {
            lock.unlock();
        }
    }

    public void addCard(final MapleClient c, final int cardid, boolean player) {
        //c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.showForeignCardEffect(c.getPlayer().getId()), false);

        Integer qty;
        lock.lock();
        try {
            qty = cards.get(cardid);

            if (qty != null) {
                if (qty < 5) {
                    cards.put(cardid, qty + 1);
                } else {
                    cards.put(cardid, 5);
                }
            } else {
                cards.put(cardid, 1);
                qty = 0;

                if (cardid / 1000 >= 2388) {
                    specialCard++;
                } else {
                    normalCard++;
                }
            }
        } finally {
            lock.unlock();
        }

        calculateLevel();   // current leveling system only accounts unique cards...
        if (qty < 5) {
            c.announce(MaplePacketCreator.addCard(false, cardid, cards.get(cardid)));
            c.announce(MaplePacketCreator.showGainCard());
        }
    }

    private void calculateLevel() {
        lock.lock();
        try {
            bookLevel = (int) Math.max(1, Math.sqrt((normalCard + specialCard) / 5));
        } finally {
            lock.unlock();
        }
    }

    public int getBookLevel() {
        lock.lock();
        try {
            return bookLevel;
        } finally {
            lock.unlock();
        }
    }

    public int getCardLevel(int card) {
        return cards.get(card);
    }

    public Map<Integer, Integer> getCards() {
        lock.lock();
        try {
            return Collections.unmodifiableMap(cards);
        } finally {
            lock.unlock();
        }
    }

    public int getTotalCards() {
        lock.lock();
        try {
            return specialCard + normalCard;
        } finally {
            lock.unlock();
        }
    }

    public int getNormalCard() {
        lock.lock();
        try {
            return normalCard;
        } finally {
            lock.unlock();
        }
    }

    public int getSpecialCard() {
        lock.lock();
        try {
            return specialCard;
        } finally {
            lock.unlock();
        }
    }

    public void loadCards(final int accid) throws SQLException {
        lock.lock();
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT cardid, level FROM monsterbook WHERE accid = ? ORDER BY cardid ASC")) {
                ps.setInt(1, accid);
                try (ResultSet rs = ps.executeQuery()) {
                    int cardid, level;
                    while (rs.next()) {
                        cardid = rs.getInt("cardid");
                        level = rs.getInt("level");
                        if (cardid / 1000 >= 2388) {
                            specialCard++;
                        } else {
                            normalCard++;
                        }
                        cards.put(cardid, level);
                    }
                }
            }

            con.close();
        } finally {
            lock.unlock();
        }

        calculateLevel();
    }

    private static int saveStringConcat(char[] data, int pos, Integer i) {
        return saveStringConcat(data, pos, i.toString());
    }

    private static int saveStringConcat(char[] data, int pos, String s) {
        int len = s.length();
        for (int j = 0; j < len; j++) {
            data[pos + j] = s.charAt(j);
        }

        return pos + len;
    }

    private static String getSaveString(Integer accid, Set<Entry<Integer, Integer>> cardSet) {
        semaphore.acquireUninterruptibly();
        try {

            char[] save = new char[400000]; // 500 * 10 * 10 * 8
            int i = 0;
            i = saveStringConcat(save, i, "REPLACE INTO monsterbook VALUES ");

            for (Entry<Integer, Integer> all : cardSet) {   // assuming maxsize 500 unique cards
                i = saveStringConcat(save, i, "(");
                i = saveStringConcat(save, i, accid);  //10 chars
                i = saveStringConcat(save, i, ", ");
                i = saveStringConcat(save, i, all.getKey());  //10 chars
                i = saveStringConcat(save, i, ", ");
                i = saveStringConcat(save, i, all.getValue());  //1 char due to being 0 ~ 5
                i = saveStringConcat(save, i, "),");
            }

            return new String(save, 0, i - 1);
        } finally {
            semaphore.release();
        }
    }

    public void saveCards(final int accid) {
        Set<Entry<Integer, Integer>> cardSet = getCardSet();

        if (cardSet.isEmpty()) {
            return;
        }

        StringBuilder pSB = new StringBuilder("REPLACE INTO monsterbook (`accid`, `cardid`, `level`) VALUES ");
        for (Entry<Integer, Integer> pCard : cardSet) {
            // yea query is ugly because of spaces, but it's for optimization when it gets giga big
            pSB.append('(')
                    .append(accid).append(',')
                    .append(pCard.getKey()).append(',')
                    .append(pCard.getValue()).append("),");
        }
        pSB.setLength(pSB.length() - 1);
        pSB.append(';');

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(pSB.toString());
            ps.execute();
            ps.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
