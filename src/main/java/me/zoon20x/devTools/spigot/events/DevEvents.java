package me.zoon20x.devTools.spigot.events;


import me.zoon20x.devTools.spigot.gui.LpsGUI;
import me.zoon20x.levelpoints.LevelPoints;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;
import java.util.List;

public class DevEvents implements Listener {


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
