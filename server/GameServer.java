import java.net.*;
import java.io.*;
import java.lang.*;

public class GameServer{

    static final int PORT_NUMBER = 8080;
    static final String RELATIVE_PATH = "../client/html";
    static final String header = "HTTP/1.0 200 OK\nContent-Type: text/html\n\n";

    public static void main(String[] args){

        try (ServerSocket serverListener = new ServerSocket(PORT_NUMBER)) { // Set up server to listen at PORT_NUMBER.

            System.out.println("GameServer listening.");

            while (true) {

                try (Socket clientSocket = serverListener.accept()){ // accept a connection from a client, fork socket for this connection.

                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // sends data to client.
                    InputStream socketInputStream = clientSocket.getInputStream(); // gets data from client.
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socketInputStream)); // stores data from client.

                    String request = bufferedReader.readLine();
                    System.out.println(request);
                    String path;
                    try{
                        path = request.split(" ")[1]; // get name of HTML page requested.
                    }catch(ArrayIndexOutOfBoundsException e){
                        path = "/index.html";
                    }
                    System.out.println(path);
                    if(path.equals("/")){
                        path = "/index.html";
                    }
                    File file = new File(RELATIVE_PATH + path);

                    if(!file.exists()){
                        out.println("404 file not found!");
                    }

                    FileReader fileReader = new FileReader(file);
                    bufferedReader = new BufferedReader(fileReader); // read the file into a buffer (reused).
                    String response = header; // append header to response.
                    String line;

                    while((line = bufferedReader.readLine()) != null){ // read until end of file.
                        response += line; // append to response.
                    }

                    out.println(response); // send response to client.

                    bufferedReader.close();
                    out.close();

                }catch(IOException e){}

            }

        }catch(IOException e){}

    }

}