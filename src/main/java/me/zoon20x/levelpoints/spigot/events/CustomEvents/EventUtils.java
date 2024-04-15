package me.zoon20x.levelpoints.spigot.events.CustomEvents;

import me.zoon20x.levelpoints.spigot.containers.Levels.LevelSettings;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
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
        LevelSettings levelSettings = LevelPoints.getInstance().getLpsSettings().getLevelSettings();
        if(data.isMax()){
            return;
        }
        data.addExp(exp);
        player.sendMessage(data.getExp() + "/" + data.getRequiredEXP());
    }
    public void triggerLevelUpEvent(Player player, PlayerData data){
        LevelUpEvent levelUpEvent = new LevelUpEvent(player, data);
        Bukkit.getPluginManager().callEvent(levelUpEvent);

    }

}