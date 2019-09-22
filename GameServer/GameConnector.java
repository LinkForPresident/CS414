package GameServer;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.NoSuchElementException;

class GameConnector extends Server{

    Socket clientSocket;
    private InputStream inputStream;
    private PrintWriter outputStream;
    BufferedReader bufferedReader;
    private Request request;
    private String HEADER = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n";

    GameConnector(){
        // generic constructor, needed to avoid a compilation error.

    }

    GameConnector(Socket clientSocket){
        // TODO: is there a way to inherit clientSocket from Server instead of passing it as an argument?
        this.clientSocket = clientSocket;

    }

    @Override
    public void run(){
        // overrides the thread run() methods.
        // set up the connection with the client.
        try {
            setUpConnection();
        } catch(NullPointerException | IOException e){
            // in case of an error in set up, tear down the connection.
            try {
                System.out.println("==ERROR==:: Encountered an error while trying to set up connection; tearing down connection.");
                e.printStackTrace();
                tearDownConnection();
                return;
            } catch (IOException ee) {
                System.out.println("==ERROR==:: Encountered an error while trying to set up connection, and encountered an error while tearing down the connection as well.");
                ee.printStackTrace();
                return;
            }
        }
        // check client authentication.
        try {
            if (clientIsAuthenticated()) {
                // client is authenticated, handle the client's request.
                try {
                    handleRequest();
                } catch (IOException e) {
                    try {
                        System.out.println("==ERROR==:: Encountered an error while attempting to handle the request; tearing down connection.");
                        e.printStackTrace();
                        tearDownConnection();
                        return;
                    } catch (IOException ee) {
                        System.out.println("==ERROR==:: Encountered an error while attempting to handle the request, and encountered an error while tearing down connection as well.");
                        ee.printStackTrace();
                        return;
                    }
                }
            } else {
                // client is not authenticated, deny access and redirect to the login page.
                try {
                    System.out.println("==DEBUG==:: Client is not authenticated, redirecting to login page.");
                    redirectTo("/login.html");
                } catch (IOException e) {
                    System.out.println("==ERROR==:: Encountered an error while attempting to redirect client to login page after failed authentication check.");
                    e.printStackTrace();
                }
            }
        } catch(SQLNonTransientConnectionException sql){
            System.out.println("==ERROR==:: Encountered an error while attempting verify authenticity due to a problem connecting to the database. " +
                    "(HINT: the proxy server is likely different than what is set.)");
            sql.printStackTrace();
        }
        // request has been fulfilled, tear down the connection.
        try {
            tearDownConnection();
        } catch (IOException e) {
            System.out.println("==ERROR==:: Encountered an error while attempting to tear down the connection after the client's request has been fulfilled.");
            e.printStackTrace();
        }
    }

    private void setUpConnection() throws IOException, NullPointerException{
        // set up the necessary data structures to handle a client socket connection.
        inputStream = this.clientSocket.getInputStream(); // gets data from client.
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // stores data from client.
        outputStream = new PrintWriter(clientSocket.getOutputStream(), true); // sends data to client.
        request = new Request(bufferedReader, clientSocket); // create request object
        int flag = request.parseRequest();
        if(flag == -1){
            throw new IOException("==DEBUG==:: This was not a real request by the user, but an automatic request by the browser.");
        }
        else if(flag == -2){
            outputStream.println(HEADER + "\r\n\r\n");
            throw new FileNotFoundException("==DEBUG==:: Client automatically requested '/css/common.css', which does not exist.");
        }
        try{
            establishDatabaseProxyAddress();
        }catch(InterruptedException | ClassNotFoundException | SQLException e){
            System.out.println("==ERROR==:: Encountered an error while attempting to curl proxy server credentials for the database.");
            e.printStackTrace();
        }
    }

    private void handleRequest() throws IOException{
        // handle the client requests, GET aor POST.
        System.out.println("==INFO==:: Handling request!");
        switch(request.method){
            case "GET":
                handleGETRequest(request.path);
                break;
            case "POST":
                handlePOSTRequest();
                break;
        }
    }

    private boolean clientIsAuthenticated() throws SQLNonTransientConnectionException{
        // check if client is logged in, or is trying to either register, login or logout.
        return request.action.equals("user_registration") || request.action.equals("login") || request.action.equals("logout") || isLoggedIn(request.cookie);
    }

    private void handleGETRequest(String path) throws IOException {
        // handle a client GET request.
        System.out.println("==INFO==:: Handling GET request!");
        String htmlResponse = HEADER + "\r\n\r\n";
        try {
            htmlResponse += getHTMLPage(path);  // get the HTML source.
            outputStream.println(htmlResponse); // send the response to the client.
        }catch(FileNotFoundException e){
            System.out.println(String.format("==DEBUG==:: 404 file %s not found", path));
            e.printStackTrace();
            outputStream.println("404 file not found.");
        }
    }

    private void handlePOSTRequest() throws IOException{
        // handle a client POST request.
        switch (request.action) {
            case "user_registration":
                System.out.println("==INFO==:: User is trying to register!");
                handleUserRegistration();
                break;
            case "login":
                handleLogin();
                break;
            case "logout":
                handleLogout();
                break;
            case "create_game":
                // createGame();
                break;
            case "enter_game":
                // enterGame();
                break;
            case "forfeit_game":
                // forfeitGame();
                break;
            case "move_piece":
                // movePiece();
                break;
            case "view_stats":
                // viewStats();
                break;
        }
    }

    private void handleLogin() throws IOException{
        // handle a client requesting to log in.
        System.out.println(String.format("==INFO==:: Attempting to log in user %s", request.username));
        try {
            login(request.user_hash);
            HEADER += String.format("Set-Cookie: user_hash=%f\r\n\r\n", request.user_hash);
            // automatically redirect to the home page.
            System.out.println(String.format("==INFO==:: User '%s' has been logged in.", request.username));
            redirectTo("/index.html");
        }catch(NoSuchElementException e) {
            // user with the username-password pair does not exist in the database.
            System.out.println(String.format("==DEBUG==:: User with username %s , password %s , and user_hash %s does " +
                    "not exist in database. Attempting to redirect to login.html", request.username, request.password,
                    request.user_hash));
            e.printStackTrace();
            redirectTo("/login.html");
        }
        catch(SQLNonTransientConnectionException sql){
            System.out.println("==ERROR==:: Encountered an error while attempting handle a login attempt due to a " +
                    "problem connecting to the database. (HINT: the proxy server is likely different than what is set.)" +
                    "Redirecting to login.html.");
            sql.printStackTrace();
            redirectTo("/login.html");
        }
    }

    private void handleLogout() throws IOException{
        // handle a client requesting to log out.
        System.out.println(String.format("==INFO==:: Attempting to log out user %s", request.username));
        try {
            logout(request.cookie);
            System.out.println(String.format("==INFO==:: User %s has been logged out", request.username));
            redirectTo("/login.html");
        }catch(SQLNonTransientConnectionException sql){
            System.out.println("==ERROR==:: Encountered an error while attempting handle a logout attempt due to a " +
                    "problem connecting to the database. (HINT: the proxy server is likely different than what is set.)" +
                    "Redirecting to login.html.");
            sql.printStackTrace();
            redirectTo("/login.html");
        }
    }

    private void handleUserRegistration() throws IOException{
        // handle a client requesting to register a new account.
        System.out.println(String.format("==INFO==:: Attempting to register user %s", request.username));
        try {
            registerUser(request.username, request.password, request.user_hash);
            System.out.println(String.format("==INFO==:: New user %s has been registered", request.username));
            redirectTo("/login.html");
        }catch(SQLNonTransientConnectionException sql){
            System.out.println("==ERROR==:: Encountered an error while attempting handle a user registration attempt due to a " +
                    "problem connecting to the database. (HINT: the proxy server is likely different than what is set.)" +
                    "Redirecting to login.html.");
            sql.printStackTrace();
            redirectTo("/login.html");
        }
    }

    private void redirectTo(String path) throws IOException{
        // helper method for making it more explicit when a redirect is happening.
        handleGETRequest(path);
    }

    private void tearDownConnection() throws IOException{
        // tear down the connection with the client.
        bufferedReader.close();
        outputStream.close();
    }

}


