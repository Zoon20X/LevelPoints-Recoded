package me.zoon20x.levelpoints.CrossNetworkStorage.Objects;

import java.io.Serializable;
import java.util.UUID;

public class NetworkPlayer implements Serializable {

    private final UUID uuid;
    private final int level;
    private final int prestige;
    private final double exp;

    private final UUID lastKnownServer;

    public NetworkPlayer(UUID uuid, int level, int prestige, double exp, UUID lastKnownServer) {
        this.uuid = uuid;
        this.level = level;
        this.prestige = prestige;
        this.exp = exp;
        this.lastKnownServer = lastKnownServer;
    }


    public UUID getUUID() {
        return uuid;
    }

    public int getLevel() {
        return level;
    }

    public int getPrestige() {
        return prestige;
    }

    public double getExp() {
        return exp;
    }

    public UUID getLastKnownServer() {
        return lastKnownServer;
    }
}
