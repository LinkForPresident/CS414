package GameServer;

import java.util.Iterator;

public class DeclineInvite extends Action {

    @Override
    Response executeAction(Request request) {
        String body = declineInvite(request.body.get("playerOne"), request.body.get("playerTwo"));
        return new Response(body);
    }

    private static String declineInvite(String playerOne, String playerTwo) {
        Terminal.printInfo(String.format("Attempting to decline the invite of %s to %s", playerOne, playerTwo));
        String JSONResponse = "";
        Iterator<String[]> invitesIterator = Server.invites.iterator();
        while(invitesIterator.hasNext()) {
            String[] invite = invitesIterator.next();
            if (invite[0].equals(playerOne) && invite[1].equals(playerTwo)) {
                invitesIterator.remove();
                String playerInvites = "";
                for (String[] inv : Server.invites) {
                    if (inv[1].equals(playerTwo)) {
                        playerInvites += inv[0] + ",";
                    }
                }
                JSONResponse = String.format("{\"invites\": \"%s\"}", playerInvites);
            }
        }
        return JSONResponse;
    }

}
