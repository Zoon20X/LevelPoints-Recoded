package me.zoon20x.levelpoints.containers.Top;

import me.zoon20x.levelpoints.LevelPoints;
import me.zoon20x.levelpoints.utils.messages.DebugSeverity;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class TopSettings {

    private List<TopData> topDataList = new ArrayList<>();


    public TopSettings() {
        new BukkitRunnable() {
            @Override
            public void run() {
                scan();
            }
        }.runTaskAsynchronously(LevelPoints.getInstance());
    }


    private void scan() {
        if(!topDataList.isEmpty()){
            topDataList.clear();
        }
        long startTime = System.nanoTime();
        File folder = new File(LevelPoints.getInstance().getDataFolder() + "/Players/");

        // Check if the provided path is a directory
        if (folder.isDirectory()) {
            // Call the recursive method to scan the folder
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    topDataList.add(new TopData(config.getString("Name"), UUID.fromString(file.getName().replace(".yml", "")), config.getInt("Level")));
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        LevelPoints.getInstance().log(DebugSeverity.SEVER, "Top Took - " + duration + "ms");
        sort();

    }

    public List<TopData> getTopDataList(){
        return topDataList;
    }
    private void sort(){
        Collections.sort(topDataList, Comparator.comparingInt(TopData::getLevel).reversed());
        List<TopData> newList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            TopData topData = topDataList.get(i);
            newList.add(topData);
        }
        this.topDataList.clear();
        setTopDataList(newList);
    }
    public void setTopDataList(List<TopData> topDataList){
        this.topDataList = topDataList;
    }

}
