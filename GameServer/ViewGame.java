package GameServer;

import GameLogic.Game;

public class ViewGame extends Action {

    @Override
    Response executeAction(Request request) {
        String body = viewGame(request.body.get("gameID"));
        return new Response(body);
    }

    private static String viewGame(String gameID){
        Terminal.printInfo(String.format("Attempting to service request to view game: '%s'.", gameID));
        String JSONResponse = "";
        for(Game game : Server.activeGames){
            if(game.gameID.equals(gameID)){
                JSONResponse = Response.formatGameResponse(game);
            }
        }
        return JSONResponse;
    }

}
