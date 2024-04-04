package me.zoon20x.levelpoints.devTools.events;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.PlayerData;
import me.zoon20x.levelpoints.devTools.DevInstance;
import me.zoon20x.levelpoints.devTools.stats.BlockStat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class DevEvents implements Listener {



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event){
        DevInstance instance = LevelPoints.getDevInstance();
        if(event.isCancelled()){
            return;
        }
        BlockStat stat = instance.getBlockStat();
        PlayerData playerData = LevelPoints.getInstance().getPlayerData();
        if(playerData.getLevel() < stat.getLevelRequired()){
            event.setCancelled(true);
        }
        playerData.addEXP(stat.getExp());

    }



}
