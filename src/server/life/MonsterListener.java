package server.life;
import client.MapleCharacter;

public interface MonsterListener {
    
    public void monsterKilled(int aniTime);
    public void monsterDamaged(MapleCharacter from, long trueDmg);
    public void monsterHealed(long trueHeal);
}
