package me.zoon20x.levelpoints.events.CustomEvents;

import me.zoon20x.levelpoints.containers.Player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class EventUtils{


    public void triggerEXPEarn(Player player, PlayerData data, double exp, Event event){
        EarnExpEvent earnExpEvent = new EarnExpEvent(player, data, exp, event);
        Bukkit.getPluginManager().callEvent(earnExpEvent);
        if(earnExpEvent.isCancelled()){
            return;
        }
        data.addExp(exp);
        player.sendMessage(data.getExp() + "/" + data.getRequiredEXP());
    }


}
