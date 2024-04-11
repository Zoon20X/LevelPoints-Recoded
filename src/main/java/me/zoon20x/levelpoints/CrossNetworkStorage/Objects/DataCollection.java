package me.zoon20x.levelpoints.CrossNetworkStorage.Objects;

import java.io.Serializable;
import java.util.UUID;

public class DataCollection implements Serializable {

    private final UUID uuid;


    public DataCollection(UUID uuid){
        this.uuid = uuid;
    }


    public UUID getUUID() {
        return uuid;
    }
}
