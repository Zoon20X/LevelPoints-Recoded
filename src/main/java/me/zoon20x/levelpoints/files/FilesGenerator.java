package me.zoon20x.levelpoints.files;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FilesGenerator {

    private File getSettingsFile(String id){
        return new File(LevelPoints.getInstance().getDataFolder(),  "/Settings/" + id);
    }
    private File getFile(String id){
        return new File(LevelPoints.getInstance().getDataFolder(), id);
    }
    private FileConfiguration getConfig(File file){
        return YamlConfiguration.loadConfiguration(file);
    }
    public void generateFiles(){
        FilesStorage.createFile(getFile("lang.yml"), "lang.yml", "lang.yml", "lang");
        FilesStorage.createFile(getSettingsFile("ExpSettings.yml"), "/Settings/ExpSettings.yml", "Settings/ExpSettings.yml", "ExpSettings");
        FilesStorage.createFile(getSettingsFile("LevelSettings.yml"), "/Settings/LevelSettings.yml", "Settings/LevelSettings.yml", "LevelSettings");
        FilesStorage.createFile(getSettingsFile("RewardSettings.yml"), "/Settings/RewardSettings.yml", "Settings/RewardSettings.yml", "RewardSettings");
        FilesStorage.createFile(getFile("TopList.yml"), "TopList.yml", "TopList.yml", "TopList");
        FilesStorage.createFile(getSettingsFile("AntiAbuse.yml"), "/Settings/AntiAbuse.yml", "Settings/AntiAbuse.yml", "AntiAbuse");
        saveGeneratedFiles();
    }

    public void saveGeneratedFiles(){
        FilesStorage.addFileToCache("langConfig", getConfig(getFile("lang.yml")));
        FilesStorage.addFileToCache("expConfig", getConfig(getSettingsFile("ExpSettings.yml")));
        FilesStorage.addFileToCache("levelsConfig", getConfig(getSettingsFile("LevelSettings.yml")));
        FilesStorage.addFileToCache("levelsConfig", getConfig(getSettingsFile("LevelSettings.yml")));
        FilesStorage.addFileToCache("rewardsConfig", getConfig(getSettingsFile("RewardSettings.yml")));
        FilesStorage.addFileToCache("antiAbuseConfig", getConfig(getSettingsFile("AntiAbuse.yml")));
    }

}
