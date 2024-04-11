package me.zoon20x.levelpoints.proxy.bungee.NetworkUtils;

import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.DataCollection;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkPlayer;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkResponse;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.Response;
import me.zoon20x.levelpoints.CrossNetworkStorage.SerializeData;
import me.zoon20x.levelpoints.proxy.bungee.LevelPoints;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Network{

    private ServerSocket serverSocket;
    private ScheduledTask task;


    public Network() {
        this.serverSocket = LevelPoints.getInstance().getNetworkSocketUtils().getServerSocket();
        startConnectionCheck();

    }
    private void startConnectionCheck() {
        task = ProxyServer.getInstance().getScheduler().schedule(LevelPoints.getInstance(), new Runnable() {
            @Override
            public void run() {
                try {
                    if (serverSocket.isClosed()) {
                        return;
                    }
                    Socket clientSocket = serverSocket.accept();

                    DataInputStream a = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
                    String c = a.readUTF();
                    Object data = SerializeData.setData(c);
                    if(data instanceof DataCollection){
                        DataCollection dataCollection = (DataCollection) data;
                        if(!LevelPoints.getInstance().getNetPlayerStorage().hasPlayer(dataCollection.getUUID())){
                            sendResponse(new Response(NetworkResponse.NoPlayer), outputStream);
                            return;
                        }
                        sendResponse(new Response(NetworkResponse.Success, LevelPoints.getInstance().getNetPlayerStorage().getPlayer(dataCollection.getUUID())), outputStream);
                        return;
                    }
                    if(data instanceof NetworkPlayer){
                        NetworkPlayer networkPlayer = (NetworkPlayer) data;
                        LevelPoints.getInstance().getNetPlayerStorage().addPlayer(networkPlayer.getUUID(), networkPlayer);
                        sendResponse(new Response(NetworkResponse.Updated), outputStream);
                    }
                    System.out.println(c);


                    //NetworkEvent.triggerNetworkReceiveEvent(data);

                    clientSocket.close();
                } catch (IOException | ClassNotFoundException e) {
                    if (serverSocket.isClosed()) {
                        return;
                    }
                    throw new RuntimeException(e);
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void sendResponse(Serializable o, DataOutputStream outputStream) {
        try {
            outputStream.writeUTF(SerializeData.toString(o));
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public ScheduledTask getTask() {
        return task;
    }


}
