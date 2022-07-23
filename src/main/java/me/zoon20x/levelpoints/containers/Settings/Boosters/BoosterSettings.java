package me.zoon20x.levelpoints.containers.Settings.Boosters;

import me.zoon20x.levelpoints.LevelPoints;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

public class BoosterSettings {

    private FileConfiguration dataConfig;
    private HashMap<String, BoosterData> data = new HashMap<>();

    public BoosterSettings(){
        File boosterData = new File(LevelPoints.getInstance().getBoostersFolder(), "BoosterData.yml");
        dataConfig = YamlConfiguration.loadConfiguration(boosterData);
        generateBooster();
    }


    public Collection<BoosterData> getAllBoosters(){
        return data.values();
    }

    public void generateBooster(){
        dataConfig.getConfigurationSection("").getKeys(false).forEach(x->{
            String id = x;
            double multiplier = dataConfig.getDouble(x + ".Multiplier");
            String time = dataConfig.getString(x + ".Time");
            BoosterData boosterData = new BoosterData(id, multiplier, time);
            addBooster(boosterData);
        });
    }

    public void addBooster(BoosterData boosterData){
        data.put(boosterData.getId(), boosterData);
    }
    public BoosterData getBooster(String id){
        return data.get(id);
    }
    public void removeBooster(String id){
        data.remove(id);
        File boosterData = new File(LevelPoints.getInstance().getBoostersFolder(), "BoosterData.yml");
        dataConfig.set(id, null);
        try {
            dataConfig.save(boosterData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasBooster(String id){
        return data.containsKey(id);
    }

    public void saveBoosters(){
        File boosterData = new File(LevelPoints.getInstance().getBoostersFolder(), "BoosterData.yml");
        data.keySet().forEach(x->{
            dataConfig.set(x + ".Multiplier", data.get(x).getMultiplier());
            dataConfig.set(x + ".Time", data.get(x).getTime());
        });
        try {
            dataConfig.save(boosterData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
