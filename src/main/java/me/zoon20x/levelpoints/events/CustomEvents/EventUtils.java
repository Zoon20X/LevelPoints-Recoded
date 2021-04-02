package me.zoon20x.levelpoints.events.CustomEvents;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import me.zoon20x.levelpoints.utils.Formatter;
import me.zoon20x.levelpoints.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;

public class EventUtils {


    public static void triggerEarnExpEvent(PlayerData data, Event event, Double amount, Player player, EarnTask task) {
        EarnExpEvent expEvent = new EarnExpEvent(data, event, amount, player, task);
        Bukkit.getPluginManager().callEvent(expEvent);
        if (!expEvent.isCancelled()) {
            data.addEXP(amount);
        }

    }

    public static void triggerLevelUpEvent(int level, PlayerData data) {

        LevelUpEvent event = new LevelUpEvent(level, data);
        Bukkit.getPluginManager().callEvent(event);
        LevelPoints.getTopListSettings().generateTopCache(50);


    }

    public static void triggerPrestigeUpEvent(int prestige, PlayerData data) {

        PrestigeEvent event = new PrestigeEvent(prestige, data);
        Bukkit.getPluginManager().callEvent(event);

        if (Bukkit.getPlayer(data.getUUID()) == null) {
            return;
        }
        if (!LevelPoints.getLangSettings().isPrestigeCommandApprovedEnabled()) {
            return;
        }
        Player player = Bukkit.getPlayer(data.getUUID());
        Formatter formatter = new Formatter(data.getName(), data.getLevel(), data.getExp(), data.getRequiredExp(), data.getPrestige(), 0, data.getProgress());
        player.sendMessage(MessageUtils.getColor(MessageUtils.format(LevelPoints.getLangSettings().getPrestigeCommandApprovedMessage(), formatter)));
    }
}
