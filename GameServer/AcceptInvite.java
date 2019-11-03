package GameServer;

import GameLogic.Game;
import GameLogic.exception.PlayerNameException;
import java.sql.*;
import java.util.Iterator;
import com.google.gson.*;

public class AcceptInvite extends Action {

    @Override
    Response executeAction(Request request) {
        String body = acceptInvite(request.body.get("playerOne"), request.body.get("playerTwo"));
        return new Response(body);
    }

    private static String acceptInvite(String playerOne, String playerTwo) {
        Terminal.printInfo(String.format("Attempting to accept the invite of %s to %s", playerOne, playerTwo));
        String JSONResponse = "{";
        Iterator<String[]> invitesIterator = Server.invites.iterator();
        while(invitesIterator.hasNext()) {
            String[] invite = invitesIterator.next();
            if (invite[0].equals(playerOne) && invite[1].equals(playerTwo)) {
				String acceptInvite = String.format("DELETE FROM Invite WHERE playerOne='%s' AND playerTwo='%s';", playerOne, playerTwo);
				Database.executeDatabaseQuery(acceptInvite);
                invitesIterator.remove();
                try {
                    String findMaxGameID = "SELECT MAX(gameID) FROM Game;";
                    ResultSet resultSet = Database.executeDatabaseQuery(findMaxGameID);
                    String gameID = "";
                    if(resultSet.next()){
                        gameID = resultSet.getString(1);
                    }
                    if(gameID == null){
                        gameID = "0";
                    }else{
                        gameID = Integer.toString(Integer.parseInt(gameID) + 1);
                    }
                    Terminal.printDebug(String.format("Game ID is: %s", gameID));
                    Game game = new Game(playerOne, playerTwo, gameID);
                    Gson gson = new GsonBuilder().create();
                    String json = gson.toJson(game);
                    json = json.replace("}", "#");
                    json = json.replace("{", "%");
                    json = json.replace("]", "&");
                    json = json.replace("[", "^");
                    Terminal.printDebug(String.format("The JSON game object is: %s", json));

                    String saveGame = String.format("INSERT INTO Game VALUES(%d, '%s');", Integer.parseInt(gameID), json);
                    Database.executeDatabaseQuery(saveGame);

                    Server.activeGames.add(game);
                    JSONResponse += "\"gameID\":\"" + game.gameID +"\", ";
                } catch (PlayerNameException | SQLException e) {
                    System.out.println(e);
                }
                String playerInvites = "";
                for (String[] inv : Server.invites) {
                    if (inv[1].equals(playerTwo)) {
                        playerInvites += inv[0] + ",";
                    }
                }
                JSONResponse += String.format("\"wasSuccessful\":\"true\", \"invites\": \"%s\"", playerInvites);
            }
        }
        return JSONResponse +"}";
    }

}
