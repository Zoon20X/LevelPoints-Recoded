package me.zoon20x.levelpoints.commands;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.commands.TabComplete.adminLpsTabComplete;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.files.FilesStorage;
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
            for (String x : LevelPoints.getLangSettings().getAdminHelp()) {
                sender.sendMessage(MessageUtils.getColor(x));
            }
        }

        if (args.length == 1) {
            args1(sender, args);
        }
        if (args.length == 4) {
            args4(sender, args);
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
                        LevelPoints.getInstance().setReloading(true);
                        FilesStorage.clearCache();
                        LevelPoints.getFilesGenerator().generateFiles();
                        LevelPoints.getPlayerGenerator().saveAllData();
                        LevelPoints.getPlayerStorage().clearPlayerCache();
                        LevelPoints.getRewardSettings().clearRewards();
                        LevelPoints.getInstance().reloadClass();
                        LevelPoints.getLevelSettings().generateRequired();
                        LevelPoints.getExpSettings().generateBlocks();
                        LevelPoints.getExpSettings().generateMobs();
                        Bukkit.getOnlinePlayers().forEach(player -> {
                            LevelPoints.getPlayerGenerator().loadPlayerFile(new File(LevelPoints.getUserFolder(), player.getUniqueId() + ".yml"));
                        });
                        LevelPoints.getInstance().setReloading(false);
                    }
                }.runTaskAsynchronously(LevelPoints.getInstance());
                break;

        }
    }

    private void args4(CommandSender sender, String[] args) {
        if(!LevelPoints.getPlayerStorage().hasPlayerFile(Bukkit.getOfflinePlayer(args[2]).getUniqueId())){
            sender.sendMessage(MessageUtils.getColor("Player data doesn't exist"));
            return;
        }
        PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
        switch (args[0]) {
            case "exp":
                switch (args[1]) {
                    case "give":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().expAdd())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.addEXP(Double.valueOf(args[3]));
                        if (LevelPoints.getLangSettings().isExpCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), 0, Double.valueOf(args[3]), 0.0, 0, 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getExperienceCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getLangSettings().isExpCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], 0, Double.valueOf(args[3]), 0.0, 0, 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getExperienceCommandSender(), formatter)));
                        }
                        break;
                    case "remove":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().expRemove())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.removeEXP(Double.valueOf(args[3]));

                        if (LevelPoints.getLangSettings().isRemoveCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), 0, Double.valueOf(args[3]), 0.0, 0, 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getRemoveCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getLangSettings().isRemoveCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], 0, Double.valueOf(args[3]), 0.0, 0, 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getRemoveCommandSender(), formatter)));
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
                        if (LevelPoints.getLangSettings().isLevelsAddCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), Integer.valueOf(args[3]), 0, 0.0, 0, 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getLevelsAddCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getLangSettings().isLevelsAddCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], Integer.valueOf(args[3]), 0, 0.0, 0, 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getLevelsAddCommandSender(), formatter)));
                        }
                        break;
                    case "remove":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().removeLevel())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.removeLevel(Integer.valueOf(args[3]));
                        if (LevelPoints.getLangSettings().isLevelsRemoveCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), Integer.valueOf(args[3]), 0, 0.0, 0, 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getLevelsRemoveCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getLangSettings().isLevelsRemoveCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], Integer.valueOf(args[3]), 0, 0.0, 0, 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getLevelsRemoveCommandSender(), formatter)));
                        }
                        break;
                    case "set":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().setLevel())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.setLevel(Integer.valueOf(args[3]));
                        if (LevelPoints.getLangSettings().isLevelsSetCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), Integer.valueOf(args[3]), 0, 0.0, 0, 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getLevelsSetCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getLangSettings().isLevelsSetCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], Integer.valueOf(args[3]), 0, 0.0, 0, 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getLevelsSetCommandSender(), formatter)));
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
                        if (LevelPoints.getLangSettings().isPrestigeAddCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), 0, 0, 0.0, Integer.valueOf(args[3]), 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getPrestigeAddCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getLangSettings().isPrestigeAddCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], 0, 0, 0.0, Integer.valueOf(args[3]), 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getPrestigeAddCommandSender(), formatter)));
                        }
                        break;
                    case "remove":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().removeLevel())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.removePrestige(Integer.valueOf(args[3]));
                        if (LevelPoints.getLangSettings().isPrestigeRemoveCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(),0, 0, 0.0, Integer.valueOf(args[3]), 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getPrestigeRemoveCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getLangSettings().isPrestigeRemoveCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], 0, 0, 0.0, Integer.valueOf(args[3]), 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getPrestigeRemoveCommandSender(), formatter)));
                        }
                        break;
                    case "set":
                        if (!sender.hasPermission(PermissionUtils.getAdminPermission().setPrestige())) {
                            sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                            return;
                        }
                        data.setPrestige(Integer.valueOf(args[3]));
                        if (LevelPoints.getLangSettings().isPrestigeSetCommandReceiverEnabled()) {
                            if (Bukkit.getPlayer(args[2]) != null) {
                                Formatter formatter = new Formatter(sender.getName(), 0, 0, 0.0, Integer.valueOf(args[3]), 0, 0);
                                Bukkit.getPlayer(args[2]).sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getPrestigeSetCommandReceiver(), formatter)));
                            }
                        }

                        if (LevelPoints.getLangSettings().isPrestigeSetCommandSenderEnabled()) {
                            Formatter formatter = new Formatter(args[2], 0, 0, 0.0, Integer.valueOf(args[3]), 0, 0);
                            sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getPrestigeSetCommandSender(), formatter)));
                        }
                        break;
                }
        }
    }


}
