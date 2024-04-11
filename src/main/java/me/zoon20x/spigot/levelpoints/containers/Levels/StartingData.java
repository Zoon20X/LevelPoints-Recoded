package me.zoon20x.spigot.levelpoints.containers.Levels;

public class StartingData {

    private int level;
    private int prestige;
    private double exp;

    public StartingData(int level, int prestige, double exp){
        this.level = level;
        this.prestige = prestige;
        this.exp = exp;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPrestige() {
        return prestige;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }
}
