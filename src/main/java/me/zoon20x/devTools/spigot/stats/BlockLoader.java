package me.zoon20x.devTools.spigot.stats;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Material;

import java.util.HashMap;

public class BlockLoader {

    private HashMap<Material, BlockStat> blocks = new HashMap<>();


    public BlockLoader(YamlDocument config){
        config.getSection("Blocks.Settings").getRoutesAsStrings(false).forEach(block ->{
            String material = block;
            int breakRequired = config.getInt("Blocks.Settings." + block + ".RequiredLevel.Break");
            int placeRequired = config.getInt("Blocks.Settings." + block + ".RequiredLevel.Place");
            double exp = config.getDouble("Blocks.Settings." + block + ".Exp");
            blocks.put(Material.valueOf(block), new BlockStat(material, exp, breakRequired, placeRequired));
        });
    }


    public HashMap<Material, BlockStat> getAllBlocks() {
        return blocks;
    }
    public BlockStat getBlock(Material material){
        return blocks.get(material);
    }
    public boolean hasBlock(Material material){
        return blocks.containsKey(material);
    }
}
