package me.zoon20x.levelpoints.proxy.bungee.events;

import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkPlayer;
import me.zoon20x.levelpoints.CrossNetworkStorage.SerializeData;
import me.zoon20x.levelpoints.proxy.bungee.LevelPoints;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

public class BungeeEvents implements Listener {

    @EventHandler
    public void onSwitch(ServerSwitchEvent event){
    }

    @EventHandler
    public void onProxyJoin(PostLoginEvent event){
        ProxiedPlayer proxiedPlayer = event.getPlayer();

        String data = LevelPoints.getInstance().getCachedPlayers().getString(String.valueOf(proxiedPlayer.getUniqueId()));
        if(data == null || data.equalsIgnoreCase("")){
            return;
        }
        try {
            LevelPoints.getInstance().getNetPlayerStorage().addPlayer(proxiedPlayer.getUniqueId(), (NetworkPlayer) SerializeData.setData(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent event){
        ProxiedPlayer player =event.getPlayer();
        try {
            LevelPoints.getInstance().getCachedPlayers().set(player.getUniqueId().toString(), SerializeData.toString(LevelPoints.getInstance().getNetPlayerStorage().getPlayer(player.getUniqueId())));
            LevelPoints.getInstance().getCachedPlayers().save();
            LevelPoints.getInstance().getNetPlayerStorage().removePlayer(player.getUniqueId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
