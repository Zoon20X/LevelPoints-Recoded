package me.zoon20x.levelpoints.spigot.NetworkUtils;

import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.DataCollection;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkPlayer;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkResponse;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.Response;
import me.zoon20x.levelpoints.CrossNetworkStorage.SerializeData;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class Network {
    private String proxyAddress;
    private int proxyPort;


    public Network(String proxyAddress, int proxyPort){
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;
    }


    public void sendToProxy(String to) {
        try {
            Socket socket = new Socket(proxyAddress, proxyPort);
            DataOutputStream o = new DataOutputStream(socket.getOutputStream());
            //String send = SerializeData.toString("meme");
            o.writeUTF("meme");
            String i = String.valueOf(listenResponse(socket));
            LevelPoints.getInstance().log(DebugSeverity.SEVER, i);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void retrieveInfo(UUID uuid) {
        try {
            Socket socket = new Socket(proxyAddress, proxyPort);
            DataOutputStream o = new DataOutputStream(socket.getOutputStream());
            String send = SerializeData.toString(new DataCollection(uuid));
            o.writeUTF(send);
            Object object = listenResponse(socket);

            if(object instanceof Response){
                Response response = (Response) object;
                if(response.getNetworkResponse() == NetworkResponse.Failed){
                    return;
                }
                if(response.getNetworkResponse() == NetworkResponse.NoPlayer){
                    LevelPoints.getInstance().log(DebugSeverity.SEVER, String.valueOf(response.getNetworkResponse()));
                    return;
                }
                NetworkPlayer networkPlayer = response.getNetworkPlayer();
                LevelPoints.getInstance().log(DebugSeverity.NORMAL, String.valueOf(networkPlayer.getUUID()));
                return;
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Object listenResponse(Socket socket) {
        try {
            socket.setSoTimeout(5000);
            final Object[] response = new Object[1];
            try {
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                String a = inputStream.readUTF();
                response[0] = SerializeData.setData(a);
                socket.close();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return response[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
