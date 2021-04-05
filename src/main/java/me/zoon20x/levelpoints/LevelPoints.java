package me.zoon20x.levelpoints;




import me.zoon20x.levelpoints.commands.adminLpsCommand;
import me.zoon20x.levelpoints.commands.lpsCommand;
import me.zoon20x.levelpoints.containers.Player.PlayerStorage;
import me.zoon20x.levelpoints.containers.Settings.Configs.*;
import me.zoon20x.levelpoints.containers.Settings.Configs.Rewards.RewardSettings;
import me.zoon20x.levelpoints.events.ExpEarningEvents;
import me.zoon20x.levelpoints.events.MoveEvent;
import me.zoon20x.levelpoints.events.PlayerEvents;
import me.zoon20x.levelpoints.files.FilesGenerator;
import me.zoon20x.levelpoints.files.PlayerGenerator;
import me.zoon20x.levelpoints.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class LevelPoints extends JavaPlugin{

    private static LevelPoints instance;
    private static File userFolder;
    private static MySQL sql;
    private static PlayerStorage storage;
    private static LevelsSettings levelsSettings;
    private static LangSettings langSettings;
    private static ExpSettings expSettings;
    private static RewardSettings rewardSettings;
    private static TopListSettings topListSettings;
    private static AntiAbuseSettings antiAbuseSettings;
    private static boolean isReloading;
    private static boolean isRunningSQL;
    private static FileConfiguration configuration;



    private void generateFolders(){
        this.saveDefaultConfig();
        this.getDataFolder().mkdirs();
        userFolder = new File(getDataFolder(), "Players");
        userFolder.mkdirs();
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        isRunningSQL = false;
        generateFolders();
        getDebug(DebugSeverity.NORMAL, "Initializing Data, this will take a second");
        Bukkit.getWorlds().get(0).getBlockAt(0, 0, 0).getData();
        getFilesGenerator().generateFiles();
        reloadClass();
        loadEvents();
        loadCommands();
        getLevelSettings().generateRequired();
        getExpSettings().generateBlocks();
        getExpSettings().generateMobs();
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new LpsExpansion().register();
            getDebug(DebugSeverity.NORMAL, "Loaded Placeholder expansion");
        }


        if(getConfig().getBoolean("MySQL.Enabled")) {
            sql = new MySQL(
                    getConfig().getString(DataLocation.sqlHost),
                    getConfig().getInt(DataLocation.sqlPort),
                    getConfig().getString(DataLocation.sqlDatabase),
                    getConfig().getString(DataLocation.sqlUsername),
                    getConfig().getString(DataLocation.sqlPassword),
                    getConfig().getString(DataLocation.sqlTable),
                    getConfig().getBoolean(DataLocation.sqlSsl));
            if(sql.isConnected()) {
                isRunningSQL = true;
            }
        }

        if(getAntiAbuseSettings().isRegionLocked()){
            getServer().getPluginManager().registerEvents(new MoveEvent(), this);
        }


        System.out.println(ChatColor.DARK_AQUA + "=============================");
        System.out.println(ChatColor.AQUA + "LevelPoints Plugin");
        System.out.println(ChatColor.AQUA + "Developer: Zoon20X");
        System.out.println(ChatColor.AQUA + "Version: " + this.getDescription().getVersion());
        System.out.println(ChatColor.AQUA + "MC-Compatible: 1.8-1.16.5");
        System.out.println(ChatColor.DARK_AQUA + "Enabled");
        System.out.println(ChatColor.DARK_AQUA + "=============================");
        sendLoadedData();
    }

    public void reloadClass(){
        storage = new PlayerStorage();
        levelsSettings = new LevelsSettings();
        langSettings = new LangSettings();
        expSettings = new ExpSettings();
        rewardSettings = new RewardSettings();
        topListSettings = new TopListSettings();
        antiAbuseSettings = new AntiAbuseSettings();
    }
    public void setReloading(boolean value){
        isReloading = value;
    }
    public boolean isReloading(){
        return isReloading;
    }


    public void sendLoadedData(){
        getDebug(DebugSeverity.WARNING, "Starting-Level:" + getLevelSettings().getStartingLevel());
        getDebug(DebugSeverity.WARNING, "Starting-Experience:" + getLevelSettings().getStartingLevel());
        getDebug(DebugSeverity.WARNING, "Starting-Prestige:" + getLevelSettings().getMaxPrestige());
        getDebug(DebugSeverity.WARNING, "Max-Level:" + getLevelSettings().getMaxLevel());
        getDebug(DebugSeverity.WARNING, "Max-Prestige:" + getLevelSettings().getMaxPrestige());
        getDebug(DebugSeverity.WARNING, "Formula-Basic:" + getLevelSettings().getBasicFormula());
        getDebug(DebugSeverity.WARNING, "Formula-Advanced:" + getLevelSettings().getAdvancedFormula(1));
        getDebug(DebugSeverity.WARNING, "LevelUp-Type:" + getLevelSettings().getLevelUpType());
        getDebug(DebugSeverity.WARNING, "Formula-Type:" + getLevelSettings().getFormulaType());
        getDebug(DebugSeverity.WARNING, "LoadedLevels:" + Arrays.asList(getLevelSettings().getLoadedLevels()));
        getDebug(DebugSeverity.WARNING, "RegionLocked:" + getAntiAbuseSettings().isRegionLocked());
    }


    private void loadCommands(){
        adminLpsCommand adminlps = new adminLpsCommand(this, "adminlps");
        lpsCommand lpsCommand = new lpsCommand(this, "lps");

    }
    private void loadEvents(){
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getServer().getPluginManager().registerEvents(new ExpEarningEvents(), this);


    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getPlayerGenerator().saveAllData();
        if(getSQL() != null) {
            getSQL().disconnect();
        }
    }
    public static void getDebug(DebugSeverity value, Object x){
         System.out.println(MessageUtils.getColor(value+ "" + x));
    }

    public static PlayerStorage getPlayerStorage() {
        return storage;
    }


    public static File getUserFolder(){
        return userFolder;
    }
    public static LevelPoints getInstance(){
        return instance;
    }


    public static PlayerGenerator getPlayerGenerator(){
        return new PlayerGenerator();
    }
    public static LevelsSettings getLevelSettings(){
        return levelsSettings;
    }
    public static ExpSettings getExpSettings(){
        return expSettings;
    }

    public static RewardSettings getRewardSettings() {
        return rewardSettings;
    }
    public static TopListSettings getTopListSettings() {
        return topListSettings;
    }

    public static FilesGenerator getFilesGenerator(){
        return new FilesGenerator();
    }
    public static LangSettings getLangSettings(){
        return langSettings;
    }

    public static AntiAbuseSettings getAntiAbuseSettings() {
        return antiAbuseSettings;
    }
    public static MySQL getSQL(){
        return sql;
    }
    public static boolean isRunningSQL() {
        return isRunningSQL;
    }


    public static Double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}