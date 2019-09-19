package GameServer;

import java.io.*;
import java.net.Socket;

public class GameConnector extends Server{

    protected Socket clientSocket;
    protected InputStream inputStream;
    protected PrintWriter outputStream;
    protected BufferedReader bufferedReader;
    protected Request request;


    GameConnector() throws IOException {

        setUpConnection();
        if(clientIsAuthenticated()){
            handleRequest();
        }
        else{
            redirectTo("/login.html");
        }
        tearDownConnection();


    }

    private void setUpConnection() throws IOException{

        clientSocket = super.serverListener.accept();
        inputStream = clientSocket.getInputStream(); // gets data from client.
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // stores data from client.
        outputStream = new PrintWriter(clientSocket.getOutputStream(), true); // sends data to client.
        request = new Request();
    }


    private void handleRequest() throws IOException{

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
                login(request.clientIP, request.user_hash);
                redirectTo("/index.html");
                break;
            case "logout":
                logout(request.clientIP);
                redirectTo("/login.html");
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

    private void redirectTo(String path) throws IOException{
        handleGETRequest(path);
    }

    void tearDownConnection() throws IOException{
        bufferedReader.close();
        outputStream.close();
    }


}


