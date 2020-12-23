package levelpoints.Utils;

import levelpoints.Cache.ExternalCache;
import levelpoints.Cache.FileCache;
import levelpoints.levelpoints.LevelPoints;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class AsyncFileCache {
    static Plugin plugin = LevelPoints.getInstance();
    static File TopListFile = new File(plugin.getDataFolder(), "TopList.yml");
    static File WSFile = new File(plugin.getDataFolder(), "/OtherSettings/WildStacker.yml");
    static File FileChanceFile = new File(plugin.getDataFolder(), "/Settings/FileChance.yml");
    static FileConfiguration FileChanceConfig = YamlConfiguration.loadConfiguration(FileChanceFile);
    static FileConfiguration TopListConfig = YamlConfiguration.loadConfiguration(TopListFile);
    static FileConfiguration WSConfig = YamlConfiguration.loadConfiguration(WSFile);
    static File EXPFile = new File(plugin.getDataFolder(), "/Settings/EXP.yml");
    static FileConfiguration EXPConfig = YamlConfiguration.loadConfiguration(EXPFile);
    static File LevelFile = new File(plugin.getDataFolder(), "/Settings/Levels.yml");
    static FileConfiguration LevelConfig = YamlConfiguration.loadConfiguration(LevelFile);
    static File RewardsFile = new File(plugin.getDataFolder(), "/Settings/Rewards.yml");
    static FileConfiguration RewardsConfig = YamlConfiguration.loadConfiguration(RewardsFile);
    static File LangFile = new File(plugin.getDataFolder(), "Lang.yml");
    static FileConfiguration LangConfig = YamlConfiguration.loadConfiguration(LangFile);
    static File MMFile = new File(plugin.getDataFolder(), "/OtherSettings/MythicMobs.yml");
    static FileConfiguration MMConfig = YamlConfiguration.loadConfiguration(MMFile);
    static File rpgMobsFile = new File(plugin.getDataFolder(), "/OtherSettings/LorinthsRpgMobs.yml");
    static FileConfiguration rpgMobsConfig = YamlConfiguration.loadConfiguration(rpgMobsFile);
    static File FormatsFile = new File(plugin.getDataFolder(), "/Settings/Formats.yml");
    static FileConfiguration FormatsConfig = YamlConfiguration.loadConfiguration(FormatsFile);
    static File RankMultiplierFile = new File(plugin.getDataFolder(), "/Settings/RankMultiplier.yml");
    static FileConfiguration RankMultiplierConfig = YamlConfiguration.loadConfiguration(RankMultiplierFile);
    public static void startAsyncCreate(){


        FileCache.createFile(TopListFile, TopListConfig, "TopList.yml", "TopList.yml", "TopList");
        FileCache.createFile(EXPFile, EXPConfig, "/Settings/EXP.yml", "Settings/EXP.yml", "EXP");
        FileCache.createFile(LevelFile, LevelConfig, "/Settings/Levels.yml", "Settings/Levels.yml", "Levels");
        FileCache.createFile(RewardsFile, RewardsConfig, "/Settings/Rewards.yml", "Settings/Rewards.yml", "Rewards");
        FileCache.createFile(WSFile, WSConfig, "/OtherSettings/WildStacker.yml", "OtherSettings/WildStacker.yml", "WildStacker");
        FileCache.createFile(MMFile, MMConfig, "/OtherSettings/MythicMobs.yml", "OtherSettings/MythicMobs.yml", "MythicMobs");
        FileCache.createFile(rpgMobsFile, rpgMobsConfig, "/OtherSettings/LorinthsRpgMobs.yml", "OtherSettings/LorinthsRpgMobs.yml", "LorinthsRpgMobs");
        FileCache.createFile(LangFile, LangConfig, "Lang.yml", "Lang.yml", "Lang");

        startAsyncCache();
    }
    public static void createFormatsFile(){
        FileCache.createFile(FormatsFile, FormatsConfig, "/Settings/Formats.yml", "Settings/Formats.yml", "Formats");
        FileCache.addFileToCache("formatsConfig", runConfig("/Settings/Formats.yml"));
    }

    public static void createRankMultiplier(){
        FileCache.createFile(RankMultiplierFile, RankMultiplierConfig, "/Settings/RankMultiplier.yml", "Settings/RankMultiplier.yml", "RankMultplier");
        FileCache.addFileToCache("rankMultiplierConfig", runConfig("/Settings/RankMultiplier.yml"));
    }

    public static FileConfiguration runConfig(String location){
        File file = new File(LevelPoints.getInstance().getDataFolder(), location);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        return config;
    }
    public static void startAsyncCache(){
        new BukkitRunnable() {
            @Override
            public void run() {
                FileCache.addFileToCache("expConfig",runConfig("/Settings/EXP.yml"));
                FileCache.addFileToCache("levelConfig", runConfig("/Settings/Levels.yml"));
                FileCache.addFileToCache("rewardsConfig", runConfig("/Settings/Rewards.yml"));
                FileCache.addFileToCache("langConfig", runConfig("Lang.yml"));
                FileCache.addFileToCache("mmConfig", runConfig("/OtherSettings/MythicMobs.yml"));
                FileCache.addFileToCache("rpgMobsConfig", runConfig("/OtherSettings/LorinthsRpgMobs.yml"));
                if(ExternalCache.isRunningWildStacker()) {
                    FileCache.addFileToCache("wildStacker", WSConfig);
                }
                if(LevelPoints.getInstance().getConfig().getBoolean("RankMultipliers")){
                    createRankMultiplier();
                }


            }
        }.runTaskAsynchronously(LevelPoints.getInstance());
    }


}
