package me.zoon20x.levelpoints.containers.Settings.Crafting;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Settings.Mobs.MobData;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.Collection;
import java.util.HashMap;

public class CraftingUtils {

    private static HashMap<Material, CraftingData> craftingData = new HashMap<>();


    public static void clear(){
        craftingData.clear();
    }

    public static Collection<CraftingData> getAllItems(){
        return craftingData.values();
    }
    public static void addItem(CraftingData data){
        LevelPoints.getDebug(DebugSeverity.WARNING, data.getMaterial().name());
        craftingData.put(data.getMaterial(), data);
    }
    public static CraftingData getCraftingData(Material type){
        return craftingData.get(type);
    }
    public static Boolean hasItem(Material type){
        return craftingData.containsKey(type);
    }
    public static Boolean hasItem(String type){
        if(Material.getMaterial(type) == null){
            return false;
        }
        return craftingData.containsKey(Material.valueOf(type));
    }

}
