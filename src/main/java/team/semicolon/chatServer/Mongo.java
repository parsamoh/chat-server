package team.semicolon.chatServer;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;

import com.mongodb.client.MongoDatabase;





import java.util.Arrays;
public class Mongo {


    static  MongoClient client = MongoClients.create(System.getenv("MONGO_URI"));
    static  MongoDatabase database = client.getDatabase("chatApp") ;



}
