package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.scene.OldSceneManager;
import net.amarantha.lightboard.zone.TextZone;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Singleton
@Path("messages")
public class MessageResource extends Resource {

    private static OldSceneManager sceneManager;

    public MessageResource() {}

    @Inject
    public MessageResource(OldSceneManager sceneManager) {
        MessageResource.sceneManager = sceneManager;
    }

    private static TextZone zone;

    public void setZone(TextZone zone) {
        MessageResource.zone = zone;
    }

    @POST
    @Path("add")
    public void putMessage(String message) {
        if ( zone!=null ) {
            zone.addMessage(message);
        }
    }

    @POST
    @Path("clear")
    public void clearMessages() {
        if ( zone!=null ) {
            zone.clearMessages();
        }
    }

    @POST
    @Path("next")
    public void nextMessage() {
        if ( zone!=null ) {
            zone.out();
        }
    }


    public void setMessage(String sceneId, String zoneId) {

    }

    public void getMessage(String sceneId, String zoneId) {

    }



}
