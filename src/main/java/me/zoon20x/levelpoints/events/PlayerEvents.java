package me.zoon20x.levelpoints.events;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Settings.Configs.Rewards.RewardData;
import me.zoon20x.levelpoints.containers.Settings.Configs.Rewards.RewardSettings;
import me.zoon20x.levelpoints.containers.Settings.Configs.Rewards.RewardTriggerType;
import me.zoon20x.levelpoints.events.CustomEvents.EarnExpEvent;
import me.zoon20x.levelpoints.events.CustomEvents.LevelUpEvent;
import me.zoon20x.levelpoints.events.CustomEvents.PrestigeEvent;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import me.zoon20x.levelpoints.utils.Formatter;
import me.zoon20x.levelpoints.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerEvents implements Listener {


    @EventHandler
    public void onAsyncJoin(AsyncPlayerPreLoginEvent event){
        UUID uuid = event.getUniqueId();
        String name = event.getName();
        AsyncPlayerPreLoginEvent.Result result = event.getLoginResult();
        if(result != AsyncPlayerPreLoginEvent.Result.ALLOWED){
            return;
        }
        if(!LevelPoints.getPlayerStorage().hasPlayerFile(uuid)){
            LevelPoints.getPlayerGenerator().generateNewPlayer(uuid, name);
            return;
        }
        if(!LevelPoints.getPlayerStorage().hasLoadedData(uuid)){
            LevelPoints.getDebug(DebugSeverity.NORMAL, "loading " + uuid);
            LevelPoints.getPlayerGenerator().loadPlayerFile(new File(LevelPoints.getUserFolder(), uuid + ".yml"));
        }

        if(LevelPoints.isRunningSQL()){
            if(!LevelPoints.getSQL().playerExists(uuid)){
                LevelPoints.getDebug(DebugSeverity.WARNING, "player not found");
                LevelPoints.getSQL().createPlayer(uuid);
                LevelPoints.getPlayerStorage().getLoadedData(uuid).setUpdateSQL(true);
                return;
            }
            LevelPoints.getSQL().updateServerData(uuid);
            LevelPoints.getPlayerStorage().getLoadedData(uuid).setUpdateSQL(true);
            return;
        }
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        if(!LevelPoints.getPlayerStorage().hasLoadedData(event.getPlayer().getUniqueId())){
            return;
        }
        PlayerData data = LevelPoints.getPlayerStorage().getLoadedData(event.getPlayer().getUniqueId());
        if(LevelPoints.isRunningSQL()){
            new BukkitRunnable() {
                @Override
                public void run() {
                    LevelPoints.getSQL().updateSQLData(event.getPlayer().getUniqueId());

                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
            LevelPoints.getPlayerStorage().getLoadedData(event.getPlayer().getUniqueId()).setUpdateSQL(false);
        }
        LevelPoints.getPlayerGenerator().savePlayerData(data);
    }

    @EventHandler
    public void onExpEarn(EarnExpEvent event) {
        Player player = event.getPlayer();
        PlayerData data = event.getPlayerData();
    }




    @EventHandler
    public void onLevelUP(LevelUpEvent event) {
        RewardSettings rewardSettings = LevelPoints.getRewardSettings();
        if(Bukkit.getPlayer(event.getPlayerData().getUUID()) == null){
            return;
        }

        Player player = Bukkit.getPlayer(event.getPlayerData().getUUID());
        for (RewardData data : rewardSettings.getAllRewards()) {
            if (data.getType() == RewardTriggerType.LEVELUP) {
                if (data.getTriggerValues().contains(String.valueOf(event.getPlayerData().getLevel()))) {
                    PlayerData playerData = event.getPlayerData();
                    Formatter formatter = new Formatter(playerData.getName(), playerData.getLevel(), playerData.getExp(), playerData.getRequiredExp(), playerData.getPrestige(), 0, playerData.getProgress());
                    for (String x : data.getCommands()) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), MessageUtils.format(x, formatter).replace(MessageUtils.rewardPlaceholder, data.getId()));
                    }
                    if(data.isMessageEnabled()) {
                        for (String x : data.getMessageList()) {
                            String who = MessageUtils.formatRewardMessages(x).get(0);
                            String type = MessageUtils.formatRewardMessages(x).get(1);
                            String value = MessageUtils.formatRewardMessages(x).get(2).replace(MessageUtils.rewardPlaceholder, data.getId());
                            switch (who) {
                                case "PLAYER":
                                    switch (type) {
                                        case "CHAT":
                                            player.sendMessage(MessageUtils.getColor(MessageUtils.format(value, formatter)));
                                            break;
                                        case "ACTIONBAR":
                                            MessageUtils.sendActionBar(player, value);
                                            break;
                                        case "TITLE":
                                            List<String> title = Arrays.asList(value.split("/"));
                                            String top = MessageUtils.format(title.get(0), formatter);
                                            String bottom = MessageUtils.format(title.get(1), formatter);
                                            MessageUtils.sendTitle(player, MessageUtils.getColor(top), MessageUtils.getColor(bottom));
                                            break;
                                    }
                                    break;
                                case "GLOBAL":
                                    switch (type) {
                                        case "CHAT":
                                            Bukkit.getOnlinePlayers().forEach(user -> {
                                                user.sendMessage(MessageUtils.getColor(MessageUtils.format(value, formatter)));
                                            });
                                            break;
                                        case "ACTIONBAR":
                                            Bukkit.getOnlinePlayers().forEach(user -> {
                                                MessageUtils.sendActionBar(user, value);
                                            });
                                            break;
                                        case "TITLE":
                                            List<String> title = Arrays.asList(value.split("/"));
                                            String top = MessageUtils.format(title.get(0), formatter);
                                            String bottom = MessageUtils.format(title.get(1), formatter);
                                            Bukkit.getOnlinePlayers().forEach(user ->{
                                                MessageUtils.sendTitle(user, MessageUtils.getColor(top), MessageUtils.getColor(bottom));
                                            });
                                            break;
                                    }
                                    break;
                            }
                        }
                    }
                    if(data.isSoundEnabled()){
                        player.playSound(player.getLocation(), data.getSound().getSound(), data.getSound().getVolume(), data.getSound().getPitch());
                    }

                }
            }
        }
    }
    @EventHandler
    public void onPrestige(PrestigeEvent event){
        RewardSettings rewardSettings = LevelPoints.getRewardSettings();
        if(Bukkit.getPlayer(event.getPlayerData().getUUID()) == null){
            return;
        }
        Player player = Bukkit.getPlayer(event.getPlayerData().getUUID());
        for (RewardData data : rewardSettings.getAllRewards()) {
            if (data.getType() == RewardTriggerType.PRESTIGE) {
                if (data.getTriggerValues().contains(String.valueOf(event.getPlayerData().getPrestige()))) {
                    PlayerData playerData = event.getPlayerData();
                    Formatter formatter = new Formatter(playerData.getName(), playerData.getLevel(), playerData.getExp(), playerData.getRequiredExp(), playerData.getPrestige(), 0, playerData.getProgress());
                    for (String x : data.getCommands()) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), MessageUtils.format(x, formatter).replace(MessageUtils.rewardPlaceholder, data.getId()));
                    }
                    if(data.isMessageEnabled()) {
                        for (String x : data.getMessageList()) {
                            String who = MessageUtils.formatRewardMessages(x).get(0);
                            String type = MessageUtils.formatRewardMessages(x).get(1);
                            String value = MessageUtils.formatRewardMessages(x).get(2).replace(MessageUtils.rewardPlaceholder, data.getId());
                            switch (who) {
                                case "PLAYER":
                                    switch (type) {
                                        case "CHAT":
                                            player.sendMessage(MessageUtils.getColor(MessageUtils.format(value, formatter)));
                                            break;
                                        case "ACTIONBAR":
                                            MessageUtils.sendActionBar(player, value);
                                            break;
                                        case "TITLE":
                                            List<String> title = Arrays.asList(value.split("/"));
                                            String top = MessageUtils.format(title.get(0), formatter);
                                            String bottom = MessageUtils.format(title.get(1), formatter);
                                            MessageUtils.sendTitle(player, MessageUtils.getColor(top), MessageUtils.getColor(bottom));
                                            break;
                                    }
                                    break;
                                case "GLOBAL":
                                    switch (type) {
                                        case "CHAT":
                                            Bukkit.getOnlinePlayers().forEach(user -> {
                                                user.sendMessage(MessageUtils.getColor(MessageUtils.format(value, formatter)));
                                            });
                                            break;
                                        case "ACTIONBAR":
                                            Bukkit.getOnlinePlayers().forEach(user -> {
                                                MessageUtils.sendActionBar(user, value);
                                            });
                                            break;
                                        case "TITLE":
                                            List<String> title = Arrays.asList(value.split("/"));
                                            String top = MessageUtils.format(title.get(0), formatter);
                                            String bottom = MessageUtils.format(title.get(1), formatter);
                                            Bukkit.getOnlinePlayers().forEach(user ->{
                                                MessageUtils.sendTitle(user, MessageUtils.getColor(top), MessageUtils.getColor(bottom));
                                            });
                                            break;
                                    }
                                    break;
                            }
                        }
                    }
                    if(data.isSoundEnabled()){
                        player.playSound(player.getLocation(), data.getSound().getSound(), data.getSound().getVolume(), data.getSound().getPitch());
                    }
                }
            }
        }
    }


}
