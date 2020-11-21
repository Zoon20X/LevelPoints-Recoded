package levelpoints.Cache;

import levelpoints.levelpoints.Formatting;
import levelpoints.levelpoints.LevelPoints;
import org.bukkit.Bukkit;

public class ExternalCache {


    public static Boolean isRunningWildStacker() {
        Boolean isEnabled = LevelPoints.getInstance().getConfig().getBoolean("WildStacker");
        if (Bukkit.getPluginManager().getPlugin("WildStacker") != null) {

            Boolean hasPlugin = Bukkit.getPluginManager().getPlugin("WildStacker").isEnabled();
            if (isEnabled && !hasPlugin) {
                System.out.println(Formatting.basicColor("&c:( sorry but it seems you are not running WildStacker, please disable in config.yml or load the plugin"));
                return false;
            }
            if (isEnabled && hasPlugin) {
                return true;
            }

        }
        return false;
    }
    public static Boolean isRunningWorldGuard(){
        Boolean isEnabled = LevelPoints.getInstance().getConfig().getBoolean("WorldGuard");
        if(Bukkit.getPluginManager().getPlugin("WorldGuard") !=null) {

            Boolean hasPlugin = Bukkit.getPluginManager().getPlugin("WorldGuard").isEnabled();
            if (isEnabled && !hasPlugin) {
                System.out.println(Formatting.basicColor("&c:( sorry but it seems you are not running WorldGuard, please disable in config.yml or load the plugin"));
                return false;
            }
            if (isEnabled && hasPlugin) {
                return true;
            }
        }
        return false;
    }
    public static Boolean isRunningChatFormat(){
        Boolean isEnabled = LevelPoints.getInstance().getConfig().getBoolean("LPSFormat");

        return isEnabled;
    }
}
