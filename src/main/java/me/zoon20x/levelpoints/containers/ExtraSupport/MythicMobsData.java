package me.zoon20x.levelpoints.containers.ExtraSupport;

import me.zoon20x.levelpoints.LevelPoints;
public class MythicMobsData {

    private String name;
    private int levelMin;
    private int levelMax;
    private double exp;

    public MythicMobsData(String mob){
        name = mob;
        levelMin = LevelPoints.getInstance().getFilesGenerator().mythicMobsConfig.getConfig().getInt(mob + ".Level.Min");
        levelMax = LevelPoints.getInstance().getFilesGenerator().mythicMobsConfig.getConfig().getInt(mob + ".Level.Max");
        exp = LevelPoints.getInstance().getFilesGenerator().mythicMobsConfig.getConfig().getDouble(mob + ".EXP");
    }



    public String getName() {
        return name;
    }

    public int getLevelMin() {
        return levelMin;
    }

    public int getLevelMax() {
        return levelMax;
    }

    public double getExp() {
        return exp;
    }
}
