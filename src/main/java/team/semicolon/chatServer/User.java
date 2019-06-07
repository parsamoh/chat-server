package team.semicolon.chatServer;

// import com.mongodb.DBCollection;
import com.mongodb.client.MongoCollection;
        import org.bson.Document;

        import java.net.Socket;
import java.util.ArrayList;

public class User {
    String username ;
    String password ;
    Socket socket ;

    static MongoCollection userCollection = Mongo.database.getCollection("users") ;

    public static User toUserObj(Document document){
        if(document == null) return null ;
        String thisUsername = (String)document.get("username");
        String thisPassword = (String)document.get("password");
        return new User(thisUsername , thisPassword , null) ;
    }

    public Document toDBObject(){
        return new Document("username" , this.username)
                    .append("password" , this.password) ;
    }

    public static ArrayList<User> users = new ArrayList<User>() ;


    private void addToCollection(){

        userCollection.insertOne(this.toDBObject());
    }

    static User findInDB(String username){

        Document query = new Document("username" , username) ;
        return toUserObj((Document)userCollection.find(query).first()) ;
    }

    static User find(String username ){

        for(int i = 0 ; i < User.users.size() ; i++ ){
            System.out.println(User.users.get(i).username);
            if(User.users.get(i).username.equals(username)) return User.users.get(i) ;
        }
        return null ;
    }

    public void updateSocket(Socket socket){
        this.socket = socket ;
    }

    static User signUp(String username , String password , Socket socket){
        User thisUser = new User(username , password , socket ) ;
        thisUser.addToCollection();
        return thisUser ;
    }

    static User signIn(String username , String password , Socket socket){
        User thisUser = new User(username , password , socket ) ;
        return thisUser;
    }




    User(String username , String password , Socket socket){
        this.username = username ;
        this.password = password;
        this.socket = socket ;
        User.users.add(this) ;
    }

}

