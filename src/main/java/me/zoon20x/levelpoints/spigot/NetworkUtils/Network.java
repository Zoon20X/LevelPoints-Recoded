package me.zoon20x.levelpoints.spigot.NetworkUtils;

import me.zoon20x.levelpoints.CrossNetworkStorage.SerializeData;
import me.zoon20x.levelpoints.spigot.LevelPoints;
import me.zoon20x.levelpoints.spigot.utils.messages.DebugSeverity;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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

    public Object listenResponse(Socket socket) {
        try {
            socket.setSoTimeout(5000);
            final Object[] response = new Object[1];
            try {
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                response[0] = inputStream.readUTF();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return response[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
