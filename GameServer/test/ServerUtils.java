package GameServer.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ServerUtils {
    public HashMap<String, String> convertJsonStringToMap(String json) {
        HashMap<String, String> map = new HashMap<String, String>();
        json = json.replaceAll("\"","");
        json = json.replaceAll("\\{","");
        json = json.replaceAll("\\}","");

        String boardString = "";

        if(json.contains("board: [")) {
            boardString = json.substring(json.indexOf("board: [")+9, json.indexOf("]]"));
            map.put("boardString", boardString);
        }

        String[] list = json.split(", ");
        for(String elem : list) {
            String[] subElem = elem.split(":");
            map.put(subElem[0].trim(), subElem[1].trim());
        }
        return map;
    }

    public String[][] convertBoardStringToBoardArray(String boardString) {
        String[][] board = new String[9][7];
        int x = 0, y = 0;
        String [] boardStrArr = boardString.split("],");
        for(String boardSubString : boardStrArr) {
            x = 0;
            boardSubString += ",";
            while(boardSubString.contains("environment")) {
                String piece = boardSubString.substring(boardSubString.indexOf("piece: ")+7);
                String available = piece.substring(piece.indexOf("available: ") + 11);
                available = available.substring(0,available.indexOf(","));
                piece = piece.substring(0, piece.indexOf(","));
                boardSubString = boardSubString.substring(boardSubString.indexOf(piece)+2);
                board[y][x] = piece +"(" + available + ")";
                x++;
            }
            y++;
        }
        return board;
    }

    public String getPieceCoordinates(String[][] board, String piece) {
        for(int y = 0; y<9; y++) {
            for(int x = 0; x<7; x++) {
                if(board[y][x].contains(piece)) {
                    return y+","+x;
                }
            }
        }
        return "piece not found!";
    }

    public boolean isCoordinateAvailable(String[][] board, int y, int x) {
        if(board[y][x].contains("true")) {
            return true;
        }
        return false;
    }

    public HashMap<String, String> sendHttpRequest(String requestMethod, String urlParameters) throws IOException {
        HashMap<String, String> map = new HashMap<String,String>();

        String url = "http://localhost:8080/" + urlParameters;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod(requestMethod);
        con.setRequestProperty("Content-Type", "application/json");

        // Send post request
        if(requestMethod.equals("POST")) {
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
        }



        int responseCode = con.getResponseCode();
        System.out.println("\nSending " + requestMethod + " request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        map.put("response",response.toString());
        map.put("responseCode",responseCode+"");
        return map;
    }
}
