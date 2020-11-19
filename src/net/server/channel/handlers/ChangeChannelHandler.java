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
package net.server.channel.handlers;

import net.AbstractMaplePacketHandler;
import tools.data.input.SeekableLittleEndianAccessor;
import client.MapleClient;
import client.autoban.AutobanFactory;
import net.server.Server;

/**
 *
 * @author Matze
 */
public final class ChangeChannelHandler extends AbstractMaplePacketHandler {

    @Override
    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int channel = slea.readByte() + 1;
        int stam = channel * 10;
        if (c.getPlayer().getEventInstance() == null) {
            if (c.getPlayer().getTotalLevel() >= stam) {

                long cooldown = 1000 * 10;
                long curr = System.currentTimeMillis();
                if (curr - c.getPlayer().getsavecooldown() >= cooldown) {
                    slea.readInt();
                    c.getPlayer().getAutobanManager().setTimestamp(6, Server.getInstance().getCurrentTimestamp(), 3);
                    if (c.getChannel() == channel) {
                        AutobanFactory.GENERAL.alert(c.getPlayer(), "CCing to same channel.");
                        c.disconnect(false, false);
                        return;
                    } else if (c.getPlayer().getCashShop().isOpened() || c.getPlayer().getMiniGame() != null || c.getPlayer().getPlayerShop() != null) {
                        return;
                    }
                    c.changeChannel(channel);
                    //c.getPlayer().removeStamina(channel);
                    c.getPlayer().setsavecooldown(System.currentTimeMillis());

                } else {
                    c.getPlayer().dropMessage(6, "Time until next available channel change " + ((long) (cooldown - (curr - c.getPlayer().getsavecooldown())) / 1000) + " seconds.");
                }
            } else {
                c.getPlayer().dropMessage(6, "You must be at least level " + stam + " to access this channel.");
            }
        } else {
            c.getPlayer().dropMessage(6, "Changing channels within an event is not allowed.");
        }
    }
}
