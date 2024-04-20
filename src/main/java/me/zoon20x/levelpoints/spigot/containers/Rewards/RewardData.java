package me.zoon20x.levelpoints.spigot.containers.Rewards;

import java.util.ArrayList;
import java.util.List;

public class RewardData {


    private boolean isEnabled;
    private boolean autoTrigger;
    private TriggerType triggerType;

    private List<String> commands;

    private MessageData messageData;

    private List<Integer> triggerValues;
    private List<Integer> triggerPrestige;

    public RewardData(boolean isEnabled, boolean autoTrigger, TriggerType triggerType, List<String> commands, MessageData messageData, List<Integer> triggerValues, List<Integer> triggerPrestige){
        this.isEnabled = isEnabled;
        this.autoTrigger = autoTrigger;
        this.triggerType = triggerType;
        this.commands = commands;
        this.messageData = messageData;
        this.triggerValues = triggerValues;
        this.triggerPrestige = triggerPrestige;
    }


    public boolean isEnabled() {
        return isEnabled;
    }

    public boolean isAutoTrigger() {
        return autoTrigger;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public List<String> getCommands() {
        return commands;
    }

    public MessageData getMessageData() {
        return messageData;
    }

    public List<Integer> getTriggerValues() {
        return triggerValues;
    }

    public List<Integer> getTriggerPrestige() {
        return triggerPrestige;
    }


}
