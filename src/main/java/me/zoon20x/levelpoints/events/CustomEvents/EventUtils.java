package me.zoon20x.levelpoints.events.CustomEvents;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.containers.Settings.Configs.TopListSettings;
import me.zoon20x.levelpoints.containers.Settings.WorldGuardSettings;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import me.zoon20x.levelpoints.utils.Formatter;
import me.zoon20x.levelpoints.utils.MessageUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EventUtils {


    public static void triggerEarnExpEvent(PlayerData data, Event event, Double amount, Player player, EarnTask task) {
        EarnExpEvent expEvent = new EarnExpEvent(data, event, amount, player, task);
        Bukkit.getPluginManager().callEvent(expEvent);
        if (!expEvent.isCancelled()) {
            Location location = null;
            if(player != null) {
                if (event instanceof BlockBreakEvent) {
                    location = ((BlockBreakEvent) event).getBlock().getLocation();
                    if(LevelPoints.getInstance().getAntiAbuseSettings().isSilkEnabled()) {
                        if (player.getInventory().getItemInMainHand() != null) {
                            if (player.getInventory().getItemInMainHand().hasItemMeta()) {
                                if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) {
                                    return;
                                }
                            }
                        }
                    }
                }
                if (location != null) {
                    if (LevelPoints.getInstance().getAntiAbuseSettings().isDenyEarnEnabled()) {
                        String name = WorldGuardSettings.getRegionName(location);
                        if (LevelPoints.getInstance().getAntiAbuseSettings().getRegionsDeny().contains(name)) {
                            return;
                        }
                    }
                }
            }

            data.addEXP(amount * data.getActiveBooster().getMultiplier());
            if(LevelPoints.getInstance().getConfigSettings().isOnExpEnabled()){
                Formatter formatter = new Formatter(player.getName(), data.getLevel(), data.getExp(), data.getRequiredExp(), data.getPrestige(), 0, data.getProgress());
                player.sendActionBar(MiniMessage.miniMessage().deserialize(
                        MessageUtils.format(LevelPoints.getInstance().getConfigSettings().getOnExpMessage(), formatter)));
            }
        }
        if(!LevelPoints.getInstance().isRunningSQL()) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {

                LevelPoints.getInstance().getSQL().updateSQLData(data.getUUID());
            }
        }.runTaskAsynchronously(LevelPoints.getInstance());


    }

    public static void triggerLevelUpEvent(int level, PlayerData data) {

        LevelUpEvent event = new LevelUpEvent(level, data);
        Bukkit.getPluginManager().callEvent(event);
        LevelPoints.getInstance().getTopListSettings().modifyLevel(data);
        LevelPoints.getInstance().getTopListSettings().generateTopCache(50);

        if(LevelPoints.getInstance().getLevelColorSettings() != null)
            data.setLevelColor(
                    LevelPoints.getInstance().getLevelColorSettings().getLevelColor(level)
            );

        if(!LevelPoints.getInstance().isRunningSQL()) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                LevelPoints.getInstance().getSQL().updateSQLData(data.getUUID());

            }
        }.runTaskAsynchronously(LevelPoints.getInstance());
    }

    public static void triggerPrestigeUpEvent(int prestige, PlayerData data) {

        PrestigeEvent event = new PrestigeEvent(prestige, data);
        Bukkit.getPluginManager().callEvent(event);

        if (Bukkit.getPlayer(data.getUUID()) == null) {
            return;
        }
        if (!LevelPoints.getInstance().getLangSettings().isPrestigeCommandApprovedEnabled()) {
            return;
        }
        Player player = Bukkit.getPlayer(data.getUUID());
        Formatter formatter = new Formatter(data.getName(), data.getLevel(), data.getExp(), data.getRequiredExp(), data.getPrestige(), 0, data.getProgress());
        player.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getInstance().getLangSettings().getPrestigeCommandApprovedMessage(), formatter)));
    }
}
