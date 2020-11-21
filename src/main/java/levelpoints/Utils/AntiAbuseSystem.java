package levelpoints.Utils;

import levelpoints.Cache.ExternalCache;
import levelpoints.Containers.EXPContainer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;

public class AntiAbuseSystem {

    private static HashSet<Location> blocks = new HashSet<>();

    public static void clearBlocksCache(){
        blocks.clear();
    }
    public static Boolean denyWorldGuard(Block block){
        if(ExternalCache.isRunningWorldGuard()){
            return AsyncEvents.isInRegion(block);
        }
        return false;
    }

    public static Boolean canEarnEXP(Location loc){
        if(EXPContainer.isRunningAntiAbuse()) {
            if (!blocks.isEmpty()) {
                if (blocks.contains(loc)) {
                    return false;
                }
            }
        }
        return true;
    }
    public static void addBlockToLocation(Location loc){
        blocks.add(loc);
    }
    public static void removeBlockFromLocation(Location loc){
        blocks.remove(loc);
    }


}
