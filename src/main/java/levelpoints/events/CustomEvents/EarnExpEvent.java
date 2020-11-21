package levelpoints.events.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

import java.math.BigDecimal;

public class EarnExpEvent extends Event implements Cancellable {
    private Boolean isCancelled;
    private TasksEnum task;
    private double amount;
    private Player player;
    private Event event;
    private static final HandlerList HANDLERS = new HandlerList();

    public EarnExpEvent(TasksEnum task,Event event, double amount, Player player){
        super(true);
        this.task = task;
        this.amount = amount;
        this.player = player;
        this.event = event;
        this.isCancelled = false;

    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public TasksEnum getTask(){
        return this.task;
    }
    public double getAmount(){
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
}
