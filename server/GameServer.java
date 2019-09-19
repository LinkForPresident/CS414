import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;

public class GameServer{

    private static final int PORT_NUMBER = 8080;
    private static final String RELATIVE_PATH = "../client/html";
    private static final String HEADER = "HTTP/1.0 200 OK\nContent-Type: text/html\n\n";
    private static final String DEFAULT_METHOD = "GET";
    private static final String DEFAULT_PAGE = "/index.html";
    private static final double HASH_KEY = 47324824;

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

    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://10.20.0.10/cs414";
    static final String USER = "user";
    static final String PASS = "the_password_123";

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
        request = "";
        method = "";
        path = "";
        action = "";
        args = new HashMap<String, String>();
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

        switch(method){
            case "GET":
                handleGETRequest();
                break;
            case "POST":
                handlePOSTRequest();
                break;
        }
    }

    private void handleGETRequest() throws IOException {

        handleUserAuthentication();
        serveGETRequest();

    }

    private void serveGETRequest() throws IOException {

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

        parsePOSTRequestArgs();
        handleUserAuthentication();
        handleAction();
    }

    private void parsePOSTRequestArgs() throws IOException{

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

        switch (action) {
            case "user_registration":
                // handleUserRegistration(); needs to register new user and redirect to index.html.
                break;
            case "login":
                handleLogin();
                break;
            case "logout":
                handleLogout();
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

    private double[] getAuthTokens(){

        double username = args.get("username").hashCode() % HASH_KEY;
        double password = args.get("password").hashCode() % HASH_KEY;
        double pk = (username % password) % HASH_KEY;
        return new double[]{username, password, pk};
    }

    private void handleUserAuthentication() throws IOException{

        String clientIP = clientSocket.getInetAddress().toString().replace("/","");
        action = args.getOrDefault("action","GET_REQUEST");

        if(!isLoggedIn(clientIP) && !action.equals("user_registration") && !action.equals("login") && !action.equals("logout")){
            redirectToLogin();
        }
    }

    private boolean isLoggedIn(String clientIP){
        // check db to see if this IP is in the "logged in" table.

        boolean loggedIn = false;

        Connection connection = null;
        Statement statement = null;
        try {

            Class.forName(JDBC_DRIVER); // register JDBC driver.
            connection = DriverManager.getConnection(
                    DB_URL, USER, PASS); //Open a connection to the database
            statement = connection.createStatement();

            String temp = String.format("DELETE FROM LOGGED_IN WHERE ip='-1492488.000000'");
            ResultSet rsas = statement.executeQuery(temp);

            String check_auth = String.format("SELECT 1 FROM LOGGED_IN WHERE ip='%s'", clientIP);
            ResultSet resultSet = statement.executeQuery(check_auth);

            if(resultSet.next()){ // User exists and has been authenticated, the request handling can continue.
                loggedIn = true;
                System.out.println("Client is logged in.");
            }
            else{   // User does not exist. Stop the request handling.
                System.out.println("Client is not logged in.");
                loggedIn = false;
            }

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null) {
                    connection.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try

        return loggedIn;
    }

    private void handleLogin() throws IOException{

        String clientIP = clientSocket.getInetAddress().toString().replace("/","");

        Connection connection = null;
        Statement statement = null;
        try {

            Class.forName(JDBC_DRIVER); // register JDBC driver.
            connection = DriverManager.getConnection(
                    DB_URL, USER, PASS); //Open a connection to the database
            statement = connection.createStatement();

            double hashCode = getAuthTokens()[2];

            String check_auth = String.format("SELECT 1 FROM User WHERE hash_code='%s'", hashCode);
            ResultSet resultSet = statement.executeQuery(check_auth);

            if(resultSet.next()){ // User exists and has been authenticated, place IP into Logged_In table.
                String addToLoggedIn = String.format("INSERT INTO LOGGED_IN VALUES ('%s');", clientIP);
                statement.executeQuery(addToLoggedIn);
            }
            else{   // User does not exist. Stop the request handling.
                redirectToLogin();
            }

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null) {
                    connection.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        path = "/index.html";
        serveGETRequest();
    }

    private void handleLogout() throws IOException{

        String clientIP = clientSocket.getInetAddress().toString().replace("/","");

        Connection connection = null;
        Statement statement = null;
        try {

            Class.forName(JDBC_DRIVER); // register JDBC driver.
            connection = DriverManager.getConnection(
                    DB_URL, USER, PASS); //Open a connection to the database
            statement = connection.createStatement();
            System.out.println(String.format("Client IP:%s",clientIP));
            String logOut = String.format("DELETE FROM LOGGED_IN WHERE ip='%s'", clientIP);
            statement.executeQuery(logOut);

            ResultSet resultSet = statement.executeQuery("SELECT * FROM LOGGED_IN;");

            while(resultSet.next()){
                System.out.println(resultSet.getString(1));
            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null) {
                    connection.close();
                }
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        path = "/login.html";
        serveGETRequest();
    }

    private void redirectToLogin() throws IOException{

        path = "/login.html";
        serveGETRequest();
        tearDownConnection();
    }

    private void tearDownConnection() throws IOException{

        bufferedReader.close();
        outputStream.close();
    }

    public static void main(String[] args){

        GameServer gameServer = new GameServer();
    }
}