package GameServer;

import java.net.*;
import java.io.*;
import java.lang.*;
import java.sql.*;
import java.util.NoSuchElementException;

public class Server extends Thread{

    private static final int PORT_NUMBER = 8080;
    private static final String RELATIVE_PATH = "client/html";
    static final String DEFAULT_METHOD = "GET";
    static final String DEFAULT_PAGE = "/index.html";
    static final double HASH_KEY = 47324824;    // used in the encryption process of user authentication.

    private ServerSocket serverListener;

    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private String PROXY_ADDRESS = "jdbc:mariadb://proxy19.rt3.io:39136/cs414";
    private static final String DB_USERNAME = "user";
    private static final String DB_PASSWORD = "the_password_123";

    private static String devAPIKey = "MTA2M0FGNDUtM0M1QS00ODMyLUFDNDgtOEVBQ0E1Q0JBRUU1";
    private static String deviceAddress = "80:00:00:00:01:01:38:E9";

    static final String RESET_TEXT_COLOR = "\u001B[0m";
    static final String RED_TEXT = "\u001B[31m";
    static final String BLUE_TEXT = "\u001B[34m";
    static final String CYAN_TEXT = "\u001B[36m";
    static final String GREEN_TEXT = "\u001B[32m";
    static final String YELLOW_TEXT = "\u001B[33m";

    static final String INFO_TAG = BLUE_TEXT + "==INFO==:: " + RESET_TEXT_COLOR;
    static final String DEBUG_TAG = CYAN_TEXT + "==DEBUG==:: " + RESET_TEXT_COLOR;
    static final String ERROR_TAG = RED_TEXT + "==ERROR==:: " + RESET_TEXT_COLOR;
    static final String WARNING_TAG = YELLOW_TEXT + "==WARNING==:: " + RESET_TEXT_COLOR;
    static final String SUCCESS_TAG = GREEN_TEXT + "==SUCCESS==:: " + RESET_TEXT_COLOR;

    protected Server(){

    }

    protected void serve(){

        try{
            //connectToDatabase();
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

    protected void establishDatabaseProxyAddress() throws IOException, InterruptedException, ClassNotFoundException, SQLException {

        try {
            System.out.println(DEBUG_TAG + "Attempting to connect to remote database with existing proxy server address.");
            Class.forName(JDBC_DRIVER); // register the JDBC driver.
            DriverManager.getConnection(
                    PROXY_ADDRESS, DB_USERNAME, DB_PASSWORD); // Open a connection to the database.
            System.out.println(SUCCESS_TAG + "Using the existing proxy server address was successful.");
            return;
        }catch(SQLNonTransientConnectionException sql){
            System.out.println(WARNING_TAG + "Using the existing proxy server address failed;" +
                    " executing curl commands to retrieve new proxy server address.");
        }
        Process process = Runtime.getRuntime().exec("GameServer/getSessionToken.sh");
        process.waitFor();
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String part = "";
        String response = "";
        while((part = bufferedReader.readLine()) != null){
            response += part;
        }
        // System.out.println("==DEBUG==:: getSessionToken.sh response: " + response);
        String sessionToken = response.split(":")[2].replace("\"", "").split(",")[0];
        System.out.println(DEBUG_TAG + "Parsed remote proxy session token: " + sessionToken);

        String getProxyAddress = String.format("curl -X POST -H \"token:%s\" -H \"developerkey\":\"%s\" -d \'{\"wait\":\"true\", " +
                "\"deviceaddress\":\"\'%s\'\"}' https://api.remot3.it/apv/v27/device/connect", sessionToken, devAPIKey, deviceAddress);
        // System.out.println(getProxyAddress);

        FileWriter fileWriter = new FileWriter("GameServer/getProxyAddress.sh");
        fileWriter.write(getProxyAddress);
        fileWriter.close();
        process = Runtime.getRuntime().exec("chmod 775 GameServer/getProxyAddress.sh");
        process.waitFor();
        process = Runtime.getRuntime().exec("GameServer/getProxyAddress.sh");
        process.waitFor();

        inputStream = process.getInputStream();
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        response = "";
        while((part = bufferedReader.readLine()) != null){
            response += part;
        }
        process.destroy();
        // System.out.println("==DEBUG==:: getProxyAddress.sh response: " + response);
        String[] components = response.split(",");
        for(String component: components){
            if(component.contains("\"proxy\"")){
                PROXY_ADDRESS = component.replace("\\", "").replace("\"", "")
                        .split("proxy:")[1].replace("http://", "");
                break;
            }
        }
        PROXY_ADDRESS = "jdbc:mariadb://" + PROXY_ADDRESS + "/cs414";
        System.out.println(DEBUG_TAG + "Parsed remote proxy database address: " + PROXY_ADDRESS);

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
        Connection connection;
        Statement statement;
        try {
            //connectToDatabase();
            Class.forName(JDBC_DRIVER); // register the JDBC driver.
            connection = DriverManager.getConnection(
                    PROXY_ADDRESS, DB_USERNAME, DB_PASSWORD); // Open a connection to the database.
            statement = connection.createStatement();
            String checkAuth = String.format("SELECT 1 FROM Logged_in WHERE user_hash='%f'", user_hash);
            ResultSet resultSet = statement.executeQuery(checkAuth);

            if(resultSet.next()){ // User exists and has been authenticated, the request handling can continue.
                loggedIn = true;
            }
            else{   // User does not exist. Stop the request handling.
                loggedIn = false;
            }
        } catch(SQLNonTransientConnectionException e){
            throw new SQLNonTransientConnectionException();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return loggedIn;
    }

    void login(double user_hash) throws SQLNonTransientConnectionException {
        System.out.println(String.format(INFO_TAG + "Attempting to log in user with user_hash: %f.", user_hash));
        // Handle a POST request to login a client.
        Connection connection;
        Statement statement;
        try {
            //connectToDatabase();
            Class.forName(JDBC_DRIVER); // register the JDBC driver.
            connection = DriverManager.getConnection(
                    PROXY_ADDRESS, DB_USERNAME, DB_PASSWORD); // Open a connection to the database.
            statement = connection.createStatement();

            String checkAuth = String.format("SELECT 1 FROM User WHERE hash_code='%f'", user_hash);
            ResultSet resultSet = statement.executeQuery(checkAuth);

            if (resultSet.next()) { // User exists and has been authenticated, place IP into Logged_In table.
                String addToLoggedIn = String.format("INSERT INTO Logged_in VALUES ('%f');", user_hash);
                statement.executeQuery(addToLoggedIn);
            } else {   // User does not exist. Stop the request handling.
                throw new NoSuchElementException(String.format(DEBUG_TAG + "User %f does not exist!", user_hash));
            }
        } catch(SQLNonTransientConnectionException e){
            throw new SQLNonTransientConnectionException();

        } catch (SQLException | ClassNotFoundException e) {
            //Handle errors for JDBC
            e.printStackTrace();

        }
    }

    void logout(double user_hash) throws SQLNonTransientConnectionException{
        // Handle a POST request to logout a client.
        System.out.println(String.format(INFO_TAG + "Attempting to log out user with user_hash: %f.", user_hash));
        Connection connection;
        Statement statement;
        try {
          //  connectToDatabase();
            Class.forName(JDBC_DRIVER); // register the JDBC driver.
            connection = DriverManager.getConnection(
                    PROXY_ADDRESS, DB_USERNAME, DB_PASSWORD); // Open a connection to the database.
            statement = connection.createStatement();
            String logOut = String.format("DELETE FROM Logged_in WHERE user_hash='%f'", user_hash);
            statement.executeQuery(logOut);

        } catch(SQLNonTransientConnectionException e){
            throw new SQLNonTransientConnectionException();

        } catch (SQLException | ClassNotFoundException  e) {
            e.printStackTrace();
        }
    }

    void registerUser(String username, String password, double user_hash) throws SQLNonTransientConnectionException{
        // handle a POST request to register a new user in the system.
        System.out.println(String.format(INFO_TAG + "Attempting to register new user with username: %s , password: %s , user_hash: %f.", username, password, user_hash));
        Connection connection;
        Statement statement;
        try {
            //connectToDatabase();
            Class.forName(JDBC_DRIVER); // register the JDBC driver.
            connection = DriverManager.getConnection(
                    PROXY_ADDRESS, DB_USERNAME, DB_PASSWORD); // Open a connection to the database.
            statement = connection.createStatement();
            String registerUser = String.format("INSERT INTO User VALUES('%s', '%s', '%f')",
                    username, password, user_hash);
            statement.executeQuery(registerUser);

        } catch(SQLNonTransientConnectionException e){
            throw new SQLNonTransientConnectionException();

        } catch (SQLException | ClassNotFoundException  e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){

        Server server = new Server();
        server.serve();
    }
}