package lightboard.scene;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("scene")
public class SceneResource {

    @POST
    public String loadScene(@QueryParam("id") int id) {
        if ( SceneManager.loadScene(id) ) {
            return "Scene " + id + " loaded";
        } else {
            return "Scene " + id + " not found";
        }
    }

    @POST
    @Path("advance")
    public void advanceScene() {
        SceneManager.advanceScene();
    }

}
