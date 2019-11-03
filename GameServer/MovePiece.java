package GameServer;

import GameLogic.*;
import com.google.gson.*;

public class MovePiece extends Action {

    @Override
    Response executeAction(Request request) {
        Terminal.printInfo(String.format("User '%s' is attempting to move a piece.", request.body.get("username")));
        String body = movePiece(request.body.get("gameID"), request.body.get("username"),
                request.body.get("row"), request.body.get("column"));
       // if (JSONResponse.length() != 0) {
            //sendJSONReponse(JSONResponse);
        //}
        return new Response(body);
    }

    private static String movePiece(String gameID, String playerID, String row, String column){
        // handle a POST request to move a piece in a game instance.
        Terminal.printInfo(String.format("Attempting to move piece at row: '%s', column: '%s', for player: '%s' and " +
                "game: '%s'.", row, column, playerID, gameID));
        String JSONResponse = "";
        for(Game game : Server.activeGames){
            if(game.gameID.equals(gameID)){
               int oldTurn = game.turnNumber;
               if(game.sendInput(playerID, Integer.parseInt(row), Integer.parseInt(column))){
                   if(game.turnNumber != oldTurn) {
                       Gson gson = new GsonBuilder().create();
                       String json = gson.toJson(game);
                       json = json.replace("}", "#");
                       json = json.replace("{", "%");
                       json = json.replace("]", "&");
                       json = json.replace("[", "^");
                       Terminal.printDebug(String.format("The JSON game object is: %s", json));

                       String saveGame = String.format("UPDATE Game SET gameJSON = '%s' WHERE gameID = %d;", json, Integer.parseInt(gameID));
                       Database.executeDatabaseQuery(saveGame);
                   }
                }
                JSONResponse = Response.formatGameResponse(game);
            }
        }

        return JSONResponse;
    }

}
