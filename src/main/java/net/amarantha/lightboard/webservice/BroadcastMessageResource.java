package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.OldSceneManager;
import net.amarantha.lightboard.updater.schedule.PostMessageUpdater;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("message")
public class BroadcastMessageResource {

    @Inject private static OldSceneManager sceneManager;

    public BroadcastMessageResource() {
    }

    @Inject
    public BroadcastMessageResource(OldSceneManager sceneManager) {
        BroadcastMessageResource.sceneManager = sceneManager;
    }

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
            sceneManager.loadScene(scene);
        }
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Message posted: " + message)
                .build();
    }


}
