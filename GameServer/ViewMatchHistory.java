package GameServer;

import GameLogic.Game;

import java.util.ArrayList;

public class ViewMatchHistory extends Action {

    @Override
    Response executeAction(Request request) {
        String body = viewMatchHistory(request.body.get("username"));
        return new Response(body);
    }

    private static String viewMatchHistory(String username){
        Terminal.printInfo(String.format("Attempting to service request to view the match history of user: '%s'.", username));
        String JSONResponse = "";
        ArrayList<String>  gameIdList = new ArrayList<String>();
        for(Game game : Server.activeGames){
            if((game.playerOne.equals(username) || game.playerTwo.equals(username)) && (game.winner != "")){
                gameIdList.add(game.gameID);
            }
        }
        return Response.formatViewUserGamesResponse(username, gameIdList);
    }
}
