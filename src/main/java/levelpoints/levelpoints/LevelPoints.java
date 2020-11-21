package levelpoints.levelpoints;

import levelpoints.Containers.PlayerContainer;
import levelpoints.Utils.AsyncEvents;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class LevelPoints extends JavaPlugin {

    public static Plugin instance;
    private static File userFolder;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.instance = this;
        userFolder = new File(getDataFolder(), "Players");
        userFolder.mkdirs();

        AsyncEvents.RunPlugin();
        MetricsLite lite = new MetricsLite(this);
        SQL.cacheSQL();
        System.out.println(ChatColor.AQUA + "=============================");
        System.out.println(ChatColor.DARK_AQUA + "LevelPoints Plugin");
        System.out.println(ChatColor.DARK_AQUA + "Developer: Zoon20X");
        System.out.println(ChatColor.DARK_AQUA + "Version: " + this.getDescription().getVersion());
        System.out.println(ChatColor.DARK_AQUA + "MC-Compatible: 1.8-1.16.4");
        System.out.println(ChatColor.AQUA + "Enabled");
        System.out.println(ChatColor.AQUA + "=============================");
        if(getConfig().getBoolean("UseSQL")) {
            SQL.loadSQL();
        }
    }
    public static Plugin getInstance(){
        return instance;
    }


    public static File getUserFolder(){
        return userFolder;
    }
    public static String getCurrentDate(){
         SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
         java.util.Date cDate = new java.util.Date();
         String cDateS = format.format(cDate);
         return cDateS;
    }
    public static PlayerContainer getPlayer(Player player){
        return AsyncEvents.getPlayerContainer(player);
    }
    @Override
    public void onDisable() {

        AsyncEvents.MassSaveCache();

        // Plugin shutdown logic
    }
}
