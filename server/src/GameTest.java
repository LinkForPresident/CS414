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
        assert(game.board[0][0].equals("r7"));
        assert(game.board[8][6].equals("b7"));
    }

    public void testTurn(){
        // Tests that the turn changes properly
        assert(game.turn.equals("blue"));
        game.sendInput("Bob", 6, 8);
        assert(game.turn.equals("blue"));
        game.sendInput("Bob", 6, 7);
        assert(game.turn.equals("red"));
        game.sendInput("Sally", 0, 2);
        assert(game.turn.equals("red"));
        game.sendInput("Sally", 0, 3);
        assert(game.turn.equals("blue"));
    }

    public void testMakeValidMove(){
        // Tests that valid moves can be made

        game.sendInput("Bob", 6, 8);
        game.sendInput("Bob", 6, 7);
        assert(game.board[6][8].equals("__"));
        assert(game.board[6][7].equals("b7"));

        game.sendInput("Sally", 0, 2);
        game.sendInput("Sally", 0, 3);
        assert(game.board[0][2].equals("__"));
        assert(game.board[0][3].equals("r1"));
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
        assert(game.board[0][3].equals("r7"));
        assert(game.board[0][2].equals("__"));

        // moving in to an enemy unit that is stronger than you
        game.sendInput("Bob",0,4);
        game.sendInput("Bob",0,3);
        assert(game.board[0][4].equals("b3"));
        assert(game.board[0][3].equals("r7"));

        // moving in to a friendly unit
        game.sendInput("Bob",0,5);
        game.sendInput("Bob",0,4);
        assert(game.board[0][5].equals("b1"));
        assert(game.board[0][4].equals("b3"));

        game.printBoard();

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