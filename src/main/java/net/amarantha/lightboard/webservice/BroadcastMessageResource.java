package net.amarantha.lightboard.webservice;

import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.updater.schedule.PostMessageUpdater;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("message")
public class BroadcastMessageResource {

    private static PostMessageUpdater updater;

    private static Integer scene = null;

    public static void bindUpdater(PostMessageUpdater updater) {
        BroadcastMessageResource.updater = updater;
    }

    public static void bindScene(int sceneNumber) { scene = sceneNumber; }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response postMessage(String message) {
        System.out.println(message);
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
