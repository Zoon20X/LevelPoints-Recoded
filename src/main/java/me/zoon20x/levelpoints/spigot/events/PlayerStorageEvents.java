package me.zoon20x.levelpoints.spigot.events;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkPlayer;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkResponse;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.Response;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.CnsSettings;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerStorage;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PlayerStorageEvents implements Listener {

    @EventHandler
    public void onAsyncJoin(AsyncPlayerPreLoginEvent event){
        if(event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED){
            return;
        }

        if(LevelPoints.getInstance().getCnsSettings().isEnabled()){
            new BukkitRunnable() {
                @Override
                public void run() {
                    loadDelay(event.getUniqueId(), event.getName(), 1);
                }
            }.runTaskLater(LevelPoints.getInstance(), 10);
            return;
        }


        LevelPoints.getInstance().getPlayerStorage().loadPlayer(event.getUniqueId(), event.getName());
    }

    private void loadDelay(UUID uuid, String name, int iteration) {
        Response response = LevelPoints.getInstance().getNetwork().retrieveInfo(uuid);
        LevelPoints.getInstance().log(DebugSeverity.SEVER, String.valueOf(response.getNetworkResponse()));
        if (response.getNetworkResponse() == NetworkResponse.Success) {
            NetworkPlayer networkPlayer = response.getNetworkPlayer();
            if (LevelPoints.getInstance().getCnsSettings().getServerID().equals(networkPlayer.getLastKnownServer())) {

                if (iteration < 3) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            loadDelay(uuid, name, iteration + 1);
                            return;
                        }
                    }.runTaskLater(LevelPoints.getInstance(), 10);
                    return;
                }
            }

            LevelPoints.getInstance().getPlayerStorage().loadPlayer(uuid, name, response.getNetworkPlayer());
            LevelPoints.getInstance().log(DebugSeverity.NORMAL, "loaded network player");
        }else{
            LevelPoints.getInstance().getPlayerStorage().loadPlayer(uuid, name);
            LevelPoints.getInstance().getNetwork().sendToProxy(LevelPoints.getInstance().getPlayerStorage().getPlayerData(uuid));
        }
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        PlayerStorage storage = LevelPoints.getInstance().getPlayerStorage();
        if(!storage.hasPlayer(player.getUniqueId())){
            return;
        }

        if(LevelPoints.getInstance().getCnsSettings().isEnabled()){
         LevelPoints.getInstance().getNetwork().sendToProxy(storage.getPlayerData(player.getUniqueId()));
        }
        storage.savePlayerData(player.getUniqueId());

    }

}
