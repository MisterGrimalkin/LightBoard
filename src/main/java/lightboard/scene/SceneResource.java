package lightboard.scene;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("scene")
public class SceneResource {

    @POST
    @Path("load")
    public Response loadScene(@QueryParam("id") int id) {
        if ( SceneManager.loadScene(id) ) {
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

    @GET
    @Path("current")
    public Response getCurrentScene() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(SceneManager.getCurrentSceneId())
                .build();
    }

    @POST
    @Path("cycle")
    public Response cycleScene(@QueryParam("id") int id, @QueryParam("cycle") boolean cycle) {
        SceneManager.getScenes().get(id).setIncludeInCycle(cycle);
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Scene " + id + " Loaded")
                .build();
    }

    @POST
    @Path("advance")
    public Response advanceScene() {
        SceneManager.advanceScene();
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Scene Advanced")
                .build();
    }

    @POST
    @Path("cycle-on")
    public Response cycleOn() {
        SceneManager.setCycleMode(true);
        return  Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Cycle Mode Enabled")
                .build();
    }

    @POST
    @Path("cycle-off")
    public Response cycleOff() {
        SceneManager.setCycleMode(false);
        return  Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Cycle Mode Disable")
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenes() {
        Map<Integer, Scene> scenes = SceneManager.getScenes();
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
        json.put("cycleMode", SceneManager.getCycleMode());
        json.put("scenes", ja);
        String result = json.toString();
        return  Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(result)
                .build();
    }

}
