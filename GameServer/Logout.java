package GameServer;

public class Logout extends Action {

    @Override
    Response executeAction(Request request) {
        Terminal.printInfo(String.format("User '%s' is attempting to log out.", request.body.get("username")));
		String userHash = Server.calculateUserHash(request.body.get("username"), request.body.get("password"));
        String body = logout(userHash);
        return new Response(body);
    }

    private static String logout(String userHash){
        // Remove user_hash from loggedInUsers list.
        String JSONResponse = "";
        Terminal.printInfo(String.format("Attempting to log out user with user_hash: '%s'.", userHash));
        Server.loggedInUsers.remove(userHash);
        Terminal.printInfo(String.format("User with user_hash: '%s' has been logged out.", userHash));
        JSONResponse = String.format("{\"loggedIn\": %b}", false);
        return JSONResponse;
    }
}
