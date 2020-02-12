package levelpoints.LevelPointsEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FarmEvent extends Event {
    private final Player player;
    private final String FarmedItem;
    private final int expAmount;
    private final String Task;

    private static final HandlerList HANDLERS = new HandlerList();

    public FarmEvent(Player player, String farmedItem, int expAmount, String Task) {
        this.FarmedItem = farmedItem;

        this.player = player;
        this.expAmount = expAmount;
        this.Task = Task;
    }


    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return this.player;
    }

    public String getFarmedItem() {
        return this.FarmedItem;
    }

    public int getEXPAmount(){
        return this.expAmount;
    }
    public String getTask(){
        return this.Task;
    }
}
