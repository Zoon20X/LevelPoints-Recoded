package me.zoon20x.levelpoints.spigot.containers.Farming;

import org.bukkit.Material;

public class FarmData {
    private Material material;
    private double exp;
    private int farmRequired;


    public FarmData(Material material){
        this.material = material;
    }
    public FarmData(Material material, double exp, int farmRequire){
        this.material = material;
        this.exp = exp;
        this.farmRequired = farmRequired;
    }
    public FarmData setEXP(double exp){
        this.exp = exp;
        return this;
    }
    public FarmData setFarmLevelRequired(int farmRequired){
        this.farmRequired = farmRequired;
        return this;
    }

    public double getExp() {
        return exp;
    }

    public int getFarmRequired() {
        return farmRequired;
    }

    public Material getMaterial() {
        return material;
    }
}
