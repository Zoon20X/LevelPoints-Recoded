package me.zoon20x.levelpoints.containers.Settings.Placeholders;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.utils.DataLocation;
import me.zoon20x.levelpoints.utils.DebugSeverity;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelColorSettings {

    private HashMap<String, LevelColorData> colorData = new HashMap<>();


    public LevelColorSettings(){
        long startTime = System.nanoTime();
        generateColorData();
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        LevelPoints.getDebug(DebugSeverity.NORMAL, "LevelColor took "+ duration + "ms, to load " + colorData.size() + " color groups");

    }

    public LevelColorData getColorData(String color){
        return colorData.get(color);
    }

    public void addColorData(LevelColorData data){
        LevelPoints.getDebug(DebugSeverity.WARNING, data.getId());
        LevelPoints.getDebug(DebugSeverity.WARNING, data.getColor() + "color");
        LevelPoints.getDebug(DebugSeverity.WARNING, data.getLevels());
        colorData.put(data.getId(), data);
    }

    public void generateColorData(){
        FileConfiguration file = LevelPoints.getInstance().getConfig();
        file.getConfigurationSection(DataLocation.LevelColorsList).getKeys(false).forEach(x->{
            String color = file.getString(DataLocation.getLevelColorsData(x + ".ID"));
            List<String> levels = file.getStringList(DataLocation.getLevelColorsData(x + ".Levels"));
            addColorData(new LevelColorData(x, color, levels));

        });


    }

    public String getLevelColor(int level){
        for(String x : colorData.keySet()){
            if(colorData.get(x).hasLevel(level)){
                return x;
            }
        }
        return "none";
    }


}
