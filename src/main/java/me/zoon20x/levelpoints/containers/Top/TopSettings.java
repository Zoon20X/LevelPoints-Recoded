package me.zoon20x.levelpoints.containers.Top;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.utils.messages.DebugSeverity;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TopSettings {

    private List<TopData> topDataList = new ArrayList<>();


    public TopSettings(){
        scan();
    }

    private void scan() {
        long startTime = System.nanoTime();
        File folder = new File(LevelPoints.getInstance().getDataFolder() + "/Players/");

        // Check if the provided path is a directory
        if (folder.isDirectory()) {
            // Call the recursive method to scan the folder
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    topDataList.add(new TopData(UUID.fromString(file.getName().replace(".yml", "")), config.getInt("Level")));
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        LevelPoints.getInstance().log(DebugSeverity.SEVER, "Top Took - " + duration + "ms");
    }

    public List<TopData> getTopDataList(){
        return topDataList;
    }


}
