package me.zoon20x.levelpoints.spigot.events;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkPlayer;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkResponse;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.Response;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerStorage;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
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
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getConfig();
        boolean cnsSupport = config.getBoolean("NetworkShare.CrossNetworkStorage.Enabled");
        if(cnsSupport){
            Response response = LevelPoints.getInstance().getNetwork().retrieveInfo(event.getUniqueId());
            if(response.getNetworkResponse() == NetworkResponse.Success){
                NetworkPlayer networkPlayer = response.getNetworkPlayer();
                LevelPoints.getInstance().getPlayerStorage().loadPlayer(event.getUniqueId(), event.getName(), response.getNetworkPlayer());
                LevelPoints.getInstance().log(DebugSeverity.NORMAL, "loaded network player");
                return;
            }
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
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getConfig();
        boolean cnsSupport = config.getBoolean("NetworkShare.CrossNetworkStorage.Enabled");
        if(cnsSupport){
         LevelPoints.getInstance().getNetwork().sendToProxy(storage.getPlayerData(player.getUniqueId()));
        }
        storage.savePlayerData(player.getUniqueId());
    }

}
