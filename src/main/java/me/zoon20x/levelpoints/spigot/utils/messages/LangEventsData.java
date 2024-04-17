package me.zoon20x.levelpoints.spigot.utils.messages;

import me.zoon20x.levelpoints.spigot.LevelPoints;

public class LangEventsData {

    private boolean isEnabled;
    private MessageType messageType;
    private String message;

    public LangEventsData(boolean isEnabled, MessageType messageType, String message){
        this.isEnabled = isEnabled;
        this.messageType = messageType;
        this.message = LevelPoints.getInstance().getMessagesUtil().getColor(message);
    }


    public boolean isEnabled() {
        return isEnabled;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }
}
