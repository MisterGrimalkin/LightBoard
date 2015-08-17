package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("scene")
public class SceneResource {

    private static SceneManager sceneManager;

    public SceneResource() {
    }

    @Inject
    public SceneResource(SceneManager sceneManager) {
        SceneResource.sceneManager = sceneManager;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenes() {
        Map<Integer, Scene> scenes = sceneManager.getScenes();
        JSONObject json = new JSONObject();
        JSONArray ja = new JSONArray();
        for (Map.Entry<Integer, Scene> scene : scenes.entrySet() ) {
            if ( scene.getKey()!=0 ) {
                JSONObject jsonScene = new JSONObject();
                jsonScene.put("sceneId", scene.getKey() + "");
                jsonScene.put("sceneName", scene.getValue().getName());
                jsonScene.put("inCycle", scene.getValue().isIncludeInCycle());
                ja.add(jsonScene);
            }
        }
        json.put("cycleMode", sceneManager.getCycleMode());
        json.put("scenes", ja);
        String result = json.toString();
        return  Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(result)
                .build();
    }

    @GET
    @Path("current")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getCurrentScene() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(sceneManager.getCurrentSceneId())
                .build();
    }

    @POST
    @Path("load")
    @Produces(MediaType.TEXT_PLAIN)
    public Response loadScene(@QueryParam("id") int id) {
        if ( sceneManager.loadScene(id) ) {
            return Response.ok()
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("Scene " + id + " Loaded")
                    .build();
        } else {
            return Response.notModified()
                    .header("Access-Control-Allow-Origin", "*")
                    .entity("Scene " + id + " not found")
                    .build();
        }
    }

    @POST
    @Path("cycle")
    @Produces(MediaType.TEXT_PLAIN)
    public Response cycleScene(@QueryParam("id") int id, @QueryParam("cycle") boolean cycle) {
        sceneManager.getScenes().get(id).setIncludeInCycle(cycle);
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Scene " + id + " cycle mode updated to " + cycle)
                .build();
    }

    @POST
    @Path("advance")
    @Produces(MediaType.TEXT_PLAIN)
    public Response advanceScene() {
        sceneManager.advanceScene();
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Scene Advanced")
                .build();
    }

    @POST
    @Path("cycle-on")
    @Produces(MediaType.TEXT_PLAIN)
    public Response cycleOn() {
        sceneManager.setCycleMode(true);
        return  Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Cycle Mode Enabled")
                .build();
    }

    @POST
    @Path("cycle-off")
    @Produces(MediaType.TEXT_PLAIN)
    public Response cycleOff() {
        sceneManager.setCycleMode(false);
        return  Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Cycle Mode Disable")
                .build();
    }

}
