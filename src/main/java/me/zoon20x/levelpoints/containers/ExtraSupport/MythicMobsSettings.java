package me.zoon20x.levelpoints.containers.ExtraSupport;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.utils.DebugSeverity;

import java.util.HashMap;

public class MythicMobsSettings {

    private HashMap<String, MythicMobsData> data = new HashMap<>();


    public MythicMobsSettings(){
        LevelPoints.getInstance().getFilesGenerator().mythicMobsConfig.getConfig().getConfigurationSection("").getKeys(false).forEach(x->{
            MythicMobsData value = new MythicMobsData(x);
            data.put(value.getName(), value);
        });
    }
    public MythicMobsData getMobData(String mob){
        return data.get(mob);
    }
    public boolean hasMobData(String mob){
        return data.containsKey(mob);
    }








}
