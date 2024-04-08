package me.zoon20x.levelpoints.utils.messages;

import me.zoon20x.levelpoints.LevelPoints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LangData {

    private boolean isEnabled;
    private List<String> message = new ArrayList<>();

    public LangData(boolean isEnabled, String message){
        this.isEnabled = isEnabled;
        this.message.add(message);
        colorize();
    }
    public LangData(boolean isEnabled, List<String> message){
        this.isEnabled = isEnabled;
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
}
