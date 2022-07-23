package me.zoon20x.levelpoints.containers.Breeding;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Settings.Mobs.MobData;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.bukkit.entity.EntityType;

import java.util.Collection;
import java.util.HashMap;

public class BreedingUtils {

    private static HashMap<EntityType, BreedingData> breedData = new HashMap<>();

    public static void clear(){
        breedData.clear();
    }
    public static Collection<BreedingData> getAllBreeds(){
        return breedData.values();
    }
    public static void addBreed(BreedingData data){
        breedData.put(data.getMob(), data);
    }
    public static BreedingData getBreedData(EntityType type){
        return breedData.get(type);
    }
    public static Boolean hasBreed(EntityType type){
        return breedData.containsKey(type);
    }
    public static Boolean hasBreed(String type){
        if(EntityType.fromName(type) == null){
            return false;
        }
        return breedData.containsKey(EntityType.valueOf(type));
    }
}
