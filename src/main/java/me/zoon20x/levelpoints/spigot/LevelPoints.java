package me.zoon20x.levelpoints.spigot;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.spigot.API.LevelPointsAPI;
import me.zoon20x.levelpoints.spigot.NetworkUtils.Network;
import me.zoon20x.levelpoints.spigot.containers.CnsSettings;
import me.zoon20x.levelpoints.spigot.events.CustomEvents.EventUtils;
import me.zoon20x.levelpoints.spigot.utils.files.ConfigUtils;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import me.zoon20x.levelpoints.spigot.utils.messages.MessagesUtil;
import me.zoon20x.devTools.spigot.DevInstance;
import me.zoon20x.levelpoints.spigot.commands.LpsCommand;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerStorage;
import me.zoon20x.levelpoints.spigot.containers.Top.TopSettings;
import me.zoon20x.levelpoints.spigot.events.EXPEarnEvents;
import me.zoon20x.levelpoints.spigot.events.PlayerStorageEvents;
import me.zoon20x.levelpoints.spigot.utils.LpsSettings;
import me.zoon20x.levelpoints.spigot.utils.messages.LangSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.N;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public final class LevelPoints extends JavaPlugin {
    private static LevelPoints instance;
    private static DevInstance devInstance;
    private static LevelPointsAPI levelPointsAPI;

    private ConfigUtils configUtils;
    private LpsSettings lpsSettings;
    private LangSettings lang;
    private TopSettings topSettings;

    private PlayerStorage playerStorage;

    private EventUtils eventUtils;
    private boolean devMode;
    private MessagesUtil messagesUtil;

    private Network network;

    private CnsSettings cnsSettings;


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        messagesUtil = new MessagesUtil();
        configUtils = new ConfigUtils();

        eventUtils = new EventUtils();
        playerStorage = new PlayerStorage();
        lang = new LangSettings();
        lpsSettings = new LpsSettings(this);
        topSettings = new TopSettings();
        loadEvents();
        loadCommands();
        loadCNSSupport();

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
    private void loadCNSSupport(){
        YamlDocument config = getConfigUtils().getConfig();
        boolean cnsSupport = config.getBoolean("NetworkShare.CrossNetworkStorage.Enabled");
        if(cnsSupport){
            String address = config.getString("NetworkShare.CrossNetworkStorage.Address");
            int port = config.getInt("NetworkShare.CrossNetworkStorage.Port");
            this.network = new Network(address, port);
            if (config.getString("NetworkShare.CrossNetworkStorage.ServerID").equalsIgnoreCase("")) {
                cnsSettings = new CnsSettings(cnsSupport, address, port);
            }else{
                cnsSettings = new CnsSettings(cnsSupport, address, port, config.getString("NetworkShare.CrossNetworkStorage.ServerID"));
            }
        }
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
        Bukkit.getPluginManager().registerEvents(new PlayerStorageEvents(), this);
    }
    private void loadCommands(){
      LpsCommand lpsCommand = new LpsCommand(this);
    }

    public void reload() throws IOException {
        getLpsSettings().getLevelSettings().reload();
        getLpsSettings().getBlockSettings().reload();
        getLpsSettings().getMobSettings().reload();
        getLang().reload();
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerStorage.reloadPlayer(player.getUniqueId());
        }
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

    public PlayerStorage getPlayerStorage() {
        return playerStorage;
    }

    public MessagesUtil getMessagesUtil() {
        return messagesUtil;
    }

    public LpsSettings getLpsSettings() {
        return lpsSettings;
    }

    public LangSettings getLang() {
        return lang;
    }
    public void log(DebugSeverity severity, String... messages) {
        Arrays.asList(messages).forEach(m->{
            Bukkit.getConsoleSender().sendMessage(severity + "" + m);
        });
    }

    public TopSettings getTopSettings() {
        return topSettings;
    }

    public Network getNetwork() {
        return network;
    }

    public CnsSettings getCnsSettings() {
        return cnsSettings;
    }
}
