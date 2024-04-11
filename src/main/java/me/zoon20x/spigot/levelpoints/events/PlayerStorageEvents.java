package me.zoon20x.spigot.levelpoints.events;

import me.zoon20x.devTools.spigot.DevInstance;
import me.zoon20x.spigot.levelpoints.LevelPoints;
import me.zoon20x.spigot.levelpoints.containers.Player.PlayerStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerStorageEvents implements Listener {

    @EventHandler
    public void onAsyncJoin(AsyncPlayerPreLoginEvent event){
        if(event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED){
            return;
        }
        LevelPoints.getInstance().getPlayerStorage().loadPlayer(event.getUniqueId(), event.getName());
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        PlayerStorage storage = LevelPoints.getInstance().getPlayerStorage();
        if(!storage.hasPlayer(player.getUniqueId())){
            return;
        }
        storage.savePlayerData(player.getUniqueId());
    }

}
