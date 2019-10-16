package GameServer;

import GameLogic.exception.PlayerNameException;
import GameLogic.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.sql.*;
import java.util.*;

public class Server extends Thread{

    private static final int PORT_NUMBER = 8080;
    private static final String RELATIVE_PATH = "client/html";
    static final String DEFAULT_METHOD = "GET";
    static final String DEFAULT_PAGE = "/index.html";
    static final double HASH_KEY = 47324824;    // used in the encryption process of user authentication.

    private ServerSocket serverListener;

    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static String PROXY_ADDRESS = "jdbc:mariadb://proxy19.rt3.io:39136/cs414";
    private static final String DB_USERNAME = "user";
    private static final String DB_PASSWORD = "the_password_123";

    private static List<Double> loggedInUsers = new ArrayList<>();
    private static List<Game> activeGames = new ArrayList<>();
    private static List<String[]> invites = new ArrayList<>();

    private static String devAPIKey = "MTA2M0FGNDUtM0M1QS00ODMyLUFDNDgtOEVBQ0E1Q0JBRUU1";
    private static String deviceAddress = "80:00:00:00:01:01:38:E9";

    static final String RESET_TEXT_COLOR = "\u001B[0m";
    static final String RED_TEXT = "\u001B[38;5;196m";
    static final String BLUE_TEXT = "\u001B[38;5;14m";
    static final String GRAY_TEXT = "\u001B[38;5;7m";
    static final String GREEN_TEXT = "\u001B[38;5;82m";
    static final String YELLOW_TEXT = "\u001B[38;5;11m";

    static final String INFO_TAG = BLUE_TEXT + "==INFO==:: " + RESET_TEXT_COLOR;
    static final String DEBUG_TAG = GRAY_TEXT + "==DEBUG==:: " + RESET_TEXT_COLOR;
    static final String ERROR_TAG = RED_TEXT + "==ERROR==:: " + RESET_TEXT_COLOR;
    static final String WARNING_TAG = YELLOW_TEXT + "==WARNING==:: " + RESET_TEXT_COLOR;
    static final String SUCCESS_TAG = GREEN_TEXT + "==SUCCESS==:: " + RESET_TEXT_COLOR;

    protected Server(){

    }

    protected void serve(){
    
		try{
			Game game = new Game("dummy_user", "the_devil_himself");
			game.gameID = "1234";
			activeGames.add(game);
		}catch(PlayerNameException e){

		}

        try{
            serverListener = new ServerSocket(PORT_NUMBER); // Set up server to listen at PORT_NUMBER.
            System.out.println(INFO_TAG + "GameServer listening.");

        }
        catch(IOException | NullPointerException e){
            System.out.println(ERROR_TAG + "Encountered an error when attempting to set up a ServerSocket.");
        }

        try {
            while (true) {
                Thread thread = new GameConnector(serverListener.accept()); // start a thread to handle the connection.
                System.out.println(SUCCESS_TAG + "Connection accepted from client.");
                thread.start();
            }
        }catch(IOException e){}
    }


    String getHTMLPage(String path) throws IOException{
        // Fetch an HTML page for the client.
        System.out.println(String.format(INFO_TAG + "Fetching HTML Page: %s", path));
        File file = new File(RELATIVE_PATH + path);
        FileReader fileReader = new FileReader(file);
        BufferedReader fileBuffer = new BufferedReader(fileReader); // read the file into a buffer.
        String line;
        String response = "";

        while((line = fileBuffer.readLine()) != null){ // read until end of file.
            response += line; // append to response.
        }

        fileBuffer.close();
        return response;
    }

    boolean isLoggedIn(double user_hash) throws SQLNonTransientConnectionException{
    // check whether a client is logged in, i.e. the client IP address is an entry in the Logged_In table.
        System.out.println(String.format(INFO_TAG + "Checking if user with user_hash: %f is logged in.", user_hash));
        boolean loggedIn = false;
        if(loggedInUsers.contains(user_hash)){
            loggedIn = true;
        }
        return loggedIn;
    }

    String login(String username, double user_hash){
        // Login a client by checking if the client is registered and if so, adding client to list of logged in users.
        System.out.println(String.format(INFO_TAG + "Attempting to log in user with username: '%s' and user_hash: '%f'.", username, user_hash));
        String selectUser = String.format("SELECT 1 FROM User WHERE hash_code='%f';", user_hash);
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
                JSONResponse = String.format("{\"loggedIn\": %b, \"username\": \"%s\", \"invites\": \"%s\"}", true, username, playerInvites);
                System.out.println(String.format(SUCCESS_TAG + "User '%s' has been logged in.", username));
            } else {   // User does not exist. Stop the request handling.
                System.out.println(String.format(DEBUG_TAG + "User '%s' does not exist.", username));
            }
        }catch(SQLException sql) {
        }finally {
            return JSONResponse;
        }
    }

    String logout(double user_hash){
	// Remove user_hash from loggedInUsers list.
		loggedInUsers.remove(user_hash);
		String JSONResponse = String.format("{\"loggedIn\": %b}", false);
		return JSONResponse;
    }

    String registerUser(String username, String password, double user_hash){
        // handle a POST request to register a new user in the system.
        System.out.println(String.format(INFO_TAG + "Attempting to register new user with username: %s , password: %s , user_hash: %f.", username, password, user_hash));
        String registerUser = String.format("INSERT INTO User VALUES('%s', '%s', '%f');", username, password, user_hash);
        ResultSet resultSet = Database.executeDatabaseQuery(registerUser);
        String checkIfRegistered = String.format("SELECT 1 FROM User WHERE hash_code='%f';", user_hash);
        resultSet = Database.executeDatabaseQuery(checkIfRegistered);
        String JSONResponse = String.format("{\"loggedIn\": %b}", false);
        try {
            if (resultSet.next()) {
                System.out.println(String.format(SUCCESS_TAG + "New user '%s' has been registered", username));
            } else {
                System.out.println(String.format(ERROR_TAG + "Encountered an error while attempting to register a new user with username '%s'.", username));
            }
        }catch(SQLException sql){
            System.out.println(String.format(ERROR_TAG + "Encountered an error while attempting to register a new user with username '%s'.", username));
        }
        finally {
            return JSONResponse;
        }
    }

    String unregisterUser(String username, double user_hash){
        System.out.println(String.format(INFO_TAG + "Attempting to unregister user: '%s'.", username));
        String unregisterUser = String.format("DELETE FROM User WHERE hash_code='%f';", user_hash);
        ResultSet resultSet = Database.executeDatabaseQuery(unregisterUser);
        String checkIfUnregistered = String.format("SELECT 1 FROM user WHERE hash_code='%f';", user_hash);
        resultSet = Database.executeDatabaseQuery(checkIfUnregistered);
        String JSONResponse = String.format("{\"success\": %b}", false);
        try{
            if(resultSet.next()){
                System.out.println(String.format(ERROR_TAG + "Encountered an error while attempting to unregister user with username '%s'.", username));
            }
            else{
                System.out.println(String.format(SUCCESS_TAG + "User '%s' has been unregistered", username));
                JSONResponse = String.format("{\"success\": %b}", true);
            }
        }catch(SQLException sql){
            System.out.println(String.format(ERROR_TAG + "Encountered an error while attempting to unregister user with username '%s'.", username));
        }
        finally{
            return JSONResponse;
        }

    }
    
    String sendInvite(String playerOne, String playerTwo){
		System.out.println(String.format(INFO_TAG + "Attempting to service the invite of %s to %s", playerOne, playerTwo));
		// TODO: Check if user exists in DB.
		String[] newInvite = {playerOne, playerTwo};
		String JSONResponse = String.format("{\"invitedPlayer\": \"%s\", \"wasSuccessful\": %b}", playerTwo, false);
		for(String[] invite : invites){
			if(invite[0].equals(newInvite[0]) && invite[1].equals(newInvite[1])){
                break; // invite already exists
			}
		}
		invites.add(newInvite);
        JSONResponse = String.format("{\"invitedPlayer\": \"%s\", \"wasSuccessful\": %b}", playerTwo, true);
		return JSONResponse;
    }
    
    String acceptInvite(String playerOne, String playerTwo) {
        System.out.println(String.format(INFO_TAG + "Attempting to accept the invite of %s to %s", playerOne, playerTwo));
        String JSONResponse = "";
        Iterator<String[]> invitesIterator = invites.iterator();
        while(invitesIterator.hasNext()) {
            String[] invite = invitesIterator.next();
            if (invite[0].equals(playerOne) && invite[1].equals(playerTwo)) {
                invitesIterator.remove();
                try {
                    Game game = new Game(playerOne, playerTwo);
                } catch (PlayerNameException e) {
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
    
    String declineInvite(String playerOne, String playerTwo) {
        System.out.println(String.format(INFO_TAG + "Attempting to decline the invite of %s to %s", playerOne, playerTwo));
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
    
    String viewGame(String gameID){
		System.out.println(String.format(INFO_TAG + "Attempting to service request to view game: '%s'.", gameID));
		String JSONResponse = "";
		for(Game game : activeGames){
			if(game.gameID.equals(gameID)){
				JSONResponse = formatGameResponse(game);
			}
		}
		return JSONResponse;
    }

    String movePiece(String gameID, String playerID, String row, String column){
		// handle a POST request to move a piece in a game instance.
		System.out.println(String.format(INFO_TAG + "Attempting to move piece at row: '%s', column: '%s', for player: '%s' and game: '%s'.", row, column, playerID, gameID));
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
    
    private String formatGameResponse(Game game){
		String boardJSON = formatBoardArrayResponse(game.board);
		String JSONResponse = String.format("{\"gameID\": \"%s\", \"playerOne\": \"%s\", \"playerTwo\": \"%s\", \"turn\": \"%s\", \"turnNumber\": %d , \"board\": %s, \"winner\": \"%s\", \"startTime\": \"%s\", \"endTime\": \"%s\"}", game.gameID, game.playerOne, game.playerTwo, game.turn, game.turnNumber, boardJSON, game.winner, game.startTime, game.endTime);
        return JSONResponse;
    }
    
    private String formatBoardArrayResponse(BoardSquare[][] state){
		String boardJSON = "[";
		for(int i=0; i<9; i++){
			String boardRow = "[";
			for(int j=0; j<7; j++){
			    BoardSquare boardSquare = state[i][j];
			    String gamePieceID = null;
			    if(boardSquare.gamePiece != null){
                    gamePieceID = "\"" + boardSquare.gamePiece.ID + "\"";
                }

				boardRow += String.format("{\"environment\": \"%s\", \"piece\": %s, \"available\": %b}", boardSquare.environment, gamePieceID, boardSquare.isValid);
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

    void forfeitGame(){

    }

    void tearDownGame(){

    }

    void viewPlayerStats(){

    }

    public static void main(String[] args){

        Server server = new Server();
        server.serve();
    }
}
