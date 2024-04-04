package me.zoon20x.devTools.spigot.events;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.PlayerData;
import me.zoon20x.devTools.spigot.DevInstance;
import me.zoon20x.devTools.spigot.stats.BlockStat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class DevEvents implements Listener {



    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event){
        DevInstance instance = LevelPoints.getDevInstance();
        Player player = event.getPlayer();
        if(event.isCancelled()){
            return;
        }
        BlockStat stat = instance.getBlockStat();
        PlayerData playerData = LevelPoints.getInstance().getPlayerData();
        if(playerData.getLevel() < stat.getLevelRequired()){
            event.setCancelled(true);
        }
        playerData.addEXP(stat.getExp());
        player.sendMessage(playerData.getExp() + "/" + playerData.getRequiredEXP());

    }



}
