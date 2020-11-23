package levelpoints.Containers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import levelpoints.Cache.FileCache;
import levelpoints.Utils.AsyncEvents;
import levelpoints.levelpoints.Formatting;
import levelpoints.levelpoints.LevelPoints;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlayerContainer {
    private Player player;

    private static HashMap<String, Object> DataCache = new HashMap<>();
    private static HashMap<Player,HashMap<String, Object>> playerCache = new HashMap<>();
    private static HashMap<Player,HashMap<BoostersContainer, Integer>> boostersCache = new HashMap<>();
    private static HashMap<String,String> cache = new HashMap<>();
    private static DecimalFormat df = new DecimalFormat("#.##");


    public PlayerContainer(Player player){
        this.player = player;
        playerCache.put(player, new HashMap<>());
        boostersCache.put(player, new HashMap<>());
    }
    public Boolean isBoosterDone(){
        Date date = null;
        date = getBoosterDate();

        Date current = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        try {
            current = format.parse(format.format(current));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!current.after(date)) {
            return false;
        }
        return true;
    }

    public double getMultiplier(){
        if(!playerCache.get(player).containsKey("Multiplier")){
            playerCache.get(player).put("Multiplier", FileCache.getConfig(player.getUniqueId().toString()).getDouble("ActiveBooster"));
        }
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.parseDouble(df.format(playerCache.get(player).get("Multiplier")));
    }
    public void setMultiplier(double multiplier){
        playerCache.get(player).put("Multiplier", multiplier);
    }
    public void setBoosterDate(String time){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        playerCache.get(player).put("BoosterDate", format.format(Formatting.formatDate(time)));
    }
    public Date getBoosterDate()  {
        if(!playerCache.get(player).containsKey("BoosterDate")){
            playerCache.get(player).put("BoosterDate", FileCache.getConfig(player.getUniqueId().toString()).getString("BoosterOff"));
        }
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date d = null;
        try {
            d = format.parse(String.valueOf(playerCache.get(player).get("BoosterDate")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
    public void setBoosters(String value){
        if(boostersCache.containsKey(playerCache)) {
            boostersCache.get(playerCache).clear();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<BoostersContainer>>() {}.getType();
        ArrayList<BoostersContainer> output = gson.fromJson(value, type);
        for(BoostersContainer x : output){
            giveBooster(x.getMultiplier(), x.getTime(), x.getAmount());
        }
    }

    public int getLevel(){

        if(!playerCache.get(player).containsKey("Level")){
            playerCache.get(player).put("Level", FileCache.getConfig(player.getUniqueId().toString()).getInt("Level"));
        }
        return (int) playerCache.get(player).get("Level");
    }
    public double getEXP(){
        if(!playerCache.get(player).containsKey("EXP")){
            playerCache.get(player).put("EXP", FileCache.getConfig(player.getUniqueId().toString()).getDouble("EXP.Amount"));
        }
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.valueOf(df.format(playerCache.get(player).get("EXP"))) * 10 / 10.0;
    }
    public double getRequiredEXP(){
        if(FileCache.getConfig("levelConfig").getBoolean("Leveling.CustomLeveling.Enabled")){
            if(!playerCache.get(player).containsKey("RequiredEXP")){
                playerCache.get(player).put("RequiredEXP", LevelsContainer.getCustomLevelsEXP(getLevel()));
            }
        }else {
            if (!playerCache.get(player).containsKey("RequiredEXP")) {
                playerCache.get(player).put("RequiredEXP", LevelsContainer.generateFormula(player, LevelsContainer.getFormula()));
            }
        }
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.parseDouble(df.format(playerCache.get(player).get("RequiredEXP"))) * 10 / 10.0;
    }
    public int getPrestige(){
        if(!playerCache.get(player).containsKey("Prestige")){
            playerCache.get(player).put("Prestige", FileCache.getConfig(player.getUniqueId().toString()).getInt("Prestige"));
        }
        return (int) playerCache.get(player).get("Prestige");
    }
    public void addPrestige(int value){
        playerCache.get(player).put("Prestige", value + getPrestige());
    }
    public void giveBooster(double multiplier, String time, int amount) {

        if (boostersCache.get(player).isEmpty()) {
            boostersCache.get(player).put(new BoostersContainer(multiplier, time, AsyncEvents.getIds(player), amount), amount);
            //System.out.println("new");
            return;
        }
        int size = boostersCache.get(player).size();
        for(BoostersContainer x : boostersCache.get(player).keySet()){

            if(x.getMultiplier() == multiplier && x.getTime().equalsIgnoreCase(time)){
                int i = boostersCache.get(player).get(x);
                boostersCache.get(player).put(x, amount + i);
                System.out.println(Formatting.basicColor("&3LevelPoints>> &bAdded booster (" + multiplier + ")"));
                break;
            }
            size--;
            if(size == 0){

                boostersCache.get(player).put(new BoostersContainer(multiplier, time, AsyncEvents.getIds(player) + 1, amount), amount);
                AsyncEvents.addIds(player);
                System.out.println(Formatting.basicColor("&3LevelPoints>> &bAdded new booster (" + multiplier + ")"));
            }

        }
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<BoostersContainer>>() {}.getType();
//        ArrayList<BoostersContainer> output = gson.fromJson(cache.get("test"), type);
//        for(BoostersContainer x : output){
//            System.out.println(x.getMultiplier());
//        }
    }
    public Integer getBoosterAmount(Integer id){
        for(BoostersContainer x : boostersCache.get(player).keySet()){
            if(x.getId() == id){
                return boostersCache.get(player).get(x);

            }
        }
        return 0;
    }

    public void removeBooster(double multiplier, String time){
        for(BoostersContainer x : boostersCache.get(player).keySet()){

            if(x.getMultiplier() == multiplier && x.getTime().equalsIgnoreCase(time)){
                int i = boostersCache.get(player).get(x);
                if(i == 1){
                   boostersCache.get(player).remove(x);
                    //System.out.println(Formatting.basicColor("&3LevelPoints>> &bNo booster (" + multiplier + ") remaining"));
                    break;
                }
                x.setAmount(getBoosterAmount(x.getId()) - 1);
                boostersCache.get(player).put(x,  getBoosterAmount(x.getId()) - 1);
                break;
            }
        }
    }

    public BoostersContainer getBooster(Integer id){
        for(BoostersContainer x : boostersCache.get(player).keySet()){
            if(x.getId() == id){
                return x;

            }
        }
        return null;
    }
    public Boolean hasBooster(Double multiplier, String time){
        for(BoostersContainer x : boostersCache.get(player).keySet()) {

            if (x.getMultiplier() == multiplier && x.getTime().equalsIgnoreCase(time)) {
                return true;
            }
        }
        return false;
    }
    public HashMap<BoostersContainer, Integer> getBoosters(){
        return boostersCache.get(player);
    }

    public void setLevel(int value){
        playerCache.get(player).put("Level", value);
        playerCache.get(player).remove("RequiredEXP");
    }
    public void setPrestige(int value){
        playerCache.get(player).put("Prestige", value);
        playerCache.get(player).remove("RequiredEXP");
    }
    public void setEXP(double value){
        playerCache.get(player).put("EXP", value);
        playerCache.get(player).remove("RequiredEXP");
    }

    public void addEXP(double value) {
        if (getLevel() < LevelsContainer.getMaxLevel()) {
            if (getRequiredEXP() >= getEXP()) {
                double has = getEXP();
                double multi = getMultiplier();
                if(LevelPoints.getInstance().getConfig().getBoolean("RankMultipliers")){
                    multi = multi * AsyncEvents.getRankMultiplier(player);
                }
                has = has + (value * multi);
                playerCache.get(player).put("EXP", has);
                //System.out.println(Formatting.basicColor("&3Added " + value + " EXP to " + player.getName()));
                if (canLevelUp()) {
                    addLevel(1, true);
                }
            } else {
                //System.out.println(Formatting.basicColor("&3Added"));
            }
        }else{
            if(getRequiredEXP() >= getEXP()){

                double has = getEXP();
                has = has + value;
                if(has > getRequiredEXP()){
                    playerCache.get(player).put("EXP", getRequiredEXP());
                }else {
                    playerCache.get(player).put("EXP", has);
                }
                //System.out.println(Formatting.basicColor("&3Added " + value + " EXP to " + player.getName()));
            }
        }
    }
    public void removeEXP(double value){
        BigDecimal amount = BigDecimal.valueOf(value);
        if(getEXP() >= value){
            double has = getEXP();
            has = has - value;
            playerCache.get(player).put("EXP", has);

        }else{
            //System.out.println(Formatting.basicColor("&cCannot remove &4" + amount + "&c of EXP as player only has &4" + getEXP()));
        }

    }
    public void saveCacheToFile(){
        System.out.println(Formatting.basicColor("&3Saving PlayerCache to file"));


        File userdata = new File(LevelPoints.getUserFolder(), player.getUniqueId() + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        UsersConfig.set("Level", getLevel());
        UsersConfig.set("EXP.Amount", getEXP());
        UsersConfig.set("Prestige", getPrestige());

        int i = 0;
        ArrayList<BoostersContainer> boosters = new ArrayList<>();;
        for(BoostersContainer x : boostersCache.get(player).keySet()){
            boosters.add(x);
            i++;
            UsersConfig.set("Boosters." + i + ".Multiplier", x.getMultiplier());
            UsersConfig.set("Boosters." + i + ".Time", x.getTime());
            UsersConfig.set("Boosters." + i + ".Amount", boostersCache.get(player).get(x));

        }
//        Gson gson = new Gson();
//        String saves = gson.toJson(boosters);
//        System.out.println(saves);
//        cache.put("test", saves);

        try {
            UsersConfig.save(userdata);
            System.out.println(Formatting.basicColor("&bSaved PlayerCache to file"));
            playerCache.get(player).clear();
            FileCache.removeFileFromCache(player.getUniqueId().toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(Formatting.basicColor("&4Failed to save playerData please contact Zoon20X"));
        }


    }
    public String getBoosterString(){
        ArrayList<BoostersContainer> boosters = new ArrayList<>();
        for(BoostersContainer x : boostersCache.get(player).keySet()){
            boosters.add(x);
        }
        Gson gson = new Gson();
        String save = gson.toJson(boosters);
        return save;
    }
    public String getChatFormat(){
        if(!playerCache.get(player).containsKey("Format")){
            for(FormatsContainer x : LevelsContainer.getFormats()){
                if(getLevel() <= x.getMaxLevel()){
                    if(getLevel() >= x.getMinLevel()){
                        playerCache.get(player).put("Format", x.getFormat());
                    }
                }
            }
        }
        System.out.println(playerCache.get(player).get("Format"));
        return String.valueOf(playerCache.get(player).get("Format"));
    }

    public Boolean canPrestige() {
        if (getPrestige() < LevelsContainer.getMaxPrestige()) {
            if (getLevel() >= LevelsContainer.getMaxLevel()) {
                if (getEXP() == getRequiredEXP()) {
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        } else {
            return false;
        }
    }


    public void clearPlayerCache(){
        playerCache.get(player).clear();
    }

    public Boolean canLevelUp(){
        return getEXP() >= getRequiredEXP();
    }

    public void addLevel(int value, Boolean removeEXP){

        if(removeEXP) {
            removeEXP(getRequiredEXP());
        }

        AsyncEvents.triggerLevelUpEvent(player, getLevel() + value);
        playerCache.get(player).put("Level", getLevel() + value);
        playerCache.get(player).remove("RequiredEXP");
        if(!canLevelUp()){
            return;
        }else{
            addLevel(1, removeEXP);
        }

    }


}
