package me.zoon20x.levelpoints.containers.Settings;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Settings.Configs.RegionData;
import me.zoon20x.levelpoints.utils.WorldGuardAPI;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldGuardSettings {


    public static String getRegionName(Location location){
        String name = "";
        WorldGuardAPI worldGuardAPI = new WorldGuardAPI(LevelPoints.getInstance().getServer().getPluginManager().getPlugin("WorldGuard"), LevelPoints.getInstance());
        ApplicableRegionSet checkSet = worldGuardAPI.getRegionSet(location);
        if(checkSet == null || worldGuardAPI == null){
            return "";
        }
        if (!checkSet.getRegions().isEmpty()) {
            for (ProtectedRegion x : checkSet.getRegions()) {
                if(LevelPoints.getAntiAbuseSettings().hasRegionData(x.getId())){
                    name = x.getId();
                    break;
                }
            }
        }
        return name;
    }
    public static Boolean canEnterRegion(Player player, String name) {
        RegionData regionData = LevelPoints.getAntiAbuseSettings().getRegionData(name);
        PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(player.getUniqueId());
        if(data.getLevel() >= regionData.getMinLevel() && data.getLevel() <= regionData.getMaxLevel()){
            return true;
        }

        return false;
    }
}
