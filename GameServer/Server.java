package GameServer;

import java.net.*;
import java.io.*;
import java.lang.*;
import java.sql.*;
import java.util.NoSuchElementException;

public class Server extends Thread{

    private static final int PORT_NUMBER = 8080;
    private static final String RELATIVE_PATH = "client/html";
    static final String HEADER = "HTTP/1.0 200 OK\nContent-Type: text/html\n\n";
    static final String DEFAULT_METHOD = "GET";
    static final String DEFAULT_PAGE = "/index.html";
    static final double HASH_KEY = 47324824;    // used in the encryption process of user authentication.

    private ServerSocket serverListener;

    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String DB_URL = "jdbc:mariadb://proxy18.rt3.io:38760/cs414";
    private static final String DB_USER = "user";
    private static final String DB_PASS = "the_password_123";

    protected Server(){

        try{
            serverListener = new ServerSocket(PORT_NUMBER); // Set up server to listen at PORT_NUMBER.
            System.out.println("GameServer listening.");

            while (true) {
                try{
                    Socket clientSocket = serverListener.accept(); // accept a connection from a client.
                    System.out.println("Connection accepted from client.");
                    Thread thread = new GameConnector(clientSocket); // start a thread to handle the connection.
                    thread.start();
                }
                catch(ArrayIndexOutOfBoundsException | NullPointerException e){
                }
            }
        }
        catch(IOException e){
        }
    }

    String getHTMLPage(String path) throws IOException{
        // Fetch an HTML page for the client.
        System.out.println(String.format("Fetching HTML Page: %s", path));
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



    boolean isLoggedIn(String clientIP){
    // check whether a client is logged in, i.e. the client IP address is an entry in the Logged_In table.
        boolean loggedIn = false;
        Connection connection;
        Statement statement;
        try {
            Class.forName(JDBC_DRIVER); // register the JDBC driver.
            connection = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASS); // Open a connection to the database.
            statement = connection.createStatement();
            String checkAuth = String.format("SELECT 1 FROM LOGGED_IN WHERE ip='%s'", clientIP);
            ResultSet resultSet = statement.executeQuery(checkAuth);

            if(resultSet.next()){ // User exists and has been authenticated, the request handling can continue.
                loggedIn = true;
                System.out.println(String.format("%s client is logged in.", clientIP));
            }
            else{   // User does not exist. Stop the request handling.
                System.out.println(String.format("%s client is not logged in.", clientIP));
                loggedIn = false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loggedIn;
    }

    void login(String clientIP, double hashCode){
        // Handle a POST request to login a client.
        Connection connection;
        Statement statement;
        try {
            Class.forName(JDBC_DRIVER); // register the JDBC driver.
            connection = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASS); // Open a connection to the database.
            statement = connection.createStatement();

            String checkAuth = String.format("SELECT 1 FROM User WHERE hash_code='%s'", hashCode);
            ResultSet resultSet = statement.executeQuery(checkAuth);

            if(resultSet.next()){ // User exists and has been authenticated, place IP into Logged_In table.
                String addToLoggedIn = String.format("INSERT INTO LOGGED_IN VALUES ('%s');", clientIP);
                statement.executeQuery(addToLoggedIn);
            }
            else{   // User does not exist. Stop the request handling.
                throw new NoSuchElementException("User does not exist!");
            }

            System.out.println(String.format("%s client has been in out.", clientIP));

        } catch (SQLException | ClassNotFoundException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }

    void logout(String clientIP) throws IOException{
        // Handle a POST request to logout a client.
        Connection connection;
        Statement statement;
        try {
            Class.forName(JDBC_DRIVER); // register the JDBC driver.
            connection = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASS); // Open a connection to the database.
            statement = connection.createStatement();
            String logOut = String.format("DELETE FROM LOGGED_IN WHERE ip='%s'", clientIP);
            statement.executeQuery(logOut);
            System.out.println(String.format("%s client has been logged out.", clientIP));

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){

        Server server = new Server();
    }
}