
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
        // returns true if the move being attempted is valid
        if(isTurn(player) && isFriendlyUnit(row1, col1)){
            if(moveIsOneTileAway() && (!isWater(row2, col2) || isRat(row1, col1))) {
                    return true;
            }
            if(isLionOrTiger(row1,col1)) return handleLionTigerMovement();
        }
        return false;
    }

    private boolean handleLionTigerMovement(){
        return true;
    }

    private boolean moveIsOneTileAway(){
        // returns true if the move being attempted moves the selected piece one tile horizontally or vertically, else returns false.
        int diff = (row1 - row2) + (col1-col2);
        return (diff == -1 || diff == 1);
    }

    private boolean isTurn(String player){
        // returns true if it is the turn of the player submitting the move, else returns false.
        return (game.turn.equals("blue") && player.equals(game.player_1)) || (game.turn.equals("red") && player.equals(game.player_2));
    }

    private boolean isLionOrTiger(int row, int col){
        // returns true if the piece at row, col is a lion or a tiger, else returns false.
        return game.board[row1][col1].charAt(1) == '7' || game.board[row1][col1].charAt(1) == '6';
    }

    private boolean isRat(int row, int col){
        // returns true if the piece at row, col is a rat, else returns false.
        return game.board[row1][col1].charAt(1) == '1';
    }

    private boolean isFriendlyUnit(int row, int col){
        // returns true if the game piece at row, col belongs to the current player, else returns false.
        char friendly = game.turn.charAt(0); //b if turn is blue, r if turn is red
        return game.board[row][col].charAt(0) == friendly;
    }

    private boolean isEnemyUnit(int row, int col){
        // returns true if the game piece at row, col belongs to the opponent of the current player, else returns false.
        char friendly = game.turn.charAt(0);
        char enemy = ' ';
        if(friendly == 'b') enemy = 'r';
        if(friendly == 'r') enemy = 'b';
        return game.board[row][col].charAt(0) == enemy;
    }

    private boolean isWater(int row, int col){
        // returns true if the tile at row, col contains water, else returns false.
        return (row == 3 || row == 4 || row == 5) && (col == 1 || col == 2 || col == 4 || col == 5);
    }

    private boolean isTrap(int row, int col){
        // returns true if the tile at row, col contains a trap, else returns false.
        return (((row == 0 || row == 8) && (col == 2 || col == 4)) || ((row == 1 || row == 7) && (col == 3)));
    }
}
