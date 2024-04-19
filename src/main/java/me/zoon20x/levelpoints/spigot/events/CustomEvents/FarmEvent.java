package me.zoon20x.levelpoints.spigot.events.CustomEvents;

import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

public class FarmEvent extends BlockBreakEvent {

    private Player player;
    private Ageable crop;
    private BlockBreakEvent event;


    private static final HandlerList HANDLERS = new HandlerList();
    public FarmEvent(Block block, Player player, BlockBreakEvent event){
        super(block, player);
        this.player = player;
        this.crop = (Ageable) block.getBlockData();
        this.event = event;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public void setCancelled(boolean cancelled){
        this.event.setCancelled(true);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer(){
        return this.player;
    }

    public boolean isRipe(){
        return crop.getAge() >= crop.getMaximumAge();
    }
    public Ageable getCrop() {
        return crop;
    }
}
