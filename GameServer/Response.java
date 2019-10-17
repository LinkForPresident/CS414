package GameServer;

import GameLogic.BoardSquare;
import GameLogic.Game;

public class Response {

    static String defaultHeader = "HTTP/1.1 200 OK\r\nContent-Type: application/json\r\n" +
            "Access-Control-Allow-Origin: *\r\nAccess-Control-Allow-Methods: POST, GET\r\n" +
            "Access-Control-Allow-Headers: *\r\n";
    private String header = "";
    private String body = "";
    String response = "";

    Response(String body){
        this.header = defaultHeader;
        this.body = body;
        this.response = header + "\r\n" + this.body;
    }

    Response(String header, String body){
        this.header = header;
        this.body = body;
        this.response = header + "\r\n" + this.body;
    }

    static String formatGameResponse(Game game){
        String JSONResponse = "";
        String boardJSON = formatBoardArrayResponse(game.board);
        JSONResponse = String.format("{\"gameID\": \"%s\", \"playerOne\": \"%s\", \"playerTwo\": \"%s\", " +
                        "\"turn\": \"%s\", \"turnNumber\": %d , \"board\": %s, \"winner\": \"%s\", \"startTime\": \"%s\", " +
                        "\"endTime\": \"%s\"}", game.gameID, game.playerOne, game.playerTwo, game.turn, game.turnNumber,
                boardJSON, game.winner, game.startTime, game.endTime);
        return JSONResponse;
    }

    private static String formatBoardArrayResponse(BoardSquare[][] state){
        String boardJSON = "[";
        for(int i=0; i<9; i++){
            String boardRow = "[";
            for(int j=0; j<7; j++){
                BoardSquare boardSquare = state[i][j];
                String gamePieceID = null;
                if(boardSquare.gamePiece != null){
                    gamePieceID = "\"" + boardSquare.gamePiece.ID + "\"";
                }

                boardRow += String.format("{\"environment\": \"%s\", \"piece\": %s, \"available\": %b}",
                        boardSquare.environment, gamePieceID, boardSquare.isValid);
                if (j < 6) {
                    boardRow += ",";
                }
            }
            boardJSON += boardRow;
            if (i < 8) {
                boardJSON += "],";
            }
            else {
                boardJSON += "]";
            }

        }
        boardJSON += "]";
        return boardJSON;
    }

}
