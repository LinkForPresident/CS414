package GameLogic;

import java.util.Arrays;

public class Move {

    private Game game;
    int selectedRow = -1;
    int selectedCol = -1;
    public String[][] validTiles = new String[9][7];
    public int numberOfValidTiles = 0;

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
        numberOfValidTiles = 0;
        for(int i=0; i<9; i++) {
            for (int j = 0; j < 7; j++) {
                validTiles[i][j] = "_";
            }
        }
        if(selectedRow != -1 && selectedCol != -1) {
            if(selectedCol <= 5) handleUpdate(selectedRow, selectedCol+1);
            if(selectedCol >= 1) handleUpdate(selectedRow, selectedCol-1);
            if(selectedRow <= 7) handleUpdate(selectedRow+1, selectedCol);
            if(selectedRow >= 1) handleUpdate(selectedRow-1, selectedCol);
        }
    }

    private void handleUpdate(int row, int col) {
        // checks tile conditions and puts a "*" in the corresponding location of validTiles if the tile can be moved to from location (selectedRow, selectedCol)
        if (!isFriendlyUnit(row, col)) { // if there is no friendly unit occupying the tile
            if (!isWater(row, col) || isRat(selectedRow, selectedCol)) { // if there is no water (unless the unit being moved is a rat)
                if((isEnemyUnit(row, col) && canCaptureUnit(row, col)) || !isEnemyUnit(row, col)) { // if there is an enemy unit that the unit being moved can capture, or there is no enemy
                    validTiles[row][col] = "*";
                    numberOfValidTiles++;
                }
            } else if (isLionOrTiger(selectedRow, selectedCol)) { // if the unit being moved is a lion or tiger
                handleLionTigerMovement(row, col);
            }
        }
    }

    private void handleLionTigerMovement(int row, int col){

        int rowOffset = row - selectedRow;
        int colOffset = col - selectedCol;

        if(colOffset != 0){ // horizontal leap
            if(!isRat(selectedRow, selectedCol+colOffset) && !isRat(selectedRow, selectedCol+(colOffset*2)) && !isRat(selectedRow, selectedCol+(colOffset*3))){
                if((isEnemyUnit(selectedRow, selectedCol+(colOffset*3)) && canCaptureUnit(selectedRow, selectedCol+(colOffset*3))) || // if there is an enemy unit that can be captured
                        (!isEnemyUnit(selectedRow, selectedCol+(colOffset*3)) && !isFriendlyUnit(selectedRow, selectedCol+(colOffset*3)))) { // if there is no enemy or friendly units
                    validTiles[selectedRow][selectedCol+(colOffset*3)] = "*";
                    numberOfValidTiles++;
                }
            }
        }

        if(rowOffset != 0){ // vertical leap
            if(!isRat(selectedRow+rowOffset, selectedCol) && !isRat(selectedRow+(rowOffset*2), selectedCol) && !isRat(selectedRow+(rowOffset*3), selectedCol)){
                if((isEnemyUnit(selectedRow+(rowOffset*4), selectedCol) && canCaptureUnit(selectedRow+(rowOffset*4), selectedCol)) || // if there is an enemy unit that can be captured
                        (!isEnemyUnit(selectedRow+(rowOffset*4), selectedCol) && !isFriendlyUnit(selectedRow+(rowOffset*4), selectedCol))) { // if there is no enemy or friendly units
                    validTiles[selectedRow+(rowOffset*4)][selectedCol] = "*";
                    numberOfValidTiles++;
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

    private boolean canCaptureUnit(int row, int col) {
        // returns true if the piece at row, col has a lesser or equal power (or row, col is a trap) to the piece at selectedRow, SelectedCol
        return ((game.board[selectedRow][selectedCol].charAt(1) >= game.board[row][col].charAt(1) || isFriendlyTrap(row, col) || (isRat(row, col) && isElephant(selectedRow, selectedCol)))
                && (!isWater(selectedRow, selectedCol) || isRat(row, col))
                && !isEnemyTrap(selectedRow, selectedCol));
    }

    private boolean isLionOrTiger(int row, int col){
        // returns true if the piece at row, col is a lion or a tiger, else returns false.
        return game.board[row][col].charAt(1) == '7' || game.board[row][col].charAt(1) == '6';
    }

    private boolean isRat(int row, int col){
        // returns true if the piece at row, col is a rat, else returns false.
        return game.board[row][col].charAt(1) == '1';
    }

    private boolean isElephant(int row, int col){
        // returns true if the piece at row, col is a rat, else returns false.
        return game.board[row][col].charAt(1) == '8';
    }

    private boolean isWater(int row, int col){
        // returns true if the tile at row, col contains water, else returns false.
        return (row == 3 || row == 4 || row == 5) && (col == 1 || col == 2 || col == 4 || col == 5);
    }

    private boolean isFriendlyTrap(int row, int col){
        // returns true if the tile at row, col contains a friendly trap, else returns false.
        if(game.turn.equals("blue")){
            return (row == 8 && (col == 2 || col == 4)) || (row == 7 && col == 3);
        }
        else
            return (row == 0 && (col == 2 || col == 4) || (row == 1 && col == 3));
    }

    private boolean isEnemyTrap(int row, int col){
        // returns true if the tile at row, col contains an enemy trap, else returns false.
        if(game.turn.equals("red")){
            return (row == 8 && (col == 2 || col == 4)) || (row == 7 && col == 3);
        }
        else
            return (row == 0 && (col == 2 || col == 4) || (row == 1 && col == 3));
    }
}
