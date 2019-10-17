package GameServer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Unregister extends Action {

    @Override
    Response executeAction(Request request) {
        // handle a client requesting to register a new account.
        Terminal.printInfo(String.format("User '%s' is attempting to unregister their account.",
                request.body.get("username")));
        String username = request.body.get("username");
        String userHash = request.header.get("cookie");
        String body = unregisterUser(username, userHash);
        return new Response(body);
    }

    private static String unregisterUser(String username, String user_hash){
        Terminal.printInfo(String.format("Attempting to unregister user: '%s'.", username));
        String unregisterUser = String.format("DELETE FROM User WHERE hash_code='%s';", user_hash);
        ResultSet resultSet = Database.executeDatabaseQuery(unregisterUser);
        String checkIfUnregistered = String.format("SELECT 1 FROM user WHERE hash_code='%s';", user_hash);
        resultSet = Database.executeDatabaseQuery(checkIfUnregistered);
        String JSONResponse = String.format("{\"success\": %b}", false);
        try{
            if(resultSet.next()){
                Terminal.printError(String.format("Encountered an error while attempting to unregister user with " +
                        "username '%s'.", username));
            }
            else{
                Terminal.printSuccess(String.format("User '%s' has been unregistered", username));
                JSONResponse = String.format("{\"success\": %b}", true);
            }
        }catch(SQLException sql){
            Terminal.printError(String.format("Encountered an error while attempting to unregister user with username" +
                    " '%s'.", username));
        }
        return JSONResponse;

    }

}
