package team.semicolon.chatServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    int port = 3000 ;


    Server() throws IOException{
        ServerSocket listener = new ServerSocket(port);
        System.out.println("Server is running...");
        while(true) {
            System.out.println("Waiting ...");
            Socket socket = listener.accept();
            System.out.println("Client is connected");
            ServerThread thread = new ServerThread(socket);
            thread.start();
        }
    }
}

