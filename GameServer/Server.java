package GameServer;

import GameLogic.exception.PlayerNameException;
import GameLogic.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.sql.*;
import java.util.*;
import com.google.gson.*;

public class Server {

    private static final int PORT_NUMBER = 8080;
    private static final double HASH_KEY = 47324824;    // used in the encryption process of user authentication.

    private ServerSocket serverListener;

    static List<String> loggedInUsers = new ArrayList<>();
    static List<Game> activeGames = new ArrayList<>();
    static List<String[]> invites = new ArrayList<>();

    static boolean debugMode = false;

    protected Server(){

    }

    protected void serve(){
    
		try{
            initializeObjectLists();
        //    String[] dummyInvite = {"dummy_user", "the_devil_himself"};
        //    invites.add(dummyInvite);
            
        }catch(SQLException ignored){

		}

        try{
            serverListener = new ServerSocket(PORT_NUMBER); // Set up server to listen at PORT_NUMBER.
            Terminal.printInfo("GameServer listening.");

        }
        catch(IOException | NullPointerException e){
            Terminal.printError("Encountered an error when attempting to set up a ServerSocket.");
        }

        try {
            while (true) {
                Thread thread = new Handler(serverListener.accept()); // start a thread to handle the connection.
                Terminal.printSuccess("Connection accepted from client.");
                thread.start();
            }
        }catch(IOException ignored){}
    }

    private static void initializeObjectLists() throws SQLException{
        String getGames = "SELECT * FROM Game;";
        ResultSet resultSet = Database.executeDatabaseQuery(getGames);
        while(resultSet.next()){
            initializeGameObject(resultSet.getString(2));
        }

        String getInvites = "SELECT * FROM Invite";
        resultSet = Database.executeDatabaseQuery(getInvites);
        while(resultSet.next()){
            initializeInviteObject(resultSet.getString(1), resultSet.getString(2));
        }
    }

    private static void initializeGameObject(String json) throws SQLException{
        // {, }, [, and ] stored as different characters b/c SQL treats them differently.
        json = json.replace("#", "}");
        json = json.replace("%", "{");
        json = json.replace("&", "]");
        json = json.replace("^", "[");
        Gson gson = new GsonBuilder().create();
        Game game = gson.fromJson(json, Game.class);
        game.move = new Move(game);
        if (game.winner.equals(""))
            activeGames.add(game);
    }

    private static void initializeInviteObject(String playerOne, String playerTwo) throws SQLException{
        String[] invite = {playerOne, playerTwo};
        invites.add(invite);
    }

    static String calculateUserHash(String username, String password) {
        // calculate the hash that acts as the primary key in the User table.
        double username_hash = username.hashCode() % HASH_KEY;
        double password_hash = password.hashCode() % HASH_KEY;
        String userHash = String.valueOf((username_hash % password_hash) % HASH_KEY);
        Terminal.printDebug(String.format("The username, password and user_hash from the POST request are: %s, %s," +
                " %s", username, password, userHash));
        return userHash;

    }

    static boolean isLoggedIn(String user_hash) {
        // check whether a client is logged in, i.e. the client IP address is an entry in the Logged_In table.
        Terminal.printInfo(String.format("Checking if user with user_hash: '%s' is logged in.", user_hash));
        boolean loggedIn = false;
        if(loggedInUsers.contains(user_hash)){
            Terminal.printDebug(String.format("User with user_hash: '%s' is logged in.", user_hash));
            loggedIn = true;
        }else{
            Terminal.printDebug(String.format("User with user_hash: '%s' is not logged in.", user_hash));
        }
        return loggedIn;

    }

    public static void main(String[] args){
        if(args.length > 0 && args[0].equals("--debug")){
            debugMode = true;
        }
        Server server = new Server();
        server.serve();

    }
}
