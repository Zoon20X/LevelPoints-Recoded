package me.zoon20x.levelpoints.containers.Top;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.zoon20x.levelpoints.LevelPoints;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TopData {

    private String name;
    private UUID uuid;
    private int level;

    public TopData(String name, UUID uuid, int level){
        this.name = name;
        this.uuid = uuid;
        this.level = level;
    }



    public UUID getUUID() {
        return uuid;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
