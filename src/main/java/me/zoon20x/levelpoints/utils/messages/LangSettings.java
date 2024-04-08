package me.zoon20x.levelpoints.utils.messages;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.LevelPoints;


import java.util.HashMap;

public class LangSettings {

    private HashMap<String, LangData> langDataMap = new HashMap<>();

    public LangSettings(){
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getLangSettings();
        for(String key : config.getSection("Lang").getRoutesAsStrings(false)){
            LangData langData;
            boolean isEnabled = config.getBoolean("Lang." + key + ".Enabled");
            boolean centerText = config.getBoolean("Lang." + key + ".CenterText");
            if(!config.isString("Lang." + key + ".Message")){
                langData = new LangData(isEnabled,centerText, config.getStringList("Lang." + key + ".Message"));
            }else {
                langData = new LangData(isEnabled, centerText, config.getString("Lang." + key + ".Message"));
            }
            addLangData(key, langData);
        }

    }

    public void addLangData(String name, LangData data){
        langDataMap.put(name, data);
    }

    public boolean hasLangData(String langData){
        return langDataMap.containsKey(langData);
    }
    public LangData getLangData(String langData){
        return langDataMap.get(langData);
    }


}
