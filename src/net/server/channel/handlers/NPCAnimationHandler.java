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

import client.MapleClient;
import net.AbstractMaplePacketHandler;
import net.opcodes.SendOpcode;
import tools.data.input.SeekableLittleEndianAccessor;
import tools.data.output.MaplePacketLittleEndianWriter;

public final class NPCAnimationHandler extends AbstractMaplePacketHandler {

    @Override
    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {

        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        /*
        try {
            if (c.tryacquireClient()) {
                int length = (int) slea.available();
                if (length == 6) { // NPC Talk
                    mplew.writeShort(SendOpcode.NPC_ACTION.getValue());
                    mplew.writeInt(slea.readInt());
                    mplew.write(slea.readByte());
                    mplew.write(slea.readByte());
                } else if (length > 6) { // NPC Move
                    mplew.writeShort(SendOpcode.NPC_ACTION.getValue());
                    mplew.write(slea.read(length - 9));
                } else {
                    System.out.println("NPC-Animation Packet - " + c.getPlayer().getName() + "\r\nPACKET: " + slea.toString());
                }
                c.getSession().write(mplew.getPacket());
            }
        } finally {
            c.releaseClient();
        }
                */
    }
}
