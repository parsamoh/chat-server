package team.semicolon.chatServer;

import java.io.IOException;

public class App 
{
    public static void main( String[] args )
    {
        try {
            Server server = new Server() ;
        }catch (IOException err){
            err.printStackTrace();
        }
    }
}
