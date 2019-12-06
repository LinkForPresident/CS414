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
        String email = request.body.get("email");
        String body = registerUser(username, password, email);
        return new Response(body);
    }

    private static String registerUser(String username, String password, String email){
        // handle a POST request to register a new user in the system.
        if(email.indexOf("@") == -1 || email.indexOf("@") == 0 || email.indexOf("@") == email.length()-1){
            String JSONResponse = String.format("{\"loggedIn\": %b, \"wasSuccessful\": %b}", false, false);
            return JSONResponse;
        }
        String userHash = Server.calculateUserHash(username, password);
        Terminal.printInfo(String.format("Attempting to register new user with username: %s , password: %s , email: %s " +
                "user_hash: %s.", username, password, email, userHash));
        try {
            String checkIfEmailExists = String.format("SELECT * FROM User WHERE email='%s';", email);
            ResultSet resultSet = Database.executeDatabaseQuery(checkIfEmailExists);
            if (resultSet.next()) {
                Terminal.printError(String.format("Encountered an error while attempting to register a new user with " +
                        "username '%s': email '%s' has already been taken by different user.", username, email));
                String JSONResponse = String.format("{\"loggedIn\": %b, \"wasSuccessful\": %b}", false, false);
                return JSONResponse;
            }
        }catch(SQLException sql){
            Terminal.printError(String.format("Encountered an error while attempting to register a new user with " +
                    "username '%s', when querying the database to check if the email is already taken.", username));
            String JSONResponse = String.format("{\"loggedIn\": %b, \"wasSuccessful\": %b}", false, false);
            return JSONResponse;
        }
        String registerUser = String.format("INSERT INTO User VALUES('%s', '%s', '%s', '%s');", username, password, userHash, email);
        ResultSet resultSet = Database.executeDatabaseQuery(registerUser);
        String checkIfRegistered = String.format("SELECT 1 FROM User WHERE hash_code='%s';", userHash);
        resultSet = Database.executeDatabaseQuery(checkIfRegistered);
        String JSONResponse = "";
        try {
            if (resultSet.next()) {
                Terminal.printSuccess(String.format("New user '%s' has been registered", username));
                JSONResponse = String.format("{\"loggedIn\": %b, \"wasSuccessful\": %b}", false, true);
            } else {
                Terminal.printError(String.format("Encountered an error while attempting to register a new user with " +
                        "username '%s'.", username));
                JSONResponse = String.format("{\"loggedIn\": %b, \"wasSuccessful\": %b}", false, false);
            }
        }catch(SQLException sql){
            Terminal.printError(String.format("Encountered an error while attempting to register a new user with " +
                    "username '%s'.", username));
            JSONResponse = String.format("{\"loggedIn\": %b, \"wasSuccessful\": %b}", false, false);
        }
        return JSONResponse;
    }

}
