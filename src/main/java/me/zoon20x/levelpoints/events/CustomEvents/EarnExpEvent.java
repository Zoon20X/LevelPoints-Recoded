package me.zoon20x.levelpoints.events.CustomEvents;

import me.zoon20x.levelpoints.containers.Player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EarnExpEvent extends Event implements Cancellable {

    private Boolean isCancelled;

    private double amount;
    private Player player;
    private Event event;
    private PlayerData data;
    private EarnTask task;
    private static final HandlerList HANDLERS = new HandlerList();

    public EarnExpEvent(PlayerData data, Event event, double amount, Player player, EarnTask task){
        this.amount = amount;
        this.data = data;
        this.player = player;
        this.event = event;
        this.task = task;
        this.isCancelled = false;

    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public double getExpAmount(){
        return this.amount;
    }
    public Player getPlayer(){
        return this.player;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }
    public Event getTaskEvent(){
        return this.event;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public PlayerData getPlayerData() {
        return data;
    }

    public EarnTask getTask() {
        return task;
    }
}
