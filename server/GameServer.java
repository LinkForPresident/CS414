import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.HashMap;
import java.util.Map;

public class GameServer{

    private static final int PORT_NUMBER = 8080;
    private static final String RELATIVE_PATH = "../client/html";
    private static final String HEADER = "HTTP/1.0 200 OK\nContent-Type: text/html\n\n";
    private static final String DEFAULT_METHOD = "GET";
    private static final String DEFAULT_PAGE = "/index.html";
    private static final double HASH_KEY = 47382;

    private ServerSocket serverListener;
    private Socket clientSocket;
    private InputStream inputStream;
    private PrintWriter outputStream;
    private BufferedReader bufferedReader;

    private String request;
    private String method;
    private String path;
    private String action;
    private Map<String, String> args = new HashMap<String, String>();

    private GameServer(){
        try{
            serverListener = new ServerSocket(PORT_NUMBER); // Set up server to listen at PORT_NUMBER.
            System.out.println("GameServer listening.");

            while (true) {
                try{
                    clientSocket = serverListener.accept(); // accept a connection from a client, fork socket for this connection.
                    setUpConnection();
                    parseRequest();
                    handleRequest();
                    tearDownConnection();
                }
                catch(IOException | ArrayIndexOutOfBoundsException | NullPointerException e){
                }
            }
        }
        catch(IOException e){
        }
    }

    private void setUpConnection() throws IOException{
        inputStream = clientSocket.getInputStream(); // gets data from client.
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // stores data from client.
        outputStream = new PrintWriter(clientSocket.getOutputStream(), true); // sends data to client.
    }

    private void parseRequest() throws IOException, ArrayIndexOutOfBoundsException, NullPointerException{
        request = bufferedReader.readLine();
        method = DEFAULT_METHOD;
        path = DEFAULT_PAGE;

        String[] parts = request.split(" ");
        method = parts[0];
        path = parts[1];

        if(path.equals("/")){
            path = DEFAULT_PAGE;
        }
    }

    private void handleRequest() throws IOException{
        System.out.println(request);
        switch(method){
            case "GET":
                handleGETRequest();
                break;
            case "POST":
                handlePOSTRequest();
                break;
        }
    }

    private void handleGETRequest() throws IOException{
        File file = new File(RELATIVE_PATH + path);

        if(!file.exists()){
            outputStream.println("404 file not found!");
        }

        FileReader fileReader = new FileReader(file);
        BufferedReader fileBuffer = new BufferedReader(fileReader); // read the file into a buffer.
        String response = HEADER; // append header to response.
        String line;

        while((line = fileBuffer.readLine()) != null){ // read until end of file.
            response += line; // append to response.
        }

        outputStream.println(response); // send response to client.
        fileBuffer.close();
    }

    private void handlePOSTRequest() throws IOException{
        System.out.println("test0");
        parsePOSTRequest();
        System.out.println("test1");
        handleUserAuthentication();
        System.out.println("test2");
        handleAction();
        System.out.println("test3");
    }

    private void parsePOSTRequest() throws IOException{
        int length = 0;
        String line = "";

        while((line = bufferedReader.readLine()).length() != 0){

            if(line.contains("Content-Length")){
                length = Integer.parseInt(line.split(" ")[1]);
            }

        }

        char[] temp = new char[length];
        bufferedReader.read(temp);
        String[] kv_arr = new String(temp).split("&"); // split by key-value pair, which are separated by &;
        for (String arg : kv_arr) {
            String[] kv = arg.split("=");   // split by key and and value, which are separated by =
            String key = kv[0];
            String value = kv[1];
            args.put(key, value);

        }
    }

    private void handleAction() throws IOException{
        action = args.get("action");
        switch (action) {
            case "user_registration":
                // handleUserRegistration(); needs to register new user and redirect to index.html.
                break;
            case "login":
                handleLogin();
                break;
            case "create_game":
                // handleCreateGame();
                break;
            case "enter_game":
                // handleEnterGame();
                break;
            case "forfeit_game":
                // handleForfeitGame();
                break;
            case "move_piece":
                // handleMovePiece();
                break;
            case "view_stats":
                // handleViewStats();
                break;
        }
    }

    private void handleUserAuthentication(){
        double username = args.get("username").hashCode() * HASH_KEY;
        double password = args.get("password").hashCode() * HASH_KEY;
        double pk = (username % password) * HASH_KEY;

        // now use pk in SELECT statement to DB to lookup whether user exists.

        // if user exists, return.

        // if user does not exist, return error message.

    }

    private void handleLogin() throws IOException{
        path = "/index.html";
        handleGETRequest();
    }

    private void tearDownConnection() throws IOException{
        bufferedReader.close();
        outputStream.close();
    }

    public static void main(String[] args){
        GameServer gameServer = new GameServer();
    }
}