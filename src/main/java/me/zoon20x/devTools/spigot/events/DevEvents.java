package me.zoon20x.devTools.spigot.events;

import me.zoon20x.devTools.spigot.player.PlayerStorage;
import me.zoon20x.devTools.spigot.stats.BlockLoader;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.PlayerData;
import me.zoon20x.devTools.spigot.DevInstance;
import me.zoon20x.devTools.spigot.stats.BlockStat;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DevEvents implements Listener {

    @EventHandler
    public void onAsyncJoin(AsyncPlayerPreLoginEvent event){
        if(event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED){
            return;
        }
        DevInstance instance = LevelPoints.getDevInstance();
        instance.getPlayerStorage().createPlayer(event.getUniqueId(), event.getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        DevInstance instance = LevelPoints.getDevInstance();
        PlayerStorage storage = instance.getPlayerStorage();
        if(!storage.hasPlayer(player.getUniqueId())){
            return;
        }
        storage.savePlayerInfo(player.getUniqueId());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        DevInstance instance = LevelPoints.getDevInstance();
        PlayerStorage storage = instance.getPlayerStorage();
        if(!storage.hasPlayer(player.getUniqueId())){
            return;
        }
        player.sendMessage(String.valueOf(storage.getPlayerInfo(player.getUniqueId()).getExp()));
        storage.getPlayerInfo(player.getUniqueId()).addExp(1.0);
    }


}
