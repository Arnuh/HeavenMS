package client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import client.MapleCharacter.SkillEntry;
import constants.GameConstants;
import tools.DatabaseConnection;
import tools.MaplePacketCreator;

public class PlayerSkillStorage {

    private final MapleCharacter chr;
    private final Map<Skill, SkillEntry> skills = new HashMap<>();
    private final Set<Skill> updatedSkills = new HashSet<>();

    public PlayerSkillStorage(MapleCharacter chr) {
        this.chr = chr;
    }

    public Map<Skill, SkillEntry> getSkills() {
        return Collections.unmodifiableMap(skills);
    }

    public void addSkill(Skill skill, SkillEntry skillEntry) { //only used for link skills, don't need to update registry
        if (!GameConstants.isInJobTree(skill.getId(), chr.getJob().getId())) {
            skills.put(skill, skillEntry);
        } else {
            if (chr.isGM()) {
                SkillEntry oldSkillEntry = skills.get(skill);
                if (skillEntry.skillevel > oldSkillEntry.skillevel) {
                    skills.put(skill, skillEntry); //don't overwrite if skill already exists (for GMs), except for overwriting link skills
                }
            }
        }
    }

    public void changeSkillLevel(Skill skill, int newLevel, int newMasterlevel, long expiration, boolean save) {
        if (newLevel > -1) {
            if (save) {
                updatedSkills.add(skill);
            }
            skills.put(skill, new SkillEntry(newLevel, newMasterlevel, expiration));
            if (!GameConstants.isHiddenSkills(skill.getId())) {
                chr.announce(MaplePacketCreator.updateSkill(skill.getId(), newLevel, newMasterlevel, expiration));
            }
        } else {
            skills.remove(skill);
            chr.announce(MaplePacketCreator.updateSkill(skill.getId(), newLevel, newMasterlevel, -1)); // Shouldn't use expiration anymore :)
            try (Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("DELETE FROM skills WHERE characterid = ? AND skillid = ?")) {
                ps.setInt(1, chr.getId());
                ps.setInt(2, skill.getId());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void load(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT skillid,skilllevel,masterlevel,expiration FROM skills WHERE characterid = ?")) {
            ps.setInt(1, chr.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Skill skill = SkillFactory.getSkill(rs.getInt("skillid"));
                    if (skill == null) {
                        continue;
                    }
                    SkillEntry skillEntry = new SkillEntry(rs.getInt("skilllevel"), rs.getInt("masterlevel"), rs.getLong("expiration"));
                    skills.put(skill, skillEntry);
                }
            }
        }
    }

    public void save(Connection con) throws SQLException {
        if (updatedSkills.isEmpty()) {
            return;
        }
        try (PreparedStatement ps = con.prepareStatement("INSERT INTO skills (skillid, characterid, skilllevel, masterlevel, expiration) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE skilllevel = VALUES(skilllevel), masterlevel = VALUES(masterlevel), expiration = VALUES(expiration)")) {
            Iterator<Skill> iter = updatedSkills.iterator();
            while (iter.hasNext()) {
                Skill cur = iter.next();
                SkillEntry entry = skills.get(cur);
                if (entry != null) {
                    ps.setInt(1, cur.getId());
                    ps.setInt(2, chr.getId());
                    ps.setInt(3, entry.skillevel);
                    ps.setInt(4, entry.masterlevel);
                    ps.setLong(5, entry.expiration);
                    ps.addBatch();
                } //don't need to handle deleted ones
            }
            ps.executeBatch();
            updatedSkills.clear();
        }
    }

    public int getSkillLevel(int skill) {
        SkillEntry ret = skills.get(SkillFactory.getSkill(skill));
        if (ret == null) {
            return 0;
        }
        return ret.skillevel;
    }

    public int getSkillLevel(Skill skill) {
        SkillEntry ret = skills.get(skill);
        if (ret == null) {
            return 0;
        }
        return ret.skillevel;
    }

    public long getSkillExpiration(int skill) {
        SkillEntry ret = skills.get(SkillFactory.getSkill(skill));
        if (ret == null) {
            return -1;
        }
        return ret.expiration;
    }

    public long getSkillExpiration(Skill skill) {
        SkillEntry ret = skills.get(skill);
        if (ret == null) {
            return -1;
        }
        return ret.expiration;
    }

    public int getMasterLevel(Skill skill) {
        SkillEntry ret = skills.get(skill);
        if (ret == null) {
            return skill.getMaxLevel();
        }
        return ret.masterlevel;
    }
}
