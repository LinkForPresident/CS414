package GameServer.test;


import GameServer.Server;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

public class ServerTest {

    //testing GET and POST methods from Server class - make sure to have Server running before test
    //Work in progress!

    @Test
    public void testBasicViewGame() throws Exception {
        System.out.println("Testing Server POST method!");

        HashMap<String, String> map = sendPOSTRequest("action=view_game&game_id=1234");

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200

        //Assert.assertTrue(map.get("response").contains(""));
    }

    @Test
    public void testBasicMovePieceAndCheckJSONResponse() throws Exception {
        System.out.println("Testing Server POST method!");

        HashMap<String, String> map = sendPOSTRequest("action=move_piece&game_id=1234&username=dummy_user&row=0&column=0");

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200

        //Assert.assertTrue(map.get("response").contains(""));
    }

    @Test
    public void testLoginWithHardcodedValue() throws Exception {
        System.out.println("Testing Server POST method!");

        String username = "dummy_user", password = "iforgot123";
        HashMap<String, String> map = sendPOSTRequest("action=login&username=" + username + "&password=" + password);

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200

        //Assert.assertTrue(map.get("response").contains(""));
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

        //print result
        System.out.println(response.toString());
        map.put("response",response.toString());
        map.put("responseCode",responseCode+"");
        return map;
    }
}


