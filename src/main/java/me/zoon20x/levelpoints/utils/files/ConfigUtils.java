package me.zoon20x.levelpoints.utils.files;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.zoon20x.levelpoints.API.ConfigAPI;
import me.zoon20x.levelpoints.LevelPoints;

import java.io.File;
import java.io.IOException;

public class ConfigUtils implements ConfigAPI {

    private YamlDocument blockSettings;
    private YamlDocument mobSettings;

    public ConfigUtils(){
        blockSettings = createConfig("BlockSettings.yml", "/Settings/");
        mobSettings = createConfig("MobSettings.yml", "/Settings/");
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
    public YamlDocument getMobSettings() {
        return mobSettings;
    }
}
