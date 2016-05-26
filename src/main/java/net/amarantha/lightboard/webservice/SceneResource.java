package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.SceneLoader;
import net.amarantha.lightboard.zone.AbstractZone;
import net.amarantha.lightboard.zone.TextZone;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("scene")
public class SceneResource extends Resource {

    private static SceneLoader sceneLoader;

    public SceneResource() {}

    @Inject
    public SceneResource(SceneLoader sceneLoader) {
        SceneResource.sceneLoader = sceneLoader;
    }

    /**
     * Load scene from file and begin display
     */
    @POST
    @Path("{sceneName}/load")
    @Produces(MediaType.TEXT_PLAIN)
    public Response loadScene(@PathParam("sceneName") String sceneName) {
        if ( sceneLoader.loadScene(sceneName) ) {
            return ok("Scene '" + sceneName + "' Loaded");
        } else {
            return error("Scene + '" + sceneName + "' not found");
        }
    }

    /**
     * Clear the message queue of a TextZone
     */
    @POST
    @Path("{sceneName}/zone/{zoneId}/clear")
    @Produces(MediaType.TEXT_PLAIN)
    public Response clearMessage(@PathParam("sceneName") String sceneName, @PathParam("zoneId") String zoneId) {
        try {
            findTextZone(sceneName, zoneId).clearMessages().end();
            return ok("Messages Cleared '"+sceneName+"/"+zoneId + "'");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * Add a message to the queue of a TextZone
     */
    @POST
    @Path("{sceneName}/zone/{zoneId}/add")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addMessage(@PathParam("sceneName") String sceneName, @PathParam("zoneId") String zoneId, String message) {
        try {
            findTextZone(sceneName, zoneId).addMessage(message).end();
            return ok("Message Added '"+sceneName+"/"+zoneId+"'");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * Clear and then add a message to a TextZone
     */
    @POST
    @Path("{sceneName}/zone/{zoneId}/replace")
    @Produces(MediaType.TEXT_PLAIN)
    public Response replaceMessage(@PathParam("sceneName") String sceneName, @PathParam("zoneId") String zoneId, String message) {
        try {
            findTextZone(sceneName, zoneId).clearMessages().end();
            findTextZone(sceneName, zoneId).addMessage(message);
            return ok("Message Replaced '"+sceneName+"/"+zoneId+"'");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /////////////
    // Utility //
    /////////////

    private TextZone findTextZone(String sceneName, String zoneId) throws Exception {
        if ( sceneLoader.getCurrentScene()!=null && sceneLoader.getCurrentScene().getName().equals(sceneName) ) {
            AbstractZone zone = sceneLoader.getCurrentScene().getZone(zoneId);
            if ( zone!=null && zone instanceof TextZone) {
                return (TextZone)zone;
            } else {
                throw new Exception("Could not find TextZone '"+zoneId+"'");
            }
        } else {
            throw new Exception("Scene '"+sceneName+"' is not active");
        }
    }

}
