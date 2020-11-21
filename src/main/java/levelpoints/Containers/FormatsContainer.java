package levelpoints.Containers;

public class FormatsContainer {

    private String format;
    private int minLevel, maxLevel, prestige;
    public FormatsContainer(String format, int minLevel, int maxLevel, int prestige){
        this.format = format;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.prestige = prestige;
    }

    public int getMinLevel(){
        return this.minLevel;
    }
    public int getMaxLevel(){
        return this.maxLevel;
    }
    public int getPrestige(){
        return this.prestige;
    }
    public String getFormat(){
        return this.format;
    }


}
