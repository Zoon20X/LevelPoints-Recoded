package me.zoon20x.levelpoints.devTools.stats;

public class BlockStat {

    private String material;
    private double exp;
    private int levelRequired;

    public BlockStat(String material, double exp, int levelRequired) {
        this.material = material;
        this.exp = exp;
        this.levelRequired = levelRequired;
    }


    public String getMaterial() {
        return material;
    }

    public double getExp() {
        return exp;
    }

    public int getLevelRequired() {
        return levelRequired;
    }
}
