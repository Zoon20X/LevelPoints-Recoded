package me.zoon20x.levelpoints.spigot.containers.Rewards;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import io.lumine.mythic.bukkit.utils.lib.jooq.impl.QOM;
import me.zoon20x.levelpoints.spigot.LevelPoints;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class RewardStorage {

    private final HashMap<String, RewardData> rewardDataMap = new HashMap<>();

    public RewardStorage(){
        File file = new File(LevelPoints.getInstance().getDataFolder(), "/Rewards/");
        file.mkdirs();
        load(file);

    }

    private void load(File rewardsFolder){

        for(File file : rewardsFolder.listFiles()){
            YamlDocument config = loadFile(file);
            String name = config.getFile().getName().replace(".yml", "");
            boolean isEnabled = config.getBoolean("Enabled");
            boolean autoTrigger = config.getBoolean("AutoTrigger");
            TriggerType triggerType = TriggerType.valueOf(config.getString("TriggerType"));
            List<String> commands = config.getStringList("Commands");
            boolean isMessageDataEnabled = config.getBoolean("Messages.Enabled");
            List<String> messages = config.getStringList("Messages.Text");
            MessageData messageData = new MessageData(isMessageDataEnabled, messages);
            List<Integer> triggerValues = config.getIntList("TriggerValues");
            List<Integer> triggerPrestige = config.getIntList("TriggerPrestige");
            RewardData rewardData = new RewardData(isEnabled, autoTrigger, triggerType, commands, messageData, triggerValues, triggerPrestige);
            rewardDataMap.put(name, rewardData);
        }
    }
    private YamlDocument loadFile(File file){
        System.out.println(file.getName());
        try {
            YamlDocument config = YamlDocument.create(file,
                    getClass().getResourceAsStream("/Rewards/ExampleLevelUp.yml"),
                    GeneralSettings.builder().setUseDefaults(false).build());
            config.update();
            config.save();
            return config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, RewardData> getRewardDataMap() {
        return rewardDataMap;
    }

    public boolean hasReward(String reward){
        return rewardDataMap.containsKey(reward);
    }
    public RewardData getReward(String reward){
        return rewardDataMap.get(reward);
    }


}
