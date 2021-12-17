package me.zoon20x.levelpoints;




import me.zoon20x.levelpoints.commands.adminLpsCommand;
import me.zoon20x.levelpoints.commands.lpsCommand;
import me.zoon20x.levelpoints.containers.ExtraSupport.MythicMobsSettings;
import me.zoon20x.levelpoints.containers.Player.PlayerStorage;
import me.zoon20x.levelpoints.containers.Settings.Boosters.BoosterSettings;
import me.zoon20x.levelpoints.containers.Settings.Configs.*;
import me.zoon20x.levelpoints.containers.Settings.Configs.Rewards.RewardSettings;
import me.zoon20x.levelpoints.containers.Settings.Placeholders.LevelColorSettings;
import me.zoon20x.levelpoints.events.ExpEarningEvents;
import me.zoon20x.levelpoints.events.MoveEvent;
import me.zoon20x.levelpoints.events.PlayerEvents;
import me.zoon20x.levelpoints.files.FilesGenerator;
import me.zoon20x.levelpoints.files.PlayerGenerator;
import me.zoon20x.levelpoints.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public final class LevelPoints extends JavaPlugin{

    private static LevelPoints instance;
    private static File userFolder;
    private static File boostersFolder;
    private static File extraSupportFolder;
    private static MySQL sql;
    private static ConfigSettings configSettings;
    private static PlayerStorage storage;
    private static LevelsSettings levelsSettings;
    private static LangSettings langSettings;
    private static ExpSettings expSettings;
    private static RewardSettings rewardSettings;
    private static TopListSettings topListSettings;
    private static AntiAbuseSettings antiAbuseSettings;
    private static BoosterSettings boosterSettings;
    private static PvpSettings pvpSettings;
    private static boolean isReloading;
    private static boolean isRunningSQL;
    private static FileConfiguration configuration;
    private static FilesGenerator generator;
    private static MythicMobsSettings mythicMobsSettings;
    private static boolean mythicMobsEnabled;
    private static LevelColorSettings levelColorSettings;


    public static MythicMobsSettings getMythicMobsSettings() {
        return mythicMobsSettings;
    }

    public static boolean isMythicMobsEnabled() {
        return mythicMobsEnabled;
    }

    public static boolean isLevelColorEnabled() {
        return configSettings.isPlaceholderColorLevelEnabled();
    }




    private void generateFolders(){
        this.saveDefaultConfig();
        this.getDataFolder().mkdirs();
        userFolder = new File(getDataFolder(), "Players");
        userFolder.mkdirs();
        boostersFolder = new File(getDataFolder(), "Boosters");
        boostersFolder.mkdirs();
        extraSupportFolder = new File(getDataFolder(), "ExtraSupport");
        extraSupportFolder.mkdirs();
    }

    @Override
    public void onLoad(){

    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        mythicMobsEnabled = false;
        isRunningSQL = false;
        generateFolders();
        getDebug(DebugSeverity.NORMAL, "Initializing Data, this will take a second");
        Bukkit.getWorlds().get(0).getBlockAt(0, 0, 0).getData();
        generator = new FilesGenerator();
        getFilesGenerator().generateFiles();
        reloadClass();
        loadEvents();
        loadCommands();
        getLevelSettings().generateRequired();
        getExpSettings().generateBlocks();
        getExpSettings().generateMobs();
        getExpSettings().generateBreeds();
        getExpSettings().generateCrafting();
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

        if(getConfig().isSet("Progressbar")) {
            ProgressStatics.userConfiguredStyle.stepMin =
                    getConfig().getInt("Progressbar.MinStep");
            ProgressStatics.userConfiguredStyle.stepMax =
                    getConfig().getInt("Progressbar.MaxStep");

            ProgressStatics.userConfiguredStyle.visualBorder =
                    getConfig().getString("Progressbar.VisualBorder");
            ProgressStatics.userConfiguredStyle.visualCompletedStep =
                    getConfig().getString("Progressbar.VisualCompletedStep");
            ProgressStatics.userConfiguredStyle.visualUncompletedStep =
                    getConfig().getString("Progressbar.VisualUncompletedStep");

        }

        if(getAntiAbuseSettings().isRegionLocked()){
            getServer().getPluginManager().registerEvents(new MoveEvent(), this);
        }


        System.out.println(ChatColor.DARK_AQUA + "=============================");
        System.out.println(ChatColor.AQUA + "LevelPoints Plugin - LITE");
        System.out.println(ChatColor.AQUA + "Developer: Zoon20X");
        System.out.println(ChatColor.AQUA + "Version: " + this.getDescription().getVersion());
        System.out.println(ChatColor.AQUA + "MC-Compatible: 1.8-1.17*");
        System.out.println(ChatColor.DARK_AQUA + "Enabled");
        System.out.println(ChatColor.DARK_AQUA + "=============================");
        sendLoadedData();
        getExpSettings().startTimedEXP();
    }

    public void reloadClass(){
        configSettings = new ConfigSettings();
        storage = new PlayerStorage();
        levelsSettings = new LevelsSettings();
        langSettings = new LangSettings();
        expSettings = new ExpSettings();
        rewardSettings = new RewardSettings();
        topListSettings = new TopListSettings();
        antiAbuseSettings = new AntiAbuseSettings();
        pvpSettings = new PvpSettings();
        boosterSettings = new BoosterSettings();
        levelColorSettings = null;
        mythicMobsSettings = null;
        if(Bukkit.getPluginManager().getPlugin("MythicMobs") !=null) {
            if (Bukkit.getPluginManager().getPlugin("MythicMobs").isEnabled()) {
                mythicMobsSettings = new MythicMobsSettings();
                mythicMobsEnabled = true;
            }
        }
        if(configSettings.isPlaceholderColorLevelEnabled()){
            levelColorSettings = new LevelColorSettings();
        }

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
        getDebug(DebugSeverity.WARNING, "OnEXPEnabled:" + getConfigSettings().getOnExpEnabled());
        getDebug(DebugSeverity.WARNING, "OnEXPMessage:" + getConfigSettings().getOnExpMessage());
        getDebug(DebugSeverity.WARNING, "LoadedPlayersAmount: " + getPlayerStorage().getAmountLoaded());
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
        getBoosterSettings().saveBoosters();
        getPlayerGenerator().saveAllData();
        if(getSQL() != null) {
            getSQL().disconnect();
        }
    }
    public static void getDebug(DebugSeverity value, Object x){

    }

    public static PlayerStorage getPlayerStorage() {
        return storage;
    }


    public static File getUserFolder(){
        return userFolder;
    }
    public static File getBoostersFolder(){
        return boostersFolder;
    }
    public static LevelPoints getInstance(){
        return instance;
    }

    public static ConfigSettings getConfigSettings() {
        return configSettings;
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
        return generator;
    }
    public static LangSettings getLangSettings(){
        return langSettings;
    }

    public static PvpSettings getPvpSettings() {
        return pvpSettings;
    }
    public static BoosterSettings getBoosterSettings() {
        return boosterSettings;
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
    public static @Nullable LevelColorSettings getLevelColorSettings() {
        return levelColorSettings;
    }

    public static Double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}