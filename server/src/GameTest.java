package src;

import junit.framework.*;

public class GameTest extends TestCase {

    private Game game;

    public void setUp(){
        game = new Game("Bob", "Sally");
    }

    public void testGamePieces(){
        // Tests that the game pieces are placed on to the board in the correct positions after the game is created.
        assert(game.board[0][0].equals("r7"));
        assert(game.board[8][6].equals("b7"));
    }

    public void testTurn(){
        // Tests that the starting turn is blue
        assert(game.turn.equals("blue"));
    }

    public void testMakeMove(){
        game.printBoard();
        game.sendInput("Bob", 5, 8);
        game.sendInput("Bob", 6, 8);
        game.sendInput("Bob", 5, 8);
        game.sendInput("Bob", 4, 8);
        game.sendInput("Sally", 0, 2);
        game.sendInput("Bob", 4, 8);
        game.sendInput("Sally", 0, 1);
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