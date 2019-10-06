package GameServer.test;


import GameServer.Server;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerTest {

    //testing GET and POST methods from Server class - make sure to have Server running before test
    //Work in progress!
    ServerUtils serverUtils = new ServerUtils();


    @Test
    public void test1LoginWithHardcodedValue() throws Exception {
        System.out.println("Testing Server POST method!");

        String username = "dummy_user", password = "iforgot123";
        HashMap<String, String> map = sendPOSTRequest("action=login&username=" + username + "&password=" + password);

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200
    }

    @Test
    public void test2BasicViewGame() throws Exception {
        System.out.println("Testing Server POST method!");

        HashMap<String, String> map = sendPOSTRequest("action=view_game&gameID=1234");

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200
        String response = map.get("response");
        System.out.println("Received Response from Server for view game: " + response);
        //Assert.assertTrue(map.get("response").contains(""));

        HashMap<String, String> jsonMap = serverUtils.convertJsonStringToMap(response);
        Assert.assertEquals(jsonMap.get("gameID"), "1234");
        Assert.assertEquals(jsonMap.get("board"), "r7,__,__,__,__,__,r6,|__,r4,__,__,__,r2,__,|r1,__,r5,__,r3,__,r8,|__,__,__,__,__,__,__,|__,__,__,__,__,__,__,|__,__,__,__,__,__,__,|b8,__,b3,__,b5,__,b1,|__,b2,__,__,__,b4,__,|b6,__,__,__,__,__,b7,|");
    }

    @Test
    public void test3BasicMovePieceAndCheckJSONResponse() throws Exception {
        System.out.println("Testing Server POST method!");

        HashMap<String, String> map = sendPOSTRequest("action=move_piece&gameID=1234&username=dummy_user&row=8&column=0"); //Select Blue Lion
        String response = map.get("response");
        System.out.println("Received Response from Server for view game: " + response);

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200

        HashMap<String, String> jsonMap = serverUtils.convertJsonStringToMap(response);
        Assert.assertEquals(jsonMap.get("availableMoves"),"_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|*,_,_,_,_,_,_,|_,*,_,_,_,_,_,|");

        map = sendPOSTRequest("action=move_piece&gameID=1234&username=dummy_user&row=7&column=0"); //Select Blue Lion Destination Sqaure
        response = map.get("response");
        System.out.println("Received Response from Server for view game: " + response);

        Assert.assertEquals(map.get("responseCode"), "200");
        jsonMap = serverUtils.convertJsonStringToMap(response);
        Assert.assertEquals(jsonMap.get("availableMoves"),"_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|_,_,_,_,_,_,_,|");
        Assert.assertEquals(jsonMap.get("board"),"r7,__,__,__,__,__,r6,|__,r4,__,__,__,r2,__,|r1,__,r5,__,r3,__,r8,|__,__,__,__,__,__,__,|__,__,__,__,__,__,__,|__,__,__,__,__,__,__,|b8,__,b3,__,b5,__,b1,|b6,b2,__,__,__,b4,__,|__,__,__,__,__,__,b7,|"); //Blue Lion should have moved to 7,0!
    }


    public HashMap<String, String> sendPOSTRequest(String urlParameters) throws IOException {
        HashMap<String, String> map = new HashMap<String,String>();

        String url = "http://localhost:8080/";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
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


