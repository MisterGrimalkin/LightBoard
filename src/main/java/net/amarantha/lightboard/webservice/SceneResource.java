package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.SceneLoader;
import net.amarantha.lightboard.zone.AbstractZone;
import net.amarantha.lightboard.zone.TextZone;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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

    @POST
    @Path("{sceneName}/load")
    @Produces(MediaType.TEXT_PLAIN)
    public Response loadScene(@PathParam("sceneName") String sceneName) {
        String result = sceneLoader.loadScene(sceneName) ? "Scene Loaded" : "Do Not Know Scene: " + sceneName;
        return ok(result+"\n");
    }

    @POST
    @Path("{sceneName}/message/{zoneId}/add")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addMessage(@PathParam("sceneName") String sceneName, @PathParam("zoneId") String zoneId, String message) {
        String result;
        try {
            AbstractZone zone = findTextZone(sceneName, zoneId);
            ((TextZone)zone).addMessage(message);
            result = "Message Added: "+sceneName+"/"+zoneId;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return ok(result+"\n");
    }

    @POST
    @Path("{sceneName}/message/{zoneId}/clear")
    @Produces(MediaType.TEXT_PLAIN)
    public Response clearMessages(@PathParam("sceneName") String sceneName, @PathParam("zoneId") String zoneId) {
        String result;
        try {
            AbstractZone zone = findTextZone(sceneName, zoneId);
            ((TextZone)zone).clearMessages();
            result = "Messages Cleared: "+sceneName+"/"+zoneId;
        } catch (Exception e) {
            result = e.getMessage();
        }
        return ok(result+"\n");
    }

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

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getScenes() {
//        Map<Integer, OldScene> scenes = sceneManager.getScenes();
//        JSONObject json = new JSONObject();
//        JSONArray ja = new JSONArray();
//        for (Map.Entry<Integer, OldScene> scene : scenes.entrySet() ) {
//            if ( scene.getKey()!=0 ) {
//                JSONObject jsonScene = new JSONObject();
//                jsonScene.put("sceneId", scene.getKey() + "");
//                jsonScene.put("sceneName", scene.getValue().getName());
//                jsonScene.put("inCycle", scene.getValue().isIncludeInCycle());
//                ja.add(jsonScene);
//            }
//        }
//        json.put("cycleMode", sceneManager.getCycleMode());
//        json.put("scenes", ja);
//        String result = json.toString();
//        return  Response.ok()
//                .header("Access-Control-Allow-Origin", "*")
//                .entity(result)
//                .build();
//    }
//
//    @GET
//    @Path("current")
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response getCurrentScene() {
//        return Response.ok()
//                .header("Access-Control-Allow-Origin", "*")
//                .entity(sceneManager.getCurrentSceneId())
//                .build();
//    }
//
//    @POST
//    @Path("load")
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response loadScene(@QueryParam("id") int id) {
//        if ( sceneManager.loadScene(id) ) {
//            return Response.ok()
//                    .header("Access-Control-Allow-Origin", "*")
//                    .entity("Scene " + id + " Loaded")
//                    .build();
//        } else {
//            return Response.notModified()
//                    .header("Access-Control-Allow-Origin", "*")
//                    .entity("Scene " + id + " not found")
//                    .build();
//        }
//    }

//    @POST
//    @Path("cycle")
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response cycleScene(@QueryParam("id") int id, @QueryParam("cycle") boolean cycle) {
//        sceneManager.getScenes().get(id).setIncludeInCycle(cycle);
//        return Response.ok()
//                .header("Access-Control-Allow-Origin", "*")
//                .entity("Scene " + id + " cycle mode updated to " + cycle)
//                .build();
//    }
//
//    @POST
//    @Path("advance")
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response advanceScene() {
//        sceneManager.advanceScene();
//        return Response.ok()
//                .header("Access-Control-Allow-Origin", "*")
//                .entity("Scene Advanced")
//                .build();
//    }
//
//    @POST
//    @Path("cycle-on")
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response cycleOn() {
//        sceneManager.setCycleMode(true);
//        return  Response.ok()
//                .header("Access-Control-Allow-Origin", "*")
//                .entity("Cycle Mode Enabled")
//                .build();
//    }
//
//    @POST
//    @Path("cycle-off")
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response cycleOff() {
//        sceneManager.setCycleMode(false);
//        return  Response.ok()
//                .header("Access-Control-Allow-Origin", "*")
//                .entity("Cycle Mode Disable")
//                .build();
//    }
//
}
