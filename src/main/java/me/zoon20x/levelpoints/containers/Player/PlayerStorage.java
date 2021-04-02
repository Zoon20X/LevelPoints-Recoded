package me.zoon20x.levelpoints.containers.Player;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.utils.DebugSeverity;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class PlayerStorage {

    private static HashMap<UUID, PlayerData> loadedData = new HashMap<>();

    public PlayerData getLoadedData(UUID uuid){
        if(!hasLoadedData(uuid)){
            LevelPoints.getPlayerGenerator().loadPlayerFile(new File(LevelPoints.getUserFolder(),uuid + ".yml"));
        }


        return loadedData.get(uuid);
    }


    public void loadData(){
        long startTime = System.nanoTime();
        LevelPoints.getPlayerGenerator().loadAllPlayerFiles();
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        LevelPoints.getDebug(DebugSeverity.NORMAL, "Player Data took " + duration + "ms, to load " + getAllLoaded().size() + " player files");

    }
    public boolean hasPlayerFile(UUID uuid){
        File dir = new File(LevelPoints.getUserFolder(), uuid + ".yml");
        return dir.exists();
    }

    public Boolean hasLoadedData(UUID uuid){
        return loadedData.containsKey(uuid);
    }
    public Collection<PlayerData> getAllLoaded(){
        return loadedData.values();
    }
    public void addData(UUID uuid, PlayerData data){

        loadedData.put(uuid, data);
    }
    public Integer getAmountLoaded(){
        return loadedData.size();
    }
    public Boolean removeData(UUID uuid){
        if(!loadedData.containsKey(uuid)){
            return false;
        }
        loadedData.remove(uuid);
        return true;
    }


}
