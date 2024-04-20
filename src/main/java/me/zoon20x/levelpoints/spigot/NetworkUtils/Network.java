package me.zoon20x.levelpoints.spigot.NetworkUtils;

import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.DataCollection;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkPlayer;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkResponse;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.Response;
import me.zoon20x.levelpoints.CrossNetworkStorage.SerializeData;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.containers.Player.PlayerData;
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


    public void sendToProxy(PlayerData data) {
        try {
            Socket socket = new Socket(proxyAddress, proxyPort);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            NetworkPlayer networkPlayer = new NetworkPlayer(data.getUUID(), data.getLevel(), data.getPrestige(), data.getExp(), LevelPoints.getInstance().getCnsSettings().getServerID());
            String send = SerializeData.toString(networkPlayer);

            outputStream.writeUTF(send);
            Response response = (Response) listenResponse(socket);
            outputStream.close();
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public Response retrieveInfo(UUID uuid) {
        try {
            Socket socket = new Socket(proxyAddress, proxyPort);

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            String send = SerializeData.toString(new DataCollection(uuid));

            outputStream.writeUTF(send);
            Object object = listenResponse(socket);

            outputStream.close();
            socket.close();
            if(object instanceof Response){
                Response response = (Response) object;
                return response;
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    public Object listenResponse(Socket socket) {
        try {
            socket.setSoTimeout(5000);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            String a = inputStream.readUTF();
            inputStream.close();
            return SerializeData.setData(a);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
