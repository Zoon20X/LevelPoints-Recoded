package me.zoon20x.levelpoints.spigot.commands;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.commands.TabComplete.AdminLpsTabComplete;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import me.zoon20x.levelpoints.spigot.utils.messages.LangChildData;
import me.zoon20x.levelpoints.spigot.utils.messages.LangData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class AdminLps implements CommandExecutor {
    private AdminLpsCommandUtils adminLpsCommandUtils;


    public AdminLps(LevelPoints levelPoints){
        levelPoints.getCommand("adminlps").setExecutor(this);
        levelPoints.getCommand("adminlps").setTabCompleter(new AdminLpsTabComplete());
        this.adminLpsCommandUtils = new AdminLpsCommandUtils();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0){
            String key = "HelpAdmin";
            if(!sender.hasPermission("lps.admin")){
                sender.sendMessage(LevelPoints.getInstance().getLang().getLangData(key).getNoPermission());
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
            return true;
        }
        if(args.length == 4){
            args4(sender, args);
        }
        return true;
    }

    private void args1(CommandSender sender, String[] args){
        if(args[0].equalsIgnoreCase("reload")){
            if(!sender.hasPermission("lps.admin.reload")){
                LangChildData langChildData = LevelPoints.getInstance().getLang().getLangData("Reload").getChildData("NoPermission");
                sender.sendMessage(langChildData.getMessage());
                return;
            }
            try {
                LevelPoints.getInstance().reload();
                LangData langData = LevelPoints.getInstance().getLang().getLangData("Reload");
                langData.getMessage().forEach(m -> {
                    if (langData.isCenteredText()) {
                        m = LevelPoints.getInstance().getMessagesUtil().centreText(m);
                    }
                    sender.sendMessage(m);
                });
                LevelPoints.getInstance().log(DebugSeverity.NORMAL, "Reload Complete");
            } catch (IOException e) {
                LevelPoints.getInstance().log(DebugSeverity.SEVER, "Reload FAILED!");
            }
        }
    }
    private void args4(CommandSender sender, String[] args){
        if(args[0].equalsIgnoreCase("exp")){
            double val = Double.parseDouble(args[3]);
            String player = args[2];
            adminLpsCommandUtils.sendEXPUpdate(sender, player, val, UpdateType.valueOf(args[1].toUpperCase()));
            return;
        }
        if(args[0].equalsIgnoreCase("level")){
            int val = Integer.parseInt(args[3]);
            String player = args[2];
            adminLpsCommandUtils.sendLevelUpdate(sender, player, val, UpdateType.valueOf(args[1].toUpperCase()));
            return;
        }
        if(args[0].equalsIgnoreCase("prestige")){
            int val = Integer.parseInt(args[3]);
            String player = args[2];
            adminLpsCommandUtils.sendPrestigeUpdate(sender, player, val, UpdateType.valueOf(args[1].toUpperCase()));
            return;
        }
    }
}
