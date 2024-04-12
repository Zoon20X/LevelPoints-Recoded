package me.zoon20x.levelpoints.spigot.containers;

import java.util.UUID;

public class CnsSettings {


    private String address;
    private int port;
    private UUID serverID;



    public CnsSettings(String address, int port){
        this.address = address;
        this.port = port;
        this.serverID = UUID.randomUUID();
    }
    public CnsSettings(String address, int port, String serverID){
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
}
