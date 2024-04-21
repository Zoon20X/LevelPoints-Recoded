package me.zoon20x.levelpoints.proxy.velocity.NetworkUtils;

import com.velocitypowered.api.scheduler.ScheduledTask;
import com.velocitypowered.api.scheduler.TaskStatus;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.DataCollection;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkPlayer;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.NetworkResponse;
import me.zoon20x.levelpoints.CrossNetworkStorage.Objects.Response;
import me.zoon20x.levelpoints.CrossNetworkStorage.SerializeData;
import me.zoon20x.levelpoints.proxy.velocity.LevelPoints;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Network {

    private ServerSocket serverSocket;
    private ScheduledTask task;


    public Network() {
        startConnectionCheck();
        this.serverSocket = LevelPoints.getInstance().getNetworkSocketUtils().getServerSocket();

    }
    private void startConnectionCheck() {
        task = LevelPoints.getInstance().getInstance().getServer().getScheduler().buildTask(LevelPoints.getInstance(), (self) -> {
            System.out.println(self.status());
            if(self.status() == TaskStatus.CANCELLED){
                return;
            }
            if(!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    if(serverSocket.isClosed()) return;
                    DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
                    String c = inputStream.readUTF();
                    Object data = SerializeData.setData(c);


                    if (data instanceof DataCollection) {
                        DataCollection dataCollection = (DataCollection) data;
                        if (!LevelPoints.getInstance().getNetPlayerStorage().hasPlayer(dataCollection.getUUID())) {
                            sendResponse(new Response(NetworkResponse.NoPlayer), outputStream);
                            return;
                        }
                        sendResponse(new Response(NetworkResponse.Success, LevelPoints.getInstance().getNetPlayerStorage().getPlayer(dataCollection.getUUID())), outputStream);
                        return;
                    }

                    if (data instanceof NetworkPlayer) {
                        NetworkPlayer networkPlayer = (NetworkPlayer) data;
                        LevelPoints.getInstance().getNetPlayerStorage().addPlayer(networkPlayer.getUUID(), networkPlayer);
                        sendResponse(new Response(NetworkResponse.Updated), outputStream);
                    }
                    clientSocket.close();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).repeat( 100L, TimeUnit.MILLISECONDS).schedule();
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
