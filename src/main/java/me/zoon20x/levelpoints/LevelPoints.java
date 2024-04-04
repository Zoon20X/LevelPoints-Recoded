package me.zoon20x.levelpoints;

import gnu.trove.impl.hash.TPrimitiveHash;
import me.zoon20x.devTools.spigot.player.PlayerStorage;
import me.zoon20x.levelpoints.API.LevelPointsAPI;
import me.zoon20x.levelpoints.containers.Blocks.BlockSettings;
import me.zoon20x.levelpoints.containers.PlayerData;
import me.zoon20x.devTools.spigot.DevInstance;
import me.zoon20x.levelpoints.events.CustomEvents.EventUtils;
import me.zoon20x.levelpoints.events.EXPEarnEvents;
import me.zoon20x.levelpoints.utils.files.ConfigUtils;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;
import java.util.HashMap;

public final class LevelPoints extends JavaPlugin {
    private static LevelPoints instance;
    private static DevInstance devInstance;
    private static LevelPointsAPI levelPointsAPI;
    private PlayerData playerData;



    private ConfigUtils configUtils;
    private BlockSettings blockSettings;


    private EventUtils eventUtils;
    private boolean devMode;
    private Expression expression;



    private HashMap<Integer, Double> levels = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        configUtils = new ConfigUtils();
        eventUtils = new EventUtils();
        loadSettings();
        loadEvents();


        if (getDescription().getVersion().contains("DEV")) {
            loadDev();
            return;
        }
        devMode = false;
        loadMetrics();

        levelPointsAPI = new LevelPointsAPI();
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "=============================");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "LevelPoints Plugin");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Lead Developer: Zoon20X");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Supported Developers: rgnter, dejvokep(BoostedYAML)");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Version: " + this.getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "MC-Compatible: 1.16.5-1.20*");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Enabled");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "=============================");



    }
    private void loadMetrics(){
        int pluginId = 6480; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("dev-mode", () -> {
            return String.valueOf(devMode);
        }));
    }

    private void loadDev(){
        devInstance = new DevInstance();

        devMode = true;
        loadMetrics();
        String expressionString2 = "50 * level + 15";
        expression = new ExpressionBuilder(expressionString2).variables("level").build();
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "=============================");
        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "LevelPoints DEV Instance");
        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "Version: " + this.getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Enabled");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "=============================");
    }


    private void loadEvents(){
        Bukkit.getPluginManager().registerEvents(new EXPEarnEvents(this), this);
    }
    public void loadSettings(){
        blockSettings = new BlockSettings(this.configUtils.getBlockSettingsConfig().getBoolean("Blocks.Enabled"));
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if(devMode) {
            devInstance.onDisable();
        }
    }



    public static LevelPointsAPI getAPI(){
        return levelPointsAPI;
    }

    public static DevInstance getDevInstance() {
        return devInstance;
    }

    public static LevelPoints getInstance(){
        return instance;
    }

    public ConfigUtils getConfigUtils() {
        return configUtils;
    }
    public EventUtils getEventUtils() {
        return eventUtils;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }

    public Expression getExpression() {
        return expression;
    }

    public BlockSettings getBlockSettings() {
        return blockSettings;
    }
}
