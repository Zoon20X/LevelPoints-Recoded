package levelpoints.Containers;

import levelpoints.Cache.FileCache;
import levelpoints.Utils.AsyncEvents;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

public class LevelsContainer {
    private static HashMap<String, Object> values = new HashMap<>();
    private static HashMap<Integer, Double> CLlevels = new HashMap<>();
    private static HashMap<Integer, Double> damageCache = new HashMap<>();
    private static HashMap<Integer, Double> healthCache = new HashMap<>();
    private static HashSet<FormatsContainer> formats = new HashSet<>();


    public static String getFormula() {
        if(!values.containsKey("formula")) {

            if (FileCache.getConfig("levelConfig").getBoolean("Leveling.RegularLeveling.Enabled")) {
                String form = FileCache.getConfig("levelConfig").getString("Leveling.RegularLeveling.Formula");
                values.put("formula", form);
                return form;
            } else if (FileCache.getConfig("levelConfig").getBoolean("Leveling.PrestigeLeveling.Enabled")) {
                String form = FileCache.getConfig("levelConfig").getString("Leveling.PrestigeLeveling.Formula");
                values.put("formula", form);
                return form;
            }
        }else{
            return values.get("formula").toString();
        }
        return null;
    }
    public static Integer getStartingLevel(){
        if(!values.containsKey("startingLevel")){
            values.put("startingLevel", FileCache.getConfig("levelConfig").getInt("Starting.Level"));
        }
        return (Integer) values.get("startingLevel");
    }
    public static Integer getStartingEXP(){
        if(!values.containsKey("startingEXP")){
            values.put("startingEXP", FileCache.getConfig("levelConfig").getInt("Starting.EXP"));
        }
        return (Integer) values.get("startingEXP");
    }
    public static Integer getMaxLevel(){
        if(!values.containsKey("maxLevel")){
            values.put("maxLevel", FileCache.getConfig("levelConfig").getInt("Max.Level"));
        }
        return (Integer) values.get("maxLevel");
    }
    public static Integer getMaxPrestige(){
        if(!values.containsKey("maxPrestige")){
            values.put("maxPrestige", FileCache.getConfig("levelConfig").getInt("Max.Prestige"));
        }
        return (Integer) values.get("maxPrestige");
    }
    public static void clearCache(){
        values.clear();
    }

    public static Double generateFormula(Integer value, String formula){
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager(null);
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
        Object ob = null;
        try {
            ob = scriptEngine.eval(formula.replace("{Level_Player}", String.valueOf(value)));
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        return Double.valueOf(ob.toString());
    }
    public static BigDecimal generateFormula(OfflinePlayer player, String formula){
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
        Object ob = null;
        try {
            ob = scriptEngine.eval(formula.replace("{Level_Player}", String.valueOf(AsyncEvents.getOfflineLevel(player.getName()))));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return BigDecimal.valueOf(Double.valueOf(ob.toString()));
    }

    public static HashSet<FormatsContainer> getFormats(){
        return formats;
    }
    public static void generateFormats(){
        ConfigurationSection cs = FileCache.getConfig("formatsConfig").getConfigurationSection("Formats");
        FileConfiguration config = FileCache.getConfig("formatsConfig");
        for(String x : cs.getKeys(false)){

            formats.add(new FormatsContainer(
                    config.getString("Formats." + x + ".Format"),
                    config.getInt("Formats." + x + ".MinLevel"),
                    config.getInt("Formats." + x + ".MaxLevel"),
                    config.getInt("Formats." + x + ".Prestige")));
        }
    }
    public static Double getCustomLevelsEXP(Integer level){
        return CLlevels.get(level);
    }


    public static void generateCustomLevels(){
        ConfigurationSection cs = FileCache.getConfig("levelConfig").getConfigurationSection("Leveling.CustomLeveling.Levels");


        FileConfiguration config = FileCache.getConfig("levelConfig");
        for(String x : cs.getKeys(false)){
            double exp = config.getDouble("Leveling.CustomLeveling.Levels." + x);
            x = x.replace("Level-", "");
            CLlevels.put(Integer.parseInt(x), exp);
        }
    }
    public static void generateLevelBonus(){
        ConfigurationSection cs = FileCache.getConfig("levelConfig").getConfigurationSection("LevelBonus.Levels");


        FileConfiguration config = FileCache.getConfig("levelConfig");
        for(String x : cs.getKeys(false)){
            double damage = config.getDouble("LevelBonus.Levels." + x + ".Damage");
            double health = config.getDouble("LevelBonus.Levels." + x + ".Health");
            x = x.replace("Level-", "");
            damageCache.put(Integer.parseInt(x), damage);
            healthCache.put(Integer.parseInt(x), health);
        }
    }
    public static Double getLevelBonus(String type, Integer Level){

            switch (type){
            case "Health":
                if(FileCache.getConfig("levelConfig").getBoolean("LevelBonus.StaticIncrease")) {
                    return FileCache.getConfig("levelConfig").getDouble("LevelBonus.StaticIncrease.Amount.Health") * Level;
                }
                return healthCache.get(Level);
            case "Damage":
                if(FileCache.getConfig("levelConfig").getBoolean("LevelBonus.StaticIncrease")) {
                    return FileCache.getConfig("levelConfig").getDouble("LevelBonus.StaticIncrease.Amount.Damage") * Level;
                }
                return damageCache.get(Level);
        }
        return null;
    }
    public static boolean hasLevelBonus(String type, Integer Level){
        if(FileCache.getConfig("levelConfig").getBoolean("StaticIncrease")){
         return true;
        }
        switch (type){
            case "Health":
                return healthCache.containsKey(Level);
            case "Damage":
                return damageCache.containsKey(Level);
        }
        return false;
    }
}
