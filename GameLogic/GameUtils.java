package GameLogic;

public class GameUtils {
    // This class contains methods that are useful to be used with Game object
    // but are not needed for functionality of Game


    void printBoardVerbose(String[][] board){
        // prints out the board in a readable fashion, including Trap and River squares
        for(int x=0; x<board.length; x++) {
            for(int y = 0; y<board[x].length; y++) {
                System.out.print(getSquare(x,y,board[x][y])+"\t");
            }
            System.out.println();
        }
        System.out.println("");
    }

    void printBoardVeryVerbose(String[][] board){
        // prints out the board in a readable fashion, including Trap and River squares, and full piece names
        for(int x=0; x<board.length; x++) {
            for(int y = 0; y<board[x].length; y++) {
                String out = getSquare(x,y,getRealPieceName(board[x][y]));
                while(out.length() < 15) {
                    out += " ";
                }
                System.out.print(out);
            }
            System.out.println();
        }
        System.out.println("\n");
    }

    String getSquare(int x, int y, String piece) {
        if(isTrapSqaure(x,y)) {
            return "{" + piece + "}";
        }
        if(isRiverSqaure(x,y)) {
            return "~" + piece + "~";
        }
        return " " + piece + " ";
    }

    boolean isTrapSqaure(int x, int y) {
        //Returns true if square is Trap, regardless on red or blue side
        if(((y == 2 || y == 4) && (x == 0 || x == 8))
                || (y == 3) && (x == 1 || x == 7)) {
            return true;
        }
        return false;
    }

    boolean isRiverSqaure(int x, int y) {
        //Returns true if square is River, regardless on red or blue side
        if(x == 3 || x == 4 || x == 5) {
            if(y == 1 || y == 2 || y == 4 || y == 5) {
                return true;
            }
        }
        return false;
    }

    String getRealPieceName(String shortName) {
        switch (shortName) {
            case "r1":
                return "Red Mouse";
            case "r2":
                return "Red Cat";
            case "r3":
                return "Red Wolf";
            case "r4":
                return "Red Dog";
            case "r5":
                return "Red Leopard";
            case "r6":
                return "Red Tiger";
            case "r7":
                return "Red Lion";
            case "r8":
                return "Red Elephant";
            case "b1":
                return "Blue Mouse";
            case "b2":
                return "Blue Cat";
            case "b3":
                return "Blue Wolf";
            case "b4":
                return "Blue Dog";
            case "b5":
                return "Blue Leopard";
            case "b6":
                return "Blue Tiger";
            case "b7":
                return "Blue Lion";
            case "b8":
                return "Blue Elephant";
            default:
                return "__________";
        }
    }
}
