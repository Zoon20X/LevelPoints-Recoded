package me.zoon20x.levelpoints.containers.Mobs;

import me.zoon20x.levelpoints.containers.Blocks.BlockData;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class MobData {

    private EntityType entityType;
    private double exp;
    private int attackRequired;


    public MobData(EntityType entityType){
        this.entityType = entityType;
    }
    public MobData(EntityType entityType, double exp, int attackRequired){
        this.entityType = entityType;
        this.exp = exp;
        this.attackRequired = attackRequired;
    }
    public MobData setEXP(double exp){
        this.exp = exp;
        return this;
    }
    public MobData setAttackRequired(int attackRequired){
        this.attackRequired = attackRequired;
        return this;
    }

    public double getExp() {
        return exp;
    }


    public EntityType getEntityType() {
        return entityType;
    }

    public int getAttackRequired() {
        return attackRequired;
    }
}
