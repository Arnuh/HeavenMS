package net.server.world;

import client.MapleCharacter;
import client.MapleClient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import net.server.coordinator.MapleInviteCoordinator;
import server.maps.MapleDoor;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.Randomizer;

public class MapleRaid {

    private int id;
    private boolean invite;
    private MapleCharacter leader;
    private final List<MapleCharacter> members = new LinkedList<>();
    private List<MapleCharacter> raidmembers = null;
    private static AtomicInteger runningraidId = new AtomicInteger();
    private Map<Integer, Integer> histMembers = new HashMap<>();
    private int nextEntry = 0;
    private Map<Integer, MapleDoor> doors = new HashMap<>();
    public boolean door = false;

    public MapleRaid(MapleCharacter leader, int id) {
        this.leader = leader;
        this.id = id;
    }

    public boolean containsMembers(MapleCharacter member) {
        return members.contains(member);
    }

    public static void createRaid(MapleCharacter leader) {
        MapleRaid raid = leader.getRaid();
        if (raid == null || leader.getParty() == null) {
            if (!leader.getRaidStatus()) {
                int raidid = runningraidId.getAndIncrement();
                //raid = leader.getWorldServer().createRaid(leader);
                leader.setRaid(new MapleRaid(leader, raidid));
                leader.getRaid().members.add(leader);
                leader.getRaid().histMembers.put(leader.getId(), leader.getRaid().nextEntry);
                leader.getRaid().nextEntry++;
                leader.setRaidLeader(true);
                leader.getRaid().raidMessage("[Raid] " + leader.getName() + " has created a raid");
                //leader.silentRaidUpdate();
                //leader.raidOperationUpdate(raid, null);
                //MapleRaid raid = leader.getRaid();
                //leader.announce(MaplePacketCreator.raidCreated(raid, leader.getId()));
            }
        }

    }

    public void invite(MapleCharacter leader, MapleCharacter member) {
        if (member != null) {
            if (leader.isRaidLeader()) {
                if (member.isLoggedinWorld() && member.getRaid() == null) {
                    if (MapleInviteCoordinator.createInvite(MapleInviteCoordinator.InviteType.RAID, leader, member.getId(), getId())) {
                        member.announce(MaplePacketCreator.raidInvite(leader));
                        member.dropMessage(5, "[Raid] " + leader.getName() + " has invitied you to join the their raid");
                    }

                    //MapleInviteCoordinator.createInvite(MapleInviteCoordinator.InviteType.RAID, leader, member.getId(), member.getId());
                    //member.dropMessage(5, "[Raid] " + leader.getName() + " has invitied you to join the their raid");
                }
            }
        }
    }

    public static boolean invited(MapleCharacter player) {
        return MapleInviteCoordinator.hasInvite(MapleInviteCoordinator.InviteType.RAID, player.getId());
    }

    public static void handleinvite(MapleCharacter player, boolean answer) {
        if (MapleInviteCoordinator.hasInvite(MapleInviteCoordinator.InviteType.RAID, player.getId())) {
            if (answer) {
                Pair<MapleInviteCoordinator.InviteResult, MapleCharacter> inviteResult = MapleInviteCoordinator.answerInvite(MapleInviteCoordinator.InviteType.RAID, player.getId(), player.getId(), true);
                if (inviteResult.getLeft() == MapleInviteCoordinator.InviteResult.ACCEPTED) {
                    MapleCharacter from = inviteResult.getRight();
                    if (from.getRaid() != null) {
                        from.getRaid().addMember(player);
                    }
                }
            } else {
                Pair<MapleInviteCoordinator.InviteResult, MapleCharacter> inviteResult = MapleInviteCoordinator.answerInvite(MapleInviteCoordinator.InviteType.RAID, player.getId(), player.getId(), false);
                if (inviteResult.getLeft() == MapleInviteCoordinator.InviteResult.DENIED) {
                    MapleCharacter from = inviteResult.getRight();
                    if (from.getRaid() != null) {
                        from.dropMessage(5, player.getName() + " has Denied your Invite");
                    }
                }
            }
        }
    }

    public void addMember(MapleCharacter member) {
        if (!containsMembers(member) || member.getRaid() == null) {
            member.setRaid(this);
            histMembers.put(member.getId(), nextEntry);
            nextEntry++;
            members.add(member);
            raidMessage("[Raid] " + member.getName() + " has joined the raid");
            member.cancelMagicDoor();
            member.getMap().updateDoor(member);
            //member.receiveRaidMemberHP();
            //member.updateRaidMemberHP();
        }
    }

    public void removeMember(MapleCharacter leader, MapleCharacter member) {
        if ((containsMembers(member) && member != this.leader) && leader == this.leader) {
            if (leader != member) {
                member.dropMessage(5, "You have been expelled from the raid.");
                member.setRaid(null);
                histMembers.remove(member.getId());
                members.remove(member);
                raidMessage("[Raid] " + member.getName() + " has been expelled from the raid");
                member.cancelMagicDoor();
                member.getMap().updateDoor(member);
            }
        }
    }

    public void leaveRaid(MapleCharacter member) {
        member.setRaid(null);
        histMembers.remove(member.getId());
        members.remove(member);
        member.dropMessage(5, "[Raid] you have left the raid");
        raidMessage("[Raid] " + member.getName() + " has left the raid");
        member.cancelMagicDoor();
        member.getMap().updateDoor(member);
    }

    public void setLeader(MapleCharacter member) {
        if (containsMembers(member) && member != this.leader) {
            this.leader = member;
            raidMessage("[Raid] " + member.getName() + " is now the leader of the raid");
        }
    }

    public void changeLeader(MapleCharacter leader, MapleCharacter member) {
        if ((containsMembers(member) && member != this.leader) && leader == this.leader) {
            if (leader != member) {
                this.leader = member;
                raidMessage("[Raid] " + member.getName() + " is now the leader of the raid");
            }
        }
    }

    public MapleCharacter getLeader() {
        return leader;
    }

    public boolean getLeader(MapleCharacter chr) {
        return chr == leader;
    }

    public List<MapleCharacter> getMembers() {
        return members;
    }

    public void disbandRaid() {
        for (MapleCharacter chr : members) {
            chr.setRaid(null);
            chr.dropMessage(5, "[Raid] Raid has been disbanded");
            chr.cancelMagicDoor();
            chr.getMap().updateDoor(chr);
        }
        histMembers.clear();
        members.clear();
    }

    public void disbandRaidByLeader(MapleCharacter member) {
        if (member == this.leader) {
            for (MapleCharacter chr : members) {
                chr.setRaid(null);
                chr.dropMessage(5, "[Raid] Raid has been disbanded");
                chr.cancelMagicDoor();
                chr.getMap().updateDoor(chr);
            }
            histMembers.clear();
            members.clear();
        }
    }

    public void raidMessage(String msg) {
        for (MapleCharacter chr : members) {
            chr.dropMessage(5, msg);
        }
    }

    public Collection<MapleCharacter> getEligibleRaidMembers() {
        return Collections.unmodifiableList(raidmembers);
    }

    public void setEligibleMembers(List<MapleCharacter> eliParty) {
        raidmembers = eliParty;
    }

    public MapleCharacter getRandomPlayer() {
        List<MapleCharacter> players = new ArrayList<>(members);
        Collections.shuffle(players);
        MapleCharacter mc = players.get(Randomizer.nextInt());
        return mc;
    }

    public MapleCharacter getRandomPlayer(MapleCharacter chr) {
        List<MapleCharacter> players = new ArrayList<>(members);
        raidMessage("[Raid] " + chr.getName() + " has logged off or disconnected.");
        players.remove(chr);
        Collections.shuffle(players);
        MapleCharacter mc = players.get(Randomizer.nextInt());
        raidMessage("[Raid] " + mc.getName() + " is now the leader of the raid");
        return mc;
    }

    public void assignNewLeader() {
        MapleCharacter newLeadr = null;
        for (MapleCharacter mpc : members) {
            if (mpc != leader) {
                newLeadr = mpc;
                this.setLeader(mpc);
                return;
            }
        }
        if (newLeadr == null) {
            histMembers.clear();
            members.clear();
        }
    }

    public List<Integer> getMembersSortedByHistory() {
        List<Map.Entry<Integer, Integer>> histList;

        histList = new LinkedList<>(histMembers.entrySet());

        Collections.sort(histList, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        List<Integer> histSort = new LinkedList<>();
        for (Map.Entry<Integer, Integer> e : histList) {
            histSort.add(e.getKey());
        }

        return histSort;
    }

    public byte getRaidDoor(int cid) {
        List<Integer> histList = getMembersSortedByHistory();
        byte slot = 0;
        for (Integer e : histList) {
            if (e == cid) {
                break;
            }
            slot++;
        }

        return slot;
    }

    public void addDoor(Integer owner, MapleDoor door) {
        this.doors.put(owner, door);
    }

    public void removeDoor(Integer owner) {
        this.doors.remove(owner);
    }

    public Map<Integer, MapleDoor> getDoors() {
        return Collections.unmodifiableMap(doors);
    }

    public int getId() {
        return id;
    }

    public MapleCharacter getMemberById(int id) {
        for (MapleCharacter chr : members) {
            if (chr.getId() == id) {
                return chr;
            }
        }
        return null;
    }

    public static void updateDoors(MapleCharacter player) {
        List<MapleCharacter> raidmembers = player.getRaidMembersOnline();
        player.raidOperationUpdate(player.getRaid(), raidmembers);
    }

    public void updateMember(MapleCharacter member) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() == member.getId()) {
                members.set(i, member);
            }
        }
    }
}
