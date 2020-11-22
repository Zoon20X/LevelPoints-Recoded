package levelpoints.Utils;

import levelpoints.Cache.ExternalCache;
import levelpoints.Cache.FileCache;
import levelpoints.Containers.EXPContainer;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class AntiAbuseSystem {

    private static HashSet<Location> blocks = new HashSet<>();
    private static HashMap<Location, String> cooldown = new HashMap<>();

    public static void clearBlocksCache(){
        blocks.clear();
    }
    public static Boolean denyWorldGuard(Block block){
        if(ExternalCache.isRunningWorldGuard()){
            return AsyncEvents.isInRegion(block);
        }
        return false;
    }

    public static Boolean canBreakBlock(Location loc){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date date = null;


        Date current = new Date();
        if(cooldown.isEmpty() || !cooldown.containsKey(loc)){
            return false;
        }
        try {
            current = format.parse(format.format(current));
            date = format.parse(cooldown.get(loc));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!current.after(date)) {
            return true;
        }
        return false;
    }

    public static Boolean canEarnEXP(Location loc){
        if(EXPContainer.isRunningAntiAbuse()) {
            if (!blocks.isEmpty()) {
                if (blocks.contains(loc)) {
                    if(FileCache.getConfig("expConfig").getBoolean("Anti-Abuse.Place.Cooldown.Enabled")){
                        cooldown.put(loc, generateCooldown());
                    }
                    return false;
                }
            }
        }
        return true;
    }
    public static String generateCooldown(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date cDate = new Date();
        Integer ticks = FileCache.getConfig("expConfig").getInt("Anti-Abuse.Place.Cooldown.Time");
        Date tomorrow = new Date(cDate.getTime() + ticks);
        return format.format(tomorrow);
    }
    public static void addBlockToLocation(Location loc){
        if(FileCache.getConfig("expConfig").getBoolean("Anti-Abuse.Place.Cooldown.Enabled")){
            Date current = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
            try {
                current = format.parse(format.format(current));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            cooldown.put(loc, format.format(current));
        }
        blocks.add(loc);
    }
    public static void removeBlockFromLocation(Location loc){
        blocks.remove(loc);
    }


}
