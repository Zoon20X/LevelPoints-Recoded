package me.zoon20x.devTools.spigot;

import me.zoon20x.devTools.spigot.stats.BlockLoader;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.devTools.spigot.events.DevEvents;
import me.zoon20x.levelpoints.containers.Player.PlayerStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DevInstance {

    private DevConfig devConfig;
    private int defaultLevel;
    private double defaultEXP;
    private double defaultPrestige;



    public DevInstance(){
        devConfig = new DevConfig();
        defaultLevel = 1;
        defaultEXP = 0.0;
        defaultPrestige = 2.0;
        loadEvents();
    }

    public void onDisable(){
    }


    private void loadEvents(){
        Bukkit.getPluginManager().registerEvents(new DevEvents(), LevelPoints.getInstance());
    }

    public DevConfig getDevConfig() {
        return devConfig;
    }

    public int getDefaultLevel() {
        return defaultLevel;
    }

    public double getDefaultEXP() {
        return defaultEXP;
    }

    public double getDefaultPrestige() {
        return defaultPrestige;
    }
}
