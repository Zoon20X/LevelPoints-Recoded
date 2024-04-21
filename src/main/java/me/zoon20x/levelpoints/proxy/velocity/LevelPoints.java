package me.zoon20x.levelpoints.proxy.velocity;


import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.zoon20x.levelpoints.CrossNetworkStorage.NetworkSocketUtils;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetPlayerStorage;
import me.zoon20x.levelpoints.CrossNetworkStorage.SerializeData;
import me.zoon20x.levelpoints.proxy.velocity.NetworkUtils.Network;
import me.zoon20x.levelpoints.proxy.velocity.events.VelocityEvents;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

@Plugin(
        id = "levelpoints",
        name = "LevelPoints",
        version = "1.0",
        authors = {"Zoon20X"}
)
public class LevelPoints {

    private static LevelPoints instance;
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    private Network network;
    private NetworkSocketUtils networkSocketUtils;
    private NetPlayerStorage netPlayerStorage;

    private YamlDocument config;
    private YamlDocument cachedPlayers;


    @Inject
    public LevelPoints(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event){
        instance = this;
        this.config = createConfig("network.yml");
        this.cachedPlayers = createConfig("CachedPlayers.yml");
        server.getEventManager().register(this, new VelocityEvents());
        int networkPort = config.getInt("NetworkPort");

        this.networkSocketUtils = new NetworkSocketUtils(networkPort);
        this.network = new Network();
        this.netPlayerStorage = new NetPlayerStorage();


        server.getScheduler().buildTask(this, ()->{
            Collection<ScheduledTask> tasks = server.getScheduler().tasksByPlugin(this);
            for (ScheduledTask task : tasks) {
                System.out.println(task.status());
                task.cancel();
                System.out.println(task.status());
            }
        }).delay(10, TimeUnit.SECONDS);
    }

    @Subscribe
    public void onProxyShutDown(ProxyShutdownEvent event){
        Collection<ScheduledTask> tasks = server.getScheduler().tasksByPlugin(this);
        for (ScheduledTask task : tasks) {
            System.out.println(task.status());
            task.cancel();
            System.out.println(task.status());
        }
//        getNetPlayerStorage().getNetworkPlayerHashMap().keySet().forEach(uuid -> {
//            try {
//                getCachedPlayers().set(uuid.toString(), SerializeData.toString(getNetPlayerStorage().getPlayer(uuid)));
//                getCachedPlayers().save();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//        });

    }

    private YamlDocument createConfig(String fileName){
        try {

            YamlDocument config = YamlDocument.create(new File(dataDirectory.toFile(), fileName),
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

    public static LevelPoints getInstance() {
        return instance;
    }

    public ProxyServer getServer() {
        return server;
    }

    public Logger getLogger() {
        return logger;
    }

    public Path getDataDirectory() {
        return dataDirectory;
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
