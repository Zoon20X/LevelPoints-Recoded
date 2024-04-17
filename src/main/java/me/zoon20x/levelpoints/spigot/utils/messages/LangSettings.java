package me.zoon20x.levelpoints.spigot.utils.messages;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.utils.placeholders.BarSettings;


import java.io.IOException;
import java.util.HashMap;

public class LangSettings {

    private HashMap<String, LangData> langDataMap = new HashMap<>();
    private HashMap<String, LangEventsData> langEventsDataMap = new HashMap<>();

    private BarSettings barSettings;

    public LangSettings(){
        load();
        loadEventsLang("OnExp");
    }
    public void reload() throws IOException {
        langDataMap.clear();
        langEventsDataMap.clear();
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getLangSettings();
        config.reload();
        load();
        loadEventsLang("OnExp");
    }
    private void load(){
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getLangSettings();
        int stepMin = config.getInt("Progressbar.MinStep");
        int stepMax = config.getInt("Progressbar.MaxStep");

        String visualBorder = config.getString("Progressbar.VisualBorder");
        String visualCompletedStep = config.getString("Progressbar.VisualCompletedStep");
        String visualUnCompletedStep = config.getString("Progressbar.VisualUnCompletedStep");
        this.barSettings = new BarSettings(stepMin, stepMax, visualBorder, visualCompletedStep, visualUnCompletedStep);
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
                String cMessage = config.getString("Lang." + key + ".Children." + keys + ".Message");
                langData.addChildData(keys, new LangChildData(cIsEnabled, cMessage));
            }
            addLangData(key, langData);
        }
    }
    private void loadEventsLang(String event){
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getLangSettings();
        boolean isEnabled = config.getBoolean("MessageEvents." + event + ".Enabled");
        MessageType messageType = MessageType.valueOf(config.getString("MessageEvents." + event + ".Type"));
        String message = config.getString("MessageEvents." + event + ".Message");

        langEventsDataMap.put(event, new LangEventsData(isEnabled, messageType, message));
    }

    public LangEventsData getLangEventsData(String name){
        return langEventsDataMap.get(name);
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


    public BarSettings getBarSettings() {
        return barSettings;
    }
}
