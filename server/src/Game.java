package src;

import src.exception.PlayerNameException;

import java.util.Arrays;
import java.lang.*;

public class Game {

    public String playerOne;
    public String playerTwo;
    public String turn;
    public String[][] board = new String[9][7];
    public Move move = new Move(this);
    public String winner = "";

    GameUtils gameUtils = new GameUtils();

    public Game(String playerOneName, String playerTwoName) throws PlayerNameException{

        if(playerOneName.isEmpty()) {
            throw new PlayerNameException("Player One Name must not be empty!");
        }
        else if(playerTwoName.isEmpty()) {
            throw new PlayerNameException("Player Two Name must not be empty!");
        }
        else if(playerOneName.equals(playerTwoName)) {
            throw new PlayerNameException("Player One Name cannot be same as Player Two Name!");
        }

        playerOne = playerOneName;
        playerTwo = playerTwoName;
        turn = "blue";

        for(int i=0; i<9; i++){
            for(int j=0; j<7; j++){
                board[i][j] = "__";
            }
        }

        board[0][0] = "r7"; //red team lion (power 7)
        board[0][6] = "r6"; //red team tiger (power 6)
        board[1][1] = "r4"; //red team dog (power 4)
        board[1][5] = "r2"; //red team cat (power 2)
        board[2][0] = "r1"; //red team mouse (power 1)
        board[2][2] = "r5"; //red team leopard (power 5)
        board[2][4] = "r3"; //red team wolf (power 3)
        board[2][6] = "r8"; //red team elephant (power 8)
        board[6][0] = "b8"; //blue team elephant (power 8)
        board[6][2] = "b3"; //blue team wolf (power 3)
        board[6][4] = "b5"; //blue team leopard (power 5)
        board[6][6] = "b1"; //blue team mouse (power 1)
        board[7][1] = "b2"; //blue team cat (power 2)
        board[7][5] = "b4"; //blue team dog (power 4)
        board[8][0] = "b6"; //blue team tiger (power 6)
        board[8][6] = "b7"; //blue team lion (power 7)

    }

    void printBoard(){
        // prints out the board in a readable fashion
        for(int i=0; i<9; i++) System.out.println(Arrays.toString(board[i]));
        System.out.println("");
    }


    void sendInput(String player, int row, int col){
        // send input to the game in the format (player sending move, column selected, row selected)
        if(isTurn(player)){
            if(move.selectedCol == col && move.selectedRow == row){ // if the second half of the move is the same as the first half (re-selecting the same tile)
                move.selectedCol = -1;
                move.selectedRow = -1;
            }
            else if(move.isFriendlyUnit(row, col)){ // if the player is selecting one of their own tiles
                move.selectedCol = col;
                move.selectedRow = row;
            }
            else if(move.validTiles[row][col].equals("*")) { // if row, col is a valid tile to move to
                makeMove(row, col);
            }
            move.updateValidTiles();
        }
    }

    private void makeMove(int row, int col){
        // once a valid move has been constructed, make it.
        String piece = board[row][col];
        board[row][col] = board[move.selectedRow][move.selectedCol];
        board[move.selectedRow][move.selectedCol] = "__";

        System.out.println(turn + " moved piece from " + row + "," + col
                + " to " + move.selectedRow +"," + move.selectedCol);

        if(turn.equals("blue")) turn = "red";
        else turn = "blue";

        move.selectedRow = -1;
        move.selectedCol = -1;
        move.updateValidTiles();

        checkIfWinner();

        System.out.println("");
        //printBoard();
    }

    private boolean isTurn(String player){
        // returns true if it is the turn of the player submitting the move, else returns false.
        return (turn.equals("blue") && player.equals(playerOne)) || (turn.equals("red") && player.equals(playerTwo));
    }

    private void checkIfWinner(){
        int redPieces = 0;
        int bluePieces = 0;
        for(int i=0; i<9; i++){
            for(int j=0; j<7; j++){
                if(board[i][j].charAt(0) == 'b'){
                    if(i == 0 && j == 3) winner = playerOne; // if there is a blue piece in the red team den
                    bluePieces++;
                }
                if(board[i][j].charAt(0) == 'r'){
                    if(i == 8 && j == 3) winner = playerTwo; // if there is a red piece in the blue team den
                    redPieces++;
                }
            }
        }
        if(redPieces == 0) winner = playerOne;
        if(bluePieces == 0) winner = playerTwo;
    }

    public static void main(String[] arg){
    }
}