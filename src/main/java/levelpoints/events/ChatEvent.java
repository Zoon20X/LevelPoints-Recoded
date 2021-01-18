package levelpoints.events;

import levelpoints.Containers.PlayerContainer;
import levelpoints.Utils.AsyncEvents;
import levelpoints.levelpoints.Formatting;
import levelpoints.levelpoints.LevelPoints;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class ChatEvent implements Listener {


    public ChatEvent(Plugin plugin) {
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        event.setCancelled(true);
        Player player = event.getPlayer();
        String msg = event.getMessage();
        PlayerContainer container = AsyncEvents.getPlayerContainer(player);
        String format = container.getChatFormat();

        if(player == null){
            return;
        }

        format = format.replace("{name}", player.getName())
                .replace("{message}", msg)
                .replace("{level}", String.valueOf(container.getLevel()))
                .replace("{exp}", String.valueOf(container.getEXP()))
                .replace("{required_exp}", String.valueOf(container.getRequiredEXP()))
                .replace("{prestige}", String.valueOf(container.getPrestige()));
        format = PlaceholderAPI.setPlaceholders(player, format);
        LevelPoints.getInstance().getServer().broadcastMessage(Formatting.basicColor(format));

    }

}
