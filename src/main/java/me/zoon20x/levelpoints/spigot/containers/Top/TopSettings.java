package me.zoon20x.levelpoints.spigot.containers.Top;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class TopSettings {

    private boolean enabled;
    private List<TopData> topDataList = new ArrayList<>();
    private int maxSlots;
    private int maxPages;

    private int updateIteration;

    private BukkitTask updateTask;

    public TopSettings() {
        load();
    }

    public void reload() throws IOException {
        updateTask.cancel();
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getTopSettings();
        config.reload();
        load();
    }
    private void load(){
        YamlDocument config = LevelPoints.getInstance().getConfigUtils().getTopSettings();
        maxSlots = config.getInt("MaxSlotsPerPage");
        maxPages = config.getInt("MaxPages");
        enabled = config.getBoolean("Enabled");
        if(enabled) {
            updateIteration = config.getInt("UpdateIteration");
            scan();
            startUpdate();
        }
    }


    private void scan() {
        if(!topDataList.isEmpty()){
            topDataList.clear();
        }
        long startTime = System.nanoTime();
        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerData data = LevelPoints.getInstance().getPlayerStorage().getPlayerData(player.getUniqueId());
            topDataList.add(new TopData(player.getName(), player.getUniqueId(), data.getLevel()));
        });


        File folder = new File(LevelPoints.getInstance().getDataFolder() + "/Players/");
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    //YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    String uuid = file.getName().replace(".yml", "");
                    if(LevelPoints.getInstance().getPlayerStorage().hasPlayer(UUID.fromString(uuid))){
                        continue;
                    }
                    YamlDocument config = createPlayerConfig(UUID.fromString(uuid), "/Players/");
                    topDataList.add(new TopData(config.getString("Name"), UUID.fromString(file.getName().replace(".yml", "")), config.getInt("Level")));
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = ((endTime - startTime) / 1000000);
        sort();

    }

    private void startUpdate(){
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                scan();
            }
        }.runTaskTimerAsynchronously(LevelPoints.getInstance(), 0, updateIteration * 20);
    }


    public List<TopData> getTopDataList(){
        return topDataList;
    }
    private void sort(){
        Collections.sort(topDataList, Comparator.comparingInt(TopData::getLevel).reversed());
        List<TopData> newList = new ArrayList<>();
        int max = maxPages * maxSlots;
        if(topDataList.size() < max){
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
    private YamlDocument createPlayerConfig(UUID uuid, String location){
        try {
            YamlDocument config = YamlDocument.create(new File(LevelPoints.getInstance().getDataFolder() + location, uuid + ".yml"),
                    getClass().getResourceAsStream( location + "template.yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());
            return config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public BukkitTask getUpdateTask() {
        return updateTask;
    }
}
