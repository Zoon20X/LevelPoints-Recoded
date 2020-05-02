package levelpoints.utils.utils;



import levelpoints.Events.MainEvents;
import levelpoints.LevelPointsEvents.FarmEvent;
import levelpoints.LevelPointsEvents.LevelUpEvent;
import levelpoints.commands.MainCommand;
import levelpoints.levelpoints.LevelPoints;
import levelpoints.levelpoints.WildStacker;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;



import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.entity.Player;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;

public class UtilCollector implements Utils {


    private LevelPoints LPS = LevelPoints.getPlugin(LevelPoints.class);

    public File TopListFile = new File(LPS.getDataFolder(), "TopList.yml");

    public File WSFile = new File(LPS.getDataFolder(), "/OtherSettings/WildStacker.yml");
    public File FileChanceFile = new File(LPS.getDataFolder(), "/Settings/FileChance.yml");

    public FileConfiguration FileChanceConfig = YamlConfiguration.loadConfiguration(FileChanceFile);

    public FileConfiguration TopListConfig = YamlConfiguration.loadConfiguration(TopListFile);
    public FileConfiguration WSConfig = YamlConfiguration.loadConfiguration(WSFile);

    public NumberFormat formatter = new DecimalFormat("0.0");

    private HashMap<UUID, YamlConfiguration> usersConfig = new HashMap<>();
    private long daytime = (1000 * 60 * 60);
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
    private java.util.Date cDate = new java.util.Date();
    private String cDateS = format.format(cDate);
    private java.util.Date until = null;
    private Date current = null;
    public Connection connection;

    public String host, database, username, password;
    public String table = "playerData";
    public int port;
    public Statement statment;
    private String VersionJoin;
    private int frostsec;
    private int frostmin;
    private int frosthour;
    private int frostday;

    private int cooldowntimer;
    private static HashMap<Player, BossBar> bossbar = new HashMap<>();
    private static HashMap<Player, Integer> Multipliers = new HashMap<>();

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createBossbar(Player player) {
        BossBar boss =  Bukkit.createBossBar(API.format(getLangConfig().getString("lpBossBarTitle").replace("{lp_level}", String.valueOf(getCurrentLevel(player)))), BarColor.valueOf(LPS.getConfig().getString("BossBarColor")), BarStyle.SOLID);
        bossbar.put(player, boss);
        if(LPS.getConfig().getBoolean("ShowOnEXPOnly")){
            BossBarShowTime(player);
        }
    }

    @Override
    public void bossbarAddPlayer(BossBar bossBar, Player player) {
        bossBar.addPlayer(player);


    }

    @Override
    public void bossbarRemovePlayer(BossBar bossBar, Player player) {
        bossBar.removePlayer(player);
        bossbar.remove(player);
    }

    @Override
    public void BossBarShowTime(Player player) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(LPS, new Runnable() {
            @Override
            public void run() {
                bossbarRemovePlayer(getBossbar(player), player);

            }
        }, 20L * LPS.getConfig().getInt("ShowTime"));
    }

    @Override
    public BossBar getBossbar(Player player) {

        return bossbar.get(player);
    }

    @Override
    public void updateBossbar(BossBar bossBar, Player player) {
        if (bossBar == null) {
            createBossbar(player);
            getBossbar(player).addPlayer(player);
            updateBossbar(getBossbar(player), player);
        } else {
            int EXPCurrent = getCurrentEXP(player);
            int EXPRequired = getRequiredEXP(player);
            double required_progress = EXPRequired;
            double current_progress = EXPCurrent;
            double progress_percentage = current_progress / required_progress;
            bossBar.setProgress(progress_percentage);
            API api = new API(player, getLangConfig().getString("lpBossBarTitle"));
            bossBar.setTitle(api.formatTags());
        }
    }

    @Override
    public void ActionBar(Player player, String Message) {

        if (!Bukkit.getVersion().contains("1.8")) {

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Message));
        } else {
            String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

            try {
                Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
                Object p = c1.cast(player);
                Object ppoc;
                Class<?> c4 = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
                Class<?> c5 = Class.forName("net.minecraft.server." + version + ".Packet");

                Class<?> c2 = Class.forName("net.minecraft.server." + version + ".ChatComponentText");
                Class<?> c3 = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
                Object o = c2.getConstructor(new Class<?>[]{String.class}).newInstance(API.format(Message));
                ppoc = c4.getConstructor(new Class<?>[]{c3, byte.class}).newInstance(o, (byte) 2);
                Method getHandle = c1.getDeclaredMethod("getHandle");
                Object handle = getHandle.invoke(p);

                Field fieldConnection = handle.getClass().getDeclaredField("playerConnection");
                Object playerConnection = fieldConnection.get(handle);

                Method sendPacket = playerConnection.getClass().getDeclaredMethod("sendPacket", c5);
                sendPacket.invoke(playerConnection, ppoc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public String getProgressBar(Player player) {
        double  required_progress = getRequiredEXP(player);
        double current_progress = getCurrentEXP(player);
        double progress_percentage = current_progress / required_progress;
        StringBuilder sb = new StringBuilder();
        int bar_length = LPS.getConfig().getInt("ActionBarSize");
        String completed = API.format(getLangConfig().getString("lpBarDesignCompleted"));
        String need = API.format(getLangConfig().getString("lpBarDesignRequired"));
        for (int i = 0; i < bar_length; i++) {
            if (i < bar_length * progress_percentage) {
                sb.append(completed); //what to append if percentage is covered (e.g. GREEN '|'s)
            } else {
                sb.append(need); //what to append if percentage is not covered (e.g. GRAY '|'s)
            }
        }
        return sb.toString();
    }

    @Override
    public void PlayerAdd(UUID uuid, String Name) {
        String PlayerFolder = LPS.getDataFolder() + "/Players/";

        File userFile = new File(LPS.userFolder, uuid + ".yml");
        if (Bukkit.getPlayer(uuid) != null) {
            if (!userFile.exists()) {
                try {
                    userFile.createNewFile();
                    LPS.getServer().getConsoleSender().sendMessage(API.format("&bCreated File: &3" + uuid));
                } catch (IOException ex) {
                    LPS.getLogger().log(Level.SEVERE, ChatColor.DARK_RED + "Can't create " + Name + " user file");
                }
            }
            usersConfig.put(uuid, YamlConfiguration.loadConfiguration(userFile));
        }
    }

    @Override
    public void PlayerDataLoad(Player player) throws IOException {


        UUID UUID = player.getUniqueId();


        File userdata = new File(LPS.userFolder, UUID + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

        if (LPS.getConfig().getBoolean("ImportOldData")) {
            String OldFolder = "plugins/LP/Players/";
            File OldFile = new File(OldFolder, player.getUniqueId() + ".yml");
            if(OldFile.exists()){
                LPS.getServer().getConsoleSender().sendMessage(API.format("&bFound Player File"));
                if(!UsersConfig.contains("Name")) {
                    LPS.getServer().getConsoleSender().sendMessage(API.format("&3ImportOldData>> &bLoading Old data"));
                    FileConfiguration OldData = YamlConfiguration.loadConfiguration(OldFile);
                    int Level = OldData.getInt(player.getName() + ".level");
                    int EXP = OldData.getInt(player.getName() + ".EXP.Amount");
                    int Prestige = OldData.getInt(player.getName() + ".Prestige");

                    LPS.getServer().getConsoleSender().sendMessage(API.format("&3Importing Old Data within file"));

                    UsersConfig.set("Name", player.getName());

                    UsersConfig.set("Level", Level);
                    UsersConfig.set("EXP.Amount", EXP);
                    UsersConfig.set("Prestige", Prestige);
                    UsersConfig.set("ActionBar", true);
                    UsersConfig.set("ActiveBooster", 1);
                    UsersConfig.set("BoosterOff", cDateS);
                    UsersConfig.save(userdata);
                }else {
                    LPS.getServer().getConsoleSender().sendMessage(API.format("&3Loading Player Data"));
                    if (!UsersConfig.getString("Name").equals(player.getName())) {
                        LPS.getServer().getConsoleSender().sendMessage(API.format("&4Found a new username"));
                        UsersConfig.set("Name", player.getName());

                        UsersConfig.save(userdata);

                    }

                }
            }else if(!UsersConfig.contains("Name")) {

                LPS.getServer().getConsoleSender().sendMessage(API.format("&3Creating Player Data within file"));
                UsersConfig.set("Name", player.getName());

                UsersConfig.set("Level", getLevelsConfig().getInt("Level"));
                UsersConfig.set("EXP.Amount", getLevelsConfig().getInt("Exp"));
                UsersConfig.set("Prestige", 0);
                UsersConfig.set("ActionBar", true);
                UsersConfig.set("ActiveBooster", 1);
                UsersConfig.set("BoosterOff", cDateS);
                UsersConfig.save(userdata);

            } else {
                LPS.getServer().getConsoleSender().sendMessage(API.format("&3Loading Player Data"));
                if (!UsersConfig.getString("Name").equals(player.getName())) {
                    LPS.getServer().getConsoleSender().sendMessage(API.format("&4Found a new username"));
                    UsersConfig.set("Name", player.getName());

                    UsersConfig.save(userdata);

                }

            }
        } else if (!UsersConfig.contains("Name")) {

            LPS.getServer().getConsoleSender().sendMessage(API.format("&3Creating Player Data within file"));
            UsersConfig.set("Name", player.getName());

            UsersConfig.set("Level", getLevelsConfig().getInt("Level"));
            UsersConfig.set("EXP.Amount", getLevelsConfig().getInt("Exp"));
            UsersConfig.set("Prestige", 0);
            UsersConfig.set("ActionBar", true);
            UsersConfig.set("ActiveBooster", 1);
            UsersConfig.set("BoosterOff", cDateS);
            UsersConfig.save(userdata);

        } else {
            LPS.getServer().getConsoleSender().sendMessage(API.format("&3Loading Player Data"));
            if (!UsersConfig.getString("Name").equals(player.getName())) {
                LPS.getServer().getConsoleSender().sendMessage(API.format("&4Found a new username"));
                UsersConfig.set("Name", player.getName());

                UsersConfig.save(userdata);

            }


            LPS.getServer().getConsoleSender().sendMessage(API.format("&bLoaded Data Successfully"));
        }


    }

    @Override
    public void MySQL() {
        host = LPS.getConfig().getString("host");
        port = LPS.getConfig().getInt("port");
        username = LPS.getConfig().getString("username");
        database = LPS.getConfig().getString("database");
        password = LPS.getConfig().getString("password");
        table = "playerData";

        try {

            synchronized (LPS) {
                if (getConnection() != null && !getConnection().isClosed()) {
                    if (!getConnection().isClosed()) {
                        LPS.getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "LevelPoints>> SQLDatabase already Connected :)");
                    }
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                LPS.getLogger().info("About to connect to database");
                setConnection(DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password));
                statment = connection.createStatement();
                statment.executeUpdate("CREATE TABLE IF NOT EXISTS `playerData` (`UUID` varchar(200), `NAME` varchar(200), `LEVEL` INT(10), EXP INT(10), PRESTIGE INT(10))");
                LPS.getServer().getConsoleSender().sendMessage(ChatColor.DARK_GREEN + "MySQL Connected");
                LPS.getServer().getConsoleSender().sendMessage(connection.toString());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void RunSQLUpdate(Player player) {
        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        Connection connection = LPS.getConnection();

        try {
            if(connection.isClosed()){
                SQLReconnect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(LPS, new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = LPS.getConnection();
                    PreparedStatement statement = connection.prepareStatement("UPDATE " + table + " SET PRESTIGE=? WHERE UUID=?");
                    statement.setString(1, String.valueOf(UsersConfig.getInt("Prestige")));
                    statement.setString(2, player.getUniqueId().toString());
                    statement.executeUpdate();

                    statement = LPS.getConnection().prepareStatement("UPDATE " + table + " SET LEVEL=? WHERE UUID=?");
                    statement.setString(1, String.valueOf(UsersConfig.getInt("Level")));
                    statement.setString(2, player.getUniqueId().toString());
                    statement.executeUpdate();

                    statement = LPS.getConnection().prepareStatement("UPDATE " + table + " SET EXP=? WHERE UUID=?");
                    statement.setString(1, String.valueOf(UsersConfig.getInt("EXP.Amount")));
                    statement.setString(2, player.getUniqueId().toString());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    Connection connection = LPS.getConnection();
                    try {
                        if(connection.isClosed()){
                            RunSQLUpdate(player);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        }, (1 * 2));
    }


    @Override
    public void RunSQLDownload(Player player) {
        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        Connection connection = LPS.getConnection();
        try {
            if (connection.isClosed()) {
                SQLReconnect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().runTaskAsynchronously(LPS, new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connection = LPS.getConnection();
                    PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
                    statement.setString(1, player.getUniqueId().toString());
                    ResultSet results = statement.executeQuery();
                    results.next();
                    if (statement != null) {
                        UsersConfig.set("EXP.Amount", results.getInt("EXP"));

                        UsersConfig.save(userdata);
                    }

                    statement = LPS.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
                    statement.setString(1, player.getUniqueId().toString());
                    results = statement.executeQuery();
                    results.next();
                    if (statement != null) {
                        UsersConfig.set("Level", results.getInt("LEVEL"));
                        UsersConfig.save(userdata);
                    }
                    statement = LPS.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
                    statement.setString(1, player.getUniqueId().toString());
                    results = statement.executeQuery();
                    results.next();
                    if (statement != null) {
                        UsersConfig.set("Prestige", results.getInt("PRESTIGE"));
                        UsersConfig.save(userdata);
                    }

                } catch (SQLException e) {
                    Connection connection = LPS.getConnection();
                    try {
                        if (connection.isClosed()) {
                            RunSQLUpdate(player);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void SQLReconnect() {
        try {
            LPS.setConnection(DriverManager.getConnection("jdbc:mysql://" + LPS.host + ":" + LPS.port + "/" + LPS.database, LPS.username, LPS.password));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            LPS.statment = LPS.connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LPS.getServer().getConsoleSender().sendMessage(API.format("&bReconnecting to SQL"));
        SQLDisconnect(LPS.getConfig().getInt("SQLCloseTimer"));
    }


    @Override
    public void LevelUpEventTrigger(Player player, int level, boolean EXPOverlap, int EXPOverlapAmount) {
        LevelUpEvent levelupevent = new LevelUpEvent(player, level, EXPOverlap, EXPOverlapAmount); // Initialize your Event
        Bukkit.getPluginManager().callEvent(levelupevent); // This fires the event and allows any listener to listen to the event

    }

    @Override
    public void FarmEventTrigger(Player player, String FarmedItem, int expAmount, String Task) {
        FarmEvent farmEvent = new FarmEvent(player, FarmedItem, expAmount, Task); // Initialize your Event
        Bukkit.getPluginManager().callEvent(farmEvent); // This fires the event and allows any listener to listen to the event
    }

    @Override
    public void RunFiles() {
        try {

            File TopListFile = new File(LPS.getDataFolder(), "TopList.yml");
            File WSFile = new File(LPS.getDataFolder(), "/OtherSettings/WildStacker.yml");
            getLevelsConfig();
            getRewardsConfig();
            getEXPConfig();
            getLangConfig();

            TopListConfig = YamlConfiguration.loadConfiguration(TopListFile);

            WSConfig = YamlConfiguration.loadConfiguration(WSFile);


            LPS.getServer().getConsoleSender().sendMessage(API.format("&3Loaded Modules>> &bAll Files Loaded"));

            LPS.getServer().getConsoleSender().sendMessage(API.format("&3Running Extra File Check...."));
            if (getRewardsConfig().getString("Type").equals("FILECHANCE")) {

                File FileChanceFile = new File(LPS.getDataFolder(), "/Settings/FileChance.yml");
                FileChanceConfig = YamlConfiguration.loadConfiguration(FileChanceFile);
            } else {
                LPS.getServer().getConsoleSender().sendMessage(API.format("&3Not running FileChance Rewards"));
            }
            if (LPS.getConfig().getBoolean("LPSFormat")) {
                getFormatsConfig();
            } else {
                LPS.getServer().getConsoleSender().sendMessage(API.format("&3Not running LevelPoints built in Chat system"));
            }
            if (LPS.getConfig().getBoolean("RankMultipliers")) {
                getMultipliersConfig();
            } else {
                LPS.getServer().getConsoleSender().sendMessage(API.format("&3Not running Rank Multipliers"));
            }
        } catch (Exception e) {
            LPS.getServer().getConsoleSender().sendMessage(API.format("&4All & or some files failed to load, please consult the developer: &cZoon20X"));
        }
    }

    @Override
    public void RunModuels() {
        File EXPFile = new File(LPS.getDataFolder(), "/Settings/EXP.yml");
        FileConfiguration EXPConfig = YamlConfiguration.loadConfiguration(EXPFile);
        File LevelFile = new File(LPS.getDataFolder(), "/Settings/Levels.yml");
        FileConfiguration LevelConfig = YamlConfiguration.loadConfiguration(LevelFile);
        File RewardsFile = new File(LPS.getDataFolder(), "/Settings/Rewards.yml");
        FileConfiguration RewardsConfig = YamlConfiguration.loadConfiguration(RewardsFile);
        File LangFile = new File(LPS.getDataFolder(), "/Lang.yml");
        FileConfiguration LangConfig = YamlConfiguration.loadConfiguration(LangFile);

        usersConfig.clear();
        LPS.saveDefaultConfig();
        LPS.getServer().getPluginManager().registerEvents(new MainEvents(LPS), LPS);
        LPS.getServer().getConsoleSender().sendMessage(API.format("&3Loaded Modules>>> &bEvents"));

        LPS.getCommand("levelpoints").setExecutor((CommandExecutor) new MainCommand(LPS));
        LPS.getServer().getConsoleSender().sendMessage(API.format("&3Loaded Modules>>> &bCommands"));


        LPS.getServer().getConsoleSender().sendMessage(API.format("&3Running Files Check...."));

        SaveLoadFiles(TopListFile, TopListConfig, "TopList.yml", "TopList.yml", "TopList");
        SaveLoadFiles(EXPFile, EXPConfig, "/Settings/EXP.yml", "Settings/EXP.yml", "EXP");
        SaveLoadFiles(LevelFile, LevelConfig, "/Settings/Levels.yml", "Settings/Levels.yml", "Levels");
        SaveLoadFiles(RewardsFile, RewardsConfig, "/Settings/Rewards.yml", "Settings/Rewards.yml", "Rewards");
        SaveLoadFiles(WSFile, WSConfig, "/OtherSettings/WildStacker.yml", "OtherSettings/WildStacker.yml", "WildStacker");

        SaveLoadFiles(LangFile, LangConfig, "Lang.yml", "Lang.yml", "Lang");

        if (getRewardsConfig().getString("Type").equals("FILECHANCE")) {

            SaveLoadFiles(FileChanceFile, FileChanceConfig, "/Settings/FileChance.yml", "Settings/FileChance.yml", "FileChance");

        } else {
            LPS.getServer().getConsoleSender().sendMessage(API.format("&3Not running FileChance Rewards"));
        }
        if (LPS.getConfig().getBoolean("LPSFormat")) {
            File FormatsFile = new File(LPS.getDataFolder(), "/Settings/Formats.yml");
            FileConfiguration FormatsConfig = YamlConfiguration.loadConfiguration(FormatsFile);
            SaveLoadFiles(FormatsFile, FormatsConfig, "/Settings/Formats.yml", "Settings/Formats.yml", "Formats");

        } else {
            LPS.getServer().getConsoleSender().sendMessage(API.format("&3Not running LevelPoints built in Chat system"));
        }
        if (LPS.getConfig().getBoolean("RankMultipliers")) {
            File MultiplierFile = new File(LPS.getDataFolder(), "/Settings/RankMultipliers.yml");
            FileConfiguration MultiplierConfig = YamlConfiguration.loadConfiguration(MultiplierFile);
            SaveLoadFiles(MultiplierFile, MultiplierConfig, "/Settings/RankMultipliers.yml", "Settings/RankMultipliers.yml", "RankMultipliers");

        } else {
            LPS.getServer().getConsoleSender().sendMessage(API.format("&3Not running Rank Multipliers"));
        }
        RunFiles();

        LPS.getServer().getConsoleSender().sendMessage(API.format("&3Running VersionChecker Module...."));
        versionChecker();
        LPS.worldGuardPlugin = LPS.getWorldGuard();
        if (LPS.getConfig().getBoolean("WildStacker")) {
            LPS.getServer().getPluginManager().registerEvents(new WildStacker(this), LPS);
        }
        TimedEXP();

    }

    @Override
    public void SaveLoadFiles(File file, FileConfiguration config, String Location, String secLoc, String Name) {
        if (file == null) {
            file = new File(LPS.getDataFolder() + Location);
            config = YamlConfiguration.loadConfiguration(file);
            LPS.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "LevelPoints>> Loading Module File " + Name + ".yml");
        }

        if (!file.exists()) {
            LPS.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "LevelPoints>> Creating Module File " + Name + ".yml");
            LPS.saveResource(secLoc, false);
        } else {
            return;
        }
    }
    @Override
    public void SQLDisconnect(int seconds) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(LPS, new Runnable() {
            public void run() {
                try {
                    LPS.connection.close();
                    LPS.getServer().getConsoleSender().sendMessage(API.format("&4Disconnected from SQL &f- &cReconnect needs to take place"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }, (seconds * 20));

    }

    @Override
    public void wait(int seconds, Player player) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(LPS, new Runnable() {
            public void run() {
                RunSQLDownload(player);
            }
        }, (seconds * 10));
    }

    @Override
    public void versionChecker() {
        if (LPS.getConfig().getBoolean("CheckUpdates")) {
            LPS.getServer().getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "Checking for Updates...");
            String key = "key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=";
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=63996").openConnection();
                String version = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
                connection.disconnect();

                if (LPS.getDescription().getVersion().equalsIgnoreCase(version)) {
                    LPS.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "No New Updates for LevelPoints");
                    VersionJoin = version;
                    return;
                } else {
                    LPS.getServer().getConsoleSender().sendMessage(ChatColor.RED + "You do not have the most updated version of LevelPoints");
                    LPS.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Download it from: https://www.spigotmc.org/resources/levelpoints-1-8-1-15-mysql.63996/");
                    VersionJoin = version;
                }
            } catch (IOException e) {
                LPS.getServer().getConsoleSender().sendMessage(ChatColor.RED + "Couldnt connect");
                e.printStackTrace();
            }
        } else {
            LPS.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + "You Are Not Checking For LevelPoints Updates");
            return;
        }

    }

    @Override
    public void Title(Player player, String Title, String Subtitle) {
        if (!Bukkit.getVersion().contains("1.8")) {
            player.sendTitle(Title, Subtitle, 10, 20, 10);
        }
    }

    @Override
    public void Rewards(Player player, int Level, int Prestige) {

        if (getRewardsConfig().getString("Type").equalsIgnoreCase("REGULAR")) {
            List<String> cmds = getRewardsConfig().getStringList(API.format("Rewards.Prestige-" + Prestige + ".Level-" + Level));
            for (String command : cmds) {
                getRewards(command, player);
            }
        }
        if (getRewardsConfig().getString("Type").equalsIgnoreCase("ONE")) {
            List<String> cmds = getRewardsConfig().getStringList("Rewards." + "CMD");
            for (String command : cmds) {
                getRewards(command, player);
            }
        }
        if (getRewardsConfig().getString("Type").equals("CHANCE")) {
            int max = getRewardsConfig().getInt("Amount");
            int min = 1;

            Random r = new Random();
            int re = r.nextInt((max - min) + 1) + min;
            List<String> cmds = getRewardsConfig().getStringList("Rewards.Prestige-" + Prestige + ".Level-" + (Level) + "." + String.valueOf(re));
            for (String command : cmds) {
                getRewards(command, player);
            }
        }
        if (getRewardsConfig().getString("Type").equals("FILECHANCE")) {
            List<String> levelrequired = getRewardsConfig().getStringList("Rewards.LevelChance");
            for (String levelss : levelrequired) {

                if (levelss.equals(String.valueOf(Level))) {
                    List<String> RewardType = FileChanceConfig.getStringList("RewardsType");

                    int max = RewardType.size() - 1;
                    int min = 0;

                    Random r = new Random();
                    int re = r.nextInt((max - min) + 1) + min;
                    String ReType = RewardType.get(re);
                    List<String> Cmds = FileChanceConfig.getStringList("Rewards." + ReType);
                    for (String command : Cmds) {
                        getRewards(command, player);
                    }
                }
            }
        }
    }

    @Override
    public void getRewards(String cmd, Player player) {

        if (getRewardsConfig().getString("RewardsMethod").equals("NONE")) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
        } else if (getRewardsConfig().getString("RewardsMethod").equals("MESSAGE")) {
            player.sendMessage(API.format(getLangConfig().getString("lpRewardMessage")));
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
        } else if (getRewardsConfig().getString("RewardsMethod").equals("TITLE")) {
            Title(player, API.format(getLangConfig().getString(API.format("lpRewardTitleTop"))), API.format(getLangConfig().getString(API.format("lpRewardTitleBottom")).replace("{lp_level}", String.valueOf(getCurrentLevel(player)))));
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
        }
    }


    @Override
    public void GainEXP(Player player, int amount) {
        int EXPCurrent = getCurrentEXP(player);
        int EXPRequired = getRequiredEXP(player);
        int CurrentLevel = getCurrentLevel(player);


        int MaxLevel = getMaxLevel();
        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        int BoosterActive = UsersConfig.getInt("ActiveBooster");

        cooldowntimer = LPS.getConfig().getInt("BoostersTime");

        Date cDate = new Date();
        String cDateS = format.format(cDate);
        try {
            until = format.parse(UsersConfig.getString("BoosterOff"));
            current = format.parse(cDateS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(LPS.getConfig().getBoolean("RankMultipliers")){
            int multi = getRankMultiplier(player);

            BoosterActive = BoosterActive + multi;

        }

        if (!current.after(until)) {

            amount = amount * BoosterActive;

        }else if(LPS.getConfig().getBoolean("RankMultipliers")){
            amount = amount * getRankMultiplier(player);
        }
        int newEXP = EXPCurrent + amount;
        if (amount != 0 || newEXP >= EXPRequired) {

            if (CurrentLevel != MaxLevel) {


                if (newEXP == EXPRequired) {

                    UsersConfig.set("EXP.Amount", 0);
                    UsersConfig.set("Level", CurrentLevel + 1);
                    try {
                        UsersConfig.save(userdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LevelUpEventTrigger(player.getPlayer(), CurrentLevel + 1, false, 0);

                } else if (newEXP > EXPRequired) {



                    int newEXPS = newEXP;
                    int RequiredEXP = EXPRequired;
                    int OverLap = newEXPS - RequiredEXP;
                    UsersConfig.set("EXP.Amount", OverLap);
                    UsersConfig.set("Level", CurrentLevel + 1);
                    try {
                        UsersConfig.save(userdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LevelUpEventTrigger(player.getPlayer(), CurrentLevel + 1, true, OverLap);
                } else {


                    int newEXPS = EXPCurrent + amount;
                    UsersConfig.set("EXP.Amount", newEXP);

                    try {
                        UsersConfig.save(userdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (UsersConfig.getBoolean("ActionBar")) {
                        if (LPS.getConfig().getBoolean("Actionbar")) {
                            API api = new API(player, getLangConfig().getString("lpActionBar"));
                            ActionBar(player, api.formatTags());
                        }
                    }
                    if(LPS.getConfig().getBoolean("BossBar")) {
                        updateBossbar(getBossbar(player), player);
                    }
                }
            } else {
                if (newEXP > EXPRequired) {
                    UsersConfig.set("EXP.Amount", EXPRequired);
                    try {
                        UsersConfig.save(userdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }else{



                    int newEXPS = EXPCurrent + amount;
                    UsersConfig.set("EXP.Amount", newEXPS);

                    try {
                        UsersConfig.save(userdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (UsersConfig.getBoolean("ActionBar")) {
                        if (LPS.getConfig().getBoolean("Actionbar")) {
                            API api = new API(player, getLangConfig().getString("lpActionBar"));
                            ActionBar(player, api.formatTags());
                        }
                    }
                    if(LPS.getConfig().getBoolean("BossBar")) {
                        updateBossbar(getBossbar(player), player);
                    }
                }
            }
        }
    }

    @Override
    public void boosteruseclick(Player player, int multiplier) throws IOException {

        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        cooldowntimer = LPS.getConfig().getInt("BoostersTime");
        Date cDate = new Date();
        cDateS = format.format(cDate);
        try {
            until = format.parse(UsersConfig.getString("BoosterOff"));
            current = format.parse(cDateS);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!current.after(until)) {
            player.closeInventory();

            long diff = until.getTime() - current.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (daytime);
            String timeleft = API.format(getLangConfig().getString("lpsTimeFormat").replace("{DAYS}", String.valueOf(diffDays)).replace("{HOURS}", String.valueOf(diffHours)).replace("{MINUTES}", String.valueOf(diffMinutes)).replace("{SECONDS}", String.valueOf(diffSeconds)));
            player.sendMessage(API.format(getLangConfig().getString("lpsBoosterCooldown").replace("{TIME_FORMAT}", timeleft)));
        } else {

            if (UsersConfig.getInt("Boosters." + multiplier) >= 1) {
                String cDateS = format.format(cDate);
                try {
                    until = format.parse(UsersConfig.getString("BoosterOff"));
                    current = format.parse(cDateS);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                cooldowntimer = LPS.getConfig().getInt("BoostersTime");
                Date tomorrow = new Date(cDate.getTime() + (daytime * cooldowntimer));

                String tc = format.format(tomorrow);
                int numm = UsersConfig.getInt("Boosters." + multiplier);
                UsersConfig.set("Boosters." + multiplier, numm - 1);
                UsersConfig.set("ActiveBooster", multiplier);
                UsersConfig.set("BoosterOff", tc);
                player.closeInventory();

                UsersConfig.save(userdata);
                player.sendMessage(ChatColor.GREEN + "Booster Has Been Activated");
            } else {
                player.closeInventory();
                player.sendMessage(ChatColor.RED + "You Need Atleast 1 " + multiplier + "X booster to use it");
            }
        }
    }

    @Override
    public int getRequiredEXP(Player player) {
        int needep;
        int MaxLevel = getMaxLevel();
        int CurrentLevel = getCurrentLevel(player);
        int EXPR = getLevelsConfig().getInt("LevelingEXP");

        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);


        if (getLevelsConfig().getBoolean("PrestigeLeveling")) {
            needep = getLevelsConfig().getInt("Prestige-" + UsersConfig.getInt("Prestige") + ".Level-" + CurrentLevel);
        } else if (getLevelsConfig().getBoolean("CustomLeveling")) {
            needep = getLevelsConfig().getInt("Level-" + CurrentLevel);
        } else {
            needep = CurrentLevel * EXPR;
        }


        return needep;
    }

    @Override
    public int getCurrentEXP(Player player) {
        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        int EXP = UsersConfig.getInt("EXP.Amount");

        return EXP;
    }

    @Override
    public int getCurrentBoosters(Player player, int Multipler) {
        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        int Current = UsersConfig.getInt("Boosters." + Multipler);
        return Current;
    }

    @Override
    public int getMaxLevel() {
        int Max = getLevelsConfig().getInt("MaxLevel");

        return Max;
    }
    @Override
    public int getRankMultiplier(Player player) {
        ConfigurationSection cs = getMultipliersConfig().getConfigurationSection("");
        Multipliers.remove(player);
        for(String x : cs.getKeys(false)) {

            boolean perm =player.hasPermission(getMultipliersConfig().getString(x + ".Permission"));
            if(perm) {

                if (Multipliers.isEmpty() || !Multipliers.containsKey(player)) {
                    Multipliers.put(player, getMultipliersConfig().getInt(x + ".Multiplier"));
                }else{
                    if(Multipliers.get(player) < getMultipliersConfig().getInt(x + ".Multiplier")){

                        Multipliers.put(player, getMultipliersConfig().getInt(x + ".Multiplier"));
                    }
                }
            }

        }
        return Multipliers.get(player);
    }

    @Override
    public double getMaxLevelEXP(Player player) {


        double MaxEXP;
        int CurrentLevel = getCurrentLevel(player);
        double EXPR = getLevelsConfig().getDouble("LevelingEXP");
        int MaxLevel = getMaxLevel();
        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

        if (getLevelsConfig().getBoolean("PrestigeLeveling")) {
            MaxEXP = getLevelsConfig().getInt("Prestige-" + UsersConfig.getDouble("Prestige") + ".Level-" + MaxLevel);
        } else if (getLevelsConfig().getBoolean("CustomLeveling")) {
            MaxEXP = getLevelsConfig().getDouble("Level-" + MaxLevel);
        } else {
            MaxEXP = MaxLevel * EXPR;
        }


        return MaxEXP;
    }

    @Override
    public FileConfiguration getLevelsConfig() {
        File LevelFile = new File(LPS.getDataFolder(), "/Settings/Levels.yml");
        FileConfiguration LevelConfig = YamlConfiguration.loadConfiguration(LevelFile);
        return LevelConfig;
    }

    @Override
    public FileConfiguration getMultipliersConfig() {
        File MultiplierFile = new File(LPS.getDataFolder(), "/Settings/RankMultipliers.yml");
        FileConfiguration MultiplierConfig = YamlConfiguration.loadConfiguration(MultiplierFile);
        return MultiplierConfig;
    }


    @Override
    public FileConfiguration getTopListConfig() {
        File TopFile = new File(LPS.getDataFolder(), "TopList.yml");
        FileConfiguration TopConfig = YamlConfiguration.loadConfiguration(TopFile);
        return TopConfig;
    }
    @Override
    public FileConfiguration getEXPConfig() {

        File EXPFile = new File(LPS.getDataFolder(), "/Settings/EXP.yml");
        FileConfiguration EXPConfig = YamlConfiguration.loadConfiguration(EXPFile);

        return EXPConfig;
    }

    @Override
    public FileConfiguration getRewardsConfig() {

        File RewardsFile = new File(LPS.getDataFolder(), "/Settings/Rewards.yml");
        FileConfiguration RewardsConfig = YamlConfiguration.loadConfiguration(RewardsFile);

        return RewardsConfig;
    }

    @Override
    public FileConfiguration getFormatsConfig() {
        File FormatsFile = new File(LPS.getDataFolder(), "/Settings/Formats.yml");
        FileConfiguration FormatsConfig = YamlConfiguration.loadConfiguration(FormatsFile);
        return FormatsConfig;
    }

    @Override
    public FileConfiguration getLangConfig() {
        File LangFile = new File(LPS.getDataFolder(), "/Lang.yml");
        FileConfiguration LangConfig = YamlConfiguration.loadConfiguration(LangFile);
        return LangConfig;
    }

    @Override
    public int getCurrentLevel(Player player) {
        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        int CL = UsersConfig.getInt("Level");

        return CL;
    }
    @Override
    public int getCurrentPrestige(Player player) {
        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        int CL = UsersConfig.getInt("Prestige");

        return CL;
    }
    @Override
    public void TimedEXP() {

        if (getEXPConfig().getBoolean("TimedEXP")) {

            Bukkit.getScheduler().scheduleSyncRepeatingTask(LPS, new Runnable() {
                public void run() {
                    if (Bukkit.getServer().getOnlinePlayers().size() >= 1) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            GainEXP(p, getEXPConfig().getInt("GiveAmount"));
                            double seconds = getEXPConfig().getInt("GiveEXP");
                            double minutes = 0;
                            double hours = 0;
                            if (seconds >= 60) {
                                minutes = seconds / 60;
                                seconds = seconds - (minutes * 60);
                            }
                            if (minutes >= 60) {
                                hours = minutes / 60;
                                minutes = minutes - (hours * 60);
                            }
                            String TimeMessage;
                            if (hours >= 1) {
                                TimeMessage = hours + " Hours";
                            } else if (minutes >= 1) {
                                TimeMessage = (minutes + " Minute(s)");
                            } else {
                                TimeMessage = seconds + " Seconds";
                            }


                            if (getEXPConfig().getBoolean("EXPMessage")) {
                                p.sendMessage(API.format(getLangConfig().getString("lpTimedReward").replace("{lp_Timed_EXP}", Integer.toString(getEXPConfig().getInt("GiveAmount"))).replace("{lp_Timed_Delay}", TimeMessage)));

                            }
                        }
                    }
                }
            }, 0L, getEXPConfig().getInt("GiveEXP") * 20L);
        }
    }
}