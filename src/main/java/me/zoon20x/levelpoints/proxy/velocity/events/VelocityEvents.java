package me.zoon20x.levelpoints.proxy.velocity.events;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkPlayer;
import me.zoon20x.levelpoints.CrossNetworkStorage.SerializeData;
import me.zoon20x.levelpoints.proxy.velocity.LevelPoints;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class VelocityEvents {

    @Subscribe
    public void onConnect(LoginEvent event){

        UUID uuid = event.getPlayer().getUniqueId();
        if(event.getResult() != ResultedEvent.ComponentResult.allowed()){
            return;
        }
        String data = LevelPoints.getInstance().getCachedPlayers().getString(String.valueOf(uuid));
        if(data == null || data.equalsIgnoreCase("")){
            return;
        }
        try {
            LevelPoints.getInstance().getNetPlayerStorage().addPlayer(uuid, (NetworkPlayer) SerializeData.setData(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event){

        Player player = event.getPlayer();
        LevelPoints.getInstance().getServer().getScheduler().buildTask(LevelPoints.getInstance(), ()->{
            try {
                LevelPoints.getInstance().getCachedPlayers().set(player.getUniqueId().toString(), SerializeData.toString(me.zoon20x.levelpoints.proxy.bungee.LevelPoints.getInstance().getNetPlayerStorage().getPlayer(player.getUniqueId())));
                LevelPoints.getInstance().getCachedPlayers().save();
                LevelPoints.getInstance().getNetPlayerStorage().removePlayer(player.getUniqueId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).delay(100, TimeUnit.MILLISECONDS).schedule();

    }


}
