package GameServer;

public class SendInvite extends Action {

    @Override
    Response executeAction(Request request) {
        String body = sendInvite(request.body.get("playerOne"), request.body.get("playerTwo"));
        return new Response(body);
    }

    private static String sendInvite(String playerOne, String playerTwo){
        Terminal.printInfo(String.format("Attempting to service the invite of %s to %s.", playerOne, playerTwo));
        String JSONResponse = "";
        // TODO: Check if user exists in DB.
        String[] newInvite = {playerOne, playerTwo};
        boolean alreadyExists = false;
        for(String[] invite : Server.invites){
            if(invite[0].equals(newInvite[0]) && invite[1].equals(newInvite[1])){
                Terminal.printDebug(String.format("An invite between %s and %s already exists.", playerOne, playerTwo));
                alreadyExists = true;
                break;
            }
        }
        if(!alreadyExists) {
            Terminal.printDebug(String.format("Creating invite between %s and %s.", playerOne, playerTwo));
            Server.invites.add(newInvite);
        }
        JSONResponse = String.format("{\"invitedPlayer\": \"%s\", \"wasSuccessful\": %b}", playerTwo,
                !alreadyExists);
        return JSONResponse;
    }

}
