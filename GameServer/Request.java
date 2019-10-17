package GameServer;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Request {

    protected Map<String, String> header = new HashMap<String, String>();
    protected Map<String, String> body = new HashMap<String, String>();

    protected BufferedReader bufferedReader;
    protected Socket clientSocket;

    public Request(BufferedReader bufferedReader, Socket clientSocket) throws ArrayIndexOutOfBoundsException, NullPointerException {
        // constructor
        this.bufferedReader = bufferedReader;
        this.clientSocket = clientSocket;
    }

    protected int parseRequest() throws IOException, ArrayIndexOutOfBoundsException, NullPointerException {
        // parse the request for various arguments and parameters.
        Terminal.printDebug("Attempting to parse the request for parameters.");

        try {
            extractRequestLine();
            extractRequestHeader();
            extractRequestBody();
            printRequest();

        }catch(NullPointerException | ArrayIndexOutOfBoundsException e) {
            return -1;

        }catch(FileNotFoundException f) {
            return -2;
        }

        return 0;
    }

    private void extractRequestLine() throws FileNotFoundException, IOException {
        String[] requestLine = bufferedReader.readLine().split(" ");
        header.put("method", requestLine[0]);
        header.put("path", requestLine[1]);

        if (header.get("path").equals("/")) {
            header.put("path", Server.DEFAULT_PAGE);
            Terminal.printDebug(String.format("The request path has been changed to: %s.", header.get("path")));
        }
        if (header.get("path").equals("/css/common.css")) {
            // browsers automatically send a request for this file, which does not exist and wastes server resources.
            throw new FileNotFoundException();
        }
    }

    private void extractRequestHeader() throws IOException {
        String headerLine = "";
        while ((headerLine = bufferedReader.readLine()).length() != 0) {
            if (headerLine.contains("Content-Length")) {
                header.put("contentLength", headerLine.split(" ")[1]);
                Terminal.printDebug(String.format("The content length of the request is: %s.",
                        header.get("contentLength")));
            }
            if(headerLine.contains("Cookie")){
                header.put("cookie", headerLine.split("=")[1]);
                Terminal.printDebug(String.format("The cookies of the request are: %s.", header.get("cookie")));
            }
        }
    }

    private void extractRequestBody() throws IOException {
        if (header.get("method").equals("POST")) {
            // TODO: Refactor, there has got to be an easier way of doing this!
            char[] temp = new char[Integer.parseInt(header.get("contentLength"))];
            bufferedReader.read(temp);
            String[] kv_arr = new String(temp).split("&"); // split by key-value pair, which are separated by &;
            // extract the POST request arguments.
            for (String arg : kv_arr) {
                String[] kv = arg.split("=");   // split by key and and value, which are separated by =
                String key = kv[0];
                String value = kv[1];
                body.put(key, value);
            }
            Terminal.printDebug(String.format("The action of the POST request is: %s.", body.get("action")));
        }
    }

    private void printRequest(){
        Terminal.printDebug("~~ Request header: ~~");
        for (Map.Entry<String, String> kv: header.entrySet()) {
            String key = kv.getKey();
            String value = kv.getValue();
            Terminal.printDebug(String.format("~~ %s: %s ~~", key, value));
        }
        Terminal.printDebug("~~ Request body: ~~");
        for (Map.Entry<String, String> kv: body.entrySet()) {
            String key = kv.getKey();
            String value = kv.getValue();
            Terminal.printDebug(String.format("~~ %s: %s ~~", key, value));
        }
    }
}
