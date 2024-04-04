package me.zoon20x.levelpoints.devTools;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.zoon20x.levelpoints.LevelPoints;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;

public class DevConfig {

    public YamlDocument dev1;

    public DevConfig(){
        dev1 = createConfig("dev.yml", "/lps-dev/");
    }



    private YamlDocument createConfig(String fileName, String location){
        try {
            YamlDocument config = YamlDocument.create(new File(LevelPoints.getInstance().getDataFolder() +"/.." + location, fileName),
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
}
