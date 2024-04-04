package me.zoon20x.devTools.spigot;

import me.zoon20x.devTools.spigot.stats.BlockLoader;
import me.zoon20x.levelpoints.API.LevelPointsAPI;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.devTools.spigot.events.DevEvents;
import me.zoon20x.devTools.spigot.stats.BlockStat;
import org.bukkit.Bukkit;

public class DevInstance {

    private DevConfig devConfig;

    private BlockLoader blockLoader;


    public DevInstance(){
        devConfig = new DevConfig();
        blockLoader = new BlockLoader(devConfig.dev1);
        loadEvents();


    }


    private void loadEvents(){
        Bukkit.getPluginManager().registerEvents(new DevEvents(), LevelPoints.getInstance());
    }

    public DevConfig getDevConfig() {
        return devConfig;
    }

    public BlockLoader getBlockLoader() {
        return blockLoader;
    }
}
