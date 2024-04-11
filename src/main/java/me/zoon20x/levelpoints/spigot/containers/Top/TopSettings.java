package me.zoon20x.levelpoints.spigot.containers.Top;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
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
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    //YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    YamlDocument config = createPlayerConfig(UUID.fromString(file.getName().replace(".yml","")), "tt", "/Players/");
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
        int max = 30;
        if(topDataList.size() < 30){
            max = topDataList.size();
        }
        for (int i = 0; i < max; i++) {
            TopData topData = topDataList.get(i);
            newList.add(topData);
        }
        this.topDataList.clear();
        setTopDataList(newList);
    }
    public void setTopDataList(List<TopData> topDataList){
        this.topDataList = topDataList;
    }
    private YamlDocument createPlayerConfig(UUID uuid, String name, String location){
        boolean newPlayer = !new File(LevelPoints.getInstance().getDataFolder() + location, uuid + ".yml").exists();
        try {
            YamlDocument config = YamlDocument.create(new File(LevelPoints.getInstance().getDataFolder() + location, uuid + ".yml"),
                    getClass().getResourceAsStream( location + "template.yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());
            config.update();
            if(newPlayer) {
                config.set("Level", LevelPoints.getInstance().getLpsSettings().getLevelSettings().getStartingData().getLevel());
                config.set("Exp.Amount", LevelPoints.getInstance().getLpsSettings().getLevelSettings().getStartingData().getExp());
                config.set("Prestige", LevelPoints.getInstance().getLpsSettings().getLevelSettings().getStartingData().getPrestige());
            }
            config.set("Name", name);
            config.save();
            return config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
