
public class Move {

    Game game;
    String player;
    int row1;
    int col1;
    int row2;
    int col2;

    public Move(Game current_game, String player_name, int x1, int y1, int x2, int y2){
        game = current_game;
        player = player_name;
        row1 = x1;
        col1 = y1;
        row2 = x2;
        col2 = y2;
    }

    public boolean isValidMove(){
        if(isTurn(player) && isPlayersPiece(player, row1, col1)) return true;
        return false;
    }

    private boolean isTurn(String player){
        return (game.turn.equals("blue") && player.equals(game.player_1)) || (game.turn.equals("red") && player.equals(game.player_2));
    }

    private boolean isPlayersPiece(String player, int row, int col){
        char id = ' ';
        if(player.equals(game.player_1)) id = 'b';
        if(player.equals(game.player_2)) id = 'r';
        return game.board[row][col].charAt(0) == id;
    }
}
