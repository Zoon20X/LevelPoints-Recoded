package me.zoon20x.levelpoints.containers.Settings.Blocks;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.bukkit.Material;

import java.util.Collection;
import java.util.HashMap;

public class BlockUtils {
    private static HashMap<Material, HashMap<Byte, BlockData>> blocks = new HashMap<>();


    public static Collection<HashMap<Byte,BlockData>> getAllBlocks(){
        return blocks.values();
    }

    public static void addBlock(Material mat, BlockData data){
        LevelPoints.getDebug(DebugSeverity.WARNING, data.getMaterial());
        if(!blocks.containsKey(mat)) {
            blocks.put(mat, new HashMap<>());
        }

        blocks.get(mat).put(data.getData(), data);
    }
    public static BlockData getBlockData(Material material, Byte x){

        return blocks.get(material).get(x);
    }
    public static Boolean hasBlockExtraData(Material material, Byte x){
        return blocks.get(material).containsKey(x);
    }

    public static Boolean hasBlockData(Material material){
        return blocks.containsKey(material);
    }
    public static Boolean hasBlockData(String material){
        if(Material.getMaterial(material) == null){
            return false;
        }

        return blocks.containsKey(Material.getMaterial(material));
    }

}
