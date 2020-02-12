package levelpoints.LevelPointsEvents;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LevelUpEvent extends Event {

    private final Player player;
    private final int level;
    private final boolean EXPOverlap;
    private final int EXPOverlapAmount;

    private static final HandlerList HANDLERS = new HandlerList();

    public LevelUpEvent(Player player, int level, boolean EXPOverlap, int EXPOverlapAmount) {
        this.player = player;
        this.level = level;
        this.EXPOverlap = EXPOverlap;
        this.EXPOverlapAmount = EXPOverlapAmount;
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
    public boolean getOverlap() {
        return this.EXPOverlap;
    }
    public int getOverlapAmount() {
        return this.EXPOverlapAmount;
    }
}
