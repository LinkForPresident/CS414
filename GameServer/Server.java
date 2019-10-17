package GameServer;

import GameLogic.exception.PlayerNameException;
import GameLogic.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.sql.*;
import java.util.*;

public class Server {

    private static final int PORT_NUMBER = 8080;
    private static final double HASH_KEY = 47324824;    // used in the encryption process of user authentication.

    private ServerSocket serverListener;

    private static List<String> loggedInUsers = new ArrayList<>();
    private static List<Game> activeGames = new ArrayList<>();
    private static List<String[]> invites = new ArrayList<>();

    static boolean debugMode = false;

    protected Server(){

    }

    protected void serve(){
    
		try{
			Game game = new Game("dummy_user", "the_devil_himself");
			game.gameID = "1234";
			activeGames.add(game);
		}catch(PlayerNameException ignored){

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

    static String login(String username, String user_hash) {
        // Login a client by checking if the client is registered and if so, adding client to list of logged in users.
        Terminal.printInfo(String.format("Attempting to log in user with username: '%s' and user_hash: '%s'.",
                username, user_hash));
        String selectUser = String.format("SELECT 1 FROM User WHERE hash_code='%s';", user_hash);
        ResultSet resultSet = Database.executeDatabaseQuery(selectUser);
        String JSONResponse = String.format("{\"loggedIn\": %b}", false);
        try {
            if (resultSet.next()) { // User exists and has been authenticated, place user_hash into loggedInUsers list.
                loggedInUsers.add(user_hash);
                String playerInvites = "";
                for (String[] inv : invites) {
                    if (inv[1].equals(username)) {
                        playerInvites += inv[0] + ",";
                    }
                }
                JSONResponse = String.format("{\"loggedIn\": %b, \"username\": \"%s\", \"invites\": \"%s\"}", true,
                        username, playerInvites);
                Terminal.printSuccess(String.format("User '%s' has been logged in.", username));
            } else {   // User does not exist. Stop the request handling.
                Terminal.printDebug(String.format("User '%s' does not exist.", username));
            }
        }catch(SQLException ignored) {
        }
        return JSONResponse;
    }

    static String logout(String user_hash){
	    // Remove user_hash from loggedInUsers list.
        String JSONResponse = "";
        Terminal.printInfo(String.format("Attempting to log out user with user_hash: '%s'.", user_hash));
		loggedInUsers.remove(user_hash);
        Terminal.printInfo(String.format("User with user_hash: '%s' has been logged out.", user_hash));
		JSONResponse = String.format("{\"loggedIn\": %b}", false);
		return JSONResponse;
    }

    static String registerUser(String username, String password){
        // handle a POST request to register a new user in the system.
        String userHash = calculateUserHash(username, password);
        Terminal.printInfo(String.format("Attempting to register new user with username: %s , password: %s , " +
                "user_hash: %s.", username, password, userHash));
        String registerUser = String.format("INSERT INTO User VALUES('%s', '%s', '%s');", username, password,
                userHash);
        ResultSet resultSet = Database.executeDatabaseQuery(registerUser);
        String checkIfRegistered = String.format("SELECT 1 FROM User WHERE hash_code='%s';", userHash);
        resultSet = Database.executeDatabaseQuery(checkIfRegistered);
        String JSONResponse = String.format("{\"loggedIn\": %b}", false);
        try {
            if (resultSet.next()) {
                Terminal.printSuccess(String.format("New user '%s' has been registered", username));
            } else {
                Terminal.printError(String.format("Encountered an error while attempting to register a new user with " +
                        "username '%s'.", username));
            }
        }catch(SQLException sql){
            Terminal.printError(String.format("Encountered an error while attempting to register a new user with " +
                    "username '%s'.", username));
        }
        return JSONResponse;
    }

    static String unregisterUser(String username, String user_hash){
        Terminal.printInfo(String.format("Attempting to unregister user: '%s'.", username));
        String unregisterUser = String.format("DELETE FROM User WHERE hash_code='%s';", user_hash);
        ResultSet resultSet = Database.executeDatabaseQuery(unregisterUser);
        String checkIfUnregistered = String.format("SELECT 1 FROM user WHERE hash_code='%s';", user_hash);
        resultSet = Database.executeDatabaseQuery(checkIfUnregistered);
        String JSONResponse = String.format("{\"success\": %b}", false);
        try{
            if(resultSet.next()){
                Terminal.printError(String.format("Encountered an error while attempting to unregister user with " +
                        "username '%s'.", username));
            }
            else{
                Terminal.printSuccess(String.format("User '%s' has been unregistered", username));
                JSONResponse = String.format("{\"success\": %b}", true);
            }
        }catch(SQLException sql){
            Terminal.printError(String.format("Encountered an error while attempting to unregister user with username" +
                    " '%s'.", username));
        }
        return JSONResponse;

    }
    
    static String sendInvite(String playerOne, String playerTwo){
        Terminal.printInfo(String.format("Attempting to service the invite of %s to %s.", playerOne, playerTwo));
        String JSONResponse = "";
		// TODO: Check if user exists in DB.
		String[] newInvite = {playerOne, playerTwo};
		boolean alreadyExists = false;
		for(String[] invite : invites){
			if(invite[0].equals(newInvite[0]) && invite[1].equals(newInvite[1])){
                Terminal.printDebug(String.format("An invite between %s and %s already exists.", playerOne, playerTwo));
                alreadyExists = true;
                break;
			}
		}
		if(!alreadyExists) {
            Terminal.printDebug(String.format("Creating invite between %s and %s.", playerOne, playerTwo));
            invites.add(newInvite);
        }
        JSONResponse = String.format("{\"invitedPlayer\": \"%s\", \"wasSuccessful\": %b}", playerTwo,
                !alreadyExists);
		return JSONResponse;
    }
    
    static String acceptInvite(String playerOne, String playerTwo) {
        Terminal.printInfo(String.format("Attempting to accept the invite of %s to %s", playerOne, playerTwo));
        String JSONResponse = "";
        Iterator<String[]> invitesIterator = invites.iterator();
        while(invitesIterator.hasNext()) {
            String[] invite = invitesIterator.next();
            if (invite[0].equals(playerOne) && invite[1].equals(playerTwo)) {
                invitesIterator.remove();
                try {
                    Game game = new Game(playerOne, playerTwo);
                } catch (PlayerNameException ignored) {
                }
                String playerInvites = "";
                for (String[] inv : invites) {
                    if (inv[1].equals(playerTwo)) {
                        playerInvites += inv[0] + ",";
                    }
                }
                JSONResponse = String.format("{\"invites\": \"%s\"}", playerInvites);
            }
        }
        return JSONResponse;
    }
    
    static String declineInvite(String playerOne, String playerTwo) {
        Terminal.printInfo(String.format("Attempting to decline the invite of %s to %s", playerOne, playerTwo));
        String JSONResponse = "";
        Iterator<String[]> invitesIterator = invites.iterator();
        while(invitesIterator.hasNext()) {
            String[] invite = invitesIterator.next();
            if (invite[0].equals(playerOne) && invite[1].equals(playerTwo)) {
                invitesIterator.remove();
                String playerInvites = "";
                for (String[] inv : invites) {
                    if (inv[1].equals(playerTwo)) {
                        playerInvites += inv[0] + ",";
                    }
                }
                JSONResponse = String.format("{\"invites\": \"%s\"}", playerInvites);
            }
        }
        return JSONResponse;
    }
    
    static String viewGame(String gameID){
		Terminal.printInfo(String.format("Attempting to service request to view game: '%s'.", gameID));
		String JSONResponse = "";
		for(Game game : activeGames){
			if(game.gameID.equals(gameID)){
				JSONResponse = formatGameResponse(game);
			}
		}
		return JSONResponse;
    }

    static String movePiece(String gameID, String playerID, String row, String column){
		// handle a POST request to move a piece in a game instance.
		Terminal.printInfo(String.format("Attempting to move piece at row: '%s', column: '%s', for player: '%s' and " +
                "game: '%s'.", row, column, playerID, gameID));
		String JSONResponse = "";
		for(Game game : activeGames){
			if(game.gameID.equals(gameID)){
				if(game.sendInput(playerID, Integer.parseInt(row), Integer.parseInt(column))){
					JSONResponse = formatGameResponse(game);
				}
			}
		}
		return JSONResponse;
    }
    
    private static String formatGameResponse(Game game){
        String JSONResponse = "";
		String boardJSON = formatBoardArrayResponse(game.board);
		JSONResponse = String.format("{\"gameID\": \"%s\", \"playerOne\": \"%s\", \"playerTwo\": \"%s\", " +
                "\"turn\": \"%s\", \"turnNumber\": %d , \"board\": %s, \"winner\": \"%s\", \"startTime\": \"%s\", " +
                "\"endTime\": \"%s\"}", game.gameID, game.playerOne, game.playerTwo, game.turn, game.turnNumber,
                boardJSON, game.winner, game.startTime, game.endTime);
        return JSONResponse;
    }
    
    private static String formatBoardArrayResponse(BoardSquare[][] state){
		String boardJSON = "[";
		for(int i=0; i<9; i++){
			String boardRow = "[";
			for(int j=0; j<7; j++){
			    BoardSquare boardSquare = state[i][j];
			    String gamePieceID = null;
			    if(boardSquare.gamePiece != null){
                    gamePieceID = "\"" + boardSquare.gamePiece.ID + "\"";
                }

				boardRow += String.format("{\"environment\": \"%s\", \"piece\": %s, \"available\": %b}",
                        boardSquare.environment, gamePieceID, boardSquare.isValid);
                if (j < 6) {
                    boardRow += ",";
                }
            }
			boardJSON += boardRow;
            if (i < 8) {
                boardJSON += "],";
            }
            else {
                boardJSON += "]";
            }

        }
		boardJSON += "]";
		return boardJSON;
    }

    static void forfeitGame(){

    }

    static void tearDownGame(){

    }

    static void viewPlayerStats(){

    }

    public static void main(String[] args){
        if(args.length > 0 && args[0].equals("--debug")){
            debugMode = true;
        }
        Server server = new Server();
        server.serve();

    }
}
