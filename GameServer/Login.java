package GameServer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends Action{

    @Override
    Response executeAction(Request request) {
        Terminal.printInfo(String.format("User '%s' is attempting to log in.", request.body.get("username")));
        String username = request.body.get("username");
        String password = request.body.get("password");
        String userHash = Server.calculateUserHash(username, password);
        String header = Response.defaultHeader + String.format("Set-Cookie: user_hash=%s\r\n\r\n", userHash);
        String body = login(request.body.get("username"), userHash);
        return new Response(header, body);
    }

    private static String login(String username, String user_hash) {
        // Login a client by checking if the client is registered and if so, adding client to list of logged in users.
        Terminal.printInfo(String.format("Attempting to log in user with username: '%s' and user_hash: '%s'.",
                username, user_hash));
        String selectUser = String.format("SELECT 1 FROM User WHERE hash_code='%s';", user_hash);
        ResultSet resultSet = Database.executeDatabaseQuery(selectUser);
        String JSONResponse = String.format("{\"loggedIn\": %b}", false);
        try {
            if (resultSet.next()) { // User exists and has been authenticated, place user_hash into loggedInUsers list.
                Server.loggedInUsers.add(user_hash);
                String playerInvites = "";
                for (String[] inv : Server.invites) {
                    if (inv[1].equals(username)) {
                        playerInvites += inv[0] + ",";
                    }
                }
                JSONResponse = String.format("{\"loggedIn\": %b, \"username\": \"%s\", \"invites\": \"%s\"}", true,
                        username, playerInvites);
                Terminal.printSuccess(String.format("User '%s' has been logged in.", username));
            } else {   // User does not exist. Stop the request handling.
                Terminal.printDebug(String.format("User '%s' does not exist.", username));
            }
        }catch(SQLException ignored) {
        }
        return JSONResponse;
    }

}
