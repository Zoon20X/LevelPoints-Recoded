package me.zoon20x.levelpoints.containers.ExtraSupport;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.utils.DebugSeverity;

import java.util.HashMap;

public class MythicMobsSettings {

    private HashMap<String, MythicMobsData> data = new HashMap<>();


    public MythicMobsSettings(){
        LevelPoints.getDebug(DebugSeverity.NORMAL, "Loading MythicMobs data");
        LevelPoints.getFilesGenerator().mythicMobsConfig.getConfig().getConfigurationSection("").getKeys(false).forEach(x->{
            MythicMobsData value = new MythicMobsData(x);
            data.put(value.getName(), value);
        });
        LevelPoints.getDebug(DebugSeverity.NORMAL, "Loaded " + data.size() + " mobs");
    }
    public MythicMobsData getMobData(String mob){
        return data.get(mob);
    }
    public boolean hasMobData(String mob){
        return data.containsKey(mob);
    }








}
