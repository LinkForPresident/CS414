package GameLogic;

import junit.framework.*;
import org.junit.Test;
import org.junit.*;
import GameLogic.exception.PlayerNameException;

public class GameTest extends TestCase {

    private Game game;
    GameUtils gameUtils = new GameUtils();

    @BeforeClass
    public void setUp() throws PlayerNameException {
        // This will run before every method
        System.out.println("\nSTART OF TEST\n===================");
        game = new Game("Bob", "Sally");
        System.out.println("Start Time: "+ game.startTime);
    }

    @Test
    public void testGamePieces(){
        // Tests that the game pieces are placed on to the board in the correct positions after the game is created.
        System.out.println("Game Piece Test: Tests that the game pieces are put in the correct locations");

        assertEquals("r7", game.board[0][0]);
        assertEquals("b7", game.board[8][6]);
    }

    @Test
    public void testTurn(){
        // Tests that the turn changes properly
        System.out.println("Turn test: Tests that the turn changes properly");

        assertEquals("blue", game.turn);
        game.sendInput("Bob", 8, 6);
        assertEquals("blue", game.turn);
        game.sendInput("Bob", 7, 6);
        assertEquals("red", game.turn);
        game.sendInput("Sally", 2, 0);
        assertEquals("red", game.turn);
        assertEquals("red", game.turn);
        game.sendInput("Sally", 3, 0);
        assertEquals("blue", game.turn);
    }

    @Test
    public void testMakeValidMove(){
        // Tests that valid moves can be made
        System.out.println("Valid move test: Tests that valid moves can be made");

        game.sendInput("Bob", 8, 6);
        game.sendInput("Bob", 7, 6);
        assertEquals("__", game.board[8][6]);
        assertEquals("b7", game.board[7][6]);

        game.sendInput("Sally", 2, 0);
        game.sendInput("Sally", 3, 0);
        assertEquals("__", game.board[2][0]);
        assertEquals("r1", game.board[3][0]);
    }

    @Test
    public void testPlayerNameException(){
        System.out.println("Testing that Player Name Exceptions are being thrown as expected");

        try {
            game = new Game("", "Sally");
        } catch (PlayerNameException e) {
            assertTrue(e.getMessage().contains("Player One Name must not be empty"));
        }

        try {
            game = new Game("Bob", "");
        } catch (PlayerNameException e) {
            assertTrue(e.getMessage().contains("Player Two Name must not be empty"));
        }

        try {
            game = new Game("Sally", "Sally");
        } catch (PlayerNameException e) {
            assertTrue(e.getMessage().contains("Player One Name cannot be same as Player Two Name"));
        }
    }

    @Test
    public void testBlueLionNotAbleToCrossItsOwnPieces() {
        // Test that you cannot move Lion onto your own Blue Mouse,
        // Blue Lion should stay at (6,7) when we try to move it to (6,6),
        // and it should remain Blue's turn after he tries to do this
        System.out.println("It is " + game.turn +"'s turn");
        gameUtils.printBoardVeryVerbose(game.board);
        game.sendInput("Bob", 8, 6);
        game.sendInput("Bob", 7, 6);
        System.out.println("It is " + game.turn +"'s turn");
        gameUtils.printBoardVeryVerbose(game.board);
        game.sendInput("Sally", 0, 6);
        game.sendInput("Sally", 1, 6);
        System.out.println("It is " + game.turn +"'s turn");
        gameUtils.printBoardVeryVerbose(game.board);
        game.sendInput("Bob", 7, 6);
        game.sendInput("Bob", 6, 6);
        gameUtils.printBoardVeryVerbose(game.board);
        System.out.println("It is " + game.turn +"'s turn");
        assertEquals(game.turn, "blue");
    }

    @Test
    public void testMakeInvalidMove(){
        // Tests that invalid moves cannot be made
        System.out.println("Invalid move test: Tests that invalid moves cannot be made");

        String newBoard[][] = {
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"r7","__","__","__","__","__","__"},
                {"b3","__","__","__","__","__","__"},
                {"b1","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"}};

        game.board = newBoard;
        game.printBoard();

        // making a move when it isn't your turn
        game.sendInput("Sally", 3,0);
        game.sendInput("Sally", 2,0);
        assertEquals("r7", game.board[3][0]);
        assertEquals("__", game.board[2][0]);

        // moving in to an enemy unit that is stronger than you
        game.sendInput("Bob",4,0);
        game.sendInput("Bob",3,0);
        assertEquals("b3", game.board[4][0]);
        assertEquals("r7", game.board[3][0]);

        // moving in to a friendly unit
        game.sendInput("Bob",5,0);
        game.sendInput("Bob",4,0);
        assertEquals("b1", game.board[5][0]);
        assertEquals("b3", game.board[4][0]);
    }

    @Test
    public void testTrap(){
        // Tests that traps work
        System.out.println("Trap Test: Tests that traps work");

        String newBoard[][] = {
                {"__","__","r7","__","__","__","__"},
                {"__","__","r1","b3","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"}};

        game.board = newBoard;
        game.printBoard();
        game.sendInput("Bob",1,3);
        game.sendInput("Bob",1,2);

        assertEquals("b3", game.board[1][3]);
        assertEquals("r1", game.board[1][2]);

        game.turn = "red";

        game.sendInput("Sally",1,2);
        game.sendInput("Sally",1,3);

    }

    @Test
    public void testWin_1(){
        System.out.println("Win test 1: Tests that you can win by capturing all enemy units");

        String newBoard[][] = {
                {"__","__","__","__","__","__","__"},
                {"__","r3","b6","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"}};

        game.board = newBoard;

        game.sendInput("Bob", 1, 2);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        game.sendInput("Bob", 1, 1);
        assertEquals("Bob", game.winner);
        System.out.println("End Time: " + game.endTime);
    }

    @Test
    public void testWin_2(){
        System.out.println("Win test 1: Tests that you can win by entering the enemy den");
        String newBoard[][] = {
                {"__","__","__","__","__","__","__"},
                {"__","r3","__","b6","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"},
                {"__","__","__","__","__","__","__"}};

        game.board = newBoard;

        game.sendInput("Bob", 1, 3);
        game.sendInput("Bob", 0, 3);
        assertEquals("Bob", game.winner);
    }
}