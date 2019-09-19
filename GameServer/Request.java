package GameServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request extends GameConnector{

    protected String method = "";
    protected String path = "";
    protected String action = "";
    protected Map<String, String> args = new HashMap<String, String>();
    protected double username;
    protected double password;
    protected double user_hash;
    protected String clientIP;

    public Request() throws IOException, ArrayIndexOutOfBoundsException, NullPointerException{

        parseRequest();


    }

    private void parseRequest() throws IOException, ArrayIndexOutOfBoundsException, NullPointerException{

        method = DEFAULT_METHOD;
        path = DEFAULT_PAGE;

        String[] parts = bufferedReader.readLine().split(" ");
        method = parts[0];
        path = parts[1];

        if(path.equals("/")){
            path = DEFAULT_PAGE;
        }

        int length = 0;
        String line = "";

        while((line = bufferedReader.readLine()).length() != 0){
            if(line.contains("Content-Length")){
                length = Integer.parseInt(line.split(" ")[1]);
            }

        }

        char[] temp = new char[length];
        bufferedReader.read(temp);
        String[] kv_arr = new String(temp).split("&"); // split by key-value pair, which are separated by &;
        for (String arg : kv_arr) {
            String[] kv = arg.split("=");   // split by key and and value, which are separated by =
            String key = kv[0];
            String value = kv[1];
            args.put(key, value);

        }
        if(method.equals("POST")){
            username = args.get("username").hashCode() % HASH_KEY;
            password = args.get("password").hashCode() % HASH_KEY;
            user_hash = (username % password) % HASH_KEY;
        }

        clientIP = clientSocket.getInetAddress().toString().replace("/","");
    }
}
