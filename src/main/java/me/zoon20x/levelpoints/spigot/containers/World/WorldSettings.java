package me.zoon20x.levelpoints.spigot.containers.World;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.spigot.LevelPoints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorldSettings {

    private List<String> disabledWorlds;
    private boolean isEnabled;

    public WorldSettings(){
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getWorldSettings();
        isEnabled = config.getBoolean("World.Enabled");
        disabledWorlds = config.getStringList("World.Disabled-Worlds");
    }
    public void reload() throws IOException {
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getBlockSettingsConfig();
        config.reload();
        isEnabled = config.getBoolean("World.Enabled");
        disabledWorlds = config.getStringList("World.Disabled-Worlds");
    }




    public void addWorld(String world){
        disabledWorlds.add(world);
    }
    public void removeWorld(String world){
        disabledWorlds.remove(world);
    }
    public boolean hasWorld(String world){
        return disabledWorlds.contains(world);
    }


    public boolean isEnabled() {
        return isEnabled;
    }
}
