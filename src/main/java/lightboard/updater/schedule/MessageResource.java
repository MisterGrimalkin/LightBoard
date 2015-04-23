package lightboard.updater.schedule;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by grimalkin on 23/04/15.
 */
@Path("message")
public class MessageResource {

    private static MessageUpdater updater;

    public static void bindUpdater(MessageUpdater updater) {
        MessageResource.updater = updater;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String simple() {
        return "And illustrated with cartoons";
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void postMessage(String message) {
        if ( updater!=null ) {
            updater.postMessage(message.split(";"));
        }
    }


}
