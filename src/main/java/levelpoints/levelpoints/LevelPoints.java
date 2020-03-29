package levelpoints.levelpoints;


import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import levelpoints.utils.utils.API;
import levelpoints.utils.utils.UtilCollector;
import lpsapi.lpsapi.LPSAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class LevelPoints extends JavaPlugin {
    private LPSAPI lpapi = (LPSAPI) Bukkit.getPluginManager().getPlugin("LPSAPI");
    public UtilCollector uc;
    public File userFolder;

    public WorldGuardPlugin worldGuardPlugin;
    public Connection connection;

    public String host, database, username, password;
    public String table = "playerData";
    public int port;
    public Statement statment;
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic


        uc = new UtilCollector();
        userFolder = new File(getDataFolder(), "Players");
        userFolder.mkdirs();
        uc.RunModuels();
        MetricsLite metrics = new MetricsLite(this);
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "=============================");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "LevelPoints Plugin");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Developer: Zoon20X");
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Version: " + this.getDescription().getVersion());
        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "MC-Compatible: 1.8-1.15.2");
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "Enabled");
        getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "=============================");


        if (getConfig().getBoolean("UseSQL")) {
            MySQL();
            getServer().getConsoleSender().sendMessage(API.format("&3Loaded Modules>>> &bMySql"));
            getServer().getPluginManager().registerEvents(new mySQL(this), this);
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }


    }
    public void MySQL() {
        host = getConfig().getString("host");
        port = getConfig().getInt("port");
        username = getConfig().getString("username");
        database = getConfig().getString("database");
        password = getConfig().getString("password");
        table = "playerData";

        try {

            synchronized (this) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    if (!getConnection().isClosed()) {
                        getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "LevelPoints>> SQLDatabase already Connected :)");
                    }
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                getLogger().info("About to connect to database");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
                statment = connection.createStatement();
                statment.executeUpdate("CREATE TABLE IF NOT EXISTS `playerData` (`UUID` varchar(200), `NAME` varchar(200), `LEVEL` INT(10), EXP INT(10), PRESTIGE INT(10))");
                getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "MySQL Connected");
                connection.close();
                getServer().getConsoleSender().sendMessage(String.valueOf(connection.isClosed()));
                getServer().getConsoleSender().sendMessage(connection.toString());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public WorldGuardPlugin getWorldGuard(){
     Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");
     if(plugin == null || !(plugin instanceof WorldGuardPlugin)){
         getServer().getConsoleSender().sendMessage(API.format("&3Not running Worldguard"));
         return null;
     }
     return (WorldGuardPlugin) plugin;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
