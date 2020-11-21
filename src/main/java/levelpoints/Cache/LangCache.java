package levelpoints.Cache;



import levelpoints.events.CustomEvents.TopEnums;
import levelpoints.levelpoints.Formatting;

import java.util.ArrayList;
import java.util.HashMap;

public class LangCache {

    private static HashMap<String, ArrayList<String>> langData = new HashMap<>();


    public static ArrayList<String> getlpHelpMessage(){
        if(langData.isEmpty() || !langData.containsKey("lps")) {
            ArrayList<String> message = new ArrayList<>();
            for(String x : FileCache.getConfig("langConfig").getStringList("lp")) {
                message.add(x);
            }
            langData.put("lps", message);
            System.out.println(Formatting.basicColor("&3Saving to cache"));

        }
        System.out.println(Formatting.basicColor("&bReading"));
        return langData.get("lps");
    }
    public static ArrayList<String> getInfoMessage(){
        if(langData.isEmpty() || !langData.containsKey("lpsInfo")) {
            ArrayList<String> message = new ArrayList<>();
            for(String x : FileCache.getConfig("langConfig").getStringList("lpsInfo")) {
                message.add(x);
            }
            langData.put("lpsInfo", message);
            System.out.println(Formatting.basicColor("&3Saving to cache"));

        }
        System.out.println(Formatting.basicColor("&bReading"));
        return langData.get("lpsInfo");
    }

    public static ArrayList<String> getRequiredLevel(){
        if(langData.isEmpty() || !langData.containsKey("RequiredLevelOre")) {
            ArrayList<String> message = new ArrayList<>();
            for(String x : FileCache.getConfig("langConfig").getStringList("RequiredLevelOre.Text")) {
                message.add(x);
            }
            langData.put("RequiredLevelOre", message);
            System.out.println(Formatting.basicColor("&3Saving to cache"));

        }
        System.out.println(Formatting.basicColor("&bReading"));
        return langData.get("RequiredLevelOre");
    }
    public static ArrayList<String> getEXPEarn(){
        if(langData.isEmpty() || !langData.containsKey("EXPEarn")) {
            ArrayList<String> message = new ArrayList<>();
            for(String x : FileCache.getConfig("langConfig").getStringList("EXPEarn.Text")) {
                message.add(x);
            }
            langData.put("EXPEarn", message);
            System.out.println(Formatting.basicColor("&3Saving to cache"));

        }
        System.out.println(Formatting.basicColor("&bReading"));
        return langData.get("EXPEarn");
    }
    public static String getLevelTopMessage(TopEnums value){
        if(!langData.containsKey("LevelTop_" + value.toString())){
            ArrayList<String> message = new ArrayList<>();
            System.out.println(FileCache.getConfig("langConfig").getStringList("LpsTop.LevelTop." + value.toString()));
            for(String x : FileCache.getConfig("langConfig").getStringList("LpsTop.LevelTop." + value.toString())){
                message.add(x);
            }
            langData.put("LevelTop_" + value.toString(), message);
        }
        return langData.get("LevelTop_" + value.toString()).get(0);
    }
    public static void clearCache(){
        langData.clear();
    }
}
