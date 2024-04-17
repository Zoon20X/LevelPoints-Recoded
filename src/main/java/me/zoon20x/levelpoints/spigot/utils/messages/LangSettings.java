package me.zoon20x.levelpoints.spigot.utils.messages;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.spigot.LevelPoints;


import java.io.IOException;
import java.util.HashMap;

public class LangSettings {

    private HashMap<String, LangData> langDataMap = new HashMap<>();

    public LangSettings(){
        load();
    }
    public void reload() throws IOException {
        langDataMap.clear();
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getLangSettings();
        config.reload();
        load();
    }
    private void load(){
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getLangSettings();
        for (String key : config.getSection("Lang").getRoutesAsStrings(false)) {
            LangData langData;
            boolean isEnabled = config.getBoolean("Lang." + key + ".Enabled");
            boolean centerText = config.getBoolean("Lang." + key + ".CenterText");
            if (!config.isString("Lang." + key + ".Message")) {
                langData = new LangData(isEnabled, centerText, config.getStringList("Lang." + key + ".Message"));
            } else {
                langData = new LangData(isEnabled, centerText, config.getString("Lang." + key + ".Message"));
            }

            for (String keys : config.getSection("Lang." + key + ".Children").getRoutesAsStrings(false)) {
                boolean cIsEnabled = config.getBoolean("Lang." + key + ".Children." + keys + ".Enabled");
                String cMessage = config.getString("Lang." + key + ".Children." + keys + ".Enabled");
                langData.addChildData(keys, new LangChildData(cIsEnabled, cMessage));
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
