package me.zoon20x.devTools.spigot.player;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.zoon20x.devTools.spigot.DevInstance;
import me.zoon20x.levelpoints.LevelPoints;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerStorage {
    private final HashMap<UUID, PlayerInfo> playerInfoMap = new HashMap<>();
    private DevInstance instance;

    public PlayerStorage(DevInstance instance){
        this.instance = instance;
        //createPlayer(UUID.fromString("d9bef46e-660c-4537-b5d7-db1d535ac300"));
    }
    public void createPlayer(UUID uuid){
        YamlDocument config = createConfig(uuid, "/lps-dev/players/");
        PlayerInfo info = new PlayerInfo(uuid);
        info.setExp(config.getDouble("Exp"));
        info.setLevel(config.getInt("Level"));
        playerInfoMap.put(uuid, info);
    }
    private YamlDocument createConfig(UUID uuid, String location){
        boolean newPlayer = new File(LevelPoints.getInstance().getDataFolder() +"/.." + location, uuid + ".yml").exists();
        Bukkit.getConsoleSender().sendMessage(String.valueOf(newPlayer));
        try {
            YamlDocument config = YamlDocument.create(new File(LevelPoints.getInstance().getDataFolder() +"/.." + location, uuid + ".yml"),
                    getClass().getResourceAsStream( location + "template.yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());
            config.update();
            if(!newPlayer) {
                config.set("UUID", uuid.toString());
                config.set("Level", instance.getDefaultLevel());
                config.set("Exp", instance.getDefaultEXP());
                config.set("Multiplier", instance.getDefaultMultiplier());
            }
            config.save();
            return config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean hasPlayer(UUID uuid){
        return playerInfoMap.containsKey(uuid);
    }
    public PlayerInfo getPlayerInfo(UUID uuid){
        return playerInfoMap.get(uuid);
    }
    public void savePlayerInfo(UUID uuid){
        try {
            PlayerInfo info = playerInfoMap.get(uuid);
            YamlDocument config = createConfig(uuid, "/lps-dev/players/");
            info.save(config);
            playerInfoMap.remove(uuid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
