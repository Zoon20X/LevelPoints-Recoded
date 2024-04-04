package me.zoon20x.devTools.spigot.stats;

public class BlockStat {

    private String material;
    private double exp;
    private int breakRequired;
    private int placeRequired;

    public BlockStat(String material, double exp, int breakRequired, int placeRequired) {
        this.material = material;
        this.exp = exp;

        this.breakRequired = breakRequired;
        this.placeRequired = placeRequired;
    }


    public String getMaterial() {
        return material;
    }

    public double getExp() {
        return exp;
    }

    public int getBreakRequired() {
        return breakRequired;
    }

    public int getPlaceRequired() {
        return placeRequired;
    }
}
