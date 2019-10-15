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
import java.util.Arrays;
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
        HashMap<String, String> map = serverUtils.sendHttpRequest("POST","action=login&username=" + username + "&password=" + password);

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200
    }

    @Test
    public void test2BasicViewGame() throws Exception {
        System.out.println("Testing GET on view_game API of Server! Should return a json showing the requested game!");

        HashMap<String, String> map = serverUtils.sendHttpRequest("GET","action=view_game&gameID=1234");

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200
        String response = map.get("response");
        System.out.println("Received Response from Server for view game: " + response);
        //Assert.assertTrue(map.get("response").contains(""));

        HashMap<String, String> jsonMap = serverUtils.convertJsonStringToMap(response);
        String[][] board = serverUtils.convertBoardStringToBoardArray(jsonMap.get("boardString"));
        System.out.println(Arrays.deepToString(board).replaceAll("], ","\n"));
        Assert.assertEquals(jsonMap.get("gameID"), "1234");
        Assert.assertEquals(serverUtils.getPieceCoordinates(board,"r7"),"0,0");
        Assert.assertEquals(serverUtils.getPieceCoordinates(board,"r6"),"0,6");
        Assert.assertEquals(serverUtils.getPieceCoordinates(board,"r4"),"1,1");
        Assert.assertEquals(serverUtils.getPieceCoordinates(board,"r2"),"1,5");
        Assert.assertEquals(serverUtils.getPieceCoordinates(board,"b6"),"8,0");
        Assert.assertEquals(serverUtils.getPieceCoordinates(board,"b7"),"8,6");
    }

    @Test
    public void test3BasicMovePieceAndCheckJSONResponse() throws Exception {
        System.out.println("Testing Server POST method!");

        HashMap<String, String> map = serverUtils.sendHttpRequest("POST","action=move_piece&gameID=1234&username=dummy_user&row=8&column=0"); //Select Blue Lion
        String response = map.get("response");
        System.out.println("Received Response from Server for view game: " + response);

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200

        HashMap<String, String> jsonMap = serverUtils.convertJsonStringToMap(response);
        String[][] board = serverUtils.convertBoardStringToBoardArray(jsonMap.get("boardString"));
        System.out.println(Arrays.deepToString(board).replaceAll("], ","\n"));
        Assert.assertEquals(jsonMap.get("gameID"), "1234");
        Assert.assertEquals(serverUtils.getPieceCoordinates(board,"b6"),"8,0");
        Assert.assertTrue(serverUtils.isCoordinateAvailable(board,7,0));
        Assert.assertTrue(serverUtils.isCoordinateAvailable(board,8,1));

        map = serverUtils.sendHttpRequest("POST","action=move_piece&gameID=1234&username=dummy_user&row=8&column=1"); //Select Blue Lion Destination Square
        response = map.get("response");
        System.out.println("Received Response from Server for view game: " + response);
        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200

        jsonMap = serverUtils.convertJsonStringToMap(response);
        board = serverUtils.convertBoardStringToBoardArray(jsonMap.get("boardString"));
        System.out.println(Arrays.deepToString(board).replaceAll("], ","\n"));
        Assert.assertEquals(serverUtils.getPieceCoordinates(board,"b6"),"8,1");
        Assert.assertFalse(serverUtils.isCoordinateAvailable(board,7,0));
        Assert.assertFalse(serverUtils.isCoordinateAvailable(board,8,1));
    }



}


