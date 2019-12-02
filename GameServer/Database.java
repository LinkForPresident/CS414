package GameServer;

import GameLogic.exception.PlayerNameException;
import GameLogic.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.sql.*;
import java.util.*;
import com.google.gson.*;

public class Database {

	private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static String PROXY_ADDRESS = "jdbc:mariadb://proxy19.rt3.io:39136/cs414";
    private static final String DB_USERNAME = "user";
    private static final String DB_PASSWORD = "the_password_123";
    private static String DEV_API_KEY = "MTA2M0FGNDUtM0M1QS00ODMyLUFDNDgtOEVBQ0E1Q0JBRUU1";
    private static String DEVICE_ADDRESS = "80:00:00:00:01:01:38:E9";
    
    protected Database(){
    
    }
    
    static void establishDatabaseProxyAddress() throws IOException, InterruptedException, ClassNotFoundException, SQLException {

        if(!canConnect()){
            Terminal.printWarning("Using the existing proxy server address failed.");
            Terminal.printInfo("Executing curl commands to retrieve new proxy server address.");
            updateProxyAddress();

            if(!canConnect()){
                Terminal.printError("Establishing the database proxy address failed.");
                throw new SQLException();
            }
            else{
                Terminal.printSuccess("The database proxy address has been successfully updated.");
            }
        }
        else{
            Terminal.printDebug("Using the existing proxy server address was successful.");
        }
    }

    static boolean canConnect() throws ClassNotFoundException{

        try {
            Terminal.printDebug("Attempting to connect to remote database with the current proxy server address.");
            Class.forName(JDBC_DRIVER); // register the JDBC driver.
            DriverManager.getConnection(
                    PROXY_ADDRESS, DB_USERNAME, DB_PASSWORD); // Open a connection to the database.
            return true;
        }catch(SQLException sql){
            return false;
        }

    }

    static String getSessionToken() throws IOException, InterruptedException{

        String response = Terminal.executeShellScript("GameServer/getSessionToken.sh");
        String sessionToken = parseGetSessionTokenResponse(response);
        return sessionToken;

    }


    static String parseGetSessionTokenResponse(String response){

        String sessionToken;
        try {
            sessionToken = response.split(":")[2].replace("\"", "").split(",")[0];
        }catch(NullPointerException e){
            Terminal.printError("Encountered an error while parsing the response for the session token.");
            throw new NullPointerException();
        }
        Terminal.printDebug(String.format("Parsed remote proxy session token: %s", sessionToken));
        return sessionToken;
    }


    static void updateProxyAddress() throws IOException, InterruptedException{

        String sessionToken = getSessionToken();
        String scriptPath = writeGetProxyAddressFile(sessionToken);
        String response = Terminal.executeShellScript(scriptPath);
        String proxyAddress = parseGetProxyAddressResponse(response);
        PROXY_ADDRESS = String.format("jdbc:mariadb://%s/cs414", proxyAddress);
        Terminal.printDebug(String.format("Parsed remote proxy database address: %s", PROXY_ADDRESS));

    }

    static String writeGetProxyAddressFile(String sessionToken) throws IOException{

        String getProxyAddress = String.format("curl -X POST -H \"token:%s\" -H \"developerkey\":\"%s\" " +
                        "-d \'{\"wait\":\"true\", \"deviceaddress\":\"\'%s\'\"}' http://api.remot3.it/apv/v27/device/connect",
                sessionToken, DEV_API_KEY, DEVICE_ADDRESS);

        Terminal.printDebug("Writing new getProxyAddress.sh.");
        FileWriter fileWriter = new FileWriter("GameServer/getProxyAddress.sh");
        try {
            fileWriter.write(getProxyAddress);
        }catch(IOException e){
            Terminal.printError("Encountered an error while attempting to write the getProxyAddress.sh file.");
            fileWriter.close();
            throw new IOException();
        }
        fileWriter.close();
        return "GameServer/getProxyAddress.sh";

    }


    static String parseGetProxyAddressResponse(String response){

        String proxyAddress = "";
        try {
            String[] components = response.split(",");
            for (String component : components) {
                if (component.contains("\"proxy\"")) {
                    proxyAddress = component.replace("\\", "").replace("\"", "")
                            .split("proxy:")[1].replace("http://", "");
                    break;
                }
            }
        }catch(NullPointerException e){
            Terminal.printError("Encountered an error while parsing the response for the proxy address.");
        }
        return proxyAddress;

    }
    
    static ResultSet executeDatabaseQuery(String query){
        Connection connection;
        Statement statement;
        ResultSet resultSet = null;
        Terminal.printInfo(String.format("Attempting to execute database query: %s", query));
        try{
            establishDatabaseProxyAddress();
            Class.forName(JDBC_DRIVER); // register the JDBC driver.
            connection = DriverManager.getConnection(
                    PROXY_ADDRESS, DB_USERNAME, DB_PASSWORD); // Open a connection to the database.
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch(SQLNonTransientConnectionException e){
            Terminal.printError("Encountered an error while connecting to the database. (HINT: the proxy server is" +
                    " likely different than what is set.)");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch(InterruptedException | IOException e) {
            Terminal.printError("Encountered an error while attempting to curl proxy server credentials for the database.");
            e.printStackTrace();
        }finally {
            return resultSet;
        }
    }

    public static String formatGameGSON(Game game){
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(game);
        json = json.replace("}", "#");
        json = json.replace("{", "%");
        json = json.replace("]", "&");
        json = json.replace("[", "^");
        return json;
    }
    
    public static void main(String[] args) throws SQLException{
        String query;
		if(args.length > 0 && args[0].equals("--debug")){
            Server.debugMode = true;
            query = args[1];
        }else {
            query = args[0];
        }
		Terminal.printInfo(String.format("The following SQL query will be executed: \"%s\".", query));
		ResultSet resultSet = executeDatabaseQuery(query);
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int numberOfColumns = rsmd.getColumnCount();
		for(int i = 1; i<=numberOfColumns; i++){
			System.out.printf("| %-25s", rsmd.getColumnName(i));
		}
		System.out.println("|");
		for(int i = 1; i<=numberOfColumns; i++){
			for(int j = 0; j<30; j++){
				System.out.print("=");
			}
		}System.out.println();
		while(resultSet.next()){
			for (int i = 1; i <= numberOfColumns; i++) {
					String columnValue = resultSet.getString(i);
					System.out.printf("| %-25s", columnValue);
				}
				System.out.println();
		}
    }
}


















