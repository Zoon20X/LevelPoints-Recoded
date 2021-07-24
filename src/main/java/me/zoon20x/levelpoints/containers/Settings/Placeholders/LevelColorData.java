package me.zoon20x.levelpoints.containers.Settings.Placeholders;

import java.util.List;

public class LevelColorData {

    private String id;
    private String color;
    private List<String> levels;

    public LevelColorData(String id, String color, List<String> levels){
        this.id = id;
        this.color = color;
        this.levels = levels;
    }


    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public List<String> getLevels() {
        return levels;
    }
    public boolean hasLevel(int level){
        return levels.contains(String.valueOf(level));
    }
}
