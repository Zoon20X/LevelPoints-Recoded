package me.zoon20x.levelpoints.commands;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.commands.TabComplete.adminLpsTabComplete;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Settings.Boosters.BoosterData;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import me.zoon20x.levelpoints.utils.Formatter;
import me.zoon20x.levelpoints.utils.MessageUtils;
import me.zoon20x.levelpoints.utils.Permissions.PermissionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class adminLpsCommand implements CommandExecutor {
    private LevelPoints lps;


    public adminLpsCommand(LevelPoints lps, String command) {
        this.lps = lps;
        lps.getCommand(command).setExecutor(this);
        lps.getCommand(command).setTabCompleter(new adminLpsTabComplete());
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            if (!sender.hasPermission(PermissionUtils.getAdminPermission().help())) {
                sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                return true;
            }
            for (String x : LevelPoints.getInstance().getLangSettings().getAdminHelp()) {
                sender.sendMessage(MessageUtils.getColor(x));
            }
        }

        if (args.length == 1) {
            args1(sender, args);
        }
        if(args.length == 3){
            args3(sender, args);
        }
        if (args.length == 4) {
            args4(sender, args);
        }
        if(args.length == 5){
            args5(sender, args);
        }
        return true;
    }

    private void args1(CommandSender sender, String[] args) {
        switch (args[0]) {
            case "reload":
                if (!sender.hasPermission(PermissionUtils.getAdminPermission().reload())) {
                    sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                    return;
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        long startTime = System.nanoTime();

                        LevelPoints.getInstance().setReloading(true);
                        LevelPoints.getInstance().reloadConfig();
                        LevelPoints.getInstance().getFilesGenerator().generateFiles();
                        LevelPoints.getInstance().getPlayerGenerator().saveAllData();
                        LevelPoints.getInstance().getPlayerStorage().clearPlayerCache();
                        LevelPoints.getInstance().getRewardSettings().clearRewards();
                        LevelPoints.getInstance().reloadClass();
                        LevelPoints.getInstance().getLevelSettings().generateRequired();
                        LevelPoints.getInstance().getExpSettings().generateBlocks();
                        LevelPoints.getInstance().getExpSettings().generateMobs();
                        LevelPoints.getInstance().getExpSettings().generateBreeds();
                        LevelPoints.getInstance().getExpSettings().generateCrafting();
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            LevelPoints.getInstance().getPlayerGenerator().loadPlayerFile(new File(LevelPoints.getInstance().getUserFolder(), player.getUniqueId() + ".yml"));
                        });
                        LevelPoints.getInstance().setReloading(false);
                        long endTime = System.nanoTime();
                        long duration = ((endTime - startTime) / 1000000);
                        LevelPoints.getDebug(DebugSeverity.NORMAL, "reload took " + duration + "ms to complete");
                    }
                }.runTaskAsynchronously(LevelPoints.getInstance());
                break;
            case "output":
                if(!sender.isOp()){
                    LevelPoints.getDebug(DebugSeverity.SEVER, "You are not op, please only run this if you need data or Zoon20X asks");
                    return;
                }
                LevelPoints.getInstance().sendLoadedData();
                break;
        }
    }

    private void args3(CommandSender sender, String[] args){
        switch (args[0]){
            case "booster":
                if(args[1].equalsIgnoreCase("delete")) {
                    if (!LevelPoints.getInstance().getBoosterSettings().hasBooster(args[2])) {
                        sender.sendMessage(MessageUtils.getColor("&cBooster doesn't exist"));
                        return;
                    }
                    LevelPoints.getInstance().getBoosterSettings().removeBooster(args[2]);
                    sender.sendMessage(MessageUtils.getColor("&bBooster Removed"));
                    return;
                }
                break;
        }
    }

    private void args4(CommandSender sender, String[] args) {
        if(!LevelPoints.getInstance().getPlayerStorage().hasPlayerFile(Bukkit.getOfflinePlayer(args[2]).getUniqueId())){
            sender.sendMessage(MessageUtils.getColor("Player data doesn't exist"));
            return;
        }
        PlayerData data = LevelPoints.getInstance().getPlayerStorage().getLoadedData(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
        switch (args[0]) {
            case "exp":
                switch (args[1]) {
                    case "give":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().expAdd())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.addEXP(Double.valueOf(args[3]));
                        if (LevelPoints.getInstance().getLangSettings().isExpCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), 0, Double.valueOf(args[3]), 0.0, 0, 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getExperienceCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getInstance().getLangSettings().isExpCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], 0, Double.valueOf(args[3]), 0.0, 0, 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getExperienceCommandSender(), formatter)));
                        }
                        break;
                    case "remove":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().expRemove())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.removeEXP(Double.valueOf(args[3]));

                        if (LevelPoints.getInstance().getLangSettings().isRemoveCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), 0, Double.valueOf(args[3]), 0.0, 0, 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getRemoveCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getInstance().getLangSettings().isRemoveCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], 0, Double.valueOf(args[3]), 0.0, 0, 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getRemoveCommandSender(), formatter)));
                        }
                        break;
                }
                break;
            case "level":
                switch (args[1]){
                    case "add":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().addLevel())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.addLevel(Integer.valueOf(args[3]), false);
                        if (LevelPoints.getInstance().getLangSettings().isLevelsAddCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), Integer.valueOf(args[3]), 0, 0.0, 0, 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getLevelsAddCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getInstance().getLangSettings().isLevelsAddCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], Integer.valueOf(args[3]), 0, 0.0, 0, 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getLevelsAddCommandSender(), formatter)));
                        }
                        break;
                    case "remove":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().removeLevel())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.removeLevel(Integer.valueOf(args[3]));
                        if (LevelPoints.getInstance().getLangSettings().isLevelsRemoveCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), Integer.valueOf(args[3]), 0, 0.0, 0, 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getLevelsRemoveCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getInstance().getLangSettings().isLevelsRemoveCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], Integer.valueOf(args[3]), 0, 0.0, 0, 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getLevelsRemoveCommandSender(), formatter)));
                        }
                        break;
                    case "set":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().setLevel())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.setLevel(Integer.valueOf(args[3]));
                        if (LevelPoints.getInstance().getLangSettings().isLevelsSetCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), Integer.valueOf(args[3]), 0, 0.0, 0, 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getLevelsSetCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getInstance().getLangSettings().isLevelsSetCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], Integer.valueOf(args[3]), 0, 0.0, 0, 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getLevelsSetCommandSender(), formatter)));
                        }
                        break;
                }
                break;
            case "prestige":
                switch (args[1]){
                    case "add":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().addPrestige())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.addPrestige(Integer.valueOf(args[3]));
                        if (LevelPoints.getInstance().getLangSettings().isPrestigeAddCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), 0, 0, 0.0, Integer.valueOf(args[3]), 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getPrestigeAddCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getInstance().getLangSettings().isPrestigeAddCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], 0, 0, 0.0, Integer.valueOf(args[3]), 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getPrestigeAddCommandSender(), formatter)));
                        }
                        break;
                    case "remove":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().removeLevel())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.removePrestige(Integer.valueOf(args[3]));
                        if (LevelPoints.getInstance().getLangSettings().isPrestigeRemoveCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(),0, 0, 0.0, Integer.valueOf(args[3]), 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getPrestigeRemoveCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getInstance().getLangSettings().isPrestigeRemoveCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], 0, 0, 0.0, Integer.valueOf(args[3]), 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getPrestigeRemoveCommandSender(), formatter)));
                        }
                        break;
                    case "set":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().setPrestige())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.setPrestige(Integer.valueOf(args[3]));
                        if (LevelPoints.getInstance().getLangSettings().isPrestigeSetCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), 0, 0, 0.0, Integer.valueOf(args[3]), 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getPrestigeSetCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getInstance().getLangSettings().isPrestigeSetCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], 0, 0, 0.0, Integer.valueOf(args[3]), 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getPrestigeSetCommandSender(), formatter)));
                        }
                        break;
                }
            case "booster":
                switch (args[1]){
                    case "give":
                        if(!sender.hasPermission(PermissionUtils.getAdminPermission().giveBooster())){
                            return;
                        }
                        //booster give <player> <booster>
                        if(!LevelPoints.getInstance().getBoosterSettings().hasBooster(args[3])){
                            return;
                        }
                        data.addBooster(LevelPoints.getInstance().getBoosterSettings().getBooster(args[3]), 1);
                        break;
                }
                break;
        }
    }
    private void args5(CommandSender sender, String[] args){
        switch (args[0]){
            case "booster":
                if(args[1].equalsIgnoreCase("create")) {
                    if(!sender.hasPermission(PermissionUtils.getAdminPermission().createBooster())){
                        return;
                    }
                    if(LevelPoints.getInstance().getBoosterSettings().hasBooster(args[2])){
                        sender.sendMessage(MessageUtils.getColor("&cBooster id in use"));
                        return;
                    }
                    LevelPoints.getInstance().getBoosterSettings().addBooster(new BoosterData(args[2], Double.valueOf(args[3]), args[4]));
                    sender.sendMessage(MessageUtils.getColor("&bBooster Created"));
                    return;
                }
                if(args[1].equalsIgnoreCase("give")){
                    if(!sender.hasPermission(PermissionUtils.getAdminPermission().giveBooster())){
                        return;
                    }
                    if(!LevelPoints.getInstance().getPlayerStorage().hasPlayerFile(Bukkit.getOfflinePlayer(args[2]).getUniqueId())){
                        sender.sendMessage(MessageUtils.getColor("Player data doesn't exist"));
                        return;
                    }
                    PlayerData data = LevelPoints.getInstance().getPlayerStorage().getLoadedData(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
                    if(!LevelPoints.getInstance().getBoosterSettings().hasBooster(args[3])){
                        return;
                    }
                    data.addBooster(LevelPoints.getInstance().getBoosterSettings().getBooster(args[3]), Integer.valueOf(args[4]));
                    break;
                }
                break;
        }
    }


}
