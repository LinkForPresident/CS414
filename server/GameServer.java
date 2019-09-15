import java.net.*;
import java.io.*;
import java.lang.*;

public class GameServer{

    static final int PORT_NUMBER = 8080;
    static final String RELATIVE_PATH = "../client/html";
    static final String HEADER = "HTTP/1.0 200 OK\nContent-Type: text/html\n\n";
    static final String DEFAULT_METHOD = "GET";
    static final String DEFAULT_PAGE = "/index.html";

    ServerSocket serverListener;
    Socket clientSocket;
    InputStream inputStream;
    PrintWriter outputStream;
    BufferedReader bufferedReader;

    String request;
    String method;
    String path;


    GameServer(){
        try{
            ServerSocket serverListener = new ServerSocket(PORT_NUMBER); // Set up server to listen at PORT_NUMBER.
            System.out.println("GameServer listening.");

            while (true) {
                try{
                    clientSocket = serverListener.accept(); // accept a connection from a client, fork socket for this connection.
                    setUpConnection();
                    parseRequest();
                    handleRequest();
                    tearDownConnection();
                }
                catch(IOException | ArrayIndexOutOfBoundsException e){
                }
            }
        }
        catch(IOException e){
        }
    }

    void setUpConnection() throws IOException{
        inputStream = clientSocket.getInputStream(); // gets data from client.
        outputStream = new PrintWriter(clientSocket.getOutputStream(), true); // sends data to client.
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // stores data from client.
    }

    void parseRequest() throws IOException, ArrayIndexOutOfBoundsException{
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

    void handleRequest() throws FileNotFoundException, IOException{
        switch(method){
            case "GET":
                handleGETRequest();
                break;
            case "POST":
                break;
        }
    }

    void handleGETRequest() throws FileNotFoundException, IOException{
        File file = new File(RELATIVE_PATH + path);

        if(!file.exists()){
            outputStream.println("404 file not found!");
        }

        FileReader fileReader = new FileReader(file);
        BufferedReader fileBuffer = new BufferedReader(fileReader); // read the file into a buffer (reused).
        String response = HEADER; // append header to response.
        String line;

        while((line = fileBuffer.readLine()) != null){ // read until end of file.
            response += line; // append to response.
        }

        outputStream.println(response); // send response to client.
        fileBuffer.close();
    }

    void tearDownConnection() throws IOException{
        bufferedReader.close();
        outputStream.close();
    }

    public static void main(String[] args){

        GameServer gameServer = new GameServer();
        return;
    }
}