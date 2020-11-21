package levelpoints.levelpoints;

import levelpoints.Containers.PlayerContainer;
import levelpoints.Utils.AsyncEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class SQL {
    private static HashMap<String, Object> sqlCache = new HashMap<>();
    private static Connection connection;
    public static Statement statment;
    public static void cacheSQL(){
        sqlCache.put("host", LevelPoints.getInstance().getConfig().getString("host"));
        sqlCache.put("port", LevelPoints.getInstance().getConfig().getInt("port"));
        sqlCache.put("username", LevelPoints.getInstance().getConfig().getString("username"));
        sqlCache.put("password", LevelPoints.getInstance().getConfig().getString("password"));
        sqlCache.put("database", LevelPoints.getInstance().getConfig().getString("database"));
        sqlCache.put("password", LevelPoints.getInstance().getConfig().getString("password"));
        sqlCache.put("table", LevelPoints.getInstance().getConfig().getString("table"));

    }

    public static Object getCacheData(String value){
        return sqlCache.get(value);
    }

    public static Connection getConnection() {
        return connection;
    }
    public static boolean playerExists(UUID uuid){
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + getCacheData("table") + " WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if(results.next()){
                return true;
            }
            System.out.println(ChatColor.DARK_RED + "Player Not Found");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void createPlayer(final UUID uuid, String name) {
        if (Bukkit.getPlayer(uuid) != null) {
            try {
                Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + getCacheData("table") + " WHERE UUID=?");
                statement.setString(1, uuid.toString());
                ResultSet results = statement.executeQuery();
                results.next();
                PlayerContainer container = AsyncEvents.getPlayerContainer(Bukkit.getPlayer(uuid));
                if (playerExists(uuid) != true) {
                    PreparedStatement insert = connection.prepareStatement("INSERT INTO " + getCacheData("table") + " (UUID,NAME,LEVEL,EXP,PRESTIGE,BOOSTERS) VALUE (?,?,?,?,?,?)");
                    insert.setString(1, uuid.toString());
                    insert.setString(2, name);
                    insert.setString(3, String.valueOf(container.getLevel()));
                    insert.setString(4, String.valueOf(container.getEXP()));
                    insert.setString(5, String.valueOf(container.getPrestige()));
                    insert.setString(6, AsyncEvents.getPlayerContainer(Bukkit.getPlayer(uuid)).getBoosterString());
                    insert.executeUpdate();

                    System.out.println(ChatColor.DARK_AQUA + "Player Added to Database");
                }else{
                    RunSQLDownload(Bukkit.getPlayer(uuid));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void RunSQLDownload(Player player){
        try {
            connection = getConnection();
            PlayerContainer container = AsyncEvents.getPlayerContainer(player);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + getCacheData("table") + " WHERE UUID=?");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet results = statement.executeQuery();
            results.next();
            if (statement != null) {
                container.setEXP(results.getDouble("EXP"));
                container.setPrestige(results.getInt("PRESTIGE"));
                container.setLevel(results.getInt("LEVEL"));
                container.setBoosters(results.getString("BOOSTERS"));
                File TopFile = new File(LevelPoints.getInstance().getDataFolder(), "TopList.yml");
                FileConfiguration TopConfig = YamlConfiguration.loadConfiguration(TopFile);
                TopConfig.set(player.getUniqueId() + ".Name", player.getName());
                TopConfig.set(player.getUniqueId() + ".Level", results.getInt("LEVEL"));
                try {
                    TopConfig.save(TopFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void RunSQLUpload(Player player){
        try {
            Connection connection = getConnection();
            PlayerContainer container = AsyncEvents.getPlayerContainer(player);
            PreparedStatement statement = connection.prepareStatement("UPDATE " + getCacheData("table") + " SET PRESTIGE=? WHERE UUID=?");
            statement.setString(1, String.valueOf(container.getPrestige()));
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();

            statement = getConnection().prepareStatement("UPDATE " + getCacheData("table") + " SET LEVEL=? WHERE UUID=?");
            statement.setString(1, String.valueOf(container.getLevel()));
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();

            statement = getConnection().prepareStatement("UPDATE " + getCacheData("table") + " SET EXP=? WHERE UUID=?");
            statement.setString(1, String.valueOf(container.getEXP()));
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
            statement = getConnection().prepareStatement("UPDATE " + getCacheData("table") + " SET BOOSTERS=? WHERE UUID=?");
            statement.setString(1, String.valueOf(container.getBoosterString()));
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void SQLReconnect() {
        String host = (String) getCacheData("host");
        Integer port = (Integer) getCacheData("port");
        String username = (String) getCacheData("username");
        String database = (String) getCacheData("database");
        String password = (String) getCacheData("password");
        String table = (String) getCacheData("table");
        try {
            setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" +port + "/" + database, username, password));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statment = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println(Formatting.basicColor("&bReconnecting to SQL"));
        SQLDisconnect(LevelPoints.getInstance().getConfig().getInt("SQLCloseTimer"));
    }
    public static void SQLDisconnect(int seconds){
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    connection.close();
                    //System.out.println(Formatting.basicColor("&4Disconnected from SQL &f- &cReconnect needs to take place"));

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                SQLReconnect();
            }
        }.runTaskLaterAsynchronously(LevelPoints.getInstance(), 300 * 20);
    }
    public static void loadSQL() {
        String host = (String) getCacheData("host");
        Integer port = (Integer) getCacheData("port");
        String username = (String) getCacheData("username");
        String database = (String) getCacheData("database");
        String password = (String) getCacheData("password");
        String table = (String) getCacheData("table");
        try {
            synchronized (LevelPoints.getInstance()) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    if (!getConnection().isClosed()) {
                        System.out.println(ChatColor.DARK_AQUA + "LevelPoints>> SQLDatabase already Connected :)");
                    }
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                LevelPoints.getInstance().getLogger().info("About to connect to database");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password));
                statment = connection.createStatement();
                statment.executeUpdate("CREATE TABLE IF NOT EXISTS `"+ table +"` (`UUID` varchar(200), `NAME` varchar(200), `LEVEL` INT(10), EXP DOUBLE(10,2), PRESTIGE INT(10), BOOSTERS TEXT(60000))");
                System.out.println(ChatColor.DARK_GREEN + "MySQL Connected");
                connection.close();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void setConnection(Connection value) {
        connection = value;
    }
}
