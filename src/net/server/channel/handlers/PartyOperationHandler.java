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
import net.server.world.MapleParty;
import net.server.world.MaplePartyCharacter;
import net.server.world.PartyOperation;
import net.server.world.World;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import client.MapleCharacter;
import client.MapleClient;
import constants.ServerConstants;
import net.server.coordinator.MapleInviteCoordinator;
import net.server.coordinator.MapleInviteCoordinator.InviteResult;
import net.server.coordinator.MapleInviteCoordinator.InviteType;
import tools.Pair;

import java.util.List;
import scripting.event.EventInstanceManager;

public final class PartyOperationHandler extends AbstractMaplePacketHandler {

    @Override
    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        int operation = slea.readByte();
        MapleCharacter player = c.getPlayer();
        World world = c.getWorldServer();
        MapleParty party = player.getParty();
        //player.dropMessage(5, "TEST!!!.");
        switch (operation) {
            case 1: { // create

                if (player.getRaid() == null) {
                    if (player.getEventInstance() == null) {
                        MapleParty.createParty(player, false);
                    } else {
                        player.dropMessage(5, "You cannot create a party while inside an event.");
                    }
                } else {
                    player.dropMessage(5, "You have to leave or disband your raid in order to make a party.");
                }
                break;
            }
            case 2: { // leave/disband
                if (party != null) {
                    List<MapleCharacter> partymembers = player.getPartyMembersOnline();

                    MapleParty.leaveParty(party, c);
                    player.updatePartySearchAvailability(true);
                    player.partyOperationUpdate(party, partymembers);
                }
                break;
            }
            case 3: { // join
                int partyid = slea.readInt();
                //Pair<InviteResult, MapleCharacter> inviteRes = MapleInviteCoordinator.answerInvite(InviteType.PARTY, player.getId(), partyid, true);
                boolean isRaid = false;
                Pair<MapleInviteCoordinator.InviteResult, MapleCharacter> inviteRes;
                if (MapleInviteCoordinator.hasInvite(MapleInviteCoordinator.InviteType.PARTY, player.getId())) {
                    inviteRes = MapleInviteCoordinator.answerInvite(MapleInviteCoordinator.InviteType.PARTY, player.getId(), partyid, true);
                } else if (MapleInviteCoordinator.hasInvite(MapleInviteCoordinator.InviteType.RAID, player.getId())) {
                    inviteRes = MapleInviteCoordinator.answerInvite(MapleInviteCoordinator.InviteType.RAID, player.getId(), partyid, true);
                    isRaid = true;
                } else {
                    return;
                }

                InviteResult res = inviteRes.getLeft();
                MapleCharacter leader = inviteRes.getRight();

                //System.out.println("Raid Id: " + partyid);
                //System.out.println("Left: " + res);
                //System.out.println("Right: " + leader);
                if (leader.getEventInstance() != null) {
                    c.announce(MaplePacketCreator.serverNotice(5, "You couldn't join the party due to party leader being inside an event or raid instance."));
                    return;
                }
                if (res == InviteResult.ACCEPTED) {
                    if (isRaid) {
                        leader.getRaid().addMember(player);
                    } else {
                        MapleParty.joinParty(player, partyid, false);
                    }
                } else {
                    c.announce(MaplePacketCreator.serverNotice(5, "You couldn't join the party due to an expired invitation request."));
                }
                break;
            }
            case 4: { // invite
                String name = slea.readMapleAsciiString();
                MapleCharacter invited = world.getPlayerStorage().getCharacterByName(name);
                if (invited != null) {

                    if (invited.getRaid() != null) {
                        c.announce(MaplePacketCreator.serverNotice(5, "The player you have invited is currently in a raid."));
                        return;
                    }
                    if (invited.isBeginnerJob() || player.isBeginnerJob()) { //min requirement is level 10
                        c.announce(MaplePacketCreator.serverNotice(5, "The player you have invited does not meet the requirements."));
                        return;
                    }
                    if (player.getEventInstance() != null || invited.getEventInstance() != null) {
                        c.announce(MaplePacketCreator.serverNotice(5, "You or player your inviting is currently inside an event or raid instance."));
                        return;
                    }
                    if (player.getRaid() != null) {
                        if (invited.getRaid() == null && invited.getParty() == null) {
                            if (MapleInviteCoordinator.createInvite(InviteType.RAID, player, player.getRaidId(), invited.getId())) {
                                invited.announce(MaplePacketCreator.raidInvite(player));
                                invited.dropMessage(5, "[Raid] " + player.getName() + " has invitied you to join the their raid");
                            }

                        } else {
                            c.announce(MaplePacketCreator.serverNotice(5, "The player you have invited is currently in a raid or party."));
                        }
                        return;
                    }

                    if (invited.getParty() == null) {
                        if (party == null) {
                            if (!MapleParty.createParty(player, false)) {
                                return;
                            }
                            party = player.getParty();
                        }
                        if (party.getMembers().size() < ServerConstants.PartySize) {
                            if (MapleInviteCoordinator.createInvite(InviteType.PARTY, player, party.getId(), invited.getId())) {
                                invited.announce(MaplePacketCreator.partyInvite(player));
                            } else {
                                c.announce(MaplePacketCreator.partyStatusMessage(22, invited.getName()));
                            }
                        } else {
                            c.announce(MaplePacketCreator.partyStatusMessage(17));
                        }

                    } else {
                        c.announce(MaplePacketCreator.partyStatusMessage(16));
                    }
                } else {
                    c.announce(MaplePacketCreator.partyStatusMessage(19));
                }
                break;
            }
            case 5: { // expel
                int cid = slea.readInt();
                MapleParty.expelFromParty(party, c, cid);
                break;
            }
            case 6: { // change leader
                int newLeader = slea.readInt();
                MaplePartyCharacter newLeadr = party.getMemberById(newLeader);
                if (player.getEventInstance() != null) {
                    c.announce(MaplePacketCreator.serverNotice(5, "You cannot change party leader while inside a raid/event."));
                    return;
                }
                if (newLeadr.getPlayer().getEventInstance() != null) {
                    c.announce(MaplePacketCreator.serverNotice(5, "You cannot change party leader with anyone inside a raid/event."));
                    return;
                }
                world.updateParty(party.getId(), PartyOperation.CHANGE_LEADER, newLeadr);
                break;
            }
        }
    }
}
