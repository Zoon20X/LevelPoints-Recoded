package me.zoon20x.levelpoints.files;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.utils.DataLocation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FilesGenerator {
    public Files lang;
    public Files expSettings;
    public Files levelSettings;
    public Files rewardSettings;
    public Files topList;
    public Files boosterData;
    public Files boosterSettings;
    public Files antiAbuse;
    public Files pvpSettings;

    public void generateFiles(){
        this.lang = new Files("lang.yml").setLocation("").build();
        this.expSettings = new Files("ExpSettings.yml").setLocation("Settings/").build();
        this.levelSettings = new Files("LevelSettings.yml").setLocation("Settings/").build();
        this.rewardSettings = new Files("RewardSettings.yml").setLocation("Settings/").build();
        this.topList = new Files("TopList.yml").setLocation("").build();
        this.boosterData = new Files("BoosterData.yml").setLocation("Boosters/").build();
        this.boosterSettings = new Files("BoosterSettings.yml").setLocation("Boosters/").build();
        this.antiAbuse = new Files("AntiAbuse.yml").setLocation("Settings/").build();
        this.pvpSettings = new Files("PvpSettings.yml").setLocation("Settings/").build();


    }

}
