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
    static final double HASH_KEY = 47324824;

    private ServerSocket serverListener;

    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String DB_URL = "jdbc:mariadb://10.20.0.10/cs414";
    private static final String USER = "user";
    private static final String PASS = "the_password_123";

    protected Server(){

        try{
            serverListener = new ServerSocket(PORT_NUMBER); // Set up server to listen at PORT_NUMBER.
            System.out.println("GameServer listening.");

            while (true) {

                try{
                    Socket clientSocket = serverListener.accept();
                    Thread thread = new GameConnector(clientSocket);
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
        System.out.println("Fetching HTML Page!");
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
        // check db to see if this IP is in the "logged in" table.

        boolean loggedIn = false;

        Connection connection = null;
        Statement statement = null;
        try {

            Class.forName(JDBC_DRIVER); // register JDBC driver.
            connection = DriverManager.getConnection(
                    DB_URL, USER, PASS); //Open a connection to the database
            statement = connection.createStatement();

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
            }
        }

        return loggedIn;
    }

    void login(String clientIP, double hashCode){

        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName(JDBC_DRIVER); // register JDBC driver.
            connection = DriverManager.getConnection(
                    DB_URL, USER, PASS); //Open a connection to the database
            statement = connection.createStatement();

            String check_auth = String.format("SELECT 1 FROM User WHERE hash_code='%s'", hashCode);
            ResultSet resultSet = statement.executeQuery(check_auth);

            if(resultSet.next()){ // User exists and has been authenticated, place IP into Logged_In table.
                String addToLoggedIn = String.format("INSERT INTO LOGGED_IN VALUES ('%s');", clientIP);
                statement.executeQuery(addToLoggedIn);
            }
            else{   // User does not exist. Stop the request handling.
                throw new NoSuchElementException("User does not exist!");
            }

        } catch (SQLException | ClassNotFoundException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }

    void logout(String clientIP) throws IOException{

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

           // ResultSet resultSet = statement.executeQuery("SELECT * FROM LOGGED_IN;");

         //   while(resultSet.next()){
             //   System.out.println(resultSet.getString(1));
            //}


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
            }
        }

    }

    public static void main(String[] args){

        Server server = new Server();
    }
}