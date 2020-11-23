package levelpoints.events;

import levelpoints.Cache.FileCache;
import levelpoints.Cache.LangCache;
import levelpoints.Utils.AsyncEvents;
import levelpoints.Utils.WorldGuardAPI;
import levelpoints.levelpoints.Formatting;
import levelpoints.levelpoints.LevelPoints;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class MoveEvent implements Listener {


    public MoveEvent(Plugin plugin) {
    }

    @EventHandler
    public void onWalk(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
            if (!AsyncEvents.canEnterRegion(event.getPlayer(), event.getTo().getBlock())) {
                player.teleport(player.getLocation().add(event.getFrom().toVector().subtract(event.getTo().toVector()).normalize().multiply(2)));
                if (FileCache.getConfig("expConfig").getBoolean("Anti-Abuse.WorldGuard.LevelRegions.UseMessage")) {
                    player.sendMessage(Formatting.basicColor(FileCache.getConfig("expConfig").getString("Anti-Abuse.WorldGuard.LevelRegions.Message")));
                }
            }
        }
    }
}
