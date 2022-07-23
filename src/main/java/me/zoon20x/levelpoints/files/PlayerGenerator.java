package me.zoon20x.levelpoints.files;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.ActiveBooster;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.utils.DataLocation;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.apache.commons.lang.time.DateUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class PlayerGenerator {


    private FileConfiguration getConfig(File file){
        return YamlConfiguration.loadConfiguration(file);
    }


    public void saveAllData(){
        for(PlayerData data : LevelPoints.getInstance().getPlayerStorage().getAllLoaded()){
            savePlayerData(data);
        }

        LevelPoints.getDebug(DebugSeverity.NORMAL, "Saved all data");
    }

    public void savePlayerData(PlayerData data){
        File userFile = new File(LevelPoints.getInstance().getUserFolder(), data.getUUID() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(userFile);
        config.set(DataLocation.Level, data.getLevel());
        config.set(DataLocation.EXP, data.getExp());
        config.set(DataLocation.Prestige,data.getPrestige());
        config.set(DataLocation.ActiveBoosterID, data.getActiveBooster().getID());
        config.set(DataLocation.ActiveBoosterMultiplier, data.getActiveBooster().getMultiplier());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        config.set(DataLocation.ActiveBoosterTime, format.format(data.getActiveBooster().getDateExpire()));
        data.getAllBoosters().forEach(x->{
            config.set(DataLocation.getUserBoosterList(x), data.getBoosterStorage().get(x));
        });
        try {
            config.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadPlayerFile(File file){
        UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        String name = config.getString(DataLocation.Name);
        Integer level = config.getInt(DataLocation.Level);
        Double exp = config.getDouble(DataLocation.EXP);
        Double requiredExp = LevelPoints.getInstance().getLevelSettings().getRequireExp(level);
        Integer prestige = config.getInt(DataLocation.Prestige);
        String id = config.getString(DataLocation.ActiveBoosterID);
        double multiplier = config.getInt(DataLocation.ActiveBoosterMultiplier);
        Date date = format(config.getString(DataLocation.ActiveBoosterTime));
        String levelColor = "none";
        if(LevelPoints.getInstance().getConfigSettings().isPlaceholderColorLevelEnabled()){
            levelColor = LevelPoints.getInstance().getLevelColorSettings().getLevelColor(level);
        }
        LevelPoints.getDebug(DebugSeverity.SEVER, levelColor);


        PlayerData data = new PlayerData(uuid, name, level, exp, requiredExp, prestige, 0, new ActiveBooster(id, date, multiplier), levelColor);
        if(config.contains(DataLocation.BoosterList))
            config.getConfigurationSection(DataLocation.BoosterList).getKeys(false).forEach(x->{
                if(LevelPoints.getInstance().getBoosterSettings().hasBooster(x)){
                    data.addBooster(LevelPoints.getInstance().getBoosterSettings().getBooster(x), config.getInt(DataLocation.getUserBoosterList(x)));
                }
            });


        LevelPoints.getInstance().getPlayerStorage().addData(uuid, data);
    }

    public void generateNewPlayer(UUID uuid, String name){
        File file = createNewPlayer(uuid);
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        configuration.set(DataLocation.Name, name);
        configuration.set(DataLocation.Level, LevelPoints.getInstance().getLevelSettings().getStartingLevel());
        configuration.set(DataLocation.EXP, LevelPoints.getInstance().getLevelSettings().getStartingExp());
        configuration.set(DataLocation.Prestige, LevelPoints.getInstance().getLevelSettings().getStartingPrestige());
        configuration.set(DataLocation.ActiveBoosterID, "none");
        configuration.set(DataLocation.ActiveBoosterMultiplier, 1.0);
        configuration.set(DataLocation.ActiveBoosterTime, getCurrentDate());
        try {
            configuration.save(file);
            loadPlayerFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createNewPlayer(UUID uuid){
        File userFile = new File(LevelPoints.getInstance().getUserFolder(), uuid + ".yml");
        if (!userFile.exists()) {
            try {
                userFile.createNewFile();
                LevelPoints.getDebug(DebugSeverity.NORMAL, "Created " + uuid + " player file");
            } catch (IOException ex) {
                LevelPoints.getDebug(DebugSeverity.SEVER, "Can't create " + uuid + " user file");
            }
        }
        return userFile;
    }

    public Boolean deleteOldPlayer(UUID uuid){
        LevelPoints.getInstance().getPlayerStorage().removeData(uuid);
        File file = new File(LevelPoints.getInstance().getUserFolder(), uuid + ".yml");
        file.delete();
        return true;
    }

    public Date formatBoosterTime(String x) {

        switch (formatTime(x)) {
            case "Minutes":
                x = x.replace("m", "");
                return DateUtils.addMinutes(new Date(), Integer.parseInt(x));
            case "Hours":
                x = x.replace("h", "");

                return DateUtils.addHours(new Date(), Integer.parseInt(x));
            case "Days":
                x = x.replace("d", "");
                return DateUtils.addDays(new Date(), Integer.parseInt(x));
            case "Seconds":
                x = x.replace("s", "");
                return DateUtils.addSeconds(new Date(), Integer.parseInt(x));
        }

        return new Date();
    }
    private String formatTime(String value){
        if(value.contains("m")){
            return "Minutes";
        }
        if(value.contains("h")){
            return "Hours";
        }
        if(value.contains("d")){
            return "Days";
        }
        if(value.contains("s")){
            return "Seconds";
        }
        return "";
    }
    public String getCurrentDate(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date cDate = new Date();
        String cDateS = format.format(cDate);
        return cDateS;
    }
    private Date format(String x){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");
        Date finish = null;
        try {
            finish = format.parse(x);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finish;
    }

}
