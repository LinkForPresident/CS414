package GameServer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import GameLogic.*;
import com.google.gson.*;
import java.time.*;
import java.util.Iterator;

public class Unregister extends Action {

    @Override
    Response executeAction(Request request) {
        // handle a client requesting to register a new account.
        Terminal.printInfo(String.format("User '%s' is attempting to unregister their account.",
                request.body.get("username")));
        String username = request.body.get("username");
        String password = request.body.get("password");
        String userHash = Server.calculateUserHash(username, password);
        String body = unregisterUser(username, userHash);
        return new Response(body);
    }

    private static String unregisterUser(String username, String user_hash){
        Terminal.printInfo(String.format("Attempting to unregister user: '%s'.", username));

        for(Game game : Server.activeGames) {

            if (game.playerOne.equals(username)) {
                game.playerOne = "Unregistered user";
                game.winner = game.playerTwo;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
                game.endTime = dtf.format(LocalDateTime.now()).toString();
                String gameJSON = Database.formatGameGSON(game);
                // Terminal.printDebug(String.format("The JSON game object is: %s", gameJSON));
                String saveGame = String.format("UPDATE Game SET gameJSON = '%s' WHERE gameID = %d;", gameJSON, Integer.parseInt(game.gameID));
                Database.executeDatabaseQuery(saveGame);
            } else if (game.playerTwo.equals(username)) {
                game.playerTwo = "Unregistered user";
                game.winner = game.playerOne;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
                game.endTime = dtf.format(LocalDateTime.now()).toString();
                String gameJSON = Database.formatGameGSON(game);
                // Terminal.printDebug(String.format("The JSON game object is: %s", gameJSON));
                String saveGame = String.format("UPDATE Game SET gameJSON = '%s' WHERE gameID = %d;", gameJSON, Integer.parseInt(game.gameID));
                Database.executeDatabaseQuery(saveGame);
            }
        }
        Iterator<String[]> invitesIterator = Server.invites.iterator();
        while(invitesIterator.hasNext()) {
            String[] invite = invitesIterator.next();
            if(invite[0].equals(username) || invite[1].equals(username)){
                invitesIterator.remove();
            }
        }
        String declineInvite = String.format("DELETE FROM Invite WHERE playerOne='%s' OR playerTwo='%s';", username, username);
        Database.executeDatabaseQuery(declineInvite);

        String unregisterUser = String.format("DELETE FROM User WHERE hash_code='%s';", user_hash);
        ResultSet resultSet = Database.executeDatabaseQuery(unregisterUser);
        String checkIfUnregistered = String.format("SELECT 1 FROM User WHERE hash_code='%s';", user_hash);
        resultSet = Database.executeDatabaseQuery(checkIfUnregistered);
        String JSONResponse = String.format("{\"success\": %b}", false);
        try{
            if(resultSet.next()){
                Terminal.printError(String.format("Encountered an error while attempting to unregister user with " +
                        "username '%s'.", username));
            }
            else{
                Terminal.printSuccess(String.format("User '%s' has been unregistered", username));
                JSONResponse = String.format("{\"success\": %b}", true);
            }
        }catch(SQLException sql){
            Terminal.printError(String.format("Encountered an error while attempting to unregister user with username" +
                    " '%s'.", username));
        }
        return JSONResponse;

    }

}
