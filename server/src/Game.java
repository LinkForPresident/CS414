import java.util.Arrays;
import java.lang.*;

public class Game {

    public String player_1;
    public String player_2;
    public String turn;
    public String[][] board = new String[9][7];

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

    String[][] get_board(){
        return board;
    }

    void print_board(){
        for(int i=0; i<9; i++){
            System.out.println(Arrays.toString(board[i]));
        }
    }

    void makeMove(String player, int col1, int row1, int col2, int row2){
        Move move = new Move(this, player, row1, col1, row2, col2);
        if(move.isValidMove()){
            System.out.println("Made valid move for player "+player+" moving piece " + board[row1][col1] + " from ("+col1+","+row1+") to ("+col2+","+row2+").");
            board[row2][col2] = board[row1][col1];
            board[row1][col1] = "__";
            print_board();
            if(turn.equals("red")) turn = "blue";
            if(turn.equals("blue")) turn = "red";
        }
        else System.out.println("INVALID MOVE");
    }

    public static void main(String arg[]){
    }
}