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
    String newlyCreatedUser = "";
    boolean loggedIn = false;

    public void sendInvite(String playerTwo) throws IOException {
        HashMap<String, String> map = serverUtils.sendHttpRequest("POST","action=SendInvite&playerOne=the_devil_himself&playerTwo=" + playerTwo + "&username=dummy_user&password=iforgot123");

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200
    }

    @Test
    public void test1LoginWithHardcodedValue() throws Exception {
        String username = "the_devil_himself", password = "666";
        HashMap<String, String> map = serverUtils.sendHttpRequest("POST","action=Login&username=" + username + "&password=" + password);
        username = "dummy_user";
        password = "iforgot123";
        map = serverUtils.sendHttpRequest("POST","action=Login&username=" + username + "&password=" + password);

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200

        String response = map.get("response");
        System.out.println("Received Response from Server for view game: " + response);

        HashMap<String, String> jsonMap = serverUtils.convertJsonStringToMap(response);
        Assert.assertEquals(jsonMap.get("loggedIn"), "true");
        Assert.assertEquals(jsonMap.get("username"), username);

        loggedIn = true;
    }

    @Test
    public void test1RegisterUserAndLogin() throws Exception {
        String username = "new_test_user"+(Math.random()+"").substring(2,8), password = "123";
        newlyCreatedUser = username;
        HashMap<String, String> map = serverUtils.sendHttpRequest("POST","action=Register&username=" + username + "&password=" + password);

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200

        String response = map.get("response");
        System.out.println("Received Response from Server for view game: " + response);

        HashMap<String, String> jsonMap = serverUtils.convertJsonStringToMap(response);
        Assert.assertEquals(jsonMap.get("loggedIn"), "false");

        map = serverUtils.sendHttpRequest("POST","action=Login&username=" + username + "&password=" + password);
        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200

        response = map.get("response");
        System.out.println("Received Response from Server for view game: " + response);

        jsonMap = serverUtils.convertJsonStringToMap(response);
        Assert.assertEquals(jsonMap.get("loggedIn"), "true");
        Assert.assertEquals(jsonMap.get("username"), username);
    }

    @Test
    public void test2BasicViewGame() throws Exception {
        test1LoginWithHardcodedValue(); //Log the user in
        System.out.println("Testing GET on view_game API of Server! Should return a json showing the requested game!");

        HashMap<String, String> map = serverUtils.sendHttpRequest("POST","action=ViewGame&gameID=1234&username=the_devil_himself&password=666");

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
    public void test2ViewUserGamesAPI() throws Exception {
        test1LoginWithHardcodedValue(); //Log the user in
        System.out.println("Testing GET on view_game API of Server! Should return a json showing the requested game!");

        HashMap<String, String> map = serverUtils.sendHttpRequest("POST","action=ViewUserGames&username=the_devil_himself&password=666");

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200
        String response = map.get("response");
        System.out.println("Received Response from Server for view user games: " + response);
        HashMap<String, String> jsonMap = serverUtils.convertJsonStringToMap(response);
        Assert.assertTrue(map.get("response").contains("1234"));
        Assert.assertTrue(map.get("response").contains("2345"));
        Assert.assertEquals(jsonMap.get("activeGames"), "1234,2345");
        Assert.assertEquals(jsonMap.get("username"), "the_devil_himself");
    }

    @Test
    public void test2SendInviteToUser() throws Exception {
        System.out.println("Testing GET on view_game API of Server! Should return a json showing the requested game!");

        HashMap<String, String> map = serverUtils.sendHttpRequest("POST","action=SendInvite&playerOne=the_devil_himself&playerTwo=dummy_user&username=dummy_user&password=iforgot123");

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200
        String response = map.get("response");
        System.out.println("Received Response from Server for SendInvite: " + response);

        HashMap<String, String> jsonMap = serverUtils.convertJsonStringToMap(response);
        Assert.assertEquals(jsonMap.get("wasSuccessful"), "true");
        Assert.assertEquals(jsonMap.get("invitedPlayer"), "dummy_user");

        //Now we send an identical invite API call and wasSuccessful should be false since there already is an existing invite
        map = serverUtils.sendHttpRequest("POST","action=SendInvite&playerOne=the_devil_himself&playerTwo=dummy_user&username=dummy_user&password=iforgot123");

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200
        response = map.get("response");
        System.out.println("Received Response from Server for SendInvite: " + response);

        jsonMap = serverUtils.convertJsonStringToMap(response);
        Assert.assertEquals(jsonMap.get("wasSuccessful"), "false");
        Assert.assertEquals(jsonMap.get("invitedPlayer"), "dummy_user");
    }

    @Test
    public void test3AcceptUserInvitation() throws Exception {
        if(!loggedIn) {
            test1LoginWithHardcodedValue();
        }
        sendInvite("dummy_user");
        System.out.println("Testing GET on view_game API of Server! Should return a json showing the requested game!");

        HashMap<String, String> map = serverUtils.sendHttpRequest("POST", "action=AcceptInvite&playerOne=the_devil_himself&playerTwo=dummy_user&username=dummy_user&password=iforgot123");

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200
        String response = map.get("response");
        System.out.println("Received Response from Server for SendInvite: " + response);

        HashMap<String, String> jsonMap = serverUtils.convertJsonStringToMap(response);
        Assert.assertEquals(jsonMap.get("invites"), ""); //After accepting an invite there should be an empty invites array
        Assert.assertEquals(jsonMap.get("wasSuccessful"), "true");
        Assert.assertTrue(jsonMap.containsKey("gameID"));
        String newGameId = jsonMap.get("gameID");
        //Now check gamesList and make sure new game is added
        map = serverUtils.sendHttpRequest("POST","action=ViewUserGames&username=the_devil_himself&password=666");
        response = map.get("response");
        jsonMap = serverUtils.convertJsonStringToMap(response);
        Assert.assertTrue(jsonMap.get("activeGames").contains(newGameId));
    }

    @Test
    public void test3DeclineUserInvitation() throws Exception {
        sendInvite("dummy_user");
        System.out.println("Testing GET on view_game API of Server! Should return a json showing the requested game!");

        HashMap<String, String> map = serverUtils.sendHttpRequest("POST", "action=AcceptInvite&playerOne=the_devil_himself&playerTwo=dummy_user&username=dummy_user&password=iforgot123");

        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200
        String response = map.get("response");
        System.out.println("Received Response from Server for SendInvite: " + response);

        HashMap<String, String> jsonMap = serverUtils.convertJsonStringToMap(response);
        Assert.assertEquals(jsonMap.get("invites"), ""); //After accepting an invite there should be an empty invites array
    }

        @Test
    public void test4InvokeMovePieceWithInvalidDestinationSquare() throws Exception {
        HashMap<String, String> map = serverUtils.sendHttpRequest("POST","action=MovePiece&gameID=1234&username=dummy_user&password=iforgot123&row=8&column=0"); //Select Blue Lion
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

        map = serverUtils.sendHttpRequest("POST","action=MovePiece&gameID=1234&username=dummy_user&password=iforgot123&row=8&column=5"); //Select Blue Lion Destination Square
        response = map.get("response");
        System.out.println("Received Response from Server for view game: " + response);
        Assert.assertEquals(map.get("responseCode"), "200"); //Assert that we get a Http Response code 200

        jsonMap = serverUtils.convertJsonStringToMap(response);
        board = serverUtils.convertBoardStringToBoardArray(jsonMap.get("boardString"));
        System.out.println(Arrays.deepToString(board).replaceAll("], ","\n"));
        Assert.assertEquals(jsonMap.get("gameID"), "1234");
        Assert.assertEquals(serverUtils.getPieceCoordinates(board,"b6"),"8,0");
        Assert.assertTrue(serverUtils.isCoordinateAvailable(board,7,0));
        Assert.assertTrue(serverUtils.isCoordinateAvailable(board,8,1)); //Pieces should not have moved from their original square
    }

    @Test
    public void test5BasicMovePieceAndCheckJSONResponse() throws Exception {
        HashMap<String, String> map = serverUtils.sendHttpRequest("POST","action=MovePiece&gameID=1234&username=dummy_user&password=iforgot123&row=8&column=0"); //Select Blue Lion
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

        map = serverUtils.sendHttpRequest("POST","action=MovePiece&gameID=1234&username=dummy_user&password=iforgot123&row=8&column=1"); //Select Blue Lion Destination Square
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


