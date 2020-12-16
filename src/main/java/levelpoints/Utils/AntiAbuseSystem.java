package levelpoints.Utils;

import com.bekvon.bukkit.residence.Residence;
import com.bekvon.bukkit.residence.containers.ResidencePlayer;
import levelpoints.Cache.ExternalCache;
import levelpoints.Cache.FileCache;
import levelpoints.Cache.SettingsCache;
import levelpoints.Containers.EXPContainer;
import levelpoints.levelpoints.LevelPoints;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.sacredlabyrinth.Phaed.PreciousStones.PreciousStones;
import org.bukkit.Location;
import org.bukkit.World;
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
    public static Boolean denyWorldGuard(Player player, Block block){
        if(ExternalCache.isRunningWorldGuard()){
            return AsyncEvents.isInRegion(player, block);
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

    public static Boolean denyWorldSupport(Player player){
         World world = player.getWorld();
         if(FileCache.getConfig("expConfig").getBoolean("Anti-Abuse.PerWorldSupport.Whitelist.Enabled")) {
             if (FileCache.getConfig("expConfig").getStringList("Anti-Abuse.PerWorldSupport.Whitelist.List").contains(world.getName())) {
                 return true;
             }
         }
        return false;
    }

    public static Boolean cancelPreciousStones(){
        if (SettingsCache.isBooleansEmpty() || !SettingsCache.isInCache("PreciousStones")) {
            SettingsCache.cacheBoolean("PreciousStones", FileCache.getConfig("expConfig").getBoolean("Anti-Abuse.PreciousStones.Enabled"));
        }

        if (SettingsCache.getBoolean("PreciousStones")) {
            return true;
        }

        return false;
    }
    public static Boolean cancelResidence(){
        if (SettingsCache.isBooleansEmpty() || !SettingsCache.isInCache("Residence")) {
            SettingsCache.cacheBoolean("Residence", LevelPoints.getInstance().getConfig().getBoolean("Residence"));
        }
        if (SettingsCache.getBoolean("Residence")) {
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

    public static Boolean canEarnEXPGP(Player player, Location loc){
        DataStore dataStore = GriefPrevention.instance.dataStore;
        Claim claim = dataStore.getClaimAt(loc, true, null);
        if(claim != null){
            if(claim.getOwnerID().equals(player.getUniqueId())){
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
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
