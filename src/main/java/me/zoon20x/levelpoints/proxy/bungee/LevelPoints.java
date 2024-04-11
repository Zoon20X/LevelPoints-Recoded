package me.zoon20x.levelpoints.proxy.bungee;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.zoon20x.levelpoints.CrossNetworkStorage.NetworkSocketUtils;
import me.zoon20x.levelpoints.proxy.bungee.NetworkUtils.Network;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class LevelPoints extends Plugin {

    private static LevelPoints instance;
    private Network network;
    private NetworkSocketUtils networkSocketUtils;

    private YamlDocument config;

    @Override
    public void onEnable(){
        instance = this;
        createConfig("network.yml");
        int networkPort = config.getInt("NetworkPort");

        this.networkSocketUtils = new NetworkSocketUtils(networkPort);
        this.network = new Network();

    }
    private void createConfig(String fileName){
        try {

            config = YamlDocument.create(new File(getDataFolder(), fileName),
                    getClass().getResourceAsStream("/"+fileName),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS).build());
            config.update();
            config.save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void onDisable(){

    }

    public static LevelPoints getInstance() {
        return instance;
    }


    public Network getNetwork() {
        return network;
    }

    public NetworkSocketUtils getNetworkSocketUtils() {
        return networkSocketUtils;
    }
}
