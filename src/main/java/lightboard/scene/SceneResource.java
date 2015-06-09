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

    @POST
    @Path("advance")
    public Response advanceScene() {
        SceneManager.advanceScene();
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Scene Advanced")
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScenes() {
        Map<Integer, Scene> scenes = SceneManager.getScenes();
        JSONObject jsonWrapper = new JSONObject();
        JSONArray ja = new JSONArray();
        JSONObject json = new JSONObject();
        for (Map.Entry<Integer, Scene> scene : scenes.entrySet() ) {
            if ( scene.getKey()!=0 ) {
                JSONObject jsonScene = new JSONObject();
                jsonScene.put("sceneId", scene.getKey() + "");
                jsonScene.put("sceneName", scene.getValue().getName());
                ja.add(jsonScene);
            }
        }
        jsonWrapper.put("scenes", ja);
        String result = jsonWrapper.toString();
        return  Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(result)
                .build();
    }

}
