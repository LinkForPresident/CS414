package GameLogic;


import GameLogic.exception.PlayerNameException;

import java.time.format.DateTimeFormatter;

import java.lang.*;
import java.time.*;
import java.util.Random;

public class Game {

    public String playerOne;
    public String playerTwo;
    public String turn;
    public BoardSquare[][] board = new BoardSquare[9][7];
    public Move move = new Move(this);
    public String winner = "";
    public String startTime;
    public String endTime = "";
    public int turnNumber = 0;
    public String gameID;

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

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        startTime = dtf.format(LocalDateTime.now()).toString();

        gameID = new Random(System.currentTimeMillis()).toString();
        
        playerOne = playerOneName;
        playerTwo = playerTwoName;
        turn = "blue";

        for(int row=0; row<9; row++){
            for(int col=0; col<7; col++){
                board[row][col] = new BoardSquare(row, col);
            }
        }

        initializeBoard();
    }

    private void initializeBoard(){
        board[0][0].setPiece("lion", "red");
        board[0][6].setPiece("tiger", "red");
        board[1][1].setPiece("dog", "red");
        board[1][5].setPiece("cat", "red");
        board[2][0].setPiece("rat", "red");
        board[2][2].setPiece("leopard", "red");
        board[2][4].setPiece("wolf", "red");
        board[2][6].setPiece("elephant", "red");
        board[6][0].setPiece("elephant", "blue");
        board[6][2].setPiece("wolf", "blue");
        board[6][4].setPiece("leopard", "blue");
        board[6][6].setPiece("rat", "blue");
        board[7][1].setPiece("cat", "blue");
        board[7][5].setPiece("dog", "blue");
        board[8][0].setPiece("tiger", "blue");
        board[8][6].setPiece("lion", "blue");
    }

    void printBoard(){
        // prints out the board in a readable fashion
        for(int row=0; row<9; row++) {
            for (int col = 0; col < 7; col++) {
                if(board[row][col].gamePiece != null)
                    System.out.print(board[row][col].gamePiece.ID + " ");
                else System.out.print("__ ");
            }
            System.out.println();
        }
    }

    public boolean sendInput(String player, int row, int col){
        // send input to the game in the format (player sending move, column selected, row selected)
        // returns true if the game state has changed, else returns false
        if(isTurn(player) && winner.isEmpty()){
            if(move.selectedSquare == null){ // selecting an empty square
                move.selectedSquare = board[row][col];
                move.updateValidTiles();
                return true;
            }
            else if(move.selectedSquare == board[row][col]){ // re-selecting the same square
                move.selectedSquare = null;
                move.updateValidTiles();
                return true;
            }
            else if(board[row][col].isValid){ // selecting a square to move to
                makeMove(row, col);
                move.updateValidTiles();
                return true;
            }
            else {
                System.out.println("Invalid Move!");
            }
        }
        return false;
    }

    private void makeMove(int row, int col){
        // once a valid move has been constructed, make it.
        turnNumber++;
        String pieceName = gameUtils.getRealPieceName(board[move.selectedSquare.row][move.selectedSquare.col].gamePiece.ID);

        board[row][col].gamePiece = new GamePiece(move.selectedSquare.gamePiece.type, move.selectedSquare.gamePiece.color);
        board[move.selectedSquare.row][move.selectedSquare.col].gamePiece = null;

        System.out.println(turn + " moved " + pieceName + " from " + row + "," + col
                + " to " + move.selectedSquare.row +"," + move.selectedSquare.col);

        if(turn.equals("blue")) turn = "red";
        else turn = "blue";

        move.selectedSquare = null;
        move.updateValidTiles();

        checkIfWinner();

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
                if(board[i][j].gamePiece != null){
                    if(board[i][j].gamePiece.color.equals("blue")){
                        if(i == 0 && j == 3) winner = playerOne;
                        bluePieces++;
                    }
                    if(board[i][j].gamePiece.color.equals("red")){
                        if(i == 8 && j == 3) winner = playerTwo;
                        redPieces++;
                    }
                }
            }
        }
        if(redPieces == 0) winner = playerOne; // if there are no red pieces left
        if(bluePieces == 0) winner = playerTwo; // if there are no blue pieces left

        if(!canMakeMove()){
            if(isTurn(playerOne)) winner = playerTwo;
            if(isTurn(playerTwo)) winner = playerOne;
        }

        if(!winner.isEmpty()){
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
            endTime = dtf.format(LocalDateTime.now()).toString();
        }
    }

    private boolean canMakeMove(){
        // Returns true if the current turn's player has any moves available

        Move testMove = new Move(this);

        for(int row = 0; row < 9; row++){
            for(int col = 0; col < 7; col++){
                testMove.selectedSquare = board[row][col];
                if (testMove.isFriendlyUnit(row, col)) {
                    testMove.updateValidTiles();
                    if (testMove.numberOfValidTiles > 0) return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] arg){
    }
}
