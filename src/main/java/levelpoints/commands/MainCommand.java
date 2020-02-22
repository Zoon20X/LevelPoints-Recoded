package levelpoints.commands;


import com.sk89q.worldedit.internal.expression.runtime.For;
import levelpoints.levelpoints.LevelPoints;

import levelpoints.utils.utils.API;
import levelpoints.utils.utils.UtilCollector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.List;


public class MainCommand implements CommandExecutor {
    private Plugin plugin = LevelPoints.getPlugin(LevelPoints.class);
    private LevelPoints lp = LevelPoints.getPlugin(LevelPoints.class);
    private String playerName;
    private String cfgName;
    public int hours;
    public int minutes;
    public int seconds;
    public String commandString;
    public int posTop;

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

                    sender.sendMessage(API.format(x));
                }
            }else{
                sender.sendMessage(API.format(uc.getLangConfig().getString("LPSErrorPermission")));
            }
        }

        if(args.length >= 1) {
            if (sender.hasPermission("lp.admin.reload")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("reload")) {

                        lp.reloadConfig();
                        uc.RunFiles();


                        sender.sendMessage(API.format(uc.getFormatsConfig().getString("Level1-10.Format")));

                        sender.sendMessage(API.format(uc.getLangConfig().getString("lpreload")));
                    }

                }
            } else {
                sender.sendMessage(API.format(uc.getLangConfig().getString("LPSErrorPermission")));
            }

            if (sender.hasPermission("lp.admin.give")) {

                if (args[0].equalsIgnoreCase("expgive")) {
                    if (args.length == 1) {
                        sender.sendMessage(API.format(uc.getLangConfig().getString("lpsEXPGIVEPlayer")));
                        return true;
                    }

                    if (args.length == 2) {
                        sender.sendMessage(API.format(uc.getLangConfig().getString("lpsEXPGIVEAmount")));
                        return true;
                    }
                    if (args.length == 3) {
                        Player target = Bukkit.getPlayer(args[1]);


                        if (target != null) {

                            //lp.CustomXP(target, Integer.parseInt(args[2]), 0);
                            uc.GainEXP(target, Integer.parseInt(args[2]));
                            sender.sendMessage(API.format(uc.getLangConfig().getString("lpAdminEXPGive").replace("{LP_TARGET}", target.getName()).replace("{EXP_AMOUNT}", args[2])));
                            target.sendMessage(API.format(uc.getLangConfig().getString("lpEXPGive").replace("{EXP_Amount}", args[2]).replace("{LP_USER}", uc.getLangConfig().getString("lpServerName"))));

                        }
                    }
                }

            }
            if (sender.hasPermission("lp.admin.remove")) {
                if (args[0].equalsIgnoreCase("expremove")) {

                    if (args.length == 1) {
                        sender.sendMessage(API.format(uc.getLangConfig().getString("lpsEXPREMOVEPlayer")));
                        return true;
                    }
                    if (args.length == 2) {
                        sender.sendMessage(API.format(uc.getLangConfig().getString("lpsEXPREMOVEAmount")));
                        return true;
                    }
                    if (args.length == 3) {
                        Player target = Bukkit.getPlayer(args[1]);
                        File userdata = new File(lp.userFolder, target.getUniqueId() + ".yml");
                        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);

                        if (target != null) {
                            int expp = uc.getCurrentEXP(target);
                            int t = Integer.parseInt(args[2]);
                            int tep = expp - t;
                            UsersConfig.set("EXP.Amount", tep);
                            try {
                                UsersConfig.save(userdata);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (lp.getConfig().getBoolean("UseSQL")) {
                            uc.RunSQLUpdate(target);
                        }


                        if (sender instanceof Player) {
                            target.sendMessage(API.format(uc.getLangConfig().getString("lpEXPRemove").replace("{EXP_AMOUNT}", args[2]).replace("{LP_USER}", sender.getName())));
                            sender.sendMessage(API.format(uc.getLangConfig().getString("lpAdminEXPRemove").replace("{LP_TARGET}", target.getName()).replace("{EXP_AMOUNT}", args[2])));
                        }else{
                            target.sendMessage(API.format(uc.getLangConfig().getString("lpEXPRemove").replace("{EXP_AMOUNT}", args[2]).replace("{LP_USER}", uc.getLangConfig().getString("lpServerName"))));
                            sender.sendMessage(API.format(uc.getLangConfig().getString("lpAdminEXPRemove").replace("{LP_TARGET}", target.getName()).replace("{EXP_AMOUNT}", args[2])));
                        }

                    }
                }
            }

            if (sender.hasPermission("lp.player")) {
                if (args[0].equalsIgnoreCase("top")) {
                    posTop = 0;


                    for (String x : uc.getLangConfig().getStringList("lpsTopListTop")) {
                        sender.sendMessage(API.format(x));
                        // Here you can send to player or do whatever you wan't.

                    }
                    ConfigurationSection cf = uc.TopListConfig.getConfigurationSection("");
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
                                    sender.sendMessage(API.format(x).replace("{LP_Ranked}", Integer.toString(posTop)).replace("{LP_Player}", playername).replace("{LP_LEVEL}", Integer.toString(points)));
                                    // Here you can send to player or do whatever you wan't.

                                }
                            });
                    for (String x : uc.getLangConfig().getStringList("lpsTopListBottom")) {
                        sender.sendMessage(API.format(x));
                        // Here you can send to player or do whatever you wan't.

                    }
                    if (args[0].equalsIgnoreCase("creator")) {
                        sender.sendMessage(ChatColor.DARK_AQUA + "LevelPoints Created by: " + ChatColor.AQUA + "Zoon20X");
                    }
                }
            } else {
                sender.sendMessage(API.format(uc.getLangConfig().getString("LPSErrorPermission")));
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
                                    player.sendMessage(API.format(uc.getLangConfig().getString("lpsPrestigeLevelUP").replace("{PRESTIGE_AMOUNT}", String.valueOf(uc.getCurrentPrestige(player)))));
                                    return true;
                                } else {
                                    int need = uc.getRequiredEXP(player) - uc.getCurrentEXP(player);


                                    player.sendMessage(API.format(uc.getLangConfig().getString("lpsPrestigeMoreEXP").replace("{EXP_AMOUNT}", String.valueOf(need))));
                                }
                            } else {
                                player.sendMessage(API.format(uc.getLangConfig().getString("lpsPrestigeLevelNot").replace("{MAX_LEVEL}", String.valueOf(uc.getMaxLevel()))));
                            }
                        }
                        if (args[0].equalsIgnoreCase("info")) {


                            int LevelUpEXP = uc.getRequiredEXP(player);
                            int levels = uc.getCurrentLevel(player);
                            int pres = UsersConfig.getInt("Prestige");

                            int expss = UsersConfig.getInt("EXP.Amount");

                            float percentage = expss * 100;

                            String EXP = expss + "/" + LevelUpEXP;
                            String Percentage = Math.round(percentage / LevelUpEXP) + "%";


                            for (String x : uc.getLangConfig().getStringList("lpsInfo")) {
                                sender.sendMessage(API.format(x.replace("{lp_player}", player.getName()).replace("{lp_level}", String.valueOf(levels)).replace("{lp_xp}", EXP).replace("{lp_progress}", Percentage).replace("{lp_prestige}", Integer.toString(pres))));
                            }
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
                            player.sendMessage(API.format(uc.getLangConfig().getString("lpsActionBarToggle").replace("{LP_Toggle_Value}", String.valueOf(UsersConfig.getBoolean("ActionBar")))));

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
                                target.sendMessage(API.format(uc.getLangConfig().getString("lpSetPrestige").replace("{PRESTIGE}", args[2]).replace("{LP_USER}", sender.getName())));
                                sender.sendMessage(API.format(uc.getLangConfig().getString("lpAdminSetPrestige").replace("{LP_TARGET}", target.getName()).replace("{PRESTIGE}", args[2])));
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

                                target.sendMessage(API.format(uc.getLangConfig().getString("lpSetLevel").replace("{LEVEL}", args[2]).replace("{LP_USER}", sender.getName())));
                                sender.sendMessage(API.format(uc.getLangConfig().getString("lpAdminSetLevel").replace("{LP_TARGET}", target.getName()).replace("{LEVEL}", args[2])));
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
                            int LevelUpEXP = uc.getRequiredEXP(TargetPlayer);
                            int expss = UsersConfig.getInt("EXP.Amount");


                            float percentage = expss * 100;
                            String EXP = expss + "/" + LevelUpEXP;
                            String Percentage = Math.round(percentage / LevelUpEXP) + "%";

                            for (String x : uc.getLangConfig().getStringList("lpsInfo")) {

                                sender.sendMessage(API.format(x.replace("{lp_player}", str.toString()).replace("{lp_level}", String.valueOf(levels)).replace("{lp_xp}", EXP).replace("{lp_progress}", Percentage).replace("{lp_prestige}", Integer.toString(pres))));
                            }
                            return true;
                        } else {
                            sender.sendMessage(API.format(uc.getLangConfig().getString("LPSNotOnline")));
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
                                        sender.sendMessage(API.format("&4Error&8>> &cString is not a Number"));
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
                                    Target.sendMessage(API.format(uc.getLangConfig().getString("BoosterGive")).replace("{BoosterMultiplier}", String.valueOf(Multipler)));
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
                                for (String x : BoostersList.getKeys(false)) {
                                    int amount = UsersConfig.getInt("Boosters." + x);
                                    player.sendMessage(API.format(uc.getLangConfig().getString("lpsBoosterList").replace("{Booster_Multiplier}", x).replace("{Booster_Amount}", String.valueOf(amount))));
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
                    }
                }
            }
        }else{
            sender.sendMessage(API.format(uc.getLangConfig().getString("LPSErrorPermission").replace("{PLAYER}", sender.getName())));
        }
        return true;
    }
}