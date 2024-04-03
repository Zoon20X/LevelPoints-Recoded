package me.zoon20x.levelpoints.events;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EXPEarnEvents implements Listener {

    private LevelPoints levelPoints;
    private PlayerData playerData;

    public EXPEarnEvents(LevelPoints levelPoints){
        this.levelPoints = levelPoints;
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event){
        this.playerData = new PlayerData(event.getUniqueId());
    }



    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();
        levelPoints.getEventUtils().triggerEXPEarn(player, playerData, 1.0, event);


    }


}
