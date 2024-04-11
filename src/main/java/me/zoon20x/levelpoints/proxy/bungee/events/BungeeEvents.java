package me.zoon20x.levelpoints.proxy.bungee.events;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeEvents implements Listener {

    @EventHandler
    public void onSwitch(ServerSwitchEvent event){

    }

    @EventHandler
    public void onLeave(ServerDisconnectEvent event){
        ProxiedPlayer player =event.getPlayer();
        ServerInfo info = event.getTarget();

    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event){
        ProxiedPlayer player = event.getPlayer();

    }

}
