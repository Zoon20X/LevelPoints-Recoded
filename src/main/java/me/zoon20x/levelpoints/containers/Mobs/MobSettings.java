package me.zoon20x.levelpoints.containers.Mobs;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.API.MobSettingsAPI;
import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.containers.Blocks.BlockData;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class MobSettings implements MobSettingsAPI {

    private boolean enabled;
    private final HashMap<EntityType, MobData> mobDataMap = new HashMap<>();

    public MobSettings(boolean isEnabled){
        setEnabled(isEnabled);

    }

    private void load(){
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getMobSettings();
        mobDataMap.clear();
        config.getSection("Mobs.Settings").getRoutesAsStrings(false).forEach(entity ->{
            EntityType entityType = EntityType.valueOf(entity);
            int attackRequired = config.getInt("Mobs.Settings." + entity + ".RequiredLevel.Attack");
            double exp = config.getDouble("Mobs.Settings." + entity + ".Exp");
            mobDataMap.put(entityType, new MobData(entityType, exp, attackRequired));
        });
    }
    private void unLoad(){
        if(mobDataMap.isEmpty()){
            return;
        }
        mobDataMap.clear();
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


    public HashMap<EntityType, MobData> getMobDataMap() {
        return mobDataMap;
    }
    public boolean hasMob(EntityType entityType){
        return mobDataMap.containsKey(entityType);
    }
    public MobData getMobData(EntityType entityType){
        return mobDataMap.get(entityType);
    }
}