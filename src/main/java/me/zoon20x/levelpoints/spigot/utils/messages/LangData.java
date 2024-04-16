package me.zoon20x.levelpoints.spigot.utils.messages;

import me.zoon20x.levelpoints.spigot.LevelPoints;

import java.util.ArrayList;
import java.util.List;

public class LangData {

    private boolean isEnabled;
    private boolean centerText;
    private List<String> message = new ArrayList<>();
    private String noPermission;

    public LangData(boolean isEnabled, boolean centerText, String message, String noPermission){
        this.isEnabled = isEnabled;
        this.centerText = centerText;
        this.message.add(message);
        this.noPermission = noPermission;
        colorize();
    }
    public LangData(boolean isEnabled, boolean centerText, List<String> message, String noPermission){
        this.isEnabled = isEnabled;
        this.centerText = centerText;
        this.message = message;
        this.noPermission = noPermission;
        colorize();
    }
    private void colorize(){
        List<String> temp = new ArrayList<>();
        message.forEach(s ->{
            temp.add(LevelPoints.getInstance().getMessagesUtil().getColor(s));
        });
        message = temp;
        this.noPermission = LevelPoints.getInstance().getMessagesUtil().getColor(this.noPermission);
    }


    public boolean isEnabled() {
        return isEnabled;
    }

    public List<String> getMessage() {
        return message;
    }

    public boolean isCenteredText() {
        return centerText;
    }

    public String getNoPermission() {
        return noPermission;
    }
}
