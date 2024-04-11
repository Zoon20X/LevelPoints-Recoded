package me.zoon20x.levelpoints.CrossNetworkStorage;


import java.io.IOException;
import java.net.ServerSocket;

public class NetworkSocketUtils {

    private ServerSocket serverSocket;


    public NetworkSocketUtils(int port){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerSocket getServerSocket(){
        return serverSocket;
    }

    public void close(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}
