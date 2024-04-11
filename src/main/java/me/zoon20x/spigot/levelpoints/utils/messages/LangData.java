package me.zoon20x.spigot.levelpoints.utils.messages;

import me.zoon20x.spigot.levelpoints.LevelPoints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LangData {

    private boolean isEnabled;
    private boolean centerText;
    private List<String> message = new ArrayList<>();

    public LangData(boolean isEnabled, boolean centerText, String message){
        this.isEnabled = isEnabled;
        this.centerText = centerText;
        this.message.add(message);
        colorize();
    }
    public LangData(boolean isEnabled, boolean centerText, List<String> message){
        this.isEnabled = isEnabled;
        this.centerText = centerText;
        this.message = message;
        colorize();
    }
    private void colorize(){
        List<String> temp = new ArrayList<>();
        message.forEach(s ->{
            temp.add(LevelPoints.getInstance().getMessagesUtil().getColor(s));
        });
        message = temp;
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
}
