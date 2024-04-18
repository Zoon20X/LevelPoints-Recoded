package me.zoon20x.levelpoints.spigot.containers.Mobs;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.spigot.API.MobSettingsAPI;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import org.bukkit.entity.EntityType;

import java.io.IOException;
import java.util.HashMap;

public class MobSettings implements MobSettingsAPI {

    private boolean enabled;
    private final HashMap<EntityType, MobData> mobDataMap = new HashMap<>();

    public MobSettings(boolean isEnabled){
        setEnabled(isEnabled);

    }

    public void reload() throws IOException {
        mobDataMap.clear();
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getMobSettingsConfig();
        config.reload();
        setEnabled(config.getBoolean("Mobs.Enabled"));
    }

    private void load(){
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getMobSettingsConfig();
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
