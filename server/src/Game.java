package src;

import java.util.Arrays;
import java.lang.*;

public class Game {

    public String player_1;
    public String player_2;
    public String turn;
    public String[][] board = new String[9][7];
    public Move move = new Move(this);

    public Game(String p1_name, String p2_name){

        player_1 = p1_name;
        player_2 = p2_name;
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

        board[0][3] = "rd"; //red team den
        board[8][3] = "bd"; //blue team den
    }

    void printBoard(){
        // prints out the board in a readable fashion
        for(int i=0; i<9; i++) System.out.println(Arrays.toString(board[i]));
    }

    void sendInput(String player, int col, int row){
        // send input to the game in the format (player sending move, column selected, row selected)
        if(isTurn(player)){
            if(move.isFriendlyUnit(row, col)){ // if the player is selecting one of their own tiles
                move.selectedCol = col;
                move.selectedRow = row;
            } else if(move.selectedCol == col && move.selectedRow == row){ // if the second half of the move is the same as the first half (re-selecting the same tile)
                move.selectedCol = -1;
                move.selectedRow = -1;
            } else if(move.validTiles[row][col].equals("*")){ // if row, col is a valid tile to move to
                makeMove(row, col);
            }
            move.updateValidTiles();
        }
    }

    private void makeMove(int row, int col){
        // once a valid move has been constructed, make it.
        board[row][col] = board[move.selectedRow][move.selectedCol];
        board[move.selectedRow][move.selectedCol] = "__";

        if(turn.equals("blue")) turn = "red";
        else turn = "blue";

        move.selectedRow = -1;
        move.selectedCol = -1;
        move.updateValidTiles();

        System.out.println("");
        printBoard();
    }

    private boolean isTurn(String player){
        // returns true if it is the turn of the player submitting the move, else returns false.
        return (turn.equals("blue") && player.equals(player_1)) || (turn.equals("red") && player.equals(player_2));
    }

    public static void main(String[] arg){
    }
}