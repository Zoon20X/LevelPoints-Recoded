package me.zoon20x.levelpoints.spigot.events.CustomEvents;

import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

public class FarmEvent extends BlockBreakEvent implements Cancellable {

    private Boolean isCancelled;
    private Player player;
    private Ageable crop;


    private static final HandlerList HANDLERS = new HandlerList();
    public FarmEvent(Block block, Player player){
        super(block, player);
        this.player = player;
        this.crop = (Ageable) block;
        this.isCancelled = false;

    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer(){
        return this.player;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
        super.setCancelled(cancel);
    }

    public boolean isRipe(){
        return crop.getAge() >= crop.getMaximumAge();
    }
    public Ageable getCrop() {
        return crop;
    }
}
