package levelpoints.Containers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import levelpoints.Cache.FileCache;
import levelpoints.Cache.SettingsCache;
import levelpoints.Utils.AsyncEvents;
import levelpoints.Utils.MessagesUtil;
import levelpoints.Utils.UtilCollector;
import levelpoints.levelpoints.Formatting;
import levelpoints.levelpoints.LevelPoints;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
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
    private final UUID uuid;
    private Integer level;
    private Double exp;
    private Double multiplier;
    private Date boosterDate;
    private Double requiredEXP;
    private Integer prestige;
    private String chatFormat;
    private Integer previousLevel;



    private static HashMap<UUID,HashMap<BoostersContainer, Integer>> boostersCache = new HashMap<>();
    private static HashMap<String,String> cache = new HashMap<>();
    private static DecimalFormat df = new DecimalFormat("#.##");



    public PlayerContainer(UUID uuid, Integer level, Double exp, Double multiplier, Date boosterDate, Double requiredEXP, Integer prestige){
        this.uuid = uuid;
        this.level = level;
        this.exp = exp;
        this.multiplier = multiplier;
        this.boosterDate = boosterDate;
        this.requiredEXP = requiredEXP;
        this.prestige = prestige;
        this.previousLevel = 0;
        boostersCache.put(uuid, new HashMap<>());
    }
    public PlayerContainer(Player player, Integer level, Double exp, Double multiplier, Date boosterDate, Double requiredEXP, Integer prestige){
        this.uuid = player.getUniqueId();
        this.level = level;
        this.exp = exp;
        this.multiplier = multiplier;
        this.boosterDate = boosterDate;
        this.requiredEXP = requiredEXP;
        this.prestige = prestige;
        this.previousLevel = 0;
        boostersCache.put(uuid, new HashMap<>());
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

        df.setRoundingMode(RoundingMode.DOWN);
        return Double.parseDouble(df.format(multiplier));
    }
    public void setMultiplier(double multiplier){
        this.multiplier = multiplier;
    }
    public void setBoosterDate(String time){
        this.boosterDate = Formatting.formatDate(time);
    }
    public Date getBoosterDate()  {

        return boosterDate;
    }
    public void setBoosters(String value){
        if(boostersCache.containsKey(uuid)) {
            boostersCache.get(uuid).clear();
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<BoostersContainer>>() {}.getType();
        ArrayList<BoostersContainer> output = gson.fromJson(value, type);
        for(BoostersContainer x : output){
            giveBooster(x.getMultiplier(), x.getTime(), x.getAmount());
        }
    }

    public int getLevel(){
        return this.level;
    }
    public double getEXP(){
        return this.exp;
    }
    public double getRequiredEXP(){
        return this.requiredEXP;
    }
    public int getPrestige(){
        return this.prestige;
    }
    public void addPrestige(int value){
        prestige = prestige + value;
    }
    public void giveBooster(double multiplier, String time, int amount) {

        if (boostersCache.get(uuid).isEmpty()) {
            boostersCache.get(uuid).put(new BoostersContainer(multiplier, time, AsyncEvents.getIds(uuid), amount), amount);
            //System.out.println("new");
            return;
        }
        int size = boostersCache.get(uuid).size();
        for(BoostersContainer x : boostersCache.get(uuid).keySet()){

            if(x.getMultiplier() == multiplier && x.getTime().equalsIgnoreCase(time)){
                int i = boostersCache.get(uuid).get(x);
                boostersCache.get(uuid).put(x, amount + i);
                System.out.println(Formatting.basicColor("&3LevelPoints>> &bAdded booster (" + multiplier + ")"));
                break;
            }
            size--;
            if(size == 0){

                boostersCache.get(uuid).put(new BoostersContainer(multiplier, time, AsyncEvents.getIds(uuid) + 1, amount), amount);
                AsyncEvents.addIds(uuid);
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
        for(BoostersContainer x : boostersCache.get(uuid).keySet()){
            if(x.getId() == id){
                return boostersCache.get(uuid).get(x);

            }
        }
        return 0;
    }

    public void removeBooster(double multiplier, String time){
        for(BoostersContainer x : boostersCache.get(uuid).keySet()){

            if(x.getMultiplier() == multiplier && x.getTime().equalsIgnoreCase(time)){
                int i = boostersCache.get(uuid).get(x);
                if(i == 1){
                   boostersCache.get(uuid).remove(x);
                    //System.out.println(Formatting.basicColor("&3LevelPoints>> &bNo booster (" + multiplier + ") remaining"));
                    break;
                }
                x.setAmount(getBoosterAmount(x.getId()) - 1);
                boostersCache.get(uuid).put(x,  getBoosterAmount(x.getId()) - 1);
                break;
            }
        }
    }

    public BoostersContainer getBooster(Integer id){
        for(BoostersContainer x : boostersCache.get(uuid).keySet()){
            if(x.getId() == id){
                return x;

            }
        }
        return null;
    }
    public Boolean hasBooster(Double multiplier, String time){
        for(BoostersContainer x : boostersCache.get(uuid).keySet()) {

            if (x.getMultiplier() == multiplier && x.getTime().equalsIgnoreCase(time)) {
                return true;
            }
        }
        return false;
    }
    public HashMap<BoostersContainer, Integer> getBoosters(){
        return boostersCache.get(uuid);
    }

    public void setLevel(int value){
        previousLevel = level;
        level = value;
        requiredEXP = UtilCollector.registerRequiredEXP(value);
        setXpBar();
    }

    public void setXpBar(){
        if(SettingsCache.isBooleansEmpty() || !SettingsCache.isInCache("LpsToXpBar")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    SettingsCache.cacheBoolean("LpsToXpBar", LevelPoints.getInstance().getConfig().getBoolean("LpsToXpBar"));
                    if (LevelPoints.getInstance().getConfig().getBoolean("LpsToXpBar")) {
                        if (Bukkit.getPlayer(uuid) != null) {
                            Player player = Bukkit.getPlayer(uuid);
                            player.setLevel(getLevel());
                            player.setExp((float) (getEXP() / getRequiredEXP()));
                        }
                    }
                }
            }.runTaskAsynchronously(LevelPoints.getInstance());
        }else{
            if(SettingsCache.getBoolean("LpsToXpBar")){
                if (Bukkit.getPlayer(uuid) != null) {
                    Player player = Bukkit.getPlayer(uuid);
                    player.setLevel(getLevel());
                    player.setExp((float) (getEXP() / getRequiredEXP()));
                }
            }
        }
    }

    public void setPrestige(int value){
        prestige = prestige;
    }
    public void setEXP(double value){
        exp = value;
        requiredEXP = UtilCollector.registerRequiredEXP(getLevel());
    }

    public void addEXP(double value) {
        if (getLevel() < LevelsContainer.getMaxLevel()) {
            if (getRequiredEXP() >= getEXP()) {
                double has = getEXP();
                double multi = getMultiplier();
                if(LevelPoints.getInstance().getConfig().getBoolean("RankMultipliers")){
                    if (Bukkit.getPlayer(uuid) != null) {
                        Player player = Bukkit.getPlayer(uuid);
                        multi = multi * AsyncEvents.getRankMultiplier(player);
                    }
                }
                has = has + (value * multi);
                exp = has;
                //System.out.println(Formatting.basicColor("&3Added " + value + " EXP to " + player.getName()));
                if (canLevelUp()) {
                    addLevel(1, true);
                }
                float percentage = (float) getEXP();
                double val = (percentage / getRequiredEXP());
                if (!Bukkit.getVersion().contains("1.8")) {
                    if (Bukkit.getPlayer(uuid) != null) {
                        Player player = Bukkit.getPlayer(uuid);
                        MessagesUtil.sendBossBar(player,
                                FileCache.getConfig("langConfig").getString("Formats.LevelUp.BossBar.Text"),
                                BarColor.valueOf(LevelPoints.getInstance().getConfig().getString("BossBarColor")),
                                BarStyle.SOLID,
                                val);
                    }
                }
            } else {
                //System.out.println(Formatting.basicColor("&3Added"));

            }
            setXpBar();
        }else{
            if(getRequiredEXP() >= getEXP()){

                double has = getEXP();
                has = has + value;
                if(has > getRequiredEXP()){
                    exp = getRequiredEXP();
                }else {
                    exp = has;
                }
                //System.out.println(Formatting.basicColor("&3Added " + value + " EXP to " + player.getName()));
                setXpBar();
            }
        }
    }
    public void removeEXP(double value){
        BigDecimal amount = BigDecimal.valueOf(value);
        if(value <= getRequiredEXP()) {
            if (getEXP() >= value) {
                double has = getEXP();
                has = has - value;
                exp = has;
                setXpBar();
            } else {
                //System.out.println(Formatting.basicColor("&cCannot remove &4" + amount + "&c of EXP as player only has &4" + getEXP()));
            }
        }else{
            removeEXP(getRequiredEXP());
        }

    }
    public void saveCacheToFile(){
        System.out.println(Formatting.basicColor("&3Saving PlayerCache to file"));


        File userdata = new File(LevelPoints.getUserFolder(), uuid + ".yml");
        FileConfiguration UsersConfig = YamlConfiguration.loadConfiguration(userdata);
        UsersConfig.set("Level", getLevel());
        UsersConfig.set("EXP.Amount", getEXP());
        UsersConfig.set("Prestige", getPrestige());

        int i = 0;
        ArrayList<BoostersContainer> boosters = new ArrayList<>();;
        for(BoostersContainer x : boostersCache.get(uuid).keySet()){
            boosters.add(x);
            i++;
            UsersConfig.set("Boosters." + i + ".Multiplier", x.getMultiplier());
            UsersConfig.set("Boosters." + i + ".Time", x.getTime());
            UsersConfig.set("Boosters." + i + ".Amount", boostersCache.get(uuid).get(x));

        }
//        Gson gson = new Gson();
//        String saves = gson.toJson(boosters);
//        System.out.println(saves);
//        cache.put("test", saves);

        try {
            UsersConfig.save(userdata);
            System.out.println(Formatting.basicColor("&bSaved PlayerCache to file"));
            FileCache.removeFileFromCache(uuid.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(Formatting.basicColor("&4Failed to save playerData please contact Zoon20X"));
        }
        AsyncEvents.removePlayerFromContainerCache(uuid);

    }
    public String getBoosterString(){
        ArrayList<BoostersContainer> boosters = new ArrayList<>();
        for(BoostersContainer x : boostersCache.get(uuid).keySet()){
            boosters.add(x);
        }
        Gson gson = new Gson();
        String save = gson.toJson(boosters);
        return save;
    }
    public String getChatFormat() {
        if(previousLevel != level) {
            for (FormatsContainer x : LevelsContainer.getFormats()) {
                if (getLevel() <= x.getMaxLevel()) {
                    if (getLevel() >= x.getMinLevel()) {
                        previousLevel = level;
                        chatFormat = x.getFormat();
                    }
                }
            }
        }

        return this.chatFormat;
    }

    public Boolean canPrestige() {
        if (getPrestige() < LevelsContainer.getMaxPrestige()) {
            if (getLevel() >= LevelsContainer.getMaxLevel()) {
                if (getEXP() == getRequiredEXP()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean canLevelUp(){
        return getEXP() >= getRequiredEXP();
    }

    public void addLevel(int value, Boolean removeEXP){
        if(LevelsContainer.getMaxLevel() == getLevel()){
            setEXP(getRequiredEXP());
            return;
        }
        if(removeEXP) {
            removeEXP(getRequiredEXP());
        }
        if(Bukkit.getPlayer(uuid) !=null) {
            Player player = Bukkit.getPlayer(uuid);
            AsyncEvents.triggerLevelUpEvent(player, getLevel() + value);
        }
        previousLevel = level;
        level = level + value;
        requiredEXP = UtilCollector.registerRequiredEXP(getLevel());
        if(!canLevelUp()){
            return;
        }else{
            addLevel(1, removeEXP);
        }

    }


}
