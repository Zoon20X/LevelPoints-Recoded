package me.zoon20x.levelpoints.containers.Blocks;

import org.bukkit.Material;

public class BlockData {
    private Material material;
    private double exp;
    private int placeRequired;
    private int breakRequired;


    public BlockData(Material material){
        this.material = material;
    }
    public BlockData(Material material, double exp, int breakRequired, int placeRequired){
        this.material = material;
        this.exp = exp;
        this.placeRequired = placeRequired;
        this.breakRequired = breakRequired;
    }
    public BlockData setEXP(double exp){
        this.exp = exp;
        return this;
    }
    public BlockData setPlaceLevelRequired(int placeRequired){
        this.placeRequired = placeRequired;
        return this;
    }
    public BlockData setBreakLevelRequired(int breakRequired){
        this.breakRequired = breakRequired;
        return this;
    }

    public double getExp() {
        return exp;
    }

    public int getPlaceLevelRequired() {
        return placeRequired;
    }

    public int getBreakLevelRequired() {
        return breakRequired;
    }

    public Material getMaterial() {
        return material;
    }
}
