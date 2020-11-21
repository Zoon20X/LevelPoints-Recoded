package levelpoints.events.CustomEvents;

import levelpoints.Containers.BoostersContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BoosterActivationEvent extends Event implements Cancellable {
    private Boolean isCancelled;
    private double multiplier;
    private String time;
    private Player player;
    private static final HandlerList HANDLERS = new HandlerList();

    public BoosterActivationEvent(double multiplier, String time, Player player){
        super(true);
        this.multiplier = multiplier;
        this.time = time;
        this.player = player;
        this.isCancelled = false;

    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public double getMultiplier(){
        return this.multiplier;
    }
    public String getTime(){
        return this.time;
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
    }
}
