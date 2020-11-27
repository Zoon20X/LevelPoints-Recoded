package levelpoints.Cache;

import levelpoints.levelpoints.LevelPoints;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;

public class FileCache {

    private static HashMap<String, FileConfiguration> cache = new HashMap<>();

    static Plugin plugin = LevelPoints.getInstance();


    public static void createFile(File file, FileConfiguration config, String Location, String secLoc, String Name){
        if (file == null) {
            file = new File(plugin.getDataFolder() + Location);
            config = YamlConfiguration.loadConfiguration(file);
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "LevelPoints>> Loading Module File " + Name + ".yml");
        }

        if (!file.exists()) {
            plugin.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "LevelPoints>> Creating Module File " + Name + ".yml");
            plugin.saveResource(secLoc, false);
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
