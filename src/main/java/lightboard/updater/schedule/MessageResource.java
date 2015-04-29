package lightboard.updater.schedule;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("message")
public class MessageResource {

    private static MessageUpdater updater;

    public static void bindUpdater(MessageUpdater updater) {
        MessageResource.updater = updater;
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public void postMessage(String message) {
        System.out.println("Received Message: " + message);
        if ( updater!=null ) {
            updater.postMessage(message.split(";"));
        }
    }


}
