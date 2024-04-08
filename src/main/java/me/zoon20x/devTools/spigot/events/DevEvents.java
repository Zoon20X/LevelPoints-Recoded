package me.zoon20x.devTools.spigot.events;


import me.zoon20x.devTools.spigot.gui.LpsGUI;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.utils.messages.DebugSeverity;
import me.zoon20x.levelpoints.utils.messages.LangData;
import me.zoon20x.levelpoints.utils.placeholders.LocalPlaceholders;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DevEvents implements Listener {


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if(event.getMessage().contains("/lps reload")){
            event.setCancelled(true);
            try {
                LevelPoints.getInstance().reload();
                LevelPoints.getInstance().log(DebugSeverity.NORMAL, "Reload Complete");
            } catch (IOException e) {
                LevelPoints.getInstance().log(DebugSeverity.SEVER, "Reload FAILED!");
            }


        }



        if (event.getMessage().contains("/lps info")) {
            event.setCancelled(true);
            if (!LevelPoints.getInstance().getLang().hasLangData("Info")) {
                LevelPoints.getInstance().log(DebugSeverity.WARNING,"does not contain INFO");
                return;
            }
            LangData langData = LevelPoints.getInstance().getLang().getLangData("Info");
            if (!langData.isEnabled()) {
                LevelPoints.getInstance().log(DebugSeverity.WARNING,"Info Not enabled");
                return;
            }
            if (!LevelPoints.getInstance().getPlayerStorage().hasPlayer(player.getUniqueId())) {
                LevelPoints.getInstance().log(DebugSeverity.WARNING,"player not loaded");
                return;
            }

            PlayerData playerData = LevelPoints.getInstance().getPlayerStorage().getPlayerData(player.getUniqueId());
            langData.getMessage().forEach(m -> {
                if (langData.isCenteredText()) {
                    m = LevelPoints.getInstance().getMessagesUtil().centreText(m);
                }
                player.sendMessage(LocalPlaceholders.parse(m, playerData));
            });
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        Player player = event.getPlayer();
        String key = "Help";
        List<String> message = LevelPoints.getInstance().getLang().getLangData(key).getMessage();
        message.forEach(m ->{
            player.sendMessage(m);
        });


    }


}
