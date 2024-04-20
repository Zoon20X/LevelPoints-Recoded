package me.zoon20x.levelpoints.spigot.events.CustomEvents;

import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PrestigeEvent extends Event {

    private Player player;
    private PlayerData data;

    private static final HandlerList HANDLERS = new HandlerList();

    public PrestigeEvent(Player player, PlayerData data) {
        this.player = player;
        this.data = data;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }


    public PlayerData getPlayerData() {
        return data;
    }

    public Player getPlayer() {
        return player;
    }
}