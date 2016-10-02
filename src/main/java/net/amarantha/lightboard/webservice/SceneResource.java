package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.AbstractScene;
import net.amarantha.lightboard.scene.SceneLoader;
import net.amarantha.lightboard.scene.XMLSceneException;
import net.amarantha.lightboard.zone.AbstractZone;
import net.amarantha.lightboard.zone.Message;
import net.amarantha.lightboard.zone.MessageGroup;
import net.amarantha.lightboard.zone.TextZone;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static net.amarantha.lightboard.zone.AbstractZone.Transitioning.*;

@Path("scene")
public class SceneResource extends Resource {

    private static SceneLoader sceneLoader;

    public SceneResource() {}

    @Inject
    public SceneResource(SceneLoader sceneLoader) {
        SceneResource.sceneLoader = sceneLoader;
    }

    ////////////
    // Scenes //
    ////////////

    /**
     * List available scenes
     */
    @GET
    @Path("list")
    @Produces(MediaType.TEXT_PLAIN)
    public Response listScenes(@QueryParam("markCurrent") Boolean markCurrent ) {
        boolean mark = markCurrent==null ? false : markCurrent;
        String currentSceneName = sceneLoader.getCurrentScene()==null ? "" : sceneLoader.getCurrentScene().getName();
        try {
            List<String> sceneNames = sceneLoader.listSceneFiles();
            String result = "";
            for (String name : sceneNames) {
                result += name + (mark&&name.equals(currentSceneName)?" *" : "") + "\n";
            }
            if ( result.length()>1 ) {
                result = result.substring(0, result.length() - 1);
            }
            return ok(result);
        } catch ( Exception e ) {
            return error(e.getMessage());
        }
    }

    /**
     * Load scene from file and begin display
     */
    @POST
    @Path("{sceneName}/load")
    @Produces(MediaType.TEXT_PLAIN)
    public Response loadScene(@PathParam("sceneName") String sceneName) {
        try {
            if (sceneLoader.loadScene(sceneName)) {
                return ok("Scene '" + sceneName + "' Loaded");
            } else {
                return error("Scene + '" + sceneName + "' not found");
            }
        } catch ( XMLSceneException e ) {
            return error(e.getMessage());
        }
    }

    @GET
    @Path("/current")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCurrentScene() {
        AbstractScene currentScene = sceneLoader.getCurrentScene();
        if ( currentScene!=null ) {
            return ok(currentScene.getName());
        } else {
            return error("No current scene");
        }
    }


    /**
     * Reload current scene
     */
    @POST
    @Path("/reload")
    @Produces(MediaType.TEXT_PLAIN)
    public Response reloadScene() {
        try {
            AbstractScene currentScene = sceneLoader.getCurrentScene();
            if ( currentScene!=null ) {
                String name = currentScene.getName();
                if (sceneLoader.loadScene(name)) {
                    return ok("Scene '" + name + "' Loaded");
                } else {
                    return error("Scene + '" + name + "' not found");
                }
            } else {
                return error("No current scene");
            }
        } catch ( XMLSceneException e ) {
            return error(e.getMessage());
        }
    }


    ////////////////////
    // Message Groups //
    ////////////////////

    /**
     * List Message Groups for Scene
     */
    @GET
    @Path("{sceneName}/list")
    @Produces(MediaType.TEXT_PLAIN)
    public Response listGroups(@PathParam("sceneName") String sceneName) {
        try {
            AbstractScene scene = findScene(sceneName);
            String result = "";
            for ( MessageGroup group : scene.getGroups() ) {
                result += group.getId()+"\n";
            }
            if ( result.length()>1 ) {
                result = result.substring(0, result.length() - 1);
            }
            return ok(result);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * List messages in Group
     */
    @GET
    @Path("{sceneName}/group/{groupId}/list")
    @Produces(MediaType.TEXT_PLAIN)
    public Response listGroupMessage(@PathParam("sceneName") String sceneName, @PathParam("groupId") String groupId) {
        try {
            MessageGroup group = findMessageGroup(sceneName, groupId);
            return ok(group.listMessages());
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @POST
    @Path("{sceneName}/group/{groupId}/add")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addGroupMessage(@PathParam("sceneName") String sceneName, @PathParam("groupId") String groupId, String messages) {
        try {
            findMessageGroup(sceneName, groupId).addMessages(messages);
            return ok("Message added to Group");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @POST
    @Path("{sceneName}/group/{groupId}/clear")
    @Produces(MediaType.TEXT_PLAIN)
    public Response clearGroupMessage(@PathParam("sceneName") String sceneName, @PathParam("groupId") String groupId) {
        return clearGroupMessage(sceneName, groupId, false);
    }

    public Response clearGroupMessage(String sceneName, String groupId, boolean keepPointer) {
        try {
            MessageGroup group = findMessageGroup(sceneName, groupId);
            group.clearMessages(!keepPointer);
            for ( TextZone zone : findZonesForGroup(sceneName, group) ) {
                if ( zone.getDirection()== DISPLAY ) {
                    zone.out();
                }
            }
            return ok("Message Group Cleared");
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    //////////////
    // Controls //
    //////////////

    @POST
    @Path("pause")
    @Produces(MediaType.TEXT_PLAIN)
    public Response pause() {
        sceneLoader.pause();
        return ok("Paused");
    }

    @POST
    @Path("resume")
    @Produces(MediaType.TEXT_PLAIN)
    public Response resume() {
        sceneLoader.resume();
        return ok("Resumed");
    }

    /////////////
    // Utility //
    /////////////

    private AbstractScene findScene(String sceneName) throws Exception {
        AbstractScene scene = sceneLoader.getCurrentScene();
        if ( scene==null || !scene.getName().equals(sceneName) ) {
            scene = sceneLoader.loadSceneFromFile("scenes/"+sceneName+".xml");
            if ( scene==null ) {
                throw new Exception("Scene '" + sceneName + "' not found");
            }
        }
        scene.setName(sceneName);
        sceneLoader.loadSceneMessages(scene);
        return scene;
    }

    private MessageGroup findMessageGroup(String sceneName, String groupId) throws Exception {
        AbstractScene scene = findScene(sceneName);
        MessageGroup group = scene.getGroup(groupId);
        if ( group!=null ) {
            return group;
        } else {
            throw new Exception("Could not find MessageGroup '"+groupId+"'");
        }
    }

    private List<TextZone> findZonesForGroup(String sceneName, MessageGroup group) throws Exception {
        AbstractScene scene = findScene(sceneName);
        List<TextZone> result = new ArrayList<>();
        for ( String zoneId : group.getZoneIds() ) {
            TextZone zone = (TextZone) scene.getZone(zoneId);
            if ( zone!=null ) {
                result.add(zone);
            } else {
                throw new Exception("Could not find TextZone '" + zoneId + "'");
            }
        }
        return result;
    }

}
