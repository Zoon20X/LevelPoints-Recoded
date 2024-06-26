package me.zoon20x.levelpoints.spigot.commands;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import me.zoon20x.levelpoints.spigot.utils.messages.LangChildData;
import me.zoon20x.levelpoints.spigot.utils.messages.LangData;
import me.zoon20x.levelpoints.spigot.utils.placeholders.LocalPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class AdminLpsCommandUtils {


    private PlayerData loadDataFromString(String name){
        PlayerData data = null;
        if (Bukkit.getPlayer(name) == null) {
            data = LevelPoints.getInstance().getPlayerStorage().loadOfflinePlayer(Bukkit.getOfflinePlayer(name).getUniqueId(), name);
        } else {
            data = LevelPoints.getInstance().getPlayerStorage().getPlayerData(Bukkit.getPlayer(name).getUniqueId());
        }
        return data;

    }

    private void sendUpdateMessage(CommandSender sender, PlayerData data, String updateMessage){
        LangData langData = LevelPoints.getInstance().getLang().getLangData(updateMessage);
        System.out.println(langData.isEnabled());
        if (!langData.isEnabled()) {
            return;
        }
        langData.getMessage().forEach(m -> {
            if (langData.isCenteredText()) {
                m = LevelPoints.getInstance().getMessagesUtil().centreText(m);
            }
            sender.sendMessage(LocalPlaceholders.parse(m, data));
        });
    }

    protected void sendLevelUpdate(CommandSender sender, String name, int value, UpdateType type){
        PlayerData data = loadDataFromString(name);
        if(data ==  null){
            sender.sendMessage(LevelPoints.getInstance().getMessagesUtil().getColor("&4LevelPoints>> &cSorry but that player does not exist"));
            return;
        }
        LangChildData langChildData = LevelPoints.getInstance().getLang().getLangData("Level").getChildData("NoPermission");
        switch (type){
            case ADD:
                if (!sender.hasPermission("lps.admin.level.add")) {
                    if(!langChildData.isEnabled()){
                        return;
                    }
                    sender.sendMessage(langChildData.getMessage());
                    return;
                }
                data.addLevel(value);
                sendUpdateMessage(sender, data, "Level");
                break;
            case REMOVE:
                if (!sender.hasPermission("lps.admin.level.remove")) {
                    if(!langChildData.isEnabled()){
                        return;
                    }
                    sender.sendMessage(langChildData.getMessage());
                    return;
                }
                boolean remove = data.removeLevel(value);
                if(!remove){
                    sender.sendMessage(LevelPoints.getInstance().getMessagesUtil().getColor("&4LevelPoints>> &cSorry but the player is too low of a level to remove that amount"));
                    break;
                }
                sendUpdateMessage(sender, data, "Level");
                break;
            case SET:
                if (!sender.hasPermission("lps.admin.level.set")) {
                    if(!langChildData.isEnabled()){
                        return;
                    }
                    sender.sendMessage(langChildData.getMessage());
                    return;
                }
                data.setLevel(value);
                sendUpdateMessage(sender, data, "Level");
                break;
        }
    }

    protected void sendEXPUpdate(CommandSender sender, String name, double value, UpdateType type){
        PlayerData data = loadDataFromString(name);
        if(data ==  null){
            sender.sendMessage(LevelPoints.getInstance().getMessagesUtil().getColor("&4LevelPoints>> &cSorry but that player does not exist"));
            return;
        }
        LangChildData langChildData = LevelPoints.getInstance().getLang().getLangData("Exp").getChildData("NoPermission");
        switch (type){
            case ADD:
                if (!sender.hasPermission("lps.admin.exp.add")) {
                    if(!langChildData.isEnabled()){
                        return;
                    }
                    sender.sendMessage(langChildData.getMessage());
                    return;
                }
                data.addExp(value);
                sendUpdateMessage(sender, data, "Exp");
                break;
            case REMOVE:
                if (!sender.hasPermission("lps.admin.exp.remove")) {
                    if(!langChildData.isEnabled()){
                        return;
                    }
                    sender.sendMessage(langChildData.getMessage());
                    return;
                }
                boolean remove = data.removeExp(value);
                if(!remove){
                    sender.sendMessage(LevelPoints.getInstance().getMessagesUtil().getColor("&4LevelPoints>> &cSorry but the player does not have enough exp to remove that amount"));
                    break;
                }
                sendUpdateMessage(sender, data, "Exp");
                break;
            case SET:
                if (!sender.hasPermission("lps.admin.exp.set")) {
                    if(!langChildData.isEnabled()){
                        return;
                    }
                    sender.sendMessage(langChildData.getMessage());
                    return;
                }
                data.setExp(value);
                sendUpdateMessage(sender, data, "Exp");
                break;
        }
    }
    protected void sendPrestigeUpdate(CommandSender sender, String name, int value, UpdateType type){
        PlayerData data = loadDataFromString(name);
        if(data ==  null){
            sender.sendMessage(LevelPoints.getInstance().getMessagesUtil().getColor("&4LevelPoints>> &cSorry but that player does not exist"));
            return;
        }
        LangChildData langChildData = LevelPoints.getInstance().getLang().getLangData("Prestige").getChildData("NoPermission");
        switch (type){
            case ADD:
                if (!sender.hasPermission("lps.admin.prestige.add")) {
                    if(!langChildData.isEnabled()){
                        return;
                    }
                    sender.sendMessage(langChildData.getMessage());
                    return;
                }
                data.addPrestige(value);
                sendUpdateMessage(sender, data, "Prestige");
                break;
            case REMOVE:
                if (!sender.hasPermission("lps.admin.prestige.remove")) {
                    if(!langChildData.isEnabled()){
                        return;
                    }
                    sender.sendMessage(langChildData.getMessage());
                    return;
                }
                boolean remove = data.removePrestige(value);
                if(!remove){
                    sender.sendMessage(LevelPoints.getInstance().getMessagesUtil().getColor("&4LevelPoints>> &cSorry but the player is too low of a prestige to remove that amount"));
                    break;
                }
                sendUpdateMessage(sender, data, "Prestige");
                break;
            case SET:
                if (!sender.hasPermission("lps.admin.prestige.set")) {
                    if(!langChildData.isEnabled()){
                        return;
                    }
                    sender.sendMessage(langChildData.getMessage());
                    return;
                }
                data.setPrestige(value, true);
                sendUpdateMessage(sender, data, "Prestige");
                break;
        }
    }

}
