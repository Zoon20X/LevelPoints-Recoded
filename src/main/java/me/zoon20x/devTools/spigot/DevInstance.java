package me.zoon20x.devTools.spigot;

import me.zoon20x.devTools.spigot.player.PlayerStorage;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.devTools.spigot.events.DevEvents;
import org.bukkit.Bukkit;

public class DevInstance {

    private DevConfig devConfig;



    public DevInstance(){
        devConfig = new DevConfig();
        PlayerStorage playerStorage = new PlayerStorage();
        //playerStorage.loadTestPlayers(50);
        loadEvents();
    }

    public void onDisable(){
    }


    private void loadEvents(){
        Bukkit.getPluginManager().registerEvents(new DevEvents(), LevelPoints.getInstance());
    }
}
