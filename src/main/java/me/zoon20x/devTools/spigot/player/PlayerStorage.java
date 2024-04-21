package me.zoon20x.devTools.spigot.player;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class PlayerStorage {
    private final HashMap<UUID, LevelSystem> playerDataMap = new HashMap<>();

    public void loadTestPlayers(int max){
        for(int i= 0; i<max; i++) {
            for (int j = 0; j < max; j++) {
                UUID uuid = UUID.randomUUID();
                createPlayerConfig(uuid, "TEST-" + i, "/Players/", UUID.randomUUID());
            }
        }
    }
    private YamlDocument createPlayerConfig(UUID uuid, String name, String location, UUID uuida){
        boolean newPlayer = !new File(LevelPoints.getInstance().getDataFolder() + location + uuid + "/", uuida + ".yml").exists();
        try {
            YamlDocument config = YamlDocument.create(new File(LevelPoints.getInstance().getDataFolder() + location, uuid + ".yml"),
                    getClass().getResourceAsStream( location + "template.yml"),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());
            config.update();
            if(newPlayer) {
                Random random = new Random();
                config.set("Level", random.nextInt(100));
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
