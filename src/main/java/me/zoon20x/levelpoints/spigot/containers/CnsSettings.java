package me.zoon20x.levelpoints.spigot.containers;

import me.zoon20x.levelpoints.spigot.LevelPoints;

import java.io.IOException;
import java.util.UUID;

public class CnsSettings {


    private boolean enabled;
    private String address;
    private int port;
    private UUID serverID;



    public CnsSettings(boolean enabled, String address, int port){
        this.enabled = enabled;
        this.address = address;
        this.port = port;
        this.serverID = UUID.randomUUID();
        try {
            LevelPoints.getInstance().getConfigUtils().getConfig().set("NetworkShare.CrossNetworkStorage.ServerID", serverID.toString());
            LevelPoints.getInstance().getConfigUtils().getConfig().save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public CnsSettings(boolean enabled,String address, int port, String serverID){
        this.enabled = enabled;
        this.address = address;
        this.port = port;
        this.serverID = UUID.fromString(serverID);
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public UUID getServerID() {
        return serverID;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
