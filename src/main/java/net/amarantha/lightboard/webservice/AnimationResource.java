package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.BlockTestScene;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("animation")
public class AnimationResource extends Resource {

    private static BlockTestScene scene;

    public AnimationResource() {}

    @Inject
    public AnimationResource(BlockTestScene scene) {
        AnimationResource.scene = scene;
    }

    @POST
    @Path("tap/{button}")
    public void processTap(@PathParam("button") int button) {
        scene.tap(button);
    }

}
