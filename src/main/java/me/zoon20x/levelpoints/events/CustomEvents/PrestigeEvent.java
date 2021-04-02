package me.zoon20x.levelpoints.events.CustomEvents;

import me.zoon20x.levelpoints.containers.Player.PlayerData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PrestigeEvent extends Event {

    private PlayerData data;
    private final int prestige;

    private static final HandlerList HANDLERS = new HandlerList();

    public PrestigeEvent(int prestige, PlayerData data) {
        this.prestige = prestige;
        this.data = data;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public int getPrestige() {
        return this.prestige;
    }

    public PlayerData getPlayerData() {
        return data;
    }
}
