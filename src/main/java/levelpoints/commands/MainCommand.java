package levelpoints.commands;



import levelpoints.levelpoints.LevelPoints;

import levelpoints.utils.utils.API;
import levelpoints.utils.utils.UtilCollector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class MainCommand implements CommandExecutor {
    private Plugin plugin = LevelPoints.getPlugin(LevelPoints.class);
    private LevelPoints lp = LevelPoints.getPlugin(LevelPoints.class);


    public int posTop;
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
    private java.util.Date cDate = new java.util.Date();
    private String cDateS = format.format(cDate);
    private java.util.Date until = null;
    private Date current = null;
    private long daytime = (1000 * 60 * 60);
    UtilCollector uc = new UtilCollector();

    public MainCommand(LevelPoints lps) {
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // List<String> translatedMessages = Lang.getStringList("uc.baseCommand");
        //Set<String> sl = Lang.getConfigurationSection("lp").getKeys(false);
        int LEXP = uc.getLangConfig().getInt("LevelingEXP");
        if (args.length == 0) {
            if(sender.hasPermission("lp.lps")) {
                for (String x : uc.getLangConfig().getStringList("lp")) {
                    API api = new API();
                    sender.sendMessage(api.format(x));
                }
            }else{
                API api = new API();
                sender.sendMessage(api.format(uc.getLangConfig().getString("LPSErrorPermission")));
            }
        }

        if(args.length >= 1) {


                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {
                        if (sender.hasPermission("lp.admin.reload")) {
                            lp.reloadConfig();
                            uc.RunFiles();
                            API api = new API();

                            sender.sendMessage(api.format(uc.getLangConfig().getString("lpreload")));
                        } else {
                            API api = new API();
                            sender.sendMessage(api.format(uc.getLangConfig().getString("LPSErrorPermission")));
                        }

                    }
                }

            if (sender.hasPermission("lp.admin.give")) {

                if (args[0].equalsIgnoreCase("expgive")) {
                    if (args.length == 1) {
                        API api = new API();
                        sender.sendMessage(api.format(uc.getLangConfig().getString("lpsEXPGIVEPlayer")));
                        return true;
                    }

                    if (args.length == 2) {
                        API api = new API();
                        sender.sendMessage(api.format(uc.getLangConfig().getString("lpsEXPGIVEAmount")));
                        return true;
                    }
                    if (args.length == 3) {
                        Player target = Bukkit.getPlayer(args[1]);


                        if (target != null) {

                            //lp.CustomXP(target, Integer.parseInt(args[2]), 0);
                            uc.GainEXP(target, Integer.valueOf(args[2]));
                            API api = new API();
                            sender.sendMessage(api.format(uc.getLangConfig().getString("lpAdminEXPGive").replace("{lp_Target}", target.getName()).replace("{lp_Earn_Exp}", args[2])));
                            target.sendMessage(api.format(uc.getLangConfig().getString("lpEXPGive").replace("{lp_Earn_Exp}", args[2]).replace("{lp_player}", uc.getLangConfig().getString("lpServerName"))));

                        }
                    }
                }

            }
            if (sender.hasPermission("lp.admin.remove")) {
                if (args[0].equalsIgnoreCase("expremove")) {

                    if (args.length == 1) {
                        API api = new API();
                        sender.sendMessage(api.format(uc.getLangConfig().getString("lpsEXPREMOVEPlayer")));
                        return true;
                    }
                    if (args.length == 2) {
                        API api = new API();
                        sender.sendMessage(api.format(uc.getLangConfig().getString("lpsEXPREMOVEAmount")));
                        return true;
                    }
                    if (args.length == 3) {
                        Player target = Bukkit.getPlayer(args[1]);
                        File userdata = new File(lp.userFolder, target.getUniqueId() + ".yml");
                        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

                        if (target != null) {
                            double expp = uc.getCurrentEXP(target);
                            double t = Double.valueOf(args[2]);
                            double tep = expp - t;
                            UsersConfig.set("EXP.Amount", tep);
                            try {
                                UsersConfig.save(userdata);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if(lp.getConfig().getBoolean("BossBar")){
                            uc.updateBossbar(uc.getBossbar(target), target);
                        }
                        if (sender instanceof Player) {
                            API api = new API();
                            target.sendMessage(api.format(uc.getLangConfig().getString("lpEXPRemove").replace("{lp_Earn_Exp}", args[2]).replace("{LP_USER}", sender.getName())));
                            sender.sendMessage(api.format(uc.getLangConfig().getString("lpAdminEXPRemove").replace("{lp_Target}", target.getName()).replace("{lp_Earn_Exp}", args[2])));
                        }else{
                            API api = new API();
                            target.sendMessage(api.format(uc.getLangConfig().getString("lpEXPRemove").replace("{lp_Earn_Exp}", args[2]).replace("{lp_player}", uc.getLangConfig().getString("lpServerName"))));
                            sender.sendMessage(api.format(uc.getLangConfig().getString("lpAdminEXPRemove").replace("{lp_Target}", target.getName()).replace("{lp_Earn_Exp}", args[2])));
                        }

                    }
                }
            }

            if (sender.hasPermission("lp.player")) {
                if (args[0].equalsIgnoreCase("top")) {
                    posTop = 0;


                    for (String x : uc.getLangConfig().getStringList("lpsTopListTop")) {
                        API api = new API((Player) sender, x);
                        sender.sendMessage(api.formatTags());
                        // Here you can send to player or do whatever you wan't.

                    }
                    ConfigurationSection cf = uc.getTopListConfig().getConfigurationSection("");
                    cf.getValues(false)
                            .entrySet()
                            .stream()
                            .sorted((a1, a2) -> {
                                int points1 = ((MemorySection) a1.getValue()).getInt("Level");
                                int points2 = ((MemorySection) a2.getValue()).getInt("Level");
                                return points2 - points1;
                            })
                            .limit(10) // Limit the number of 'results'
                            .forEach(f -> {
                                posTop += 1;

                                int points = ((MemorySection) f.getValue()).getInt("Level");
                                String playername = ((MemorySection) f.getValue()).getString("Name");

                                for (String x : uc.getLangConfig().getStringList("lpsTopListMid")) {
                                    API api = new API((Player) sender, x);
                                    sender.sendMessage(api.formatTags());
                                    // Here you can send to player or do whatever you wan't.

                                }
                            });
                    for (String x : uc.getLangConfig().getStringList("lpsTopListBottom")) {
                        API api = new API((Player) sender, x);
                        sender.sendMessage(api.formatTags());
                        // Here you can send to player or do whatever you wan't.

                    }
                    if (args[0].equalsIgnoreCase("creator")) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "LevelPoints Created by: " + ChatColor.AQUA + "Zoon20X");
                    }
                }
            } else {
                API api = new API();
                sender.sendMessage(api.format(uc.getLangConfig().getString("LPSErrorPermission")));
            }
        }

        if (sender.hasPermission("lp.player")) {

            if (sender instanceof Player) {
                if (args.length >= 1) {
                    Player player = (Player) sender;
                    File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
                    FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);


                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("prestige")) {
                            if (UsersConfig.getInt("Level") == uc.getMaxLevel()) {
                                if (UsersConfig.getInt("EXP.Amount") >= uc.getMaxLevelEXP(player)) {

                                    int pres = UsersConfig.getInt("Prestige");
                                    sender.sendMessage(ChatColor.DARK_AQUA + "You Prestiged");
                                    UsersConfig.set("Level", 1);
                                    UsersConfig.set("Prestige", pres + 1);
                                    UsersConfig.set("EXP.Amount", 0);

                                    try {
                                        UsersConfig.save(userdata);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    API api = new API(player, uc.getLangConfig().getString("lpsPrestigeLevelUP"));
                                    player.sendMessage(api.formatTags());
                                    return true;
                                } else {
                                    double need = uc.getRequiredEXP(player) - uc.getCurrentEXP(player);

                                    API api = new API();
                                    player.sendMessage(api.format(uc.getLangConfig().getString("lpsPrestigeMoreEXP").replace("{lp_Required_EXP}", String.valueOf(need))));
                                }
                            } else {
                                player.sendMessage(API.format(uc.getLangConfig().getString("lpsPrestigeLevelNot").replace("{lp_Max_Level}", String.valueOf(uc.getMaxLevel()))));
                            }
                        }
                        if (args[0].equalsIgnoreCase("info")) {



                            double LevelUpEXP = uc.getRequiredEXP(player);
                            int levels = uc.getCurrentLevel(player);
                            int pres = UsersConfig.getInt("Prestige");

                            double expss = uc.getCurrentEXP(player);

                            double percentage = expss * 100;


                            String Percentage = Math.round(percentage / LevelUpEXP) + "%";

                            String LPRE = NumberFormat.getNumberInstance(Locale.US).format(Math.round(Float.parseFloat(uc.formatter.format(LevelUpEXP))));
                            String LPHAVE = NumberFormat.getNumberInstance(Locale.US).format(Math.round(Float.parseFloat(uc.formatter.format(expss))));

                            String EXP = LPHAVE + "/" + LPRE;
                            for (String x : uc.getLangConfig().getStringList("lpsInfo")) {
                                API api = new API(player, x);
                                sender.sendMessage(api.formatTags());
                            }
;
                            return true;
                        }
                        if (args[0].equalsIgnoreCase("toggle")) {
                            if (UsersConfig.getBoolean("ActionBar")) {
                                UsersConfig.set("ActionBar", false);
                            } else {
                                UsersConfig.set("ActionBar", true);
                            }
                            try {
                                UsersConfig.save(userdata);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            API api = new API();
                            player.sendMessage(api.format(uc.getLangConfig().getString("lpsActionBarToggle").replace("{LP_Toggle_Value}", String.valueOf(UsersConfig.getBoolean("ActionBar")))));

                        }
                    }
                    if (args[0].equalsIgnoreCase("setPrestige")) {
                        if (player.hasPermission("lp.admin")) {
                            if (args.length == 1) {
                                sender.sendMessage(ChatColor.RED + "You Must Pick a Player");
                                return true;
                            }
                            if (args.length == 2) {
                                sender.sendMessage(ChatColor.RED + "You Must Pick a Level to set");
                                return true;
                            }
                            Player target = Bukkit.getPlayer(args[1]);
                            File targetData = new File(lp.userFolder, target.getUniqueId() + ".yml");
                            FileConfiguration TargetConfig = YamlConfiguration.loadConfiguration(targetData);
                            if (target != null) {
                                int prestige = Integer.parseInt(args[2]);
                                TargetConfig.set("Prestige", prestige);


                                try {
                                    TargetConfig.save(targetData);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                API api = new API();
                                target.sendMessage(api.format(uc.getLangConfig().getString("lpSetPrestige").replace("{lp_Earn_Prestige}", args[2]).replace("{lp_player}", sender.getName())));
                                sender.sendMessage(api.format(uc.getLangConfig().getString("lpAdminSetPrestige").replace("{lp_Target}", target.getName()).replace("{lp_Earn_Prestige}", args[2])));
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You Have Insufficient Permission");
                        }
                    }
                    if (args[0].equalsIgnoreCase("setlevel")) {
                        if (player.hasPermission("lp.admin")) {
                            if (args.length == 1) {
                                sender.sendMessage(ChatColor.RED + "You Must Pick a Player");
                                return true;
                            }
                            if (args.length == 2) {
                                sender.sendMessage(ChatColor.RED + "You Must Pick a Level to set");
                                return true;
                            }
                            Player target = Bukkit.getPlayer(args[1]);
                            File targetData = new File(lp.userFolder, target.getUniqueId() + ".yml");
                            FileConfiguration TargetConfig = YamlConfiguration.loadConfiguration(targetData);
                            if (target != null) {
                                int level = Integer.parseInt(args[2]);
                                TargetConfig.set("Level", level);
                                uc.TopListConfig.set(target.getUniqueId() + ".Name", target.getName());
                                uc.TopListConfig.set(target.getUniqueId() + ".Level", level);

                                try {
                                    TargetConfig.save(targetData);
                                    uc.TopListConfig.save(uc.TopListFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                API api = new API();
                                target.sendMessage(api.format(uc.getLangConfig().getString("lpSetLevel").replace("{lp_Earn_Level}", args[2]).replace("{lp_player}", sender.getName())));
                                sender.sendMessage(api.format(uc.getLangConfig().getString("lpAdminSetLevel").replace("{lp_Target}", target.getName()).replace("{lp_Earn_Level}", args[2])));
                                if(lp.getConfig().getBoolean("BossBar")){
                                    uc.updateBossbar(uc.getBossbar(target), target);
                                }
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + "You Have Insufficient Permission");
                        }

                    }
                    if (args[0].equalsIgnoreCase("addlevel")) {
                        if (player.hasPermission("lp.admin")) {
                            if (args.length == 1) {
                                sender.sendMessage(ChatColor.RED + "You Must Pick a Player");
                                return true;
                            }
                            if (args.length == 2) {
                                sender.sendMessage(ChatColor.RED + "You Must Pick a Level to add");
                                return true;
                            }

                            Player target = Bukkit.getPlayer(args[1]);
                            File targetData = new File(lp.userFolder, target.getUniqueId() + ".yml");
                            FileConfiguration TargetConfig = YamlConfiguration.loadConfiguration(targetData);
                            int CurrentLevel = uc.getCurrentLevel(target);
                            TargetConfig.set("Level", CurrentLevel + Integer.parseInt(args[2]));
                            uc.TopListConfig.set(target.getUniqueId() + ".Name", target.getName());
                            uc.TopListConfig.set(target.getUniqueId() + ".Level", CurrentLevel + Integer.parseInt(args[2]));
                            try {
                                uc.TopListConfig.save(uc.TopListFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            try {
                                TargetConfig.save(targetData);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if(lp.getConfig().getBoolean("BossBar")){
                                uc.updateBossbar(uc.getBossbar(target), target);
                            }
                        }
                    }
                }


                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("info")) {
                        StringBuilder str = new StringBuilder(args[1]);

                        for (int i = 2; i < args.length; i++) {
                            str.append(' ').append(args[i]);
                        }
                        if (Bukkit.getPlayer(str.toString()) != null) {
                            Player TargetPlayer = Bukkit.getPlayer(str.toString());

                            File userdata = new File(lp.userFolder, TargetPlayer.getUniqueId() + ".yml");
                            FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

                            int levels = uc.getCurrentLevel(TargetPlayer);
                            int pres = UsersConfig.getInt("Prestige");
                            double LevelUpEXP = uc.getRequiredEXP(TargetPlayer);
                            double expss = uc.getCurrentEXP(TargetPlayer);


                            double percentage = expss * 100;


                            String EXP = expss + "/" + LevelUpEXP;
                            String Percentage = Math.round(percentage / LevelUpEXP) + "%";


                            for (String x : uc.getLangConfig().getStringList("lpsInfo")) {
                                API api = new API(TargetPlayer, x);
                                sender.sendMessage(api.format(api.formatTags()));
                            }
                            return true;
                        } else {
                            API api = new API();
                            sender.sendMessage(api.format(uc.getLangConfig().getString("LPSNotOnline")));
                        }
                    }
                }

                if(args.length == 5){
                    if(sender.hasPermission("lp.*")){
                        if(args[0].equalsIgnoreCase("booster")){
                            if(args[1].equalsIgnoreCase("give")){
                                Player Target = Bukkit.getPlayer(args[2]);
                                if(Target != null) {
                                    int Multipler = 0;
                                    int amount = 0;
                                    try {
                                        Multipler = Integer.parseInt(args[3]);
                                        amount = Integer.parseInt(args[4]);
                                    }catch (NumberFormatException e){
                                        API api = new API();
                                        sender.sendMessage(api.format("&4Error&8>> &cString is not a Number"));
                                    }
                                    File userdata = new File(lp.userFolder, Target.getUniqueId() + ".yml");
                                    FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

                                    if(UsersConfig.getInt("Boosters." + Multipler) == 0) {
                                        UsersConfig.set("Boosters." + Multipler, amount);
                                    }else{
                                        int current = uc.getCurrentBoosters(Target, Multipler);
                                        UsersConfig.set("Boosters." + Multipler, amount + current);
                                    }
                                    try {
                                        UsersConfig.save(userdata);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    API api = new API();
                                    sender.sendMessage(api.format(uc.getLangConfig().getString("BoosterAdminGive")
                                            .replace("{BoosterMultiplier}", String.valueOf(Multipler))
                                            .replace("{lp_player}", Target.getName()))
                                            .replace("{Booster_Amount}", String.valueOf(amount)));
                                    Target.sendMessage(api.format(uc.getLangConfig().getString("BoosterGive"))
                                            .replace("{BoosterMultiplier}", String.valueOf(Multipler))
                                            .replace("{Booster_Amount}", String.valueOf(amount)));
                                }
                            }
                        }
                    }
                }
                if(sender.hasPermission("lp.player")) {
                    if (args.length >= 1) {
                        if (args[0].equalsIgnoreCase("booster")) {
                            Player player = (Player) sender;
                            File userdata = new File(lp.userFolder, player.getUniqueId() + ".yml");
                            FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
                            if (args[1].equalsIgnoreCase("list")) {

                                ConfigurationSection BoostersList = UsersConfig.getConfigurationSection("Boosters");
                                if (BoostersList == null) {
                                    API api = new API();
                                    player.sendMessage(api.format("&cYou currently have 0 boosters"));
                                } else {
                                    for (String x : BoostersList.getKeys(false)) {
                                        int amount = UsersConfig.getInt("Boosters." + x);
                                        API api = new API();
                                        player.sendMessage(api.format(uc.getLangConfig().getString("lpsBoosterList").replace("{Booster_Multiplier}", x).replace("{Booster_Amount}", String.valueOf(amount))));
                                    }
                                }
                            }
                            if (args[1].equalsIgnoreCase("use")) {
                                try {
                                    uc.boosteruseclick(player, Integer.parseInt(args[2]));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }else{

                    }
                }
            }else if (sender instanceof ConsoleCommandSender){
                if(args.length == 5){
                    if(sender.hasPermission("lp.*")){
                        if(args[0].equalsIgnoreCase("booster")){
                            if(args[1].equalsIgnoreCase("give")){
                                Player Target = Bukkit.getPlayer(args[2]);
                                if(Target != null) {
                                    int Multipler = 0;
                                    int amount = 0;
                                    try {
                                        Multipler = Integer.parseInt(args[3]);
                                        amount = Integer.parseInt(args[4]);
                                    }catch (NumberFormatException e){
                                        API api = new API();
                                        sender.sendMessage(api.format("&4Error&8>> &cString is not a Number"));
                                    }
                                    File userdata = new File(lp.userFolder, Target.getUniqueId() + ".yml");
                                    FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

                                    if(UsersConfig.getInt("Boosters." + Multipler) == 0) {
                                        UsersConfig.set("Boosters." + Multipler, amount);
                                    }else{
                                        int current = uc.getCurrentBoosters(Target, Multipler);
                                        UsersConfig.set("Boosters." + Multipler, amount + current);
                                    }
                                    try {
                                        UsersConfig.save(userdata);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    API api = new API();
                                    sender.sendMessage(api.format(uc.getLangConfig().getString("BoosterAdminGive")
                                            .replace("{BoosterMultiplier}", String.valueOf(Multipler))
                                            .replace("{lp_player}", Target.getName())
                                            .replace("{Booster_Amount}", String.valueOf(amount))));
                                    Target.sendMessage(api.format(uc.getLangConfig().getString("BoosterGive"))
                                            .replace("{BoosterMultiplier}", String.valueOf(Multipler))
                                            .replace("{Booster_Amount}", String.valueOf(amount)));
                                }
                            }
                        }
                    }
                }
            }
        }else{
            API api = new API((Player) sender, uc.getLangConfig().getString("LPSErrorPermission"));
            sender.sendMessage(api.formatTags());
        }
        return true;
    }
}