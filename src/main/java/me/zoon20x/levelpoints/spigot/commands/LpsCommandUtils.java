package me.zoon20x.levelpoints.spigot.commands;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import me.zoon20x.levelpoints.spigot.containers.Top.TopData;
import me.zoon20x.levelpoints.spigot.containers.Top.TopSettings;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import me.zoon20x.levelpoints.spigot.utils.messages.LangData;
import me.zoon20x.levelpoints.spigot.utils.placeholders.LocalPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class LpsCommandUtils {


    private PlayerData loadDataFromString(String name){
        PlayerData data = null;
        if (Bukkit.getPlayer(name) == null) {
            data = LevelPoints.getInstance().getPlayerStorage().loadOfflinePlayer(Bukkit.getOfflinePlayer(name).getUniqueId(), name);
        } else {
            data = LevelPoints.getInstance().getPlayerStorage().getPlayerData(Bukkit.getPlayer(name).getUniqueId());
        }
        return data;

    }

    protected void sendInfo(CommandSender sender, String name){
        PlayerData data = loadDataFromString(name);
        if(data ==  null){
            sender.sendMessage(LevelPoints.getInstance().getMessagesUtil().getColor("&4LevelPoints>> &cSorry but that player does not exist"));
            return;
        }

        if (!LevelPoints.getInstance().getLang().hasLangData("Info")) {
            LevelPoints.getInstance().log(DebugSeverity.WARNING,"does not contain INFO");
            return;
        }
        LangData langData = LevelPoints.getInstance().getLang().getLangData("Info");
        if (!langData.isEnabled()) {
            LevelPoints.getInstance().log(DebugSeverity.WARNING,"Info Not enabled");
            return;
        }
        if(!sender.hasPermission("lps.player.info")){
            sender.sendMessage(langData.getNoPermission());
            return;
        }
        langData.getMessage().forEach(m -> {
            if (langData.isCenteredText()) {
                m = LevelPoints.getInstance().getMessagesUtil().centreText(m);
            }
            sender.sendMessage(LocalPlaceholders.parse(m, data));
        });
    }
    protected void sendTop(CommandSender sender, int val){
        TopSettings topSettings = LevelPoints.getInstance().getTopSettings();
        LangData langData = LevelPoints.getInstance().getLang().getLangData("Top");
        if(!langData.isEnabled()){
            return;
        }
        if(!sender.hasPermission("lps.player.top")){
            sender.sendMessage(langData.getNoPermission());
            return;
        }
        int maxSlots = LevelPoints.getInstance().getTopSettings().getMaxSlots();
        for(String message : langData.getMessage()){
            if(!message.contains("{lps_top_position}")){
                sender.sendMessage(LocalPlaceholders.parse(message, "","", 0, val));
                continue;
            }
            int add = 0;
            if(val>1){
                add = maxSlots*(val - 1);
            }
            for (int i = add; i < maxSlots*val ; i++) {
                if (i >= topSettings.getTopDataList().size()) {
                    sender.sendMessage(LocalPlaceholders.parse(message, "", "", (i + 1), val));
                } else {
                    TopData topData = topSettings.getTopDataList().get(i);

                    sender.sendMessage(LocalPlaceholders.parse(message, topData.getLevel(), topData.getName(), (i + 1), val));
                }
            }
        }
    }
}
