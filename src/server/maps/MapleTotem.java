/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server.maps;

import server.life.MapleMonster;

/**
 *
 * @author David
 */
public class MapleTotem {
    
    private final int ownerId;
    private MapleMonster monster;
    
    public MapleTotem(int ownerId) {
        this.ownerId = ownerId;
    }
    
    public int getOwnerId() {
        return ownerId;
    }

    public MapleMonster getMonster() {
        return monster;
    }
    
    public void setMonster(MapleMonster monster) {
        this.monster = monster;
    }
}
