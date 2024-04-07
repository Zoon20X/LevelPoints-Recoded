package me.zoon20x.levelpoints.containers.Player;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.zoon20x.levelpoints.LevelPoints;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerStorage {
    private final HashMap<UUID, PlayerData> playerDataMap = new HashMap<>();

    public void loadPlayer(UUID uuid, String name){
        YamlDocument config = createPlayerConfig(uuid,name, "/Players/");
        PlayerData data = new PlayerData(uuid, config);
        data.setExp(config.getDouble("Exp.Amount"));
        data.setPrestige(config.getInt("Prestige"));
        data.setLevel(config.getInt("Level"));
        playerDataMap.put(uuid, data);
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
                config.set("Level", 1);
                config.set("Exp.Amount", 0.0);
                config.set("Prestige", 0);
            }
            config.set("Name", name);
            config.save();
            return config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean hasPlayer(UUID uuid){
        return playerDataMap.containsKey(uuid);
    }
    public PlayerData getPlayerInfo(UUID uuid){
        return playerDataMap.get(uuid);
    }
    public void savePlayerInfo(UUID uuid){
        try {
            PlayerData data = playerDataMap.get(uuid);
            YamlDocument config = data.getConfig();
            data.save(config);
            playerDataMap.remove(uuid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}