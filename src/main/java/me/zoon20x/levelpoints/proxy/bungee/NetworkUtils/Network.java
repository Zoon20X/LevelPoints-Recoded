package me.zoon20x.levelpoints.proxy.bungee.NetworkUtils;

import me.zoon20x.levelpoints.proxy.bungee.LevelPoints;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Network {

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
                    //Object data = SerializeData.setData(c);
                    System.out.println(c);

                    sendResponse(c, outputStream);
                    //NetworkEvent.triggerNetworkReceiveEvent(data);

                    clientSocket.close();
                } catch (IOException e) {
                    if (serverSocket.isClosed()) {
                        return;
                    }
                    throw new RuntimeException(e);
                }
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void sendResponse(Object o, DataOutputStream outputStream) {
        String utf = "CHECK";


        try {
            outputStream.writeUTF(utf);
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public ScheduledTask getTask() {
        return task;
    }


}
