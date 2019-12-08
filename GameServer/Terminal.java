package GameServer;

import java.net.*;
import java.io.*;
import java.lang.*;
import java.sql.*;
import java.util.*;

public class Terminal {

    private static final String RESET_TEXT_COLOR = "\u001B[0m";
    private static final String RED_TEXT = "\u001B[38;5;196m";
    private static final String BLUE_TEXT = "\u001B[38;5;14m";
    private static final String GRAY_TEXT = "\u001B[38;5;7m";
    private static final String GREEN_TEXT = "\u001B[38;5;82m";
    private static final String YELLOW_TEXT = "\u001B[38;5;11m";

    private static final String INFO_TAG = BLUE_TEXT + "==INFO== " + RESET_TEXT_COLOR;
    private static final String DEBUG_TAG = GRAY_TEXT + "==DEBUG== " + RESET_TEXT_COLOR;
    private static final String WARNING_TAG = YELLOW_TEXT + "==WARNING== " + RESET_TEXT_COLOR;
    private static final String ERROR_TAG = RED_TEXT + "==ERROR== " + RESET_TEXT_COLOR;
    private static final String SUCCESS_TAG = GREEN_TEXT + "==SUCCESS== " + RESET_TEXT_COLOR;

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

    static String executeShellScript(String scriptPath) throws IOException, InterruptedException{

        Terminal.printDebug(String.format("Changing permissions for %s to 775.", scriptPath));
        Process process = Runtime.getRuntime().exec(String.format("chmod 775 %s", scriptPath));
        process.waitFor();
        Terminal.printDebug(String.format("Executing %s.", scriptPath));
        process = Runtime.getRuntime().exec(scriptPath);
        process.waitFor();
        InputStream inputStream = process.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String part = "";
        String response = "";
        while((part = bufferedReader.readLine()) != null){
            response += part;
        }
        process.destroy();
        Terminal.printDebug(String.format("%s response: %s", scriptPath, response));
        return response;
    }

}