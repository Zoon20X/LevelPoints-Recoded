package me.zoon20x.levelpoints.spigot.utils.messages;

import me.zoon20x.levelpoints.spigot.LevelPoints;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LangData {

    private boolean isEnabled;
    private boolean centerText;
    private List<String> message = new ArrayList<>();
    private HashMap<String, LangChildData> childDataMap = new HashMap<>();

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

    public void addChildData(String key, LangChildData data){
        childDataMap.put(key, data);
    }

    public LangChildData getChildData(String key){
        return childDataMap.get(key);
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
