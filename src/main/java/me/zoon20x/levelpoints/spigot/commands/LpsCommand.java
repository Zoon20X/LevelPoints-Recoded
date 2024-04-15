package me.zoon20x.levelpoints.spigot.commands;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.commands.TabComplete.LpsTabComplete;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import me.zoon20x.levelpoints.spigot.containers.Top.TopData;
import me.zoon20x.levelpoints.spigot.containers.Top.TopSettings;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import me.zoon20x.levelpoints.spigot.utils.messages.LangData;
import me.zoon20x.levelpoints.spigot.utils.placeholders.LocalPlaceholders;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class LpsCommand implements CommandExecutor {
    private lpsCommandUtils lpsCommandUtils;

    public LpsCommand(LevelPoints levelPoints){
        levelPoints.getCommand("lps").setExecutor(this);
        levelPoints.getCommand("lps").setTabCompleter(new LpsTabComplete());
        this.lpsCommandUtils = new lpsCommandUtils();
    }


    @Override
    public boolean onCommand( CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;
        if(args.length == 0){
            String key = "Help";
            List<String> message = LevelPoints.getInstance().getLang().getLangData(key).getMessage();
            message.forEach(m ->{
                sender.sendMessage(m);
            });
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
        if(!(sender instanceof Player)){
            LevelPoints.getInstance().log(DebugSeverity.SEVER, "Warning!, these commands can only be executed by a player");
            return;
        }
        Player player = (Player) sender;
        if(args[0].equalsIgnoreCase("info")){
            lpsCommandUtils.sendInfo(sender, LevelPoints.getInstance().getPlayerStorage().getPlayerData(player.getUniqueId()));
        }
        if(args[0].equalsIgnoreCase("top")){
            lpsCommandUtils.sendTop(sender, 1);
        }
    }
    private void args2(CommandSender sender, String[] args){
        if(!(sender instanceof Player)){
            LevelPoints.getInstance().log(DebugSeverity.SEVER, "Warning!, these commands can only be executed by a player");
            return;
        }
        if(args[0].equalsIgnoreCase("top")){
            try {
                int i = Integer.parseInt(args[1]);
                if(i < LevelPoints.getInstance().getTopSettings().getMaxPages()) {
                    lpsCommandUtils.sendTop(sender, i);
                }else{
                    lpsCommandUtils.sendTop(sender, LevelPoints.getInstance().getTopSettings().getMaxPages());
                }
            }catch (Exception e){
                sender.sendMessage(LevelPoints.getInstance().getMessagesUtil().getColor("&4LevelPoints>> &cSorry but this requires a number input!"));
            }


        }
    }




}
