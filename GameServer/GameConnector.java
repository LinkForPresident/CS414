package GameServer;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;

class GameConnector extends Server{

    Socket clientSocket;
    private InputStream inputStream;
    private PrintWriter outputStream;
    BufferedReader bufferedReader;
    private Request request;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(NullPointerException e){
            // in case of an error in set up, tear down the connection.
            try {
                tearDownConnection();
                return;
            } catch (IOException ee) {
                e.printStackTrace();
            }
        }
        // check client authentication.
        if(clientIsAuthenticated()){
            // client is authenticated, handle the client's request.
            try {
                handleRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            // client is not authenticated, deny access and redirect to the login page.
            try {
                redirectTo("/login.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // request has been fulfilled, tear down the connection.
        try {
            tearDownConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpConnection() throws IOException, NullPointerException{
        // set up the necessary data structures to handle a client socket connection.
        inputStream = this.clientSocket.getInputStream(); // gets data from client.
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // stores data from client.
        outputStream = new PrintWriter(clientSocket.getOutputStream(), true); // sends data to client.
        request = new Request(bufferedReader, clientSocket); // create request object
    }

    private void handleRequest() throws IOException{
        // handle the client requests, GET aor POST.
        System.out.println("Handling request!");
        switch(request.method){
            case "GET":
                handleGETRequest(request.path);
                break;
            case "POST":
                handlePOSTRequest();
                break;
        }
    }

    private boolean clientIsAuthenticated(){
        // check if client is logged in, or is trying to either register, login or logout.
        return isLoggedIn(request.clientIP) || request.action.equals("user_registration") || request.action.equals("login") || request.action.equals("logout");
    }

    private void handleGETRequest(String path) throws IOException {
        // handle a client GET request.
        System.out.println("Handling GET request!");
        String htmlResponse = HEADER;
        try {
            htmlResponse += getHTMLPage(path);  // get the HTML source.
            outputStream.println(htmlResponse); // send the response to the client.
        }catch(FileNotFoundException e){
            outputStream.println("404 file not found!");
        }
    }

    private void handlePOSTRequest() throws IOException{
        // handle a client POST request.
        switch (request.action) {
            case "user_registration":
                // registerUser();
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
        try {
            login(request.clientIP, request.user_hash);
        }catch(NoSuchElementException e){
            // user with the username-password pair does not exist in the database.
            redirectTo("/login.html");
            return;
        }
        // automatically redirect to the home page.
        redirectTo("/index.html");
    }

    private void handleLogout() throws IOException{
        // handle a client requesting to log out.
        logout(request.clientIP);
        redirectTo("/login.html");
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


