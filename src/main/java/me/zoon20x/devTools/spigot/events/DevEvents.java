package me.zoon20x.devTools.spigot.events;


import me.zoon20x.devTools.spigot.gui.LpsGUI;
import me.zoon20x.spigot.levelpoints.LevelPoints;
import me.zoon20x.spigot.levelpoints.containers.Player.PlayerData;
import me.zoon20x.spigot.levelpoints.utils.messages.DebugSeverity;
import me.zoon20x.spigot.levelpoints.utils.messages.LangData;
import me.zoon20x.spigot.levelpoints.utils.placeholders.LocalPlaceholders;
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
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Block block = event.getBlock();
        Player player = event.getPlayer();


    }


}
