package net.amarantha.lightboard.webservice;

import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.updater.schedule.MessageUpdater;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("message")
public class MessageResource {

    private static MessageUpdater updater;

    private static Integer scene = null;

    public static void bindUpdater(MessageUpdater updater) {
        MessageResource.updater = updater;
    }

    public static void bindScene(int sceneNumber) { scene = sceneNumber; }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response postMessage(String message) {
        if ( updater!=null ) {
            updater.postMessage(message);
        }
        if ( scene!=null ) {
            SceneManager.loadScene(scene);
        }
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Message posted: " + message)
                .build();
    }


}