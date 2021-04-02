package me.zoon20x.levelpoints.events.CustomEvents;

import me.zoon20x.levelpoints.containers.Player.PlayerData;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LevelUpEvent extends Event {

    private PlayerData data;
    private final int level;

    private static final HandlerList HANDLERS = new HandlerList();

    public LevelUpEvent(int level, PlayerData data) {

        this.level = level;
        this.data = data;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public int getLevel() {
        return this.level;
    }

    public PlayerData getPlayerData() {
        return data;
    }
}
