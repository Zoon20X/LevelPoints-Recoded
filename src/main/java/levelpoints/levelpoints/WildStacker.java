package levelpoints.levelpoints;

import com.bgsoftware.wildstacker.api.events.SpawnerPlaceEvent;
import com.bgsoftware.wildstacker.api.objects.StackedSpawner;
import levelpoints.Cache.FileCache;
import levelpoints.Containers.PlayerContainer;
import levelpoints.Utils.AsyncEvents;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class WildStacker implements Listener {
    private Plugin plugin = LevelPoints.getPlugin(LevelPoints.class);


    @EventHandler
    public void onSpawnerPlace(SpawnerPlaceEvent event){
        StackedSpawner spawner = event.getSpawner();
        Player player = event.getPlayer();
        if(FileCache.getConfig("wildStacker").getBoolean("DebugName")){
            player.sendMessage(spawner.getSpawner().getCreatureTypeName().toString());
        }
        PlayerContainer container = AsyncEvents.getPlayerContainer(player);

        if(!(container.getLevel() >= FileCache.getConfig("wildStacker").getInt(spawner.getSpawner().getCreatureTypeName()))){
            event.setCancelled(true);
        }

    }
}
