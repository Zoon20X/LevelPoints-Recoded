package me.zoon20x.levelpoints.commands;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.commands.TabComplete.lpsTabComplete;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import me.zoon20x.levelpoints.utils.Formatter;
import me.zoon20x.levelpoints.utils.MessageUtils;
import me.zoon20x.levelpoints.utils.Permissions.PermissionUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

public class lpsCommand implements CommandExecutor {

    public lpsCommand(LevelPoints lps, String command){

        lps.getCommand(command).setExecutor(this);
        lps.getCommand(command).setTabCompleter(new lpsTabComplete());
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            if (!sender.hasPermission(PermissionUtils.getPlayerPermission().help())) {
                sender.sendMessage(MessageUtils.getColor(DebugSeverity.SEVER + "You do not have permission"));
                return true;
            }
            for(String x : LevelPoints.getLangSettings().getLpHelp()){
                sender.sendMessage(MessageUtils.getColor(x));
            }
        }

        if(!(sender instanceof Player)) {
            return true;
        }
        if(args.length == 1){
            args1(sender, args);
        }
        if(args.length == 2){
            args2(sender, args);
        }

        return true;
    }


    private void args1(CommandSender sender, String[] args){
        Player player = (Player) sender;
        if(!LevelPoints.getPlayerStorage().hasPlayerFile(player.getUniqueId())){
            sender.sendMessage(MessageUtils.getColor("Player data doesn't exist"));
            return;
        }
        PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(player.getUniqueId());
        switch (args[0]){
            case "info":
                Formatter info = new Formatter(player.getName(), data.getLevel(), data.getExp(), data.getRequiredExp(), data.getPrestige(), 0, data.getProgress());
                for(String x : LevelPoints.getLangSettings().getPlayerInfo()){
                    sender.sendMessage(MessageUtils.getColor(MessageUtils.format(x, info)));
                }
                break;
            case "prestige":
                if(!data.canPrestige()){
                    if(!LevelPoints.getLangSettings().isPrestigeCommandDeniedEnabled()){
                        return;
                    }
                    Formatter prestige = new Formatter(player.getName(), data.getLevel(), data.getExp(), data.getRequiredExp(), data.getPrestige(), LevelPoints.getLevelSettings().getMaxLevel(), data.getProgress());
                    sender.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getPrestigeCommandDeniedMessage(), prestige)));
                    return;
                }
                data.prestige();

                break;
            case "top":
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        LevelPoints.getTopListSettings().sendTopList(player);
                    }
                }.runTaskAsynchronously(LevelPoints.getInstance());
                break;
            }
            return;
        }
    private void args2(CommandSender sender, String[] args){
        switch (args[0]){
            case "info":
                OfflinePlayer uuid = Bukkit.getOfflinePlayer(args[1]);
                if(!LevelPoints.getPlayerStorage().hasPlayerFile(uuid.getUniqueId())){
                    sender.sendMessage(MessageUtils.getColor("Player data doesn't exist"));
                    return;
                }
                PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(uuid.getUniqueId());
                Formatter formatter = new Formatter(uuid.getName(), data.getLevel(), data.getExp(), data.getRequiredExp(), data.getPrestige(), 0, data.getProgress());
                for(String x : LevelPoints.getLangSettings().getPlayerInfo()){
                    sender.sendMessage(MessageUtils.getColor(MessageUtils.format(x, formatter)));
                }
                break;
        }
    }
}
