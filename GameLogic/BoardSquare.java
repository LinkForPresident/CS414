package GameLogic;

public class BoardSquare {
    String environment = null;
    public GamePiece gamePiece;
    public int row;
    public int col;

    public BoardSquare(int row, int col){
        this.row = row;
        this.col = col;

        if(isWater(row, col)) environment = "water";
        else if(isTrap(row, col)) environment = "trap";
        else if(isDen(row, col)) environment = "den";
        else environment = "empty";
    }

    public void setPiece(String type, String color){
        gamePiece = new GamePiece(type, color);
    }

    private boolean isWater(int row, int col){
        // returns true if the tile at row, col contains water, else returns false.
        return (row == 3 || row == 4 || row == 5) && (col == 1 || col == 2 || col == 4 || col == 5);
    }

    private boolean isTrap(int row, int col){
        // returns true if the tile at row, col contains a trap, else returns false.
        return ((row == 8 && (col == 2 || col == 4)) || (row == 7 && col == 3)) || (row == 0 && (col == 2 || col == 4) || (row == 1 && col == 3));
    }

    private boolean isDen(int row, int col){
        // returns true if the tile at row, col contains a den, else returns false.
        return (row == 0 && col == 3) || (row == 8 && col == 3);
    }

}
