package me.zoon20x.levelpoints.commands;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.commands.TabComplete.LpsTabComplete;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.utils.messages.DebugSeverity;
import me.zoon20x.levelpoints.utils.messages.LangData;
import me.zoon20x.levelpoints.utils.placeholders.LocalPlaceholders;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("info")){
                sendInfo(sender, LevelPoints.getInstance().getPlayerStorage().getPlayerData(player.getUniqueId()));
            }
        }
        return true;
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
