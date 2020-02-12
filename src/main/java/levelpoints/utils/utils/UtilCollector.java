package levelpoints.utils.utils;


import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import levelpoints.Events.MainEvents;
import levelpoints.LevelPointsEvents.FarmEvent;
import levelpoints.LevelPointsEvents.LevelUpEvent;
import levelpoints.commands.MainCommand;
import levelpoints.levelpoints.LevelPoints;
import levelpoints.levelpoints.WildStacker;
import levelpoints.levelpoints.mySQL;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtilCollector implements Utils {


    private LevelPoints LPS = LevelPoints.getPlugin(LevelPoints.class);

    public File LangFile = new File(LPS.getDataFolder(), "Lang.yml");
    public File TopListFile = new File(LPS.getDataFolder(), "TopList.yml");
    public File EXPFile = new File(LPS.getDataFolder(), "/Settings/EXP.yml");
    public File LevelFile = new File(LPS.getDataFolder(), "/Settings/Levels.yml");
    public File RewardsFile = new File(LPS.getDataFolder(), "/Settings/Rewards.yml");
    public File WSFile = new File(LPS.getDataFolder(), "/OtherSettings/WildStacker.yml");
    public File FileChanceFile = new File(LPS.getDataFolder(), "/Settings/FileChance.yml");
    public File FormatsFile = new File(LPS.getDataFolder(), "/Settings/Formats.yml");
    public FileConfiguration LangConfig = YamlConfiguration.loadConfiguration(LangFile);
    public FileConfiguration FileChanceConfig = YamlConfiguration.loadConfiguration(FileChanceFile);
    public FileConfiguration FormatsConfig = YamlConfiguration.loadConfiguration(FormatsFile);
    public FileConfiguration TopListConfig = YamlConfiguration.loadConfiguration(TopListFile);


    public File WSfile = new File(LPS.getDataFolder(), "/OtherSettings/WildStacker.yml");
    public FileConfiguration WSConfig = YamlConfiguration.loadConfiguration(WSfile);
    public File EXPfile = new File(LPS.getDataFolder(), "/Settings/EXP.yml");
    public FileConfiguration EXPConfig = YamlConfiguration.loadConfiguration(EXPfile);
    public File Levelfile = new File(LPS.getDataFolder(), "/Settings/Levels.yml");
    public FileConfiguration LevelConfig = YamlConfiguration.loadConfiguration(Levelfile);
    public File Rewardsfile = new File(LPS.getDataFolder(), "/Settings/Rewards.yml");
    public FileConfiguration RewardsConfig = YamlConfiguration.loadConfiguration(Rewardsfile);
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

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void ActionBar(Player player, String Message) {


        if (!Bukkit.getVersion().contains("1.8")) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Message));
        }

    }

    @Override
    public void PlayerAdd(UUID uuid, String Name) {
        String PlayerFolder = LPS.getDataFolder() + "/Players/";

        File userFile = new File(LPS.userFolder, uuid + ".yml");
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

    @Override
    public void PlayerDataLoad(Player player) throws IOException {


        UUID UUID = player.getUniqueId();


        File userdata = new File(LPS.userFolder, UUID + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

        if (!UsersConfig.contains("Name")) {

            LPS.getServer().getConsoleSender().sendMessage(API.format("&3Creating Player Data within file"));
            UsersConfig.set("Name", player.getName());

            UsersConfig.set("Level", LevelConfig.getInt("Level"));
            UsersConfig.set("EXP.Amount", LevelConfig.getInt("Exp"));
            UsersConfig.set("Prestige", 0);
            UsersConfig.set("ActionBar", true);
            UsersConfig.set("ActiveBooster", 1);
            UsersConfig.set("BoosterOff", cDateS);
            UsersConfig.save(userdata);

        } else {
            LPS.getServer().getConsoleSender().sendMessage(API.format("&3Loading Player Data"));

            if (LPS.getConfig().getBoolean("UseSQL")) {

            }
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
        Bukkit.getScheduler().runTaskAsynchronously(LPS, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = LPS.getConnection().prepareStatement("UPDATE " + table + " SET PRESTIGE=? WHERE UUID=?");
                    statement.setString(1, String.valueOf(UsersConfig.getInt("Prestige")));
                    statement.setString(2, player.getUniqueId().toString());
                    statement.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    PreparedStatement statement = LPS.getConnection().prepareStatement("UPDATE " + table + " SET LEVEL=? WHERE UUID=?");
                    statement.setString(1, String.valueOf(UsersConfig.getInt("Level")));
                    statement.setString(2, player.getUniqueId().toString());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    PreparedStatement statement = LPS.getConnection().prepareStatement("UPDATE " + table + " SET EXP=? WHERE UUID=?");
                    statement.setString(1, String.valueOf(UsersConfig.getInt("EXP.Amount")));
                    statement.setString(2, player.getUniqueId().toString());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    LPS.getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    LPS.getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void RunSQLDownload(Player player) {
        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        Bukkit.getScheduler().runTaskAsynchronously(LPS, new Runnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement statement = LPS.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
                    statement.setString(1, player.getUniqueId().toString());
                    ResultSet results = statement.executeQuery();
                    results.next();
                    if (statement != null) {
                        UsersConfig.set("EXP.Amount", results.getInt("EXP"));

                        UsersConfig.save(userdata);
                    }

                } catch (SQLException e) {

                    e.printStackTrace();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    PreparedStatement statement = LPS.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
                    statement.setString(1, player.getUniqueId().toString());
                    ResultSet results = statement.executeQuery();
                    results.next();
                    if (statement != null) {
                        UsersConfig.set("Level", results.getInt("LEVEL"));
                        UsersConfig.save(userdata);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    PreparedStatement statement = LPS.getConnection().prepareStatement("SELECT * FROM " + table + " WHERE UUID=?");
                    statement.setString(1, player.getUniqueId().toString());
                    ResultSet results = statement.executeQuery();
                    results.next();
                    if (statement != null) {
                        UsersConfig.set("Prestige", results.getInt("PRESTIGE"));
                        UsersConfig.save(userdata);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    LPS.getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    LPS.getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });
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


            SaveLoadFiles(TopListFile, TopListConfig, "TopList.yml", "TopList.yml", "TopList");
            SaveLoadFiles(EXPFile, EXPConfig, "/Settings/EXP.yml", "Settings/EXP.yml", "EXP");
            SaveLoadFiles(LevelFile, LevelConfig, "/Settings/Levels.yml", "Settings/Levels.yml", "Levels");
            SaveLoadFiles(RewardsFile, RewardsConfig, "/Settings/Rewards.yml", "Settings/Rewards.yml", "Rewards");
            SaveLoadFiles(WSFile, WSConfig, "/OtherSettings/WildStacker.yml", "OtherSettings/WildStacker.yml", "WildStacker");

            SaveLoadFiles(LangFile, LangConfig, "Lang.yml", "Lang.yml", "Lang");
            LangFile = new File(LPS.getDataFolder(), "Lang.yml");

            TopListFile = new File(LPS.getDataFolder(), "TopList.yml");
            EXPFile = new File(LPS.getDataFolder(), "/Settings/EXP.yml");
            WSFile = new File(LPS.getDataFolder(), "/OtherSettings/WildStacker.yml");
            RewardsFile = new File(LPS.getDataFolder(), "/Settings/Rewards.yml");
            LevelFile = new File(LPS.getDataFolder(), "/Settings/Levels.yml");

            FormatsConfig = YamlConfiguration.loadConfiguration(FormatsFile);
            FileChanceConfig = YamlConfiguration.loadConfiguration(FileChanceFile);

            LangConfig = YamlConfiguration.loadConfiguration(LangFile);
            EXPConfig = YamlConfiguration.loadConfiguration(EXPFile);
            LevelConfig = YamlConfiguration.loadConfiguration(LevelFile);
            RewardsConfig = YamlConfiguration.loadConfiguration(RewardsFile);
            WSConfig = YamlConfiguration.loadConfiguration(WSFile);
        } catch (Exception e) {
            LPS.getServer().getConsoleSender().sendMessage(API.format("&4All & or some files failed to load, please consult the developer: &cZoon20X"));
        }
        LPS.getServer().getConsoleSender().sendMessage(API.format("&3Loaded Modules>> &bAll Files Loaded"));

        LPS.getServer().getConsoleSender().sendMessage(API.format("&3Running Extra File Check...."));
        if (RewardsConfig.getString("Type").equals("FILECHANCE")) {

            SaveLoadFiles(FileChanceFile, FileChanceConfig, "/Settings/FileChance.yml", "Settings/FileChance.yml", "FileChance");
            FileChanceFile = new File(LPS.getDataFolder(), "/Settings/FileChance.yml");
            FileChanceConfig = YamlConfiguration.loadConfiguration(FileChanceFile);
        } else {
            LPS.getServer().getConsoleSender().sendMessage(API.format("&3Not running FileChance Rewards"));
        }
        if (LPS.getConfig().getBoolean("LPSFormat")) {

            SaveLoadFiles(FormatsFile, FormatsConfig, "Settings/Formats.yml", "Settings/Formats.yml", "FileChance");
            FormatsFile = new File(LPS.getDataFolder(), "/Settings/Formats.yml");
            FormatsConfig = YamlConfiguration.loadConfiguration(FormatsFile);
        } else {
            LPS.getServer().getConsoleSender().sendMessage(API.format("&3Not running LevelPoints built in Chat system"));
        }
    }

    @Override
    public void RunModuels() {


        usersConfig.clear();
        LPS.saveDefaultConfig();
        LPS.getServer().getPluginManager().registerEvents(new MainEvents(LPS), LPS);
        LPS.getServer().getConsoleSender().sendMessage(API.format("&3Loaded Modules>>> &bEvents"));

        LPS.getCommand("levelpoints").setExecutor((CommandExecutor) new MainCommand(LPS));
        LPS.getServer().getConsoleSender().sendMessage(API.format("&3Loaded Modules>>> &bCommands"));


        LPS.getServer().getConsoleSender().sendMessage(API.format("&3Running Files Check...."));
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
    public void Rewards(Player player, int Level) {


        player.sendMessage(String.valueOf(Level));
        if (RewardsConfig.getString("Type").equalsIgnoreCase("REGULAR")) {
            List<String> cmds = RewardsConfig.getStringList(API.format("Rewards.Level-" + Level));
            for (String command : cmds) {
                getRewards(command, player);
            }
        }
        if (RewardsConfig.getString("Type").equalsIgnoreCase("ONE")) {
            List<String> cmds = RewardsConfig.getStringList("Rewards." + "CMD");
            for (String command : cmds) {
                getRewards(command, player);
            }
        }
        if (RewardsConfig.getString("Type").equals("CHANCE")) {
            int max = RewardsConfig.getInt("Amount");
            int min = 1;

            Random r = new Random();
            int re = r.nextInt((max - min) + 1) + min;
            List<String> cmds = RewardsConfig.getStringList("Rewards.Level-" + (Level + 1) + "." + String.valueOf(re));
            for (String command : cmds) {
                getRewards(command, player);
            }
        }
        if (RewardsConfig.getString("Type").equals("FILECHANCE")) {
            List<String> levelrequired = RewardsConfig.getStringList("Rewards.LevelChance");
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

        if (RewardsConfig.getString("RewardsMethod").equals("NONE")) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
        } else if (RewardsConfig.getString("RewardsMethod").equals("MESSAGE")) {
            player.sendMessage(API.format(LangConfig.getString("lpRewardMessage")));
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
        } else if (RewardsConfig.getString("RewardsMethod").equals("TITLE")) {
            Title(player, API.format(LangConfig.getString(API.format("lpRewardTitleTop"))), API.format(LangConfig.getString(API.format("lpRewardTitleBottom")).replace("{lp_level}", String.valueOf(getCurrentLevel(player)))));
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
        if (!current.after(until)) {

            amount = amount * BoosterActive;

        }
        int newEXP = EXPCurrent + amount;
        if (amount != 0) {
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


                    int OverLap = newEXP - EXPRequired;

                    UsersConfig.set("EXP.Amount", OverLap);
                    UsersConfig.set("Level", CurrentLevel + 1);
                    try {
                        UsersConfig.save(userdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LevelUpEventTrigger(player.getPlayer(), CurrentLevel + 1, true, 0);
                } else {
                    UsersConfig.set("EXP.Amount", EXPCurrent + amount);
                    try {
                        UsersConfig.save(userdata);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                double required_progress = EXPRequired;
                double current_progress = newEXP;
                double progress_percentage = current_progress / required_progress;
                StringBuilder sb = new StringBuilder();
                int bar_length = 30;
                String completed = API.format(LangConfig.getString("lpBarDesignCompleted"));
                String need = API.format(LangConfig.getString("lpBarDesignRequired"));
                for (int i = 0; i < bar_length; i++) {
                    if (i < bar_length * progress_percentage) {
                        sb.append(completed); //what to append if percentage is covered (e.g. GREEN '|'s)
                    } else {
                        sb.append(need); //what to append if percentage is not covered (e.g. GRAY '|'s)
                    }
                }
                if (UsersConfig.getBoolean("ActionBar")) {
                    if (LPS.getConfig().getBoolean("Actionbar")) {
                        ActionBar(player, API.format(LangConfig.getString("lpActionBar").replace("{PLAYER_LEVEL}", String.valueOf(CurrentLevel)).replace("{BAR}", sb.toString()).replace("{EXP_CURRENT}", String.valueOf(newEXP)).replace("{EXP_REQUIRED}", String.valueOf(EXPRequired))));
                    }
                }
            } else {
                return;
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
            String timeleft = API.format(LangConfig.getString("lpsTimeFormat").replace("{DAYS}", String.valueOf(diffDays)).replace("{HOURS}", String.valueOf(diffHours)).replace("{MINUTES}", String.valueOf(diffMinutes)).replace("{SECONDS}", String.valueOf(diffSeconds)));
            player.sendMessage(API.format(LangConfig.getString("lpsBoosterCooldown").replace("{TIME_FORMAT}", timeleft)));
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
        int EXPR = LevelConfig.getInt("LevelingEXP");

        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);


        if (LevelConfig.getBoolean("PrestigeLeveling")) {
            needep = LevelConfig.getInt("Prestige-" + UsersConfig.getInt("Prestige") + ".Level-" + CurrentLevel);
        } else if (LevelConfig.getBoolean("CustomLeveling")) {
            needep = LevelConfig.getInt("Level-" + CurrentLevel);
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
        int Max = LevelConfig.getInt("MaxLevel");

        return Max;
    }

    @Override
    public int getMaxLevelEXP(Player player) {

        int MaxEXP;
        int CurrentLevel = getCurrentLevel(player);
        int EXPR = LevelConfig.getInt("LevelingEXP");
        int MaxLevel = getMaxLevel();
        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

        if (LevelConfig.getBoolean("PrestigeLeveling")) {
            MaxEXP = LevelConfig.getInt("Prestige-" + UsersConfig.getInt("Prestige") + ".Level-" + MaxLevel);
        } else if (LevelConfig.getBoolean("CustomLeveling")) {
            MaxEXP = LevelConfig.getInt("Level-" + MaxLevel);
        } else {
            MaxEXP = MaxLevel * EXPR;
        }


        return MaxEXP;
    }

    @Override
    public int getCurrentLevel(Player player) {
        File userdata = new File(LPS.userFolder, player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        int CL = UsersConfig.getInt("Level");

        return CL;
    }

    @Override
    public void TimedEXP() {

        if (EXPConfig.getBoolean("TimedEXP")) {

            Bukkit.getScheduler().scheduleSyncRepeatingTask(LPS, new Runnable() {
                public void run() {
                    if (Bukkit.getServer().getOnlinePlayers().size() >= 1) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            GainEXP(p, EXPConfig.getInt("GiveAmount"));
                            double seconds = EXPConfig.getInt("GiveEXP");
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



                            p.sendMessage(API.format(LangConfig.getString("lpTimedReward").replace("{EXP_Timed_Amount}", Integer.toString(EXPConfig.getInt("GiveAmount"))).replace("{EXP_Timed_Delay}", TimeMessage)));

                        }
                    }
                }
            }, 0L, EXPConfig.getInt("GiveEXP") * 20L);
        }
    }
}