import junit.framework.*;

public class GameTest extends TestCase {

    private Game testGame;

    public void setUp(){
        testGame = new Game("Bob", "Sally");
    }

    public void testGamePieces(){
        // Tests that the game pieces are placed on to the board in the correct positions after the game is created.
        assert(testGame.board[0][0].equals("r7"));
        assert(testGame.board[8][6].equals("b7"));
    }

    public void testTurn(){
        // Tests that the starting turn is blue
        assert(testGame.turn.equals("blue"));
    }

    public void testMakeMove(){
        testGame.print_board();
        testGame.makeMove("Bob", 6,8,5,8);
        testGame.makeMove("Sally", 0, 2, 0, 5);
        testGame.makeMove("Sally", 0, 2, 0, 3);
        testGame.makeMove("Sally", 0, 2, 0, 4);
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