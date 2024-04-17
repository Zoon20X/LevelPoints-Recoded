package me.zoon20x.levelpoints.spigot.commands;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.commands.TabComplete.LpsTabComplete;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import me.zoon20x.levelpoints.spigot.utils.messages.LangChildData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class LpsCommand implements CommandExecutor {
    private LpsCommandUtils lpsCommandUtils;

    public LpsCommand(LevelPoints levelPoints){
        levelPoints.getCommand("lps").setExecutor(this);
        levelPoints.getCommand("lps").setTabCompleter(new LpsTabComplete());
        this.lpsCommandUtils = new LpsCommandUtils();
    }


    @Override
    public boolean onCommand( CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;

        if(args.length == 0){
            String key = "Help";
            if(!player.hasPermission("lps.player")){
                LangChildData langChildData = LevelPoints.getInstance().getLang().getLangData(key).getChildData("NoPermission");
                if(!langChildData.isEnabled()){
                    return true;
                }
                sender.sendMessage(langChildData.getMessage());
                return true;
            }
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
            lpsCommandUtils.sendInfo(sender, player.getName());
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
        if(args[0].equalsIgnoreCase("info")){
            String name = args[1];
            lpsCommandUtils.sendInfo(sender, name);
        }
    }




}
