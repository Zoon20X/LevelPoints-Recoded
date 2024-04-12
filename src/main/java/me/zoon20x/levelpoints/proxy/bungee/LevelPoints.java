package me.zoon20x.levelpoints.proxy.bungee;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.zoon20x.levelpoints.CrossNetworkStorage.NetworkSocketUtils;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetPlayerStorage;
import me.zoon20x.levelpoints.CrossNetworkStorage.SerializeData;
import me.zoon20x.levelpoints.proxy.bungee.NetworkUtils.Network;
import me.zoon20x.levelpoints.proxy.bungee.events.BungeeEvents;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class LevelPoints extends Plugin {

    private static LevelPoints instance;
    private Network network;
    private NetworkSocketUtils networkSocketUtils;
    private NetPlayerStorage netPlayerStorage;

    private YamlDocument config;
    private YamlDocument cachedPlayers;


    @Override
    public void onEnable(){
        instance = this;
        config = createConfig("network.yml");
        cachedPlayers = createConfig("CachedPlayers.yml");
        int networkPort = config.getInt("NetworkPort");

        this.networkSocketUtils = new NetworkSocketUtils(networkPort);
        this.network = new Network();
        this.netPlayerStorage = new NetPlayerStorage();
        getProxy().getPluginManager().registerListener(this, new BungeeEvents());


    }
    private YamlDocument createConfig(String fileName){
        try {

            YamlDocument config = YamlDocument.create(new File(getDataFolder(), fileName),
                    getClass().getResourceAsStream("/"+fileName),
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
    public void onDisable() {
        getNetPlayerStorage().getNetworkPlayerHashMap().keySet().forEach(uuid -> {
            try {
                getCachedPlayers().set(uuid.toString(), SerializeData.toString(getNetPlayerStorage().getPlayer(uuid)));
                getCachedPlayers().save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        network.getTask().cancel();
        networkSocketUtils.close();
    }

    public static LevelPoints getInstance() {
        return instance;
    }

    public YamlDocument getConfig(){
        return config;
    }

    public Network getNetwork() {
        return network;
    }

    public NetworkSocketUtils getNetworkSocketUtils() {
        return networkSocketUtils;
    }

    public NetPlayerStorage getNetPlayerStorage() {
        return netPlayerStorage;
    }

    public YamlDocument getCachedPlayers() {
        return cachedPlayers;
    }
}
