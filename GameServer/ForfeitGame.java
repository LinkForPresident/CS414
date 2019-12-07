package GameServer;

import java.time.format.DateTimeFormatter;
import java.time.*;
import GameLogic.*;
import com.google.gson.*;

public class ForfeitGame extends Action {

    @Override
    Response executeAction(Request request) {
        Terminal.printInfo(String.format("User '%s' is attempting to forfeit the game: '%s'.", request.body.get("username"), request.body.get("gameID")));
        String body = forfeitGame(request.body.get("username"), request.body.get("gameID"));
        return new Response(body);
    }

    private static String forfeitGame(String username, String gameID){
        // handle a POST request to forfeit this game instance.
        Terminal.printInfo(String.format("Attempting to forfeit game: '%s' for player: '%s'", username, gameID));
        String JSONResponse = "";
        for(Game game : Server.activeGames){
            if(game.gameID.equals(gameID)){
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
                game.endTime = dtf.format(LocalDateTime.now()).toString();
                game.winner = game.playerOne.equals(username) ? game.playerTwo: game.playerOne;

                String gameJSON = Database.formatGameGSON(game);
                Terminal.printDebug(String.format("The JSON game object is: %s", gameJSON));
                String saveGame = String.format("UPDATE Game SET gameJSON = '%s' WHERE gameID = %d;", gameJSON, Integer.parseInt(gameID));
                Database.executeDatabaseQuery(saveGame);
                JSONResponse = Response.formatGameResponse(game);
                break;
            }
        }

        return JSONResponse;
    }

}
