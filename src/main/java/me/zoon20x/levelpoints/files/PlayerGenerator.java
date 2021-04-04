package me.zoon20x.levelpoints.files;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Player.ActiveBooster;
import me.zoon20x.levelpoints.containers.Player.PlayerData;
import me.zoon20x.levelpoints.utils.DataLocation;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerGenerator {


    private FileConfiguration getConfig(File file){
        return YamlConfiguration.loadConfiguration(file);
    }


    public void saveAllData(){
        for(PlayerData data : LevelPoints.getPlayerStorage().getAllLoaded()){
            savePlayerData(data);
        }

        LevelPoints.getDebug(DebugSeverity.NORMAL, "Saved all data");
    }

    public void savePlayerData(PlayerData data){
        File userFile = new File(LevelPoints.getUserFolder(), data.getUUID() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(userFile);
        config.set(DataLocation.Level, data.getLevel());
        config.set(DataLocation.EXP, data.getExp());
        config.set(DataLocation.Prestige,data.getPrestige());
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
        Double requiredExp = LevelPoints.getLevelSettings().getRequireExp(level);
        Integer prestige = config.getInt(DataLocation.Prestige);
        PlayerData data = new PlayerData(uuid, name, level, exp, requiredExp, prestige, 0, new ActiveBooster());

        LevelPoints.getPlayerStorage().addData(uuid, data);
    }

    public void generateNewPlayer(UUID uuid, String name){
        File file = createNewPlayer(uuid);
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        configuration.set(DataLocation.Name, name);
        configuration.set(DataLocation.Level, LevelPoints.getLevelSettings().getStartingLevel());
        configuration.set(DataLocation.EXP, LevelPoints.getLevelSettings().getStartingExp());
        configuration.set(DataLocation.Prestige, LevelPoints.getLevelSettings().getStartingPrestige());
        try {
            configuration.save(file);
            loadPlayerFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createNewPlayer(UUID uuid){
        File userFile = new File(LevelPoints.getUserFolder(), uuid + ".yml");
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
        LevelPoints.getPlayerStorage().removeData(uuid);
        File file = new File(LevelPoints.getUserFolder(), uuid + ".yml");
        file.delete();
        return true;
    }


}
