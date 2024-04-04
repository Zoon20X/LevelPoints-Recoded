package me.zoon20x.levelpoints;

import me.zoon20x.levelpoints.containers.PlayerData;
import me.zoon20x.levelpoints.devTools.DevInstance;
import me.zoon20x.levelpoints.events.CustomEvents.EventUtils;
import me.zoon20x.levelpoints.events.EXPEarnEvents;
import me.zoon20x.levelpoints.utils.files.ConfigUtils;
import org.apache.commons.jexl3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;

public final class LevelPoints extends JavaPlugin {
    private static LevelPoints instance;
    private static DevInstance devInstance;
    private PlayerData playerData;

    private ConfigUtils configUtils;
    private EventUtils eventUtils;
    private boolean devMode;


    private HashMap<Integer, Double> levels = new HashMap<>();




    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        configUtils = new ConfigUtils();
        eventUtils = new EventUtils();
        loadEvents();
        if (getDescription().getVersion().contains("DEV")) {
            loadDev();
            return;
        }
        devMode = false;
        loadMetrics();


        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "=============================");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "LevelPoints Plugin");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Lead Developer: Zoon20X");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Supported Devs: rgnter, dejvokep(BoostedYAML)");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Version: " + this.getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "MC-Compatible: 1.16.5-1.20*");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Enabled");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "=============================");


//        long startTime = System.nanoTime();
//        String expressionString = "50*level+15D";
//        JexlExpression expression = new JexlBuilder().safe(true).create().createExpression(expressionString);
//        JexlContext context = new MapContext();
//        for(int i=0;i<100;i++) {
//            context.set("level", i);
//            double result = (double) expression.evaluate(context);
//            levels.put(i, result);
//        }
//        long endTime = System.nanoTime();
//        long duration = ((endTime - startTime) / 1000000);
//        System.out.println(duration + " ms");
//        System.out.println(levels.size());
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
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "=============================");
        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "LevelPoints DEV Instance");
        Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "Version: " + this.getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Enabled");
        Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "=============================");
    }


    private void loadEvents(){
        Bukkit.getPluginManager().registerEvents(new EXPEarnEvents(this), this);
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
}
