package team.semicolon.chatServer;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {

    User user ;
    private Socket socket;
    BufferedReader reader;






    public ServerThread(Socket socket) throws IOException {

        this.socket = socket ;

        while (true){



            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String[] lineArr = new String[0] ;
            String line = reader.readLine() ;

            if(line != null ){
                lineArr = line.split(" ") ;
            }



            if(lineArr.length == 3){

                String command = lineArr[0] ;
                String username = lineArr[1] ;
                String password = lineArr[2] ;
//                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                if(command.equals("signin")){

                    User thisUser = User.findInDB(username) ;
                    if(thisUser != null && thisUser.password.equals(password)){
                        thisUser.updateSocket(socket);
                        user = thisUser ;
                        writeOnSocket(socket , "signed in successfully");
//                        writer.println("signed in successfully");
                        break;
                    }else{
                        writeOnSocket(socket , "someThing goes wrong :( check your username & password." );
                    }
                }else if(command.equals("signup")){

                    if(User.findInDB(username) == null){
                        user = User.signUp(username , password , socket ) ;
                        User.users.add(user) ;
                        writeOnSocket(socket , "signed up successfully");
//                        writer.println("signed up successfully");
                        break;
                    }else{
                        writeOnSocket(socket , "username already taken . \n try with other one");
                    }

                }else {
                    writeOnSocket(socket , "Command not Found");
                }
            }else{

                writeOnSocket(socket , "you should have 3 parameters !!!");

            }


        }

//        this.socket = socket;
//        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        String user = reader.readLine();
//        Server.userList.add(user);
//        Server.socketList.add(socket);
    }

    private String getBody(String[] arr){
        String s = new String("") ;
        s = s + arr[2] ;
        for(int i = 3 ; i < arr.length ; i++ ){
            s = s + " " + arr[i] ;
        }
        return  s ;
    }

    private void writeOnSocket(Socket s , String msg) throws IOException{

        PrintWriter writer = new PrintWriter(s.getOutputStream() ) ;
        writer.println(msg);
        writer.flush();

    }

    @Override
    public void run() {
        super.run();

        try{
            while(socket.isConnected()){


                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = reader.readLine() ;
                String[] stringArr = new String[0] ;
                if(line != null){
                    stringArr = line.split(" ") ;
                }

                if(stringArr.length > 2){

                    String sendTo = stringArr[0] ;
                    String command = stringArr[1] ;
                    String body = getBody(stringArr) ;

                    User sendToUser = User.find(sendTo) ;
                    Boolean isUserInDB = (User.findInDB(sendTo) != null ) ;

                    if(isUserInDB){
                        if(sendToUser != null){

                            writeOnSocket(sendToUser.socket , String.format("%s %s %s" , user.username , command , body));
                            writeOnSocket(user.socket , String.format("massage sent to %s" , sendTo ));
                        }else{
                            writeOnSocket(user.socket , String.format("unfortunately %s is offline now.\n" ,sendTo));
                        }
                    }else {
                        writeOnSocket(user.socket , String.format("there is no username : %s .\n" ,sendTo));
                    }

                }else{

                }

            }
        }catch (IOException err){
            err.printStackTrace();
            System.out.printf("%s diconnected" , user.username);
            User.users.remove(user) ;
        }

        System.out.printf("%s diconnected" , user.username);
        User.users.remove(user) ;

    }
}
