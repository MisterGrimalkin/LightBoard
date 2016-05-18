package net.amarantha.lightboard.webservice;

import net.amarantha.lightboard.zone.old.TextZone_Old;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Timer;
import java.util.TimerTask;

@Path("ticket")
public class ShowerTicketResource {

    private static TextZone_Old femaleTextZone;
    private static TextZone_Old maleTextZone;

    private static Integer lastFemale;
    private static Integer lastMale;

    public static void setTextZones(TextZone_Old femaleTextZone, TextZone_Old maleTextZone) {
        ShowerTicketResource.femaleTextZone = femaleTextZone;
        ShowerTicketResource.maleTextZone = maleTextZone;
    }

    @POST
    public static Response postTickets(@QueryParam("female") Integer female, @QueryParam("male") Integer male) {

        if ( femaleTextZone!=null && maleTextZone!=null ) {

            if ( female!=null ) {
                femaleTextZone.clearMessages();
                femaleTextZone.addMessage("{yellow}Female: " + female);
                femaleTextZone.resetScroll();
                if ( !female.equals(lastFemale) ) {
                    femaleTextZone.overrideMessage("Female: {green}" + female);
                }
                femaleTextZone.advanceMessage();
                femaleTextZone.render();
                lastFemale = female;
            }

            if ( male!=null ) {
                maleTextZone.clearMessages();
                maleTextZone.addMessage("{yellow}Male: " + male);
                maleTextZone.resetScroll();
                if ( !male.equals(lastMale) ) {
                    maleTextZone.overrideMessage("Male: {green}" + male);
                }
                maleTextZone.advanceMessage();
                maleTextZone.render();
                lastMale = male;
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    femaleTextZone.clearOverride();
                    maleTextZone.clearOverride();
                }
            }, 5000);

            return Response.ok()
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("Ticket Numbers Posted")
                    .build();
        } else {
            return Response.serverError()
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("TextZones Not Bound")
                    .build();
        }


    }
}
