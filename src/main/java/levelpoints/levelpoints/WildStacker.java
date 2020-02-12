package levelpoints.levelpoints;

import com.bgsoftware.wildstacker.api.events.SpawnerPlaceEvent;
import com.bgsoftware.wildstacker.api.objects.StackedSpawner;

import levelpoints.utils.utils.UtilCollector;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class WildStacker implements Listener {
    private Plugin plugin = LevelPoints.getPlugin(LevelPoints.class);
    private LevelPoints lp = LevelPoints.getPlugin(LevelPoints.class);
    

    UtilCollector uc = new UtilCollector();

    public WildStacker(UtilCollector utilCollector) {
    }

    @EventHandler
    public void onSpawnerPlace(SpawnerPlaceEvent event){
        StackedSpawner spawner = event.getSpawner();
        Player player = event.getPlayer();
        File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        if(uc.WSConfig.getBoolean("DebugName")){
            player.sendMessage(spawner.getSpawner().getCreatureTypeName().toString());
        }

        if(!(UsersConfig.getInt("Level") >= uc.WSConfig.getInt(spawner.getSpawner().getCreatureTypeName().toString()))){
            event.setCancelled(true);
        }

    }
}
