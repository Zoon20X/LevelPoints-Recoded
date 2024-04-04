package me.zoon20x.levelpoints.events;

import me.zoon20x.devTools.spigot.DevInstance;
import me.zoon20x.devTools.spigot.stats.BlockLoader;
import me.zoon20x.devTools.spigot.stats.BlockStat;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Blocks.BlockData;
import me.zoon20x.levelpoints.containers.Blocks.BlockSettings;
import me.zoon20x.levelpoints.containers.PlayerData;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EXPEarnEvents implements Listener {

    private LevelPoints levelPoints;


    public EXPEarnEvents(LevelPoints levelPoints){
        this.levelPoints = levelPoints;
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event){
        LevelPoints.getInstance().setPlayerData(new PlayerData(event.getUniqueId()));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event){
        if(event.isCancelled()){
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlock();
        BlockSettings blockSettings = LevelPoints.getInstance().getBlockSettings();
        if(!blockSettings.isEnabled()){
            return;
        }

        if(!blockSettings.hasBlock(block.getType())){
            return;
        }

        BlockData blockData = blockSettings.getBlockData(block.getType());
        PlayerData playerData = LevelPoints.getInstance().getPlayerData();

        if(playerData.getLevel() < blockData.getBreakLevelRequired()){
            event.setCancelled(true);

            return;
        }


        playerData.addEXP(blockData.getExp());
        player.sendMessage(playerData.getExp() + "/" + playerData.getRequiredEXP());

    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent event){
        if(event.isCancelled()){
            return;
        }
        BlockSettings blockSettings = LevelPoints.getInstance().getBlockSettings();
        if(!blockSettings.isEnabled()){
            return;
        }

        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();


        if(!blockSettings.hasBlock(block.getType())){
            return;
        }
        BlockData blockData = blockSettings.getBlockData(block.getType());
        PlayerData playerData = LevelPoints.getInstance().getPlayerData();
        if(playerData.getLevel() < blockData.getPlaceLevelRequired()){
            event.setCancelled(true);
            return;
        }

    }


}
