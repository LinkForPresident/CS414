package GameServer;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

class Handler extends Thread {

    private Socket clientSocket;
    private PrintWriter outputStream;
    private BufferedReader bufferedReader;
    private Request request;
    private Response response;

    Handler(Socket clientSocket){
        this.clientSocket = clientSocket;

    }

    @Override
    public void run() {
        // overrides the thread run() methods.
        // set up the connection with the client.
        try {
            setUpConnection();

        }catch(FileNotFoundException f){
                Terminal.printWarning("Client automatically requested '/css/common.css', which does not exist.");
                tearDownConnection();
                return;
        }
        catch(IOException e){
                Terminal.printWarning("This was not a real request by the user, but an automatic request by the " +
                        "browser.");
                tearDownConnection();
                return;
        }
        catch(NullPointerException n){
            // in case of an error in set up, tear down the connection.
                Terminal.printError("Encountered an error while trying to set up connection; tearing down connection.");
                n.printStackTrace();
                tearDownConnection();
                return;
        }
        // check client authentication.
        if (clientIsAuthenticated()) {
            // client is authenticated, handle the client's request.
            try {
                handleRequest();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                    Terminal.printError("Encountered an error while attempting to handle the request; tearing " +
                            "down connection.");
                    tearDownConnection();
                    return;
            }
        } else {
            // client is not authenticated, deny access and redirect to the login page.
            Terminal.printDebug("Client is not authenticated.");
            tearDownConnection();
            return;
        }
        // request has been fulfilled, tear down the connection.
        tearDownConnection();
        Terminal.printSuccess("Request has been served and connection successfully torn down.");

    }

    private void setUpConnection() throws IOException, NullPointerException{
        // set up the necessary data structures to handle a client socket connection.
        Terminal.printDebug("Setting up connection.");
        InputStream inputStream = this.clientSocket.getInputStream(); // gets data from client.
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // stores data from client.
        outputStream = new PrintWriter(clientSocket.getOutputStream(), true); // sends data to client.
        request = new Request(bufferedReader); // create request object
        int flag = request.parseRequest();
        if(flag == -1){
            throw new IOException();
        }
        else if(flag == -2){
            throw new FileNotFoundException();
        }
    }

    private boolean clientIsAuthenticated() {
		// check if client is logged in, or is trying to either register, login or logout.
        Terminal.printDebug("Checking is client is authenticated for this action.");
        String action = request.body.get("action");
        String cookie = request.header.get("cookie");
        return action.equals("Register") || action.equals("Login") || action.equals("Logout") ||
                Server.isLoggedIn(cookie);
    }

    private void handleRequest() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException{

        String action = request.body.get("action");
        Class<?> classSupportingAction = Class.forName(String.format("GameServer.%s", action));
        Object actionInstance = classSupportingAction.newInstance();
        Method executeAction = classSupportingAction.getDeclaredMethod("executeAction", Request.class);
        response = ((Response)executeAction.invoke(actionInstance, request));
        sendJSONResponse(response.response);

    }

    
   private void sendJSONResponse(String response) {
		// send a JSON response to the client.
        Terminal.printInfo(String.format("Sending JSON response: \n %s", response));
		outputStream.println(response); // send the response to the client.
    }


    private void tearDownConnection(){
        // tear down the connection with the client.
        Terminal.printInfo("Attempting to tear down the GameConnector instance.");
        try {
            bufferedReader.close();
            outputStream.close();
        }catch(IOException e){
            Terminal.printError("Encountered an error while tearing down the connection.");
            return;
        }
        Terminal.printInfo("Connection has been torn down.");

    }

}


