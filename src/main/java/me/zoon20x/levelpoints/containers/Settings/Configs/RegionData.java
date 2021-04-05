package me.zoon20x.levelpoints.containers.Settings.Configs;

import java.util.List;

public class RegionData {

    private String name;
    private int minLevel;
    private int maxLevel;
    private boolean teleportEnabled;
    private String teleportLocation;
    private boolean messageEnabled;
    private List<String> messageText;


    public RegionData(String name, int minLevel, int maxLevel, boolean teleportEnabled, String teleportLocation, boolean messageEnabled, List<String> messageText){
        this.name = name;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.teleportEnabled = teleportEnabled;
        this.teleportLocation = teleportLocation;
        this.messageEnabled = messageEnabled;
        this.messageText = messageText;
    }


    public String getName() {
        return name;
    }

    public int getMinLevel() {
        return minLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public boolean isTeleportEnabled() {
        return teleportEnabled;
    }

    public String getTeleportLocation() {
        return teleportLocation;
    }

    public boolean isMessageEnabled() {
        return messageEnabled;
    }

    public List<String> getMessageText() {
        return messageText;
    }
}
