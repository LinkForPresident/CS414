package GameLogic;

import java.util.Arrays;

public class Move {

    private Game game;
    public int numberOfValidTiles = 0;
    public BoardSquare selectedSquare;

    Move(Game current_game){
        game = current_game;
    }

    void printValidTiles(){
        for(int row=0; row<9; row++) {
            for (int col = 0; col < 7; col++) {
                if(game.board[row][col].isValid)
                    System.out.print(" * ");
                else System.out.print(" _ ");
            }
            System.out.println();
        }
    }

    void updateValidTiles(){

        numberOfValidTiles = 0;

        if(selectedSquare != null){
            if(isFriendlyUnit(selectedSquare.row, selectedSquare.col)) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 7; j++) {
                        game.board[i][j].isValid = false;
                    }
                }

                if (selectedSquare.col <= 5) handleUpdate(selectedSquare.row, selectedSquare.col + 1);
                if (selectedSquare.col >= 1) handleUpdate(selectedSquare.row, selectedSquare.col - 1);
                if (selectedSquare.row <= 7) handleUpdate(selectedSquare.row + 1, selectedSquare.col);
                if (selectedSquare.row >= 1) handleUpdate(selectedSquare.row - 1, selectedSquare.col);
            }
        } else {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 7; j++) {
                    game.board[i][j].isValid = false;
                    if(game.board[i][j].gamePiece != null){
                        game.board[i][j].isValid = game.board[i][j].gamePiece.color.equals(this.game.turn);
                    }
                }
            }
        }
    }


    private void handleUpdate(int row, int col) {
        // checks tile conditions and puts a "*" in the corresponding location of validTiles if the tile can be moved to from location (selectedRow, selectedCol)
        if (!isFriendlyUnit(row, col)) { // if there is no friendly unit occupying the tile
            if (!isWater(row, col) || isRat(selectedSquare.row, selectedSquare.col)) { // if there is no water (unless the unit being moved is a rat)
                if(!isEnemyUnit(row, col) || canCaptureUnit(row, col)) { // if there is an enemy unit that the unit being moved can capture, or there is no enemy
                    game.board[row][col].isValid = true;
                    numberOfValidTiles++;
                }
            } else if (isLionOrTiger(selectedSquare.row, selectedSquare.col)) { // if the unit being moved is a lion or tiger
                handleLionTigerMovement(row, col);
            }
        }
    }

    private void handleLionTigerMovement(int row, int col){

        int rowOffset = row - selectedSquare.row;
        int colOffset = col - selectedSquare.col;

        if(colOffset != 0){ // horizontal leap
            if(!isRat(selectedSquare.row, selectedSquare.col+colOffset) && !isRat(selectedSquare.row, selectedSquare.col+(colOffset*2)) && !isRat(selectedSquare.row, selectedSquare.col+(colOffset*3))){
                if((isEnemyUnit(selectedSquare.row, selectedSquare.col+(colOffset*3)) && canCaptureUnit(selectedSquare.row, selectedSquare.col+(colOffset*3))) || // if there is an enemy unit that can be captured
                        (!isEnemyUnit(selectedSquare.row, selectedSquare.col+(colOffset*3)) && !isFriendlyUnit(selectedSquare.row, selectedSquare.col+(colOffset*3)))) { // if there is no enemy or friendly units
                    game.board[selectedSquare.row][selectedSquare.col+(colOffset*3)].isValid = true;
                    numberOfValidTiles++;
                }
            }
        }

        if(rowOffset != 0){ // vertical leap
            if(!isRat(selectedSquare.row+rowOffset, selectedSquare.col) && !isRat(selectedSquare.row+(rowOffset*2), selectedSquare.col) && !isRat(selectedSquare.row+(rowOffset*3), selectedSquare.col)){
                if((isEnemyUnit(selectedSquare.row+(rowOffset*4), selectedSquare.col) && canCaptureUnit(selectedSquare.row+(rowOffset*4), selectedSquare.col)) || // if there is an enemy unit that can be captured
                        (!isEnemyUnit(selectedSquare.row+(rowOffset*4), selectedSquare.col) && !isFriendlyUnit(selectedSquare.row+(rowOffset*4), selectedSquare.col))) { // if there is no enemy or friendly units
                    game.board[selectedSquare.row+(rowOffset*4)][selectedSquare.col].isValid = true;
                    numberOfValidTiles++;
                }
            }
        }
    }

    boolean isFriendlyUnit(int row, int col){
        // returns true if the game piece at row, col belongs to the current player, else returns false.
        if(game.board[row][col].gamePiece != null && selectedSquare.gamePiece != null){ // if there is a unit in the square at row, col
            if(game.board[row][col].gamePiece.color == selectedSquare.gamePiece.color){ // if the unit at square row, col is an enemy
                return true;
            } else return false;
        } else return false;
    }

    private boolean isEnemyUnit(int row, int col){
        // returns true if the game piece at row, col belongs to the opponent of the current player, else returns false.
        if(game.board[row][col].gamePiece != null && selectedSquare.gamePiece != null){ // if there is a unit in the square at row, col
            if(game.board[row][col].gamePiece.color != selectedSquare.gamePiece.color){ // if the unit at square row, col is an enemy
                return true;
            } else return false;
        } else return false;
    }

    private boolean canCaptureUnit(int row, int col) {
        // returns true if the piece at row, col has a lesser or equal power (or row, col is a trap) to the piece at selectedRow, SelectedCol
        return ((selectedSquare.gamePiece.power >= game.board[row][col].gamePiece.power) || isFriendlyTrap(row, col) || isRat(selectedSquare.row, selectedSquare.col) && isElephant(row, col))
                && (!game.board[row][col].environment.equals("water") || isRat(selectedSquare.row, selectedSquare.col))
                && !isEnemyTrap(selectedSquare.row, selectedSquare.col);
    }

    private boolean isLionOrTiger(int row, int col){
        // returns true if the piece at row, col is a lion or a tiger, else returns false.
        if(game.board[row][col].gamePiece != null){
            return game.board[row][col].gamePiece.type.equals("lion") || game.board[row][col].gamePiece.type.equals("tiger");
        }
        return false;
    }

    private boolean isRat(int row, int col){
        // returns true if the piece at row, col is a rat, else returns false.
        if(game.board[row][col].gamePiece != null){
            return game.board[row][col].gamePiece.type.equals("rat");
        }
        return false;
    }

    private boolean isElephant(int row, int col){
        // returns true if the piece at row, col is a rat, else returns false.
        if(game.board[row][col].gamePiece != null){
            return game.board[row][col].gamePiece.type.equals("elephant");
        }
        return false;
    }

    private boolean isWater(int row, int col){
        // returns true if the tile at row, col contains water, else returns false.
        return game.board[row][col].environment.equals("water");
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
