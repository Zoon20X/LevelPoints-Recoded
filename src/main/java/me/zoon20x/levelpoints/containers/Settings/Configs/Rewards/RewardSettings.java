package me.zoon20x.levelpoints.containers.Settings.Configs.Rewards;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.files.FilesStorage;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RewardSettings {

    private static HashSet<RewardData> rewards = new HashSet<>();


    public RewardSettings(){
        long startTime = System.nanoTime();
        generateRewards();
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        LevelPoints.getDebug(DebugSeverity.NORMAL, "Rewards took "+ duration + "ms, to load " + rewards.size() + " rewards");
    }

    public void addReward(RewardData data){
        rewards.add(data);
    }
    public void removeReward(RewardData data){
        rewards.remove(data);
    }
    public boolean hasReward(RewardData data){
        return rewards.contains(data);
    }
    public Set<RewardData> getAllRewards(){
        return rewards;
    }
    public void clearRewards(){
        rewards.clear();
    }
    private void generateRewards() {
        FileConfiguration config = FilesStorage.getConfig("rewardsConfig");
        for (String x : config.getConfigurationSection("Rewards").getKeys(false)) {
            String id = x;
            RewardTriggerType type = RewardTriggerType.valueOf(config.getString("Rewards." + x + ".TriggerType"));
            List<String> commands = config.getStringList("Rewards." + x + ".Commands");
            boolean messagedEnabled = config.getBoolean("Rewards." + x + ".Message.Enabled");
            List<String> messages = config.getStringList("Rewards." + x + ".Message.Text");
            boolean soundEnabled = config.getBoolean("Rewards." + x + ".Sound.Enabled");
            Sound sound = Sound.valueOf(config.getString("Rewards." + x + ".Sound.Type"));
            float volume = config.getLong("Rewards." + x + ".Sound.Volume");
            float pitch = config.getLong("Rewards." + x + ".Sound.Pitch");
            List<String> triggerValues = config.getStringList("Rewards." + x + ".TriggerValues");
            if (type == RewardTriggerType.PRESTIGE) {
                RewardData data = new RewardData(
                        id,
                        type,
                        commands,
                        messagedEnabled,
                        messages,
                        soundEnabled,
                        new RewardSound(sound, volume, pitch),
                        triggerValues);
                addReward(data);
            } else {
                List<String> triggerPrestige = config.getStringList("Rewards." + x + ".TriggerPrestige");
                RewardData data = new RewardData(
                        id,
                        type,
                        commands,
                        messagedEnabled,
                        messages,
                        soundEnabled,
                        new RewardSound(sound, volume, pitch),
                        triggerValues, triggerPrestige);
                addReward(data);
            }
        }
    }


}
