package me.zoon20x.levelpoints.commands;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.commands.TabComplete.LpsTabComplete;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Top.TopData;
import me.zoon20x.levelpoints.containers.Top.TopSettings;
import me.zoon20x.levelpoints.utils.messages.DebugSeverity;
import me.zoon20x.levelpoints.utils.messages.LangData;
import me.zoon20x.levelpoints.utils.placeholders.LocalPlaceholders;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class LpsCommand implements CommandExecutor {

    public LpsCommand(LevelPoints levelPoints){
        levelPoints.getCommand("lps").setExecutor(this);
        levelPoints.getCommand("lps").setTabCompleter(new LpsTabComplete());
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
            if(args[0].equalsIgnoreCase("info")){
                sendInfo(sender, LevelPoints.getInstance().getPlayerStorage().getPlayerData(player.getUniqueId()));
                return true;
            }
            if(args[0].equalsIgnoreCase("reload")){
                try {
                    LevelPoints.getInstance().reload();
                    LevelPoints.getInstance().log(DebugSeverity.NORMAL, "Reload Complete");
                } catch (IOException e) {
                    LevelPoints.getInstance().log(DebugSeverity.SEVER, "Reload FAILED!");
                }
                return true;
            }
            if(args[0].equalsIgnoreCase("top")){
                sendTop(sender, 1);
            }
        }
        if(args.length == 2){
            if(args[0].equalsIgnoreCase("top")){
                int val = Integer.parseInt(args[1]);
                sendTop(sender, val);
            }
        }
        return true;
    }

    private void sendTop(CommandSender sender, int val){
        TopSettings topSettings = LevelPoints.getInstance().getTopSettings();
        LangData langData = LevelPoints.getInstance().getLang().getLangData("Top");
        if(!langData.isEnabled()){
            return;
        }

        for(String message : langData.getMessage()){
            if(!message.contains("{lps_top_position}")){
                sender.sendMessage(LocalPlaceholders.parse(message, 0,"", 0, val));
                continue;
            }
            if(topSettings.getTopDataList().size() <10){
                for (int i = 0; i < topSettings.getTopDataList().size() ; i++) {
                    TopData topData = topSettings.getTopDataList().get(i);
                    sender.sendMessage(LocalPlaceholders.parse(message, topData.getLevel(), topData.getName(), (i + 1), val));
                }
                return;
            }
            int add = 0;
            if(val>1){
                add = 10*(val - 1);
            }
            for (int i = add; i < 10*val ; i++) {
                if (i >= topSettings.getTopDataList().size()) {
                    sender.sendMessage(LocalPlaceholders.parse(message, 0, "", (i + 1), val));
                } else {
                    TopData topData = topSettings.getTopDataList().get(i);

                    sender.sendMessage(LocalPlaceholders.parse(message, topData.getLevel(), topData.getName(), (i + 1), val));
                }
            }
        }
    }

    private void sendInfo(CommandSender sender, PlayerData playerData){
        if (!LevelPoints.getInstance().getLang().hasLangData("Info")) {
            LevelPoints.getInstance().log(DebugSeverity.WARNING,"does not contain INFO");
            return;
        }
        LangData langData = LevelPoints.getInstance().getLang().getLangData("Info");
        if (!langData.isEnabled()) {
            LevelPoints.getInstance().log(DebugSeverity.WARNING,"Info Not enabled");
            return;
        }

        langData.getMessage().forEach(m -> {
            if (langData.isCenteredText()) {
                m = LevelPoints.getInstance().getMessagesUtil().centreText(m);
            }
            sender.sendMessage(LocalPlaceholders.parse(m, playerData));
        });
    }
}
