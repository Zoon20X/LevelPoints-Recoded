package me.zoon20x.levelpoints.spigot.utils.messages;

import me.zoon20x.levelpoints.spigot.LevelPoints;

public class LangChildData {

    private boolean isEnabled;
    private String message;

    public LangChildData(boolean isEnabled, String message){
        this.isEnabled = isEnabled;
        this.message = LevelPoints.getInstance().getMessagesUtil().getColor(message);
    }


    public boolean isEnabled() {
        return isEnabled;
    }

    public String getMessage() {
        return message;
    }
}
