package GameServer;

import java.io.*;
import java.net.Socket;
import java.rmi.NoSuchObjectException;

class GameConnector extends Server{

    Socket clientSocket;
    private InputStream inputStream;
    private PrintWriter outputStream;
    BufferedReader bufferedReader;
    private Request request;

    GameConnector(){

    }

    GameConnector(Socket clientSocket){
        this.clientSocket = clientSocket;

    }

    @Override
    public void run(){
        try {
            setUpConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(NullPointerException n){
            try {
                tearDownConnection();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(clientIsAuthenticated()){
            try {
                handleRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                redirectTo("/login.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            tearDownConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpConnection() throws IOException, NullPointerException{
        inputStream = this.clientSocket.getInputStream(); // gets data from client.
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // stores data from client.
        outputStream = new PrintWriter(clientSocket.getOutputStream(), true); // sends data to client.
        request = new Request(bufferedReader, clientSocket);

    }


    private void handleRequest() throws IOException{
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

        return isLoggedIn(request.clientIP) || request.action.equals("user_registration") || request.action.equals("login") || request.action.equals("logout");
    }

    private void handleGETRequest(String path) throws IOException {
        System.out.println("Handling GET request!");
        String htmlResponse = HEADER;
        try {
            htmlResponse += getHTMLPage(path);
            outputStream.println(htmlResponse);
        }catch(FileNotFoundException e){
            outputStream.println("404 file not found!");
        }
    }

    private void handlePOSTRequest() throws IOException{

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

        try {
            login(request.clientIP, request.user_hash);
        }catch(NoSuchObjectException e){
            redirectTo("/login.html");
        }
        redirectTo("/index.html");
    }

    private void handleLogout() throws IOException{

        logout(request.clientIP);
        redirectTo("/login.html");
    }


    private void redirectTo(String path) throws IOException{

        handleGETRequest(path);
    }

    private void tearDownConnection() throws IOException{

        bufferedReader.close();
        outputStream.close();
    }


}


