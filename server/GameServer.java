import java.net.*;
import java.io.*;
import java.lang.*;

public class GameServer{

    static final int PORT_NUMBER = 8080;
    static final String RELATIVE_PATH = "../client/html";
    static final String HEADER = "HTTP/1.0 200 OK\nContent-Type: text/html\n\n";
    static final String DEFAULT_METHOD = "GET";
    static final String DEFAULT_PAGE = "/index.html";

    GameServer(){
        try{
            ServerSocket serverListener = new ServerSocket(PORT_NUMBER); // Set up server to listen at PORT_NUMBER.
            System.out.println("GameServer listening.");

            while (true) {
                try{
                    Socket clientSocket = serverListener.accept(); // accept a connection from a client, fork socket for this connection.
                    InputStream InputStream = clientSocket.getInputStream(); // gets data from client.
                    PrintWriter outputStream = new PrintWriter(clientSocket.getOutputStream(), true); // sends data to client.
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(InputStream)); // stores data from client.

                    String request = bufferedReader.readLine();
                    String method = DEFAULT_METHOD;
                    String path = DEFAULT_PAGE;

                    try{
                        String[] parts = request.split(" ");
                        method = parts[0];
                        path = parts[1];
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                    }

                    if(path.equals("/")){
                        path = DEFAULT_PAGE;
                    }

                    if(method.equals("GET")){
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

                    bufferedReader.close();
                    outputStream.close();
                }
                catch(IOException e){
                }
            }

        }
        catch(IOException e){
        }
    }

    public static void main(String[] args){

        GameServer gameServer = new GameServer();
        return 0;
    }
}