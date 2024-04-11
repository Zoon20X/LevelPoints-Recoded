package me.zoon20x.levelpoints.spigot.containers.Levels;

public class MaxData {

    private int level;
    private int prestige;
    private boolean usePrestige;

    public MaxData(int level, int prestige, boolean usePrestige){
        this.level = level;
        this.prestige = prestige;
        this.usePrestige = usePrestige;
        if(!usePrestige){
            this.prestige = 0;
        }
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

    public boolean usePrestige() {
        return usePrestige;
    }

    public void setUsePrestige(boolean usePrestige) {
        this.usePrestige = usePrestige;
    }
}
