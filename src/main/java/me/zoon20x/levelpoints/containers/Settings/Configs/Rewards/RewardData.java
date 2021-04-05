package me.zoon20x.levelpoints.containers.Settings.Configs.Rewards;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RewardData {

    private String id;
    private RewardTriggerType type;
    private List<String> commands;

    private boolean messageEnabled;
    private List<String> messageList;

    private boolean soundEnabled;
    private RewardSound sound;

    private List<String> triggerValues;
    private List<String> triggerPrestige;

    public RewardData(String id, RewardTriggerType type, List<String> commands, boolean messageEnabled, List<String> messageList, boolean soundEnabled, RewardSound sound, List<String> triggerValues){
        this.id = id;
        this.type = type;
        this.commands = commands;

        this.messageEnabled = messageEnabled;
        this.messageList = messageList;

        this.soundEnabled = soundEnabled;
        this.sound = sound;

        this.triggerValues = triggerValues;
    }
    public RewardData(String id, RewardTriggerType type, List<String> commands, boolean messageEnabled, List<String> messageList, boolean soundEnabled, RewardSound sound, List<String> triggerValues, List<String> triggerPrestige){
        this.id = id;
        this.type = type;
        this.commands = commands;

        this.messageEnabled = messageEnabled;
        this.messageList = messageList;

        this.soundEnabled = soundEnabled;
        this.sound = sound;

        this.triggerValues = triggerValues;
        this.triggerPrestige = triggerPrestige;
    }

    public String getId() {
        return id;
    }

    public RewardTriggerType getType() {
        return type;
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean isMessageEnabled() {
        return messageEnabled;
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public RewardSound getSound() {
        return sound;
    }

    @NotNull
    public List<String> getTriggerValues() {
        return triggerValues;
    }

    @Nullable
    public List<String> getTriggerPrestige() {
        return triggerPrestige;
    }
}
