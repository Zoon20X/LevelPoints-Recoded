package me.zoon20x.levelpoints.files;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;

public class FilesStorage {

    private static HashMap<String, FileConfiguration> cache = new HashMap<>();



    public static void createFile(File file, String Location, String secLoc, String Name){
        if (file == null) {
            file = new File(LevelPoints.getInstance().getDataFolder() + Location);
        }

        if (!file.exists()) {
                LevelPoints.getDebug(DebugSeverity.NORMAL, "Creating file " + Name + ".yml");
            LevelPoints.getInstance().saveResource(secLoc, false);
        } else {
            return;
        }
    }



    public static void addFileToCache(String name, FileConfiguration config){
        if(!cache.containsKey(name)){
            if(!cache.containsValue(config)){
                cache.put(name, config);

            }
        }
    }
    public static Boolean containsFile(String x){
        return cache.containsKey(x);
    }

    public static void clearCache(){
        cache.clear();
    }

    public static void removeFileFromCache(String name){
        cache.remove(name);
    }

    public static FileConfiguration getConfig(String name){
        return cache.get(name);
    }

}
