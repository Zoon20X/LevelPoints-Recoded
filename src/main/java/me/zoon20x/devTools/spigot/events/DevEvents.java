package me.zoon20x.devTools.spigot.events;


import me.zoon20x.devTools.spigot.gui.LpsGUI;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.utils.messages.LangData;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;

public class DevEvents implements Listener {


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (event.getMessage().contains("lps info")) {
            event.setCancelled(true);
            if (!LevelPoints.getInstance().getLangSettings().hasLangData("Info")) {
                return;
            }
            LangData langData = LevelPoints.getInstance().getLangSettings().getLangData("Info");
            if (langData.isEnabled()) {
                return;
            }
            if (!LevelPoints.getInstance().getPlayerStorage().hasPlayer(player.getUniqueId())) {
                return;
            }

            PlayerData playerData = LevelPoints.getInstance().getPlayerStorage().getPlayerData(player.getUniqueId());
            langData.getMessage().forEach(m -> {
                if (langData.isCenteredText()) {
                    m = LevelPoints.getInstance().getMessagesUtil().centreText(m);
                }
                player.sendMessage(m);
            });
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        Player player = event.getPlayer();
        String key = "Help";
        List<String> message = LevelPoints.getInstance().getLangSettings().getLangData(key).getMessage();
        message.forEach(m ->{
            player.sendMessage(m);
        });


    }


}
