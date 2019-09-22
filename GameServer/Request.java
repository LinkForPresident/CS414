package GameServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Request extends GameConnector{

    protected String method = "";
    protected String path = "";
    protected String action = "";
    protected Map<String, String> args = new HashMap<String, String>();
    protected String username;
    protected String password;
    protected double user_hash;
    protected String clientIP;
    protected double cookie = -1;
    protected BufferedReader bufferedReader;
    protected Socket clientSocket;

    public Request(BufferedReader bufferedReader, Socket clientSocket) throws IOException, ArrayIndexOutOfBoundsException, NullPointerException{
        // constructor
        this.bufferedReader = bufferedReader;
        this.clientSocket = clientSocket;

    }

    protected int parseRequest() throws IOException, ArrayIndexOutOfBoundsException, NullPointerException{
        // parse the request for various arguments and parameters.
        clientIP = clientSocket.getRemoteSocketAddress().toString().split(":")[0];
        method = DEFAULT_METHOD; // GET, POST, etc.
        path = DEFAULT_PAGE; // used for GET requests.

        try {
            String[] parts = bufferedReader.readLine().split(" ");
            method = parts[0];
            path = parts[1];

            if (path.equals("/")) {
                path = DEFAULT_PAGE;
            }

            int length = 0;
            String line = "";
            while ((line = bufferedReader.readLine()).length() != 0) {
                if (line.contains("Content-Length")) {
                    length = Integer.parseInt(line.split(" ")[1]);
                }
                if(line.contains("Cookie")){
                    cookie = Double.parseDouble(line.split("=")[1]);
                }
            }

            if (method.equals("POST")) {
                // TODO: Refactor, there has got to be an easier way of doing this!

                char[] temp = new char[length];
                bufferedReader.read(temp);
                String[] kv_arr = new String(temp).split("&"); // split by key-value pair, which are separated by &;
                // extract the POST request arguments.
                for (String arg : kv_arr) {
                    String[] kv = arg.split("=");   // split by key and and value, which are separated by =
                    String key = kv[0];
                    String value = kv[1];
                    args.put(key, value);

                }
                action = args.get("action"); // whatever the client is trying to do: "login", "move_piece", etc.
                if(action.equals("login") || action.equals("user_registration")) {
                    username = args.get("username");
                    double username_hash = username.hashCode() % HASH_KEY;
                    password = args.get("password");
                    double password_hash = password.hashCode() % HASH_KEY;
                    user_hash = (username_hash % password_hash) % HASH_KEY;   // calculate the hash that acts as the primary key in the User table.
                    System.out.println(String.format("==DEBUG==:: Username, Password and User_Hash: %s, %s, %f", username, password, user_hash));
                }
            }
        }catch(NullPointerException | ArrayIndexOutOfBoundsException e){
            return -1;
        }
        return 0;
    }
}
