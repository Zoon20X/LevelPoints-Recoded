package me.zoon20x.levelpoints.containers.Settings.Mobs;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.bukkit.entity.EntityType;

import java.util.Collection;
import java.util.HashMap;

public class MobUtils {
    private static HashMap<EntityType, MobData> mobData = new HashMap<>();


    public static Collection<MobData> getAllMobs(){
        return mobData.values();
    }
    public static void addMob(MobData data){
        LevelPoints.getDebug(DebugSeverity.WARNING, data.getMob().name());
        mobData.put(data.getMob(), data);
    }
    public static MobData getMobData(EntityType type){
        return mobData.get(type);
    }
    public static Boolean hasMob(EntityType type){
        return mobData.containsKey(type);
    }
    public static Boolean hasMob(String type){
        if(EntityType.fromName(type) == null){
            return false;
        }
        return mobData.containsKey(EntityType.valueOf(type));
    }

}
