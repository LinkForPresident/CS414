package GameServer;

public class Logout extends Action {

    @Override
    Response executeAction(Request request) {
        Terminal.printInfo(String.format("User '%s' is attempting to log out.", request.body.get("username")));
        String body = logout(request.header.get("cookie"));
        return new Response(body);
    }

    private static String logout(String user_hash){
        // Remove user_hash from loggedInUsers list.
        String JSONResponse = "";
        Terminal.printInfo(String.format("Attempting to log out user with user_hash: '%s'.", user_hash));
        Server.loggedInUsers.remove(user_hash);
        Terminal.printInfo(String.format("User with user_hash: '%s' has been logged out.", user_hash));
        JSONResponse = String.format("{\"loggedIn\": %b}", false);
        return JSONResponse;
    }
}
