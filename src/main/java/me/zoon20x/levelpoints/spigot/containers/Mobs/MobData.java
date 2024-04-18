package me.zoon20x.levelpoints.spigot.containers.Mobs;

import org.bukkit.entity.EntityType;

public class MobData {

    private String entityType;
    private double exp;
    private int attackRequired;


    public MobData(String entityType){
        this.entityType = entityType;
    }
    public MobData(String entityType, double exp, int attackRequired){
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


    public String getEntityType() {
        return entityType;
    }

    public int getAttackRequired() {
        return attackRequired;
    }
}
