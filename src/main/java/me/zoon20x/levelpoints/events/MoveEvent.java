package me.zoon20x.levelpoints.events;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Settings.Configs.RegionData;
import me.zoon20x.levelpoints.containers.Settings.WorldGuardSettings;
import me.zoon20x.levelpoints.utils.MessageUtils;
import me.zoon20x.levelpoints.utils.WorldGuardAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener {

    @EventHandler
    public void onWalk(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ()) {
            String regionName = WorldGuardSettings.getRegionName(event.getTo());
            if(regionName.equalsIgnoreCase("")){
                return;
            }
            if (!WorldGuardSettings.canEnterRegion(event.getPlayer(), regionName)) {
                RegionData data = LevelPoints.getInstance().getAntiAbuseSettings().getRegionData(regionName);
                if(data.isTeleportEnabled()){
                    String[] cords = data.getTeleportLocation().split(",");
                    World world = player.getWorld();
                    int x = Integer.parseInt(cords[0]);
                    int y = Integer.parseInt(cords[1]);
                    int z = Integer.parseInt(cords[2]);
                    player.teleport(new Location(world, x, y, z));
                }else {
                    player.teleport(player.getLocation().add(event.getFrom().toVector().subtract(event.getTo().toVector()).normalize().multiply(2)));
                }
                if(!data.isMessageEnabled()){
                    return;
                }
                for(String x : data.getMessageText()) {
                    player.sendMessage(MessageUtils.getColor(x));
                }
            }
        }
    }
}
