package GameServer;

import GameLogic.exception.PlayerNameException;
import GameLogic.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.sql.*;
import java.util.*;

public class Database {

	private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static String PROXY_ADDRESS = "jdbc:mariadb://proxy19.rt3.io:39136/cs414";
    private static final String DB_USERNAME = "user";
    private static final String DB_PASSWORD = "the_password_123";
    private static String devAPIKey = "MTA2M0FGNDUtM0M1QS00ODMyLUFDNDgtOEVBQ0E1Q0JBRUU1";
    private static String deviceAddress = "80:00:00:00:01:01:38:E9";
    
    protected Database(){
    
    }
    
    static void establishDatabaseProxyAddress() throws IOException, InterruptedException, ClassNotFoundException, SQLException {

        try {
            Terminal.printDebug("Attempting to connect to remote database with existing proxy server address.");
            Class.forName(JDBC_DRIVER); // register the JDBC driver.
            DriverManager.getConnection(
                    PROXY_ADDRESS, DB_USERNAME, DB_PASSWORD); // Open a connection to the database.
            Terminal.printSuccess("Using the existing proxy server address was successful.");
            return;
        }catch(SQLNonTransientConnectionException sql){
            Terminal.printWarning("Using the existing proxy server address failed; executing curl commands to " +
                    "retrieve new proxy server address.");
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
		Terminal.printDebug("getSessionToken.sh response: " + response);
        String sessionToken = response.split(":")[2].replace("\"", "").split(",")[0];
        Terminal.printDebug(String.format("Parsed remote proxy session token: %s", sessionToken));

        String getProxyAddress = String.format("curl -X POST -H \"token:%s\" -H \"developerkey\":\"%s\" " +
                "-d \'{\"wait\":\"true\", \"deviceaddress\":\"\'%s\'\"}' http://api.remot3.it/apv/v27/device/connect",
                sessionToken, devAPIKey, deviceAddress);
        Terminal.printDebug(getProxyAddress);

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
        Terminal.printDebug("getProxyAddress.sh response: " + response);
        String[] components = response.split(",");
        for(String component: components){
            if(component.contains("\"proxy\"")){
                PROXY_ADDRESS = component.replace("\\", "").replace("\"", "")
                        .split("proxy:")[1].replace("http://", "");
                break;
            }
        }
        PROXY_ADDRESS = "jdbc:mariadb://" + PROXY_ADDRESS + "/cs414";
        Terminal.printDebug(String.format("Parsed remote proxy database address: ", PROXY_ADDRESS));
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
    
    public static void main(String[] args) throws SQLException{
		if(args.length > 0 && args[0].equals("--debug")){
				Server.debugMode = true;
			}
		String query = args[1];
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


















