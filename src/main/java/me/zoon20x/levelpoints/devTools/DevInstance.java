package me.zoon20x.levelpoints.devTools;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.devTools.events.DevEvents;
import me.zoon20x.levelpoints.devTools.stats.BlockStat;
import org.bukkit.Bukkit;

public class DevInstance {

    private DevConfig devConfig;
    private BlockStat blockStat;

    public DevInstance(){
        devConfig = new DevConfig();
        blockStat = new BlockStat("", 1.0, 2);
        loadEvents();
    }


    private void loadEvents(){
        Bukkit.getPluginManager().registerEvents(new DevEvents(), LevelPoints.getInstance());
    }

    public DevConfig getDevConfig() {
        return devConfig;
    }

    public BlockStat getBlockStat() {
        return blockStat;
    }
}
