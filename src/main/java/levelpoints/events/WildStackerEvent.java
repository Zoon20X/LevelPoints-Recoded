package levelpoints.events;

import com.bgsoftware.wildstacker.api.WildStacker;
import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.events.SpawnerPlaceEvent;
import com.bgsoftware.wildstacker.api.objects.StackedSpawner;
import levelpoints.Cache.ExternalCache;
import levelpoints.Cache.FileCache;
import levelpoints.Containers.PlayerContainer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class WildStackerEvent implements Listener {
    public WildStackerEvent(Plugin plugin) {
    }

    @EventHandler
    public void onSpawnerPlace(SpawnerPlaceEvent event){
        StackedSpawner spawner = event.getSpawner();

        Player player = event.getPlayer();


        if(FileCache.getConfig("wildStacker").getBoolean("DebugName")){
            player.sendMessage(spawner.getSpawner().getCreatureTypeName());
        }

        if(!(new PlayerContainer(event.getPlayer()).getLevel() >= FileCache.getConfig("wildStacker").getInt(spawner.getSpawner().getCreatureTypeName().toString()))){
            event.setCancelled(true);
        }

    }
}
