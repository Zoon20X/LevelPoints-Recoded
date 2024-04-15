package me.zoon20x.levelpoints.spigot.commands;

import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.commands.TabComplete.LpsTabComplete;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import me.zoon20x.levelpoints.spigot.utils.messages.LangData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class AdminLps implements CommandExecutor {

    public AdminLps(LevelPoints levelPoints){
        levelPoints.getCommand("adminlps").setExecutor(this);
        levelPoints.getCommand("adminlps").setTabCompleter(new LpsTabComplete());
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, @NotNull String s, String[] args) {
        if(args.length == 0){
            String key = "HelpAdmin";
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
        return true;
    }

    private void args1(CommandSender sender, String[] args){
        if(args[0].equalsIgnoreCase("reload")){
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

}
