package me.zoon20x.spigot.levelpoints.containers.Blocks;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.devTools.spigot.stats.BlockStat;
import me.zoon20x.spigot.levelpoints.API.BlockSettingsAPI;
import me.zoon20x.spigot.levelpoints.LevelPoints;
import org.bukkit.Material;

import java.io.IOException;
import java.util.HashMap;

public class BlockSettings implements BlockSettingsAPI {

    private boolean enabled;
    private final HashMap<Material, BlockData> blockDataMap = new HashMap<>();

    public BlockSettings(boolean isEnabled){
        setEnabled(isEnabled);

    }

    public void reload() throws IOException {
        blockDataMap.clear();
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getBlockSettingsConfig();
        config.reload();
        load();
    }

    private void load(){
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getBlockSettingsConfig();
        config.getSection("Blocks.Settings").getRoutesAsStrings(false).forEach(block ->{
            Material material = Material.getMaterial(block);
            int breakRequired = config.getInt("Blocks.Settings." + block + ".RequiredLevel.Break");
            int placeRequired = config.getInt("Blocks.Settings." + block + ".RequiredLevel.Place");
            double exp = config.getDouble("Blocks.Settings." + block + ".Exp");
            blockDataMap.put(Material.valueOf(block), new BlockData(material, exp, breakRequired, placeRequired));
        });
    }
    private void unLoad(){
        if(blockDataMap.isEmpty()){
            return;
        }
        blockDataMap.clear();
    }
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            load();
            return;
        }
        unLoad();
    }


    public HashMap<Material, BlockData> getBlockDataMap() {
        return blockDataMap;
    }
    public boolean hasBlock(Material material){
        return blockDataMap.containsKey(material);
    }
    public BlockData getBlockData(Material material){
        return blockDataMap.get(material);
    }

}
