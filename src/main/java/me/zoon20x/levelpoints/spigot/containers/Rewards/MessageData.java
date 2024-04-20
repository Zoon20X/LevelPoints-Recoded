package me.zoon20x.levelpoints.spigot.containers.Rewards;

import java.util.List;

public class MessageData {
    private boolean isEnabled;
    private List<String> messages;


    public MessageData(boolean isEnabled, List<String> messages) {
        this.isEnabled = isEnabled;
        this.messages = messages;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public List<String> getMessages() {
        return messages;
    }
}
