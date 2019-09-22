package src;

import junit.framework.*;

public class GameTest extends TestCase {

    private Game game;

    public void setUp(){
        System.out.println("\nSTART OF TEST\n===================");
        game = new Game("Bob", "Sally");
    }

    public void testGamePieces(){
        // Tests that the game pieces are placed on to the board in the correct positions after the game is created.
        assertEquals("r7", game.board[0][0]);
        assertEquals("b7", game.board[8][6]);
    }

    public void testTurn(){
        // Tests that the turn changes properly
        assertEquals("blue", game.turn);
        game.sendInput("Bob", 6, 8);
        assertEquals("blue", game.turn);
        game.sendInput("Bob", 6, 7);
        assertEquals("red", game.turn);
        game.sendInput("Sally", 0, 2);
        assertEquals("red", game.turn);
        assertEquals("red", game.turn);
        game.sendInput("Sally", 0, 3);
        assertEquals("blue", game.turn);
    }

    public void testMakeValidMove(){
        // Tests that valid moves can be made

        game.sendInput("Bob", 6, 8);
        game.sendInput("Bob", 6, 7);
        assertEquals("__", game.board[8][6]);
        assertEquals("b7", game.board[7][6]);

        game.sendInput("Sally", 0, 2);
        game.sendInput("Sally", 0, 3);
        assertEquals("__", game.board[2][0]);
        assertEquals("r1", game.board[3][0]);
    }

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
        game.sendInput("Sally", 0,3);
        game.sendInput("Sally", 0,2);
        assertEquals("r7", game.board[3][0]);
        assertEquals("__", game.board[2][0]);

        // moving in to an enemy unit that is stronger than you
        game.sendInput("Bob",0,4);
        game.sendInput("Bob",0,3);
        assertEquals("b3", game.board[4][0]);
        assertEquals("r7", game.board[3][0]);

        // moving in to a friendly unit
        game.sendInput("Bob",0,5);
        game.sendInput("Bob",0,4);
        assertEquals("b1", game.board[5][0]);
        assertEquals("b3", game.board[4][0]);

    }

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
        game.sendInput("Bob",3,1);
        game.sendInput("Bob",2,1);

        assertEquals("b3", game.board[1][3]);
        assertEquals("r1", game.board[1][2]);

        game.turn = "red";

        game.sendInput("Sally",2,1);
        game.sendInput("Sally",3,1);

    }

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

        game.sendInput("Bob", 2, 1);
        game.sendInput("Bob", 1, 1);
        assertEquals("Bob", game.winner);
    }

    public void testWin_2(){
        System.out.println("Win test 1: Tests that you can win by capturing all enemy units");
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

        game.sendInput("Bob", 3, 1);
        game.sendInput("Bob", 3, 0);
        assertEquals("Bob", game.winner);
    }


    public GameTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(GameTest.class);
    }

    public static void main(String[] args) {
        TestSuite suite = new TestSuite();

        if (args.length != 0)
            for (String arg : args) suite.addTest(new GameTest(arg)); // Run specific tests as indicated from the command line
        else
            suite.addTest(GameTest.suite()); // Dynamically discover all of them, or use user-defined suite

        junit.textui.TestRunner.run(suite);
    }
}