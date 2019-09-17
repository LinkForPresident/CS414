package src;

import java.util.Arrays;

class Move {

    private Game game;
    int selected_row = -1;
    int selected_col = -1;
    String[][] validTiles = new String[9][7];


    Move(Game current_game){
        game = current_game;
        for(int i=0; i<9; i++){
            for(int j=0; j<7; j++){
                validTiles[i][j] = "_";
            }
        }
    }

    void printValidTiles(){
        for(int i=0; i<9; i++) System.out.println(Arrays.toString(validTiles[i]));
    }

    void updateValidTiles(){
        if(selected_row != -1 && selected_col != -1) {
            if(selected_col <= 5) validTiles[selected_row][selected_col + 1] = "*";
            if(selected_col >= 1) validTiles[selected_row][selected_col - 1] = "*";
            if(selected_row <= 7) validTiles[selected_row + 1][selected_col] = "*";
            if(selected_row >= 1) validTiles[selected_row - 1][selected_col] = "*";
        } else {
            for(int i=0; i<9; i++){
                for(int j=0; j<7; j++){
                    validTiles[i][j] = "_";
                }
            }
        }
    }

    boolean isFriendlyUnit(int row, int col){
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

    /*
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

    private boolean isLionOrTiger(int row, int col){
        // returns true if the piece at row, col is a lion or a tiger, else returns false.
        return game.board[row1][col1].charAt(1) == '7' || game.board[row1][col1].charAt(1) == '6';
    }

    private boolean isRat(int row, int col){
        // returns true if the piece at row, col is a rat, else returns false.
        return game.board[row1][col1].charAt(1) == '1';
    }

    private boolean isWater(int row, int col){
        // returns true if the tile at row, col contains water, else returns false.
        return (row == 3 || row == 4 || row == 5) && (col == 1 || col == 2 || col == 4 || col == 5);
    }

    private boolean isTrap(int row, int col){
        // returns true if the tile at row, col contains a trap, else returns false.
        return (((row == 0 || row == 8) && (col == 2 || col == 4)) || ((row == 1 || row == 7) && (col == 3)));
    }
     */
}
