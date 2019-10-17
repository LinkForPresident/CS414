package GameServer;

import java.io.*;
import java.net.Socket;
import java.sql.SQLNonTransientConnectionException;

class Handler extends Thread {

    private Socket clientSocket;
    private PrintWriter outputStream;
    private BufferedReader bufferedReader;
    private Request request;
    private String HEADER = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\nAccess-Control-Allow-Origin: " +
            "*\r\nAccess-Control-Allow-Methods: POST, GET\r\nAccess-Control-Allow-Headers: *\r\n";

    Handler(Socket clientSocket){
        this.clientSocket = clientSocket;

    }

    @Override
    public void run() {
        // overrides the thread run() methods.
        // set up the connection with the client.
        try {
            setUpConnection();

        }catch(FileNotFoundException f){
                Terminal.printWarning("Client automatically requested '/css/common.css', which does not exist.");
                tearDownConnection();
                return;
        }
        catch(IOException e){
                Terminal.printWarning("This was not a real request by the user, but an automatic request by the " +
                        "browser.");
                tearDownConnection();
                return;
        }
        catch(NullPointerException n){
            // in case of an error in set up, tear down the connection.
                Terminal.printError("Encountered an error while trying to set up connection; tearing down connection.");
                n.printStackTrace();
                tearDownConnection();
                return;
        }
        // check client authentication.
        try {
            if (clientIsAuthenticated()) {
                // client is authenticated, handle the client's request.
                try {
                    handleRequest();
                } catch (IOException e) {
                        e.printStackTrace();
                        Terminal.printError("Encountered an error while attempting to handle the request; tearing " +
                                "down connection.");
                        tearDownConnection();
                        return;
                }
            } else {
                // client is not authenticated, deny access and redirect to the login page.
                try {
                    Terminal.printDebug("Client is not authenticated, redirecting to login page.");
                    redirectTo("/login.html");
                } catch (IOException e) {
                        e.printStackTrace();
                        Terminal.printError("Encountered an error while attempting to redirect client to login page" +
                                " after failed authentication check.");
                        tearDownConnection();
                        return;

                }
            }
        } catch(SQLNonTransientConnectionException sql){
            Terminal.printError("Encountered an error while attempting verify authenticity due to a problem " +
                    "connecting to the database. (HINT: the proxy server is likely different than what is set.); " +
                    "tearing down connection.");
            sql.printStackTrace();
            tearDownConnection();
            return;
        }
        // request has been fulfilled, tear down the connection.
        tearDownConnection();
        Terminal.printSuccess("Request has been served and connection successfully torn down.");
    }

    private void setUpConnection() throws IOException, NullPointerException{
        // set up the necessary data structures to handle a client socket connection.
        Terminal.printDebug("Setting up connection.");
        InputStream inputStream = this.clientSocket.getInputStream(); // gets data from client.
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // stores data from client.
        outputStream = new PrintWriter(clientSocket.getOutputStream(), true); // sends data to client.
        request = new Request(bufferedReader, clientSocket); // create request object
        int flag = request.parseRequest();
        if(flag == -1){
            throw new IOException();
        }
        else if(flag == -2){
            outputStream.println(HEADER + "\r\n\r\n");
            throw new FileNotFoundException();
        }
    }

    private void handleRequest() throws IOException{
        // handle the client requests, GET aor POST.
        Terminal.printDebug("Handling request.");
        String method = request.header.get("method");
        switch(method){
            case "GET":
                String path = request.header.get("path");
                handleGETRequest(path);
                break;
            case "POST":
                handlePOSTRequest();
                break;
			case "OPTIONS":
				handleOPTIONSRequest();
				break;
        }
    }

    private boolean clientIsAuthenticated() throws SQLNonTransientConnectionException{
		// check if client is logged in, or is trying to either register, login or logout.
        Terminal.printDebug("Checking is client is authenticated for this action.");
        String action = request.body.get("action");
        String cookie = request.header.get("cookie");
        return action.equals("user_registration") || action.equals("login") || action.equals("logout") ||
                Server.isLoggedIn(cookie);
    }
    
    private void handleOPTIONSRequest() throws IOException{
        Terminal.printDebug("Handling OPTIONS request.");
		sendJSONReponse("");
    }

    private void handleGETRequest(String path) throws IOException {
        // handle a client GET request.
        Terminal.printDebug("Handling GET request.");
        String action = request.body.get("action");
        switch(action){
            case "view_game":
                handleViewGame();
                break;
            case "view_stats":
                // viewPlayerStats();
                break;
        }

    }

    private void handlePOSTRequest() throws IOException{
        // handle a client POST request.
        Terminal.printDebug("Handling POST request.");
        String action = request.body.get("action");
        switch(action){
            case "user_registration":
                handleUserRegistration();
                break;
            case "user_unregistration":
                handleUserUnregistration();
                break;
            case "login":
                handleLogin();
                break;
            case "logout":
                handleLogout();
                break;
            case "send_invite":
                handleSendInvite();
                break;
			case "accept_invite":
                handleAcceptInvite();
				break;
			case "decline_invite":
			    handleDeclineInvite();
				break;
            case "forfeit_game":
                // forfeitGame();
                break;
            case "move_piece":
                handleMovePiece();
                break;
        }
    }

    private void handleLogin() throws IOException{
        // handle a client requesting to log in.
        Terminal.printInfo(String.format("User '%s' is attempting to log in.", request.body.get("username")));
        String username = request.body.get("username");
        String password = request.body.get("password");
        String userHash = Server.calculateUserHash(username, password);
		String JSONResponse = Server.login(request.body.get("username"), userHash);
		HEADER += String.format("Set-Cookie: user_hash=%s\r\n\r\n", userHash);
		sendJSONReponse(JSONResponse);
    }

    private void handleLogout() throws IOException{
        // handle a client requesting to log out.
        Terminal.printInfo(String.format("User '%s' is attempting to log out.", request.body.get("username")));
		String JSONResponse = Server.logout(request.header.get("cookie"));
		sendJSONReponse(JSONResponse);
    }

    private void handleUserRegistration() throws IOException{
        // handle a client requesting to register a new account.
        Terminal.printInfo(String.format("User '%s' is attempting to register a new account.",
                request.body.get("username")));
        String username = request.body.get("username");
        String password = request.body.get("password");
        String JSONResponse = Server.registerUser(request.body.get("username"), request.body.get("password"));
        sendJSONReponse(JSONResponse);
    }

    private void handleUserUnregistration() throws IOException{
        // handle a client requesting to register a new account.
        Terminal.printInfo(String.format("User '%s' is attempting to unregister their account.",
                request.body.get("username")));
        String username = request.body.get("username");
        String userHash = request.header.get("cookie");
        String JSONResponse = Server.unregisterUser(username, userHash);
        sendJSONReponse(JSONResponse);
    }
    
    
    private void handleMovePiece() throws IOException{
        // handle a client requesting to move a game piece.
        Terminal.printInfo(String.format("User '%s' is attempting to move a piece.", request.body.get("username")));
		String JSONResponse = "";
		JSONResponse = Server.movePiece(request.body.get("gameID"), request.body.get("username"),
                request.body.get("row"), request.body.get("column"));
		if (JSONResponse.length() != 0) {
		    sendJSONReponse(JSONResponse);
        }
    }

    private void handleSendInvite() throws IOException{
        
        String JSONResponse = Server.sendInvite(request.body.get("playerOne"), request.body.get("playerTwo"));
        sendJSONReponse(JSONResponse);

    }

    private void handleAcceptInvite() throws IOException{


        String JSONResponse = Server.acceptInvite(request.body.get("playerOne"), request.body.get("playerTwo"));
        sendJSONReponse(JSONResponse);

    }

    private void handleDeclineInvite() throws IOException{

        String JSONResponse = Server.declineInvite(request.body.get("playerOne"), request.body.get("playerTwo"));
        sendJSONReponse(JSONResponse);

    }

    private void handleViewGame() throws IOException{

        String JSONresponse = Server.viewGame(request.body.get("gameID"));
        sendJSONReponse(JSONresponse);

    }
    
    private void sendJSONReponse(String JSONResponse) throws IOException{
		// send a JSON response to the client.
		String response = HEADER + "\r\n" + JSONResponse;
        Terminal.printInfo(String.format("Sending JSON response: \n %s", response));
		outputStream.println(response); // send the response to the client.
 
    }
    

    private void redirectTo(String path) throws IOException{
        // helper method for making it more explicit when a redirect is happening.
        handleGETRequest(path);

    }

    private void tearDownConnection(){
        // tear down the connection with the client.
        Terminal.printInfo("Attempting to tear down the GameConnector instance.");
        try {
            bufferedReader.close();
            outputStream.close();
        }catch(IOException e){
            Terminal.printError("Encountered an error while tearing down the connection.");
            return;
        }
        Terminal.printInfo("Connection has been torn down.");

    }

}


