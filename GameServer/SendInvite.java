package GameServer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

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
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
            String createInvite = String.format("INSERT INTO Invite VALUES('%s', '%s', '%s');", playerOne, playerTwo, dateFormat.format(date));
			Database.executeDatabaseQuery(createInvite);
            Server.invites.add(newInvite);
        }
        JSONResponse = String.format("{\"invitedPlayer\": \"%s\", \"wasSuccessful\": %b}", playerTwo,
                !alreadyExists);
        return JSONResponse;
    }

}
