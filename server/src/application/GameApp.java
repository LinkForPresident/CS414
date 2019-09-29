package src.application;

import src.Game;
import src.GameUtils;
import src.exception.PlayerNameException;

import java.util.Arrays;
import java.util.Scanner;

public class GameApp {


    public static void main(String[] args) throws PlayerNameException {
        GameUtils gameUtils = new GameUtils();

        String RED = Colors.RED;
        String BLUE = Colors.BLUE;
        String WHITE = Colors.WHITE;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Beginning a game");
        System.out.println("Enter the Blue Player name: ");
        String bluePlayer = scanner.nextLine();

        System.out.println("Enter the Red Player name: ");
        String redPlayer = scanner.nextLine();

        System.out.println("Starting a game between " + BLUE + bluePlayer + WHITE + " and " + RED + redPlayer + WHITE + ".");

        Game game = new Game(bluePlayer, redPlayer);
        gameUtils.printBoardVeryVerboseWithColor(game.board);

        while(game.winner.isEmpty()) {
            while(game.turn.equals("blue") && game.winner.isEmpty()) {
                System.out.println(WHITE + "It is " + BLUE + bluePlayer + "'s " + WHITE + "move!  Select the Piece you want to move.");
                String[] coords = scanner.nextLine().split(",");
                int x =  Integer.parseInt(coords[0]);
                int y =  Integer.parseInt(coords[1]);
                System.out.println(Arrays.toString(coords) + " is " + gameUtils.getSquare(x,y,gameUtils.getRealPieceName(game.board[x][y])));
                game.sendInput(bluePlayer, x, y);
                System.out.println("Now select your destination for this piece!");
                coords = scanner.nextLine().split(",");
                x =  Integer.parseInt(coords[0]);
                y =  Integer.parseInt(coords[1]);
                game.sendInput(bluePlayer, x, y);
                if(game.turn.equals("red") || !game.winner.isEmpty()) {
                    gameUtils.printBoardVeryVerboseWithColor(game.board);
                }
                else {
                    System.out.println("Something was wrong with that choice!  Try inputting a move again!");
                }
            }
            while(game.turn.equals("red") && game.winner.isEmpty()) {
                System.out.println(WHITE + "It is " + RED + redPlayer + "'s " + WHITE + "move!  Select the Piece you want to move.");
                String[] coords = scanner.nextLine().split(",");
                int x =  Integer.parseInt(coords[0]);
                int y =  Integer.parseInt(coords[1]);
                System.out.println(Arrays.toString(coords) + " is " + gameUtils.getSquare(x,y,gameUtils.getRealPieceName(game.board[x][y])));
                game.sendInput(redPlayer, x, y);
                System.out.println("Now select your destination for this piece!");
                coords = scanner.nextLine().split(",");
                x =  Integer.parseInt(coords[0]);
                y =  Integer.parseInt(coords[1]);
                game.sendInput(redPlayer, x, y);
                if(game.turn.equals("blue") || !game.winner.isEmpty()) {
                    gameUtils.printBoardVeryVerboseWithColor(game.board);
                }
                else {
                    System.out.println("Something was wrong with that choice!  Try inputting a move again!");
                }
            }
        }
        System.out.println("GAME OVER! " + game.winner + " was the winner of this match of Jungle! Congratulations!");
    }


}
