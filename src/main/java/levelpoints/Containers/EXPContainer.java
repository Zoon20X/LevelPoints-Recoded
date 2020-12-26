package levelpoints.Containers;

import com.cryptomorin.xseries.XBlock;
import com.cryptomorin.xseries.XMaterial;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import levelpoints.Cache.FileCache;
import levelpoints.events.CustomEvents.SettingsEnum;
import levelpoints.events.CustomEvents.TasksEnum;
import levelpoints.levelpoints.Formatting;
import levelpoints.levelpoints.LevelPoints;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.nio.channels.FileLockInterruptionException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class EXPContainer {
    private static HashMap<String, HashMap<Integer, Object>> minExp = new HashMap<>();
    private static HashMap<String, HashMap<Integer, Object>> maxExp = new HashMap<>();
    private static HashMap<String, Object> random = new HashMap<>();
    private static HashMap<String, Object> values = new HashMap<>();
    private static HashMap<String, Integer> requiredBreak = new HashMap<>();
    private static HashMap<String, Integer> requiredPlace = new HashMap<>();
    private static HashMap<String, Integer> requiredDamage = new HashMap<>();

    public static Boolean gainEXP(TasksEnum value){
        switch (value){
            case BlockBreak:
                if(!values.containsKey("expFromBlocks")){
                    values.put("expFromBlocks", FileCache.getConfig("expConfig").getBoolean("BlockEXP.Enabled"));
                }
                return Boolean.valueOf(values.get("expFromBlocks").toString());
            case MobDeath:
                if(!values.containsKey("expFromMobs")){
                    values.put("expFromMobs", FileCache.getConfig("expConfig").getBoolean("MobsEXP.Enabled"));
                }
                return Boolean.valueOf(values.get("expFromMobs").toString());
            case Farming:
                if(!values.containsKey("expFromFarming")){
                    values.put("expFromFarming", FileCache.getConfig("expConfig").getBoolean("FarmingEXP.Enabled"));
                }
                return Boolean.valueOf(values.get("expFromFarming").toString());
            case PlayerDeath:
                if(!values.containsKey("expFromPlayerDeath")){
                    values.put("expFromPlayerDeath", FileCache.getConfig("expConfig").getBoolean("Pvp.Enabled"));
                }
                return Boolean.valueOf(values.get("expFromPlayerDeath").toString());
        }
        return false;
    }
    public static Double getMinEXP(Block block, Boolean isCrops){
        if(isCrops){
            if (FileCache.getConfig("expConfig").getInt("FarmingEXP.Items." + block.getType().toString() + ".EXP.Min") > 0) {
                if (!minExp.containsKey(block.getType().toString())) {
                    minExp.put(block.getType().toString(), new HashMap<Integer, Object>());
                    minExp.get(block.getType().toString()).put(0, FileCache.getConfig("expConfig").getInt("FarmingEXP.Items." + block.getType().toString() + ".EXP.Min"));
                } else {
                    if (!minExp.get(block.getType().toString()).containsKey(0)) {
                        minExp.get(block.getType().toString()).put(0, FileCache.getConfig("expConfig").getInt("FarmingEXP.Items." + block.getType().toString() + ".EXP.Min"));
                    }
                }
                return Double.valueOf(minExp.get(block.getType().toString()).get(0).toString());
            }
        }

        if(FileCache.getConfig("expConfig").getBoolean("BlockEXP.Blocks." + block.getType().toString() + ".Data")){
            int value = block.getData();
            if(value == block.getData()) {
                if (FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + block.getType().toString() + "." + value + ".EXP.Min") > 0) {
                    if (!minExp.containsKey(block.getType().toString())) {
                        minExp.put(block.getType().toString(), new HashMap<Integer, Object>());
                        minExp.get(block.getType().toString()).put(value, FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + block.getType().toString() + "." + value + ".EXP.Min"));
                    }else{
                        if(!minExp.get(block.getType().toString()).containsKey(value)){
                            minExp.get(block.getType().toString()).put(value, FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + block.getType().toString() + "." + value + ".EXP.Min"));
                        }
                    }
                    return Double.valueOf(minExp.get(block.getType().toString()).get(value).toString());
                }
            }
        }else {
            if (FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + block.getType().toString() + ".EXP.Min") > 0) {
                if (!minExp.containsKey(block.getType().toString())) {
                    minExp.put(block.getType().toString(), new HashMap<Integer, Object>());
                    minExp.get(block.getType().toString()).put(0, FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + block.getType().toString() + ".EXP.Min"));
                } else {
                    if (!minExp.get(block.getType().toString()).containsKey(0)) {
                        minExp.get(block.getType().toString()).put(0, FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + block.getType().toString() + ".EXP.Min"));
                    }
                }
                return Double.valueOf(minExp.get(block.getType().toString()).get(0).toString());
            }
        }

        return 0.0;
    }
    public static Double getMinEXP(EntityType value){

        if (FileCache.getConfig("expConfig").getInt("MobsEXP.Mobs." + value.toString() + ".EXP.Min") > 0) {
            if (!minExp.containsKey(value.toString())) {
                minExp.put(value.toString(), new HashMap<Integer, Object>());
                minExp.get(value.toString()).put(0,FileCache.getConfig("expConfig").getInt("MobsEXP.Mobs." + value.toString() + ".EXP.Min"));
            }
            return Double.valueOf(minExp.get(value.toString()).get(0).toString());
        }
        return 0.0;
    }
    public static Double getEXP(EntityType value, Boolean showRandomDebugMessage) {
        if (!getIsRandom(value)) {
            return getMinEXP(value);
        }else {

            if (getMinEXP(value) > 0) {
                if (getMaxEXP(value) > getMinEXP(value)) {
                    double random = ThreadLocalRandom.current().nextInt(getMinEXP(value).intValue(),getMaxEXP(value).intValue());

                    return random;
                } else {
                    if(showRandomDebugMessage) {
                        System.out.println(Formatting.basicColor("&3LevelPoints>> &4Your max value is lower or equal to min, random will not occur this time"));
                    }
                    return getMinEXP(value);
                }
            }else{
                return 0.0;
            }
        }
    }
    public static Double getEXP(Block value, Boolean showRandomDebugMessage, Boolean isCrop) {
        Material val = value.getType();
        if(isCrop){
            if (!getIsRandom(value.getType(), true)) {
                return getMinEXP(value, true);
            }else {
                if (getMinEXP(value, true) > 0) {
                    if (getMaxEXP(value, true) > getMinEXP(value, true)) {
                        double random = ThreadLocalRandom.current().nextInt(getMinEXP(value, true).intValue(), getMaxEXP(value, true).intValue());

                        return random;
                    } else {
                        if(showRandomDebugMessage) {
                            System.out.println(Formatting.basicColor("&3LevelPoints>> &4Your max value is lower or equal to min, random will not occur this time"));
                        }
                        return getMinEXP(value, true);
                    }
                }else{
                    return 0.0;
                }
            }
        }
        if (!getIsRandom(value.getType(), false)) {
            return getMinEXP(value, false);
        }else {

            if (getMinEXP(value, false) > 0) {
                if (getMaxEXP(value, false) > getMinEXP(value, false)) {
                    double random = ThreadLocalRandom.current().nextInt(getMinEXP(value, false).intValue(), getMaxEXP(value, false).intValue());

                    return random;
                } else {
                    if(showRandomDebugMessage) {
                        System.out.println(Formatting.basicColor("&3LevelPoints>> &4Your max value is lower or equal to min, random will not occur this time"));
                    }
                    return getMinEXP(value, false);
                }
            }else{
                return 0.0;
            }
        }
    }
    public static Double getMaxEXP(Block block, Boolean isCrops) {
        if(isCrops){
            if (FileCache.getConfig("expConfig").getInt("FarmingEXP.Items." + block.getType().toString() + ".EXP.Min") > 0) {
                if (!maxExp.containsKey(block.getType().toString())) {
                    maxExp.put(block.getType().toString(), new HashMap<Integer, Object>());
                    maxExp.get(block.getType().toString()).put(0, FileCache.getConfig("expConfig").getInt("FarmingEXP.Items." + block.getType().toString() + ".EXP.Min"));
                } else {
                    if (!maxExp.get(block.getType().toString()).containsKey(0)) {
                        maxExp.get(block.getType().toString()).put(0, FileCache.getConfig("expConfig").getInt("FarmingEXP.Items." + block.getType().toString() + ".EXP.Min"));
                    }
                }
                return Double.valueOf(maxExp.get(block.getType().toString()).get(0).toString());
            }
        }
        if (FileCache.getConfig("expConfig").getBoolean("BlockEXP.Blocks." + block.getType().toString() + ".Data")) {
            int value = block.getData();

            if (value == block.getData()) {
                if (FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + block.getType().toString() + "." + value + ".EXP.Max") > 0) {
                    if (!maxExp.containsKey(block.getType().toString())) {
                        maxExp.put(block.getType().toString(), new HashMap<Integer, Object>());
                        maxExp.get(block.getType().toString()).put(value, FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + block.getType().toString() + "." + value + ".EXP.Max"));
                    } else {
                        if (!maxExp.get(block.getType().toString()).containsKey(value)) {
                            maxExp.get(block.getType().toString()).put(value, FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + block.getType().toString() + "." + value + ".EXP.Max"));
                        }
                    }
                    return Double.valueOf(maxExp.get(block.getType().toString()).get(value).toString());
                }
            }
        } else {
            if (FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + block.getType().toString() + ".EXP.Max") > 0) {
                if (!maxExp.containsKey(block.getType().toString())) {
                    maxExp.put(block.getType().toString(), new HashMap<Integer, Object>());
                    maxExp.get(block.getType().toString()).put(0, FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + block.getType().toString() + ".EXP.Max"));
                } else {
                    if (!maxExp.get(block.getType().toString()).containsKey(0)) {
                        maxExp.get(block.getType().toString()).put(0, FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + block.getType().toString() + ".EXP.Max"));
                    }
                }
                return Double.valueOf(maxExp.get(block.getType().toString()).get(0).toString());
            }
        }
        return 0.0;
    }
    public static Double getMaxEXP(EntityType value){

        if (FileCache.getConfig("expConfig").getInt("MobsEXP.Mobs." + value.toString() + ".EXP.Max") > 0) {
            if (!maxExp.containsKey(value.toString())) {
                maxExp.put(value.toString(), new HashMap<Integer, Object>());
                maxExp.get(value.toString()).put(0,FileCache.getConfig("expConfig").getInt("MobsEXP.Mobs." + value.toString() + ".EXP.Max"));
            }
            return Double.valueOf(maxExp.get(value.toString()).get(0).toString());
        }
        return 0.0;
    }
    public static Boolean getIsRandom(Material value, Boolean isCrop){
        if(isCrop){
            if (!random.containsKey(value.toString())) {
                random.put(value.toString(), FileCache.getConfig("expConfig").getBoolean("FarmingEXP.Items." + value.toString() + ".Random"));
            }
        }else {
            if (!random.containsKey(value.toString())) {
                random.put(value.toString(), FileCache.getConfig("expConfig").getBoolean("BlockEXP.Blocks." + value.toString() + ".Random"));
            }
        }

        return (Boolean) random.get(value.toString());
    }
    public static Boolean getIsRandom(EntityType value){
        if(!random.containsKey(value.toString())){
            random.put(value.toString(), FileCache.getConfig("expConfig").getBoolean("MobsEXP.Mobs."+ value.toString() +".Random"));
        }

        return (Boolean) random.get(value.toString());
    }
    public static int getRequiredLevel(Material value, SettingsEnum setting){


        if(getCache(setting) == null){
            return 0;
        }

        if(FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + value + ".RequiredLevel." + setting.toString()) > 0) {
            if(!getCache(setting).containsKey(value.toString())) {
                getCache(setting).put(value.toString(), FileCache.getConfig("expConfig").getInt("BlockEXP.Blocks." + value.toString() + ".RequiredLevel." + setting.toString()));
            }
            return getCache(setting).get(value.toString());

        }else{
            return 0;
        }
    }
    public static int getRequiredLevel(EntityType value, SettingsEnum setting){


        if(getCache(setting) == null){
            return 0;
        }
        if(FileCache.getConfig("expConfig").getInt("MobsEXP.Mobs." + value + ".RequiredLevel." + setting.toString()) > 0) {
            if(!getCache(setting).containsKey(value.toString())) {
                getCache(setting).put(value.toString(), FileCache.getConfig("expConfig").getInt("MobsEXP.Mobs." + value.toString() + ".RequiredLevel." + setting.toString()));
            }
            return getCache(setting).get(value.toString());

        }else{
            return 0;
        }
    }
    public static HashMap<String, Integer> getCache(SettingsEnum setting){
        switch(setting){
            case Break:
                return requiredBreak;
            case Place:
                return requiredPlace;
            case Damage:
                return requiredDamage;
        }
        return null;
    }
    public static Boolean isRunningAntiAbuse(){
        if(!values.containsKey("antiAbuse")){
            values.put("antiAbuse", FileCache.getConfig("expConfig").getBoolean("Anti-Abuse.Place.Enabled"));
        }return Boolean.valueOf(values.get("antiAbuse").toString());
    }
    public static void clearCache(EXPCache cache){
        switch(cache){
            case ALL:
                minExp.clear();
                maxExp.clear();
                values.clear();
                random.clear();
                requiredBreak.clear();
                requiredPlace.clear();
                requiredDamage.clear();
                break;
            case EXP:
                minExp.clear();
                maxExp.clear();
                random.clear();
                break;
            case Values:
                values.clear();
                break;
            case Required:
                requiredBreak.clear();
                requiredPlace.clear();
                requiredDamage.clear();
                break;

        }
    }
}
