package GameServer;

public class Terminal {

    static final String RESET_TEXT_COLOR = "\u001B[0m";
    static final String RED_TEXT = "\u001B[38;5;196m";
    static final String BLUE_TEXT = "\u001B[38;5;14m";
    static final String GRAY_TEXT = "\u001B[38;5;7m";
    static final String GREEN_TEXT = "\u001B[38;5;82m";
    static final String YELLOW_TEXT = "\u001B[38;5;11m";

    static final String INFO_TAG = BLUE_TEXT + "==INFO==:: " + RESET_TEXT_COLOR;
    static final String DEBUG_TAG = GRAY_TEXT + "==DEBUG==:: " + RESET_TEXT_COLOR;
    static final String WARNING_TAG = YELLOW_TEXT + "==WARNING==:: " + RESET_TEXT_COLOR;
    static final String ERROR_TAG = RED_TEXT + "==ERROR==:: " + RESET_TEXT_COLOR;
    static final String SUCCESS_TAG = GREEN_TEXT + "==SUCCESS==:: " + RESET_TEXT_COLOR;

    protected Terminal(){

    }

    static void printInfo(String message){
        System.out.println(INFO_TAG + message);
    }

    static void printDebug(String message){
        if(Server.debugMode) {
            System.out.println(DEBUG_TAG + message);
        }
    }

    static void printWarning(String message){
        System.out.println(WARNING_TAG + message);
    }

    static void printError(String message){
        System.out.println(ERROR_TAG + message);
    }

    static void printSuccess(String message){
        System.out.println(SUCCESS_TAG + message);
    }

}