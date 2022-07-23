package me.zoon20x.levelpoints.events;

import me.clip.placeholderapi.PlaceholderAPI;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Settings.Configs.PvpBracketData;
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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.nio.file.FileStore;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlayerEvents implements Listener {


    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player)){
            return;
        }
        if(!(event.getEntity() instanceof Player)){
            return;
        }
        if(!LevelPoints.getInstance().getPvpSettings().isPvpBracketsEnabled()){
            return;
        }
        Player playera = (Player) event.getEntity();
        PlayerData dataa = LevelPoints.getInstance().getPlayerStorage().getLoadedData(playera.getUniqueId());
        Player playerb = (Player) event.getDamager();
        PlayerData datab = LevelPoints.getInstance().getPlayerStorage().getLoadedData(playerb.getUniqueId());
        if(datab.getBracketData() == null && dataa.getBracketData() == null){
            return;
        }
        if(datab.getBracketData() == null && dataa.getBracketData() != null){
            if(LevelPoints.getInstance().getPvpSettings().isDifferentMessageEnabled()){
                playerb.sendMessage(MessageUtils.getColor(LevelPoints.getInstance().getPvpSettings().getDifferentMessageText()));
            }
            event.setCancelled(true);
            return;
        }
        if(datab.getBracketData() != null && dataa.getBracketData() == null){
            if(LevelPoints.getInstance().getPvpSettings().isDifferentMessageEnabled()){
                playerb.sendMessage(MessageUtils.getColor(LevelPoints.getInstance().getPvpSettings().getDifferentMessageText()));
            }
            event.setCancelled(true);
            return;
        }


        PvpBracketData pvpa = dataa.getBracketData();
        PvpBracketData pvpb = datab.getBracketData();
        if(!pvpb.isPvpEnabled()){
            if(LevelPoints.getInstance().getPvpSettings().isNoPvpMessageEnabled()){
                playerb.sendMessage(MessageUtils.getColor(LevelPoints.getInstance().getPvpSettings().getNoPvpMessageText()));
            }

            event.setCancelled(true);
            return;
        }
        if(!pvpa.isPvpEnabled()){
            if(LevelPoints.getInstance().getPvpSettings().isNoPvpMessageEnabled()){
                playerb.sendMessage(MessageUtils.getColor(LevelPoints.getInstance().getPvpSettings().getNoPvpMessageText()));
            }
            event.setCancelled(true);

            return;
        }
        if(!pvpb.getId().equalsIgnoreCase(pvpa.getId())){
            if(LevelPoints.getInstance().getPvpSettings().isDifferentMessageEnabled()){
                playerb.sendMessage(MessageUtils.getColor(LevelPoints.getInstance().getPvpSettings().getDifferentMessageText()));
            }
            event.setCancelled(true);
            return;
        }


    }

    @EventHandler
    public void onAsyncJoin(AsyncPlayerPreLoginEvent event){
        UUID uuid = event.getUniqueId();
        String name = event.getName();
        AsyncPlayerPreLoginEvent.Result result = event.getLoginResult();
        if(result != AsyncPlayerPreLoginEvent.Result.ALLOWED){
            return;
        }


        if(!LevelPoints.getInstance().getPlayerStorage().hasPlayerFile(uuid)){
            LevelPoints.getInstance().getPlayerGenerator().generateNewPlayer(uuid, name);
            return;
        }

        if(!LevelPoints.getInstance().getPlayerStorage().hasLoadedData(uuid)){
            LevelPoints.getDebug(DebugSeverity.NORMAL, "loading " + uuid);
            LevelPoints.getInstance().getPlayerGenerator().loadPlayerFile(new File(LevelPoints.getInstance().getUserFolder(), uuid + ".yml"));
        }

        if(LevelPoints.getInstance().isRunningSQL()){
            if(!LevelPoints.getInstance().getSQL().playerExists(uuid)){
                LevelPoints.getDebug(DebugSeverity.WARNING, "player not found");
                LevelPoints.getInstance().getSQL().createPlayer(uuid);
                LevelPoints.getInstance().getPlayerStorage().getLoadedData(uuid).setUpdateSQL(true);
                return;
            }
            LevelPoints.getInstance().getSQL().updateServerData(uuid);
            LevelPoints.getInstance().getPlayerStorage().getLoadedData(uuid).setUpdateSQL(true);
            return;
        }

    }



    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        if(!LevelPoints.getInstance().getPlayerStorage().hasLoadedData(event.getPlayer().getUniqueId())){
            return;
        }
        PlayerData data = LevelPoints.getInstance().getPlayerStorage().getLoadedData(event.getPlayer().getUniqueId());
        if(LevelPoints.getInstance().isRunningSQL()){
            new BukkitRunnable() {
                @Override
                public void run() {
                    LevelPoints.getInstance().getSQL().updateSQLData(event.getPlayer().getUniqueId());

                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
            LevelPoints.getInstance().getPlayerStorage().getLoadedData(event.getPlayer().getUniqueId()).setUpdateSQL(false);
        }
        LevelPoints.getInstance().getPlayerGenerator().savePlayerData(data);
    }

    @EventHandler
    public void onExpEarn(EarnExpEvent event) {
        Player player = event.getPlayer();
        PlayerData data = event.getPlayerData();


    }



    @EventHandler
    public void onLevelUP(LevelUpEvent event) {
        RewardSettings rewardSettings = LevelPoints.getInstance().getRewardSettings();
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
        RewardSettings rewardSettings = LevelPoints.getInstance().getRewardSettings();
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
