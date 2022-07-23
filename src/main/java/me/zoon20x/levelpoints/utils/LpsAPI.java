package me.zoon20x.levelpoints.utils;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.LpsPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LpsAPI {


    public LpsPlayer getPlayer(Player player){
        return LevelPoints.getInstance().getPlayerStorage().getLoadedData(player.getUniqueId());
    }
    public LpsPlayer getPlayer(UUID uuid){
        return LevelPoints.getInstance().getPlayerStorage().getLoadedData(uuid);
    }


}
