package levelpoints.events.CustomEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LevelUpEvent extends Event {

    private final Player player;
    private final int level;

    private static final HandlerList HANDLERS = new HandlerList();

    public LevelUpEvent(Player player, int level) {
        super(true);
        this.player = player;
        this.level = level;
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

    public int getLevel() {
        return this.level;
    }

}
