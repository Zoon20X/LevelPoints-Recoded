package me.zoon20x.levelpoints.CrossNetworkStorage.Objects;

import javax.annotation.Nullable;
import java.io.Serializable;

public class Response implements Serializable {

    private NetworkPlayer networkPlayer;
    private NetworkResponse networkResponse;

    public Response(NetworkResponse networkResponse){
        this.networkResponse = networkResponse;
        this.networkPlayer = null;
    }
    public Response(NetworkResponse networkResponse, NetworkPlayer networkPlayer){
        this.networkResponse = networkResponse;
        this.networkPlayer = networkPlayer;
    }

    public NetworkResponse getNetworkResponse() {
        return networkResponse;
    }

    @Nullable
    public NetworkPlayer getNetworkPlayer() {
        return networkPlayer;
    }
}
