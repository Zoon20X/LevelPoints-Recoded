package me.zoon20x.devTools.spigot.events;

import me.zoon20x.devTools.spigot.stats.BlockLoader;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.PlayerData;
import me.zoon20x.devTools.spigot.DevInstance;
import me.zoon20x.devTools.spigot.stats.BlockStat;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class DevEvents implements Listener {



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event){
        DevInstance instance = LevelPoints.getDevInstance();
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if(event.isCancelled()){
            return;
        }
        BlockLoader blockLoader = instance.getBlockLoader();
        if(!blockLoader.hasBlock(block.getType())){
            return;
        }

        BlockStat stat = blockLoader.getBlock(block.getType());
        PlayerData playerData = LevelPoints.getInstance().getPlayerData();

        if(playerData.getLevel() < stat.getBreakRequired()){
            event.setCancelled(true);
        }
        playerData.addEXP(stat.getExp());
        player.sendMessage(playerData.getExp() + "/" + playerData.getRequiredEXP());

    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event){
        DevInstance instance = LevelPoints.getDevInstance();
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        if(event.isCancelled()){
            return;
        }
        BlockLoader blockLoader = instance.getBlockLoader();
        if(!blockLoader.hasBlock(block.getType())){
            return;
        }
        BlockStat stat = blockLoader.getBlock(block.getType());
        PlayerData playerData = LevelPoints.getInstance().getPlayerData();
        if(playerData.getLevel() < stat.getPlaceRequired()){
            event.setCancelled(true);
            return;
        }

    }



}
