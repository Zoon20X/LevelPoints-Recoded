package me.zoon20x.levelpoints.CrossNetworkStorage.Objects;

import java.util.HashMap;
import java.util.UUID;

public class NetPlayerStorage {

    private final HashMap<UUID, NetworkPlayer> networkPlayerHashMap = new HashMap<>();

    public boolean hasPlayer(UUID uuid){
        return networkPlayerHashMap.containsKey(uuid);
    }
    public NetworkPlayer getPlayer(UUID uuid){
        return networkPlayerHashMap.get(uuid);
    }
    public void addPlayer(UUID uuid, NetworkPlayer networkPlayer){
        networkPlayerHashMap.put(uuid, networkPlayer);
    }
    public void removePlayer(UUID uuid){
        networkPlayerHashMap.remove(uuid);
    }


}
