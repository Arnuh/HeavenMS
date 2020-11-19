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
package server.life;

import client.MapleCharacter;
import java.awt.Point;
import java.util.concurrent.atomic.AtomicInteger;
import net.server.Server;

public class SpawnPoint {

    private int monster, mobTime, team, fh, f, rank, ch;
    private Point pos;
    private long nextPossibleSpawn;
    private int mobInterval = 5000;
    private AtomicInteger spawnedMonsters = new AtomicInteger(0);
    private boolean immobile, denySpawn = false, boss = false;

    public SpawnPoint(final MapleMonster monster, Point pos, boolean immobile, int mobTime, int mobInterval, int team, int ch) {
        mobTime = (monster.isBoss()) ? 0 : mobTime;
        this.monster = monster.getId();
        this.pos = new Point(pos);
        this.mobTime = mobTime;
        this.team = team;
        this.fh = monster.getFh();
        this.f = monster.getF();
        this.immobile = immobile;
        this.mobInterval = mobInterval;
        this.nextPossibleSpawn = Server.getInstance().getCurrentTime();
        this.boss = monster.isBoss();
        this.rank = monster.getStats().getScale();
        this.ch = ch;
    }

    public int getSpawned() {
        return spawnedMonsters.intValue();
    }

    public void setDenySpawn(boolean val) {
        denySpawn = val;
    }

    public boolean getDenySpawn() {
        return denySpawn;
    }

    public boolean isbanned() {
        switch (this.monster) {
            case 6090000:
            case 6090001:
            case 6090002:
            case 6090003:
            case 6090004:
            case 5090001:
                return true;
        }
        return false;
    }

    public boolean shouldSpawn() {
        if (isbanned()) {
            return false;
        }
        if (rank > 3 && spawnedMonsters.get() > 0) {
            return false;
        }
        if (immobile && (spawnedMonsters.get() > 0)) {
            return false;
        }
        if ((denySpawn || mobTime < 0)) {
            return false;
        }
        return true;
    }

    public boolean shouldForceSpawn() {
        return !(mobTime < 0 || spawnedMonsters.get() > 0);
    }

    public boolean shouldForceSpawn(MapleMonster mob) {
        return !(mob.isMobile() || spawnedMonsters.get() > 0);
    }

    public MapleMonster getMonster(int channel) {
        //MapleMonster mob = new MapleMonster(MapleLifeFactory.getMonster(monster, channel), -1, channel, -1, true, true, null);
        MapleMonster mob = MapleLifeFactory.getMonster(monster, channel);
        mob.setPosition(new Point(pos));
        mob.setTeam(team);
        mob.setFh(fh);
        mob.setF(f);
        spawnedMonsters.incrementAndGet();
        mob.addListener(new MonsterListener() {

            @Override
            public void monsterKilled(int aniTime) {
                nextPossibleSpawn = Server.getInstance().getCurrentTime() + aniTime;
                if (mobTime > 0) {
                    nextPossibleSpawn += mobTime;
                }
                spawnedMonsters.decrementAndGet();
            }

            @Override
            public void monsterDamaged(MapleCharacter from, long trueDmg) {
            }

            @Override
            public void monsterHealed(long trueHeal) {
            }
        });
        return mob;
    }

    public int getMonsterId() {
        return monster;
    }

    public Point getPosition() {
        return pos;
    }

    public final int getF() {
        return f;
    }

    public final int getFh() {
        return fh;
    }

    public int getMobTime() {
        return mobTime;
    }

    public int getTeam() {
        return team;
    }
}
