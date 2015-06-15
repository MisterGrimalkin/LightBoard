package net.amarantha.lightboard.webservice;

import net.amarantha.lightboard.zone.impl.TextZone;
import net.sf.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@Path("ticket")
public class ShowerTicketResource {

    private static TextZone femaleTextZone;
    private static TextZone maleTextZone;

    private static Integer lastFemale;
    private static Integer lastMale;

    public static void setTextZones(TextZone femaleTextZone, TextZone maleTextZone) {
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
                maleTextZone.render();
                lastMale = male;
            }

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
