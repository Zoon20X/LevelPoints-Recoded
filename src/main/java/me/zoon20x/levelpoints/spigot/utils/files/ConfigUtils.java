package me.zoon20x.levelpoints.spigot.utils.files;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.zoon20x.levelpoints.spigot.API.ConfigAPI;
import me.zoon20x.levelpoints.spigot.LevelPoints;

import java.io.File;
import java.io.IOException;

public class ConfigUtils implements ConfigAPI {
    private final YamlDocument levelSettings;

    private final YamlDocument blockSettings;
    private final YamlDocument mobSettings;
    private final YamlDocument langSettings;

    public ConfigUtils(){
        levelSettings = createConfig("LevelSettings.yml", "/Settings/");
        blockSettings = createConfig("BlockSettings.yml", "/Settings/");
        mobSettings = createConfig("MobSettings.yml", "/Settings/");
        langSettings = createConfig("lang.yml", "/");
    }



    private YamlDocument createConfig(String fileName, String location){
        try {
            YamlDocument config = YamlDocument.create(new File(LevelPoints.getInstance().getDataFolder() + location, fileName),
                    getClass().getResourceAsStream( location + fileName),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());
            config.update();
            config.save();
            return config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public YamlDocument getBlockSettingsConfig(){
        return this.blockSettings;
    }


    @Override
    public YamlDocument getMobSettingsConfig() {
        return mobSettings;
    }

    @Override
    public YamlDocument getLangSettings() {
        return langSettings;
    }

    @Override
    public YamlDocument getLevelSettings() {
        return levelSettings;
    }
}
