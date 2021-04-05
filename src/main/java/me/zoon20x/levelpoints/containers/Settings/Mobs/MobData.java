package me.zoon20x.levelpoints.containers.Settings.Mobs;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Settings.Blocks.BlockRequired;
import org.bukkit.entity.EntityType;

public class MobData {

    private EntityType mob;

    private Integer damageRequired;
    private Double exp;
    private Double maxExp;

    public MobData(EntityType mob, Double exp, Double maxExp, Integer damageRequired){
        this.mob = mob;
        this.exp = exp;
        this.maxExp = maxExp;
        this.damageRequired = damageRequired;

    }



    public Double getRequiredEXP(BlockRequired required, PlayerData data) {
        Double exp = 0.0;
        for (int i = data.getLevel(); i < getDamageRequired() + 1; i++) {
            double ex = LevelPoints.getLevelSettings().getRequireExp(i);
            exp += ex;
        }

        return exp - data.getExp();
    }

    public EntityType getMob() {
        return mob;
    }
    public Double getExp() {
        return exp;
    }

    public void setExp(Double exp) {
        this.exp = exp;
    }

    public Integer getDamageRequired() {
        return damageRequired;
    }

    public void setDamageRequired(Integer damageRequired) {
        this.damageRequired = damageRequired;
    }

    public Double getMaxExp() {
        return maxExp;
    }
}
