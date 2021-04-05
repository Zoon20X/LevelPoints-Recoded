package me.zoon20x.levelpoints.containers.Settings.Configs;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.files.FilesStorage;
import me.zoon20x.levelpoints.utils.DataLocation;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;

public class AntiAbuseSettings {


    private boolean regionLocked;

    private HashMap<String, RegionData> regions = new HashMap<>();

    public AntiAbuseSettings(){
        FileConfiguration configuration = FilesStorage.getConfig("antiAbuseConfig");
        regionLocked = configuration.getBoolean(DataLocation.abuseRegionLockedEnabled);
        generateRegions(configuration);
    }

    private void generateRegions(FileConfiguration config){
        for(String x : config.getConfigurationSection(DataLocation.abuseRegionLockedRegions).getKeys(false)){
            String name = x;
            int minLevel = config.getInt(DataLocation.getAbuseRegionLevel("Min", x));
            int maxLevel = config.getInt(DataLocation.getAbuseRegionLevel("Max", x));

            boolean teleportEnabled = config.getBoolean(DataLocation.getAbuseRegionTeleportEnabled(x));
            String teleportLocation = config.getString(DataLocation.getAbuseRegionTeleportCords(x));

            boolean messageEnabled = config.getBoolean(DataLocation.getAbuseRegionMessageEnabled(x));
            List<String> messageText = config.getStringList(DataLocation.getAbuseRegionMessageText(x));
            RegionData data = new RegionData(name, minLevel, maxLevel, teleportEnabled, teleportLocation, messageEnabled, messageText);
            regions.put(name, data);
        }

        LevelPoints.getDebug(DebugSeverity.NORMAL, "Loaded " + regions.size() + " regions");

    }

    public boolean hasRegionData(String name){
        return regions.containsKey(name);
    }
    public RegionData getRegionData(String name){
        return regions.get(name);
    }

    public boolean isRegionLocked(){
        return regionLocked;
    }


}
