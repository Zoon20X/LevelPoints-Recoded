package me.zoon20x.levelpoints.spigot.containers.Farming;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.spigot.API.BlockSettingsAPI;
import me.zoon20x.levelpoints.spigot.API.FarmSettingsAPI;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import org.bukkit.Material;

import java.io.IOException;
import java.util.HashMap;

public class FarmSettings implements FarmSettingsAPI {

    private boolean enabled;
    private final HashMap<Material, FarmData> farmDataMap = new HashMap<>();

    public FarmSettings(boolean isEnabled){
        setEnabled(isEnabled);

    }

    public void reload() throws IOException {
        farmDataMap.clear();
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getFarmSettings();
        config.reload();
        setEnabled(config.getBoolean("Farming.Enabled"));
    }

    private void load(){
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getFarmSettings();
        config.getSection("Farming.Settings").getRoutesAsStrings(false).forEach(block ->{
            Material material = Material.getMaterial(block);
            int farmRequired = config.getInt("Farming.Settings." + block + ".RequiredLevel.Farm");
            double exp = config.getDouble("Farming.Settings." + block + ".Exp");
            farmDataMap.put(Material.valueOf(block), new FarmData(material, exp, farmRequired));
        });
    }
    private void unLoad(){
        if(farmDataMap.isEmpty()){
            return;
        }
        farmDataMap.clear();
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


    public HashMap<Material, FarmData> getFarmDataMap() {
        return farmDataMap;
    }
    public boolean hasFarm(Material material){
        return farmDataMap.containsKey(material);
    }
    public FarmData getFarmData(Material material){
        return farmDataMap.get(material);
    }

}
