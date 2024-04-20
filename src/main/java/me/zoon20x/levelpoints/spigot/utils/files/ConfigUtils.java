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
    private final YamlDocument config;

    private final YamlDocument levelSettings;

    private final YamlDocument blockSettings;
    private final YamlDocument mobSettings;
    private final YamlDocument farmSettings;
    private final YamlDocument langSettings;
    private final YamlDocument topSettings;

    private final YamlDocument worldSettings;


    private final YamlDocument mythicMobsSettings;

    public ConfigUtils(){
        config = createConfig("config.yml", "/", true);
        levelSettings = createConfig("LevelSettings.yml", "/Settings/", true);
        blockSettings = createConfig("BlockSettings.yml", "/Settings/", true);
        mobSettings = createConfig("MobSettings.yml", "/Settings/", true);
        farmSettings = createConfig("FarmSettings.yml", "/Settings/", true);
        topSettings = createConfig("TopSettings.yml", "/Settings/", true);
        langSettings = createConfig("lang.yml", "/", true);
        worldSettings = createConfig("WorldSettings.yml", "/Settings/", true);

        mythicMobsSettings = createConfig("MythicMobs.yml", "/ExtraSupport/", true);
    }



    private YamlDocument createConfig(String fileName, String location, boolean useFileVersion){
        if(useFileVersion) {
            try {
                YamlDocument config = YamlDocument.create(new File(LevelPoints.getInstance().getDataFolder() + location, fileName),
                        getClass().getResourceAsStream(location + fileName),
                        GeneralSettings.builder().setUseDefaults(false).build(),
                        LoaderSettings.builder().setAutoUpdate(true).build(),
                        DumperSettings.DEFAULT,
                        UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());

                config.update();

                config.save();
                return config;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                YamlDocument config = YamlDocument.create(new File(LevelPoints.getInstance().getDataFolder() + location, fileName),
                        getClass().getResourceAsStream(location + fileName),
                        GeneralSettings.builder().setUseDefaults(false).build());
                config.update();
                config.save();
                return config;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

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

    @Override
    public YamlDocument getConfig() {
        return config;
    }

    @Override
    public YamlDocument getTopSettings() {
        return topSettings;
    }

    public YamlDocument getMythicMobsSettings() {
        return mythicMobsSettings;
    }

    public YamlDocument getWorldSettings() {
        return worldSettings;
    }

    public YamlDocument getFarmSettings() {
        return farmSettings;
    }
}
