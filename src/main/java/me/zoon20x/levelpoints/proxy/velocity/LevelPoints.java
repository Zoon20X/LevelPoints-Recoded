package me.zoon20x.levelpoints.proxy.velocity;


import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;

import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(id = "levelpoints",
        name = "LevelPoints",
        version = "1.0",
        authors = {"Zoon20X"})
public class LevelPoints {

    private static LevelPoints instance;
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    @Inject
    public LevelPoints(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        instance = this;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;

    }
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Do some operation demanding access to the Velocity API here.
        // For instance, we could register an event:

    }

    @Subscribe
    public void onProxyClose(ProxyShutdownEvent event){
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
}
