package me.zoon20x.levelpoints.spigot.events.CustomEvents;

import me.zoon20x.levelpoints.spigot.containers.Levels.LevelSettings;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import me.zoon20x.levelpoints.spigot.containers.Rewards.RewardData;
import me.zoon20x.levelpoints.spigot.containers.Rewards.RewardStorage;
import me.zoon20x.levelpoints.spigot.containers.Rewards.TriggerType;
import me.zoon20x.levelpoints.spigot.utils.messages.LangEventsData;
import me.zoon20x.levelpoints.spigot.utils.placeholders.LocalPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

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
        LangEventsData langEventsData = LevelPoints.getInstance().getLang().getLangEventsData("OnExp");
        if(!langEventsData.isEnabled()){
            return;
        }
        switch (langEventsData.getMessageType()){
            case ACTIONBAR:
                LevelPoints.getInstance().getMessagesUtil().sendActionBar(player, LocalPlaceholders.parse(langEventsData.getMessage(), data));
                break;
            case CHAT:
                player.sendMessage(LocalPlaceholders.parse(langEventsData.getMessage(), data));
                break;
        }

    }
    public void triggerLevelUpEvent(Player player, PlayerData data){
        LevelUpEvent levelUpEvent = new LevelUpEvent(player, data);
        Bukkit.getPluginManager().callEvent(levelUpEvent);
        RewardStorage storage = LevelPoints.getInstance().getRewardStorage();
        for(String key : storage.getRewardDataMap().keySet()){
            RewardData rewardData = storage.getReward(key);
            if(rewardData.getTriggerType() != TriggerType.LevelUp){
                continue;
            }
            if(rewardData.getTriggerValues().contains(data.getLevel()) && rewardData.getTriggerPrestige().contains(data.getPrestige())){
                rewardData.getCommands().forEach(command ->{
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), LocalPlaceholders.parse(command,key, data));
                });
            }
            if(rewardData.getMessageData().isEnabled()){
                rewardData.getMessageData().getMessages().forEach(message ->{
                    player.sendMessage(LevelPoints.getInstance().getMessagesUtil().getColor(LocalPlaceholders.parse(message, key, data)));
                });
            }
        }

    }
    public void triggerPrestigeEvent(Player player, PlayerData data){
        PrestigeEvent prestigeEvent = new PrestigeEvent(player, data);
        Bukkit.getPluginManager().callEvent(prestigeEvent);

        RewardStorage storage = LevelPoints.getInstance().getRewardStorage();
        for(String key : storage.getRewardDataMap().keySet()){
            RewardData rewardData = storage.getReward(key);
            if(rewardData.getTriggerType() != TriggerType.Prestige){
                continue;
            }
            if(rewardData.getTriggerValues().contains(data.getPrestige())){
                rewardData.getCommands().forEach(command ->{
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), LocalPlaceholders.parse(command,key, data));
                });
            }
            if(rewardData.getMessageData().isEnabled()){
                rewardData.getMessageData().getMessages().forEach(message ->{
                    player.sendMessage(LevelPoints.getInstance().getMessagesUtil().getColor(LocalPlaceholders.parse(message, key, data)));
                });
            }
        }

    }
    public void triggerFarmEvent(Block block, Player player, BlockBreakEvent event){
        FarmEvent farmEvent = new FarmEvent(block, player, event);
        Bukkit.getPluginManager().callEvent(farmEvent);

    }
}
