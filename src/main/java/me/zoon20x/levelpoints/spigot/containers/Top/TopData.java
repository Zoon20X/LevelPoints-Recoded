package me.zoon20x.levelpoints.spigot.containers.Top;

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
