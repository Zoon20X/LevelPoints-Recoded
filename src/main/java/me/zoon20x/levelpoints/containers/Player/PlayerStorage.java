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
        load(data);
        playerDataMap.put(uuid, data);
    }

    public void reloadPlayer(UUID uuid) throws IOException {
        PlayerData data = playerDataMap.get(uuid);
        data.save();
        data.getConfig().reload();
        load(data);

    }
    private void load(PlayerData data){
        data.setExp(data.getConfig().getDouble("Exp.Amount"));
        data.setPrestige(data.getConfig().getInt("Prestige"));
        data.setLevel(data.getConfig().getInt("Level"));
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
    public boolean hasPlayer(UUID uuid){
        return playerDataMap.containsKey(uuid);
    }
    public PlayerData getPlayerData(UUID uuid){
        return playerDataMap.get(uuid);
    }
    public void savePlayerData(UUID uuid){
        try {
            PlayerData data = playerDataMap.get(uuid);
            data.save();
            playerDataMap.remove(uuid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
