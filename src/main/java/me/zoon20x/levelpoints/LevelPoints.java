package me.zoon20x.levelpoints;

import me.zoon20x.levelpoints.API.LevelPointsAPI;
import me.zoon20x.devTools.spigot.DevInstance;
import me.zoon20x.levelpoints.containers.Player.PlayerStorage;
import me.zoon20x.levelpoints.events.CustomEvents.EventUtils;
import me.zoon20x.levelpoints.events.EXPEarnEvents;
import me.zoon20x.levelpoints.events.PlayerStorageEvents;
import me.zoon20x.levelpoints.utils.LpsSettings;
import me.zoon20x.levelpoints.utils.files.ConfigUtils;
import me.zoon20x.levelpoints.utils.messages.LangSettings;
import me.zoon20x.levelpoints.utils.messages.MessagesUtil;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class LevelPoints extends JavaPlugin {
    private static LevelPoints instance;
    private static DevInstance devInstance;
    private static LevelPointsAPI levelPointsAPI;

    private ConfigUtils configUtils;
    private LpsSettings lpsSettings;
    private LangSettings langSettings;

    private PlayerStorage playerStorage;

    private EventUtils eventUtils;
    private boolean devMode;
    private Expression expression;
    private MessagesUtil messagesUtil;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        messagesUtil = new MessagesUtil();
        configUtils = new ConfigUtils();

        eventUtils = new EventUtils();
        playerStorage = new PlayerStorage();
        langSettings = new LangSettings();
        lpsSettings = new LpsSettings(this);
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
        Bukkit.getPluginManager().registerEvents(new PlayerStorageEvents(), this);
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (devMode) {
            devInstance.onDisable();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (playerStorage.hasPlayer(player.getUniqueId())) {
                playerStorage.savePlayerData(player.getUniqueId());
            }
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

    public Expression getExpression() {
        return expression;
    }

    public PlayerStorage getPlayerStorage() {
        return playerStorage;
    }

    public MessagesUtil getMessagesUtil() {
        return messagesUtil;
    }

    public LpsSettings getLpsSettings() {
        return lpsSettings;
    }

    public LangSettings getLangSettings() {
        return langSettings;
    }
}
