package GameServer;

import java.util.Iterator;

public class CancelInvite extends Action {

    @Override
    Response executeAction(Request request) {
        String body = cancelInvite(request.body.get("playerOne"), request.body.get("playerTwo"));
        return new Response(body);
    }

    private static String cancelInvite(String playerOne, String playerTwo) {
        Terminal.printInfo(String.format("Attempting to cancel the invite of %s to %s", playerOne, playerTwo));
        String JSONResponse = "";
        Iterator<String[]> invitesIterator = Server.invites.iterator();
        while(invitesIterator.hasNext()) {
            String[] invite = invitesIterator.next();
            if (invite[0].equals(playerOne) && invite[1].equals(playerTwo)) {
                String declineInvite = String.format("DELETE FROM Invite WHERE playerOne='%s' AND playerTwo='%s';", playerOne, playerTwo);
                Database.executeDatabaseQuery(declineInvite);
                invitesIterator.remove();
            }
        }
        String incomingInvites = "";
        for (String[] inv : Server.invites) {
            if (inv[1].equals(playerOne)) {
                incomingInvites += inv[0] + ",";
            }
        }
        String outgoingInvites = "";
        for (String[] inv : Server.invites) {
            if (inv[0].equals(playerOne)) {
                outgoingInvites += inv[1] + ",";
            }
        }
        JSONResponse = String.format("{\"incomingInvites\": \"%s\", \"outgoingInvites\": \"%s\"}", incomingInvites, outgoingInvites);
        return JSONResponse;
    }

}
