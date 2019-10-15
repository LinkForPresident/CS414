package GameLogic;

public class GamePiece {

    public String ID = "";
    public int power;
    public String color;
    public String type;


    public GamePiece(String type, String color){
        //TODO add exception throw for invalid input
        this.color = color;
        this.type = type;

        ID += color.charAt(0);

        switch(type) {
            case "rat":
                power = 1;
                ID += "1";
                break;
            case "cat":
                power = 2;
                ID += "2";
                break;
            case "wolf":
                power = 3;
                ID += "3";
                break;
            case "dog":
                power = 4;
                ID += "4";
                break;
            case "leopard":
                power = 5;
                ID += "5";
                break;
            case "tiger":
                power = 6;
                ID += "6";
                break;
            case "lion":
                power = 7;
                ID += "7";
                break;
            case "elephant":
                power = 8;
                ID += "8";
                break;
        }

    }

}
