package GameServer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Register extends Action {

    @Override
    Response executeAction(Request request) {
        // handle a client requesting to register a new account.
        Terminal.printInfo(String.format("User '%s' is attempting to register a new account.",
                request.body.get("username")));
        String username = request.body.get("username");
        String password = request.body.get("password");
        String body = registerUser(username, password);
        return new Response(body);
    }

    private static String registerUser(String username, String password){
        // handle a POST request to register a new user in the system.
        String userHash = Server.calculateUserHash(username, password);
        Terminal.printInfo(String.format("Attempting to register new user with username: %s , password: %s , " +
                "user_hash: %s.", username, password, userHash));
        String registerUser = String.format("INSERT INTO User VALUES('%s', '%s', '%s');", username, password,
                userHash);
        ResultSet resultSet = Database.executeDatabaseQuery(registerUser);
        String checkIfRegistered = String.format("SELECT 1 FROM User WHERE hash_code='%s';", userHash);
        resultSet = Database.executeDatabaseQuery(checkIfRegistered);
        String JSONResponse = String.format("{\"loggedIn\": %b}", false);
        try {
            if (resultSet.next()) {
                Terminal.printSuccess(String.format("New user '%s' has been registered", username));
            } else {
                Terminal.printError(String.format("Encountered an error while attempting to register a new user with " +
                        "username '%s'.", username));
            }
        }catch(SQLException sql){
            Terminal.printError(String.format("Encountered an error while attempting to register a new user with " +
                    "username '%s'.", username));
        }
        return JSONResponse;
    }

}
