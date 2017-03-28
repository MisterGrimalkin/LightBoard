package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import net.amarantha.lightboard.Main;
import net.amarantha.lightboard.entity.Colour;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.font.SmallFont_Old;
import net.amarantha.lightboard.scene.SceneLoader;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.utils.properties.PropertiesService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Timer;
import java.util.TimerTask;

@Path("system")
public class SystemResource extends Resource {

    private static PropertiesService props;
    private static LightBoardSurface surface;
    private static SceneLoader sceneLoader;
    private static Sync sync;

    public SystemResource() {
    }

    @Inject
    public SystemResource(Sync sync, LightBoardSurface surface, PropertiesService props, SceneLoader sceneLoader) {
        SystemResource.props = props;
        SystemResource.surface = surface;
        SystemResource.sync = sync;
        SystemResource.sceneLoader = sceneLoader;
    }

    @GET
    @Path("name")
    @Produces(MediaType.TEXT_PLAIN)
    public static Response getName() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(props.getString("name", "LightBoard-" + (int)Math.floor(Math.random()*100000)))
                .build();
    }

    @POST
    @Path("shutdown")
    @Produces(MediaType.TEXT_PLAIN)
    public static Response shutdown() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                surface.clearSurface();
                surface.sleep();
                System.exit(0);
            }
        }, 2000);
        displayShutdownMessage();
        System.out.println("System Shutting Down");
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("So long and thanks for all the fish")
                .build();
    }

    private static void displayShutdownMessage() {
        sceneLoader.stop();
        surface.clearSurface();
        Pattern shutdownPattern = new SmallFont_Old().renderString("{red}SHUTTING DOWN");
        int x = (surface.getCols() - shutdownPattern.getWidth()) / 2;
        int y =(surface.getRows() - shutdownPattern.getHeight()) / 2;
        surface.outlineRegion(LightBoardSurface.LAYERS-1, Colour.RED, surface.safeRegion(x-2, y-2, shutdownPattern.getWidth()+4, shutdownPattern.getHeight()+4));
        surface.drawPattern(LightBoardSurface.LAYERS-1, x, y, shutdownPattern);
    }

    @POST
    @Path("sleep")
    @Produces(MediaType.TEXT_PLAIN)
    public Response sleep() {
        surface.sleep();
        return ok("Sleeping");
    }

    @POST
    @Path("wake")
    @Produces(MediaType.TEXT_PLAIN)
    public Response wake() {
        surface.wake();
        return ok("Awake");
    }

    @POST
    @Path("test/squares")
    public Response testSquares() {
        surface.testSquares();
        return ok("Squares");
    }

    @POST
    @Path("test/scan/{colour}")
    public Response testScan(@PathParam("colour") String colourString, @QueryParam("size") Integer size) {
        if ( props.isArgumentPresent(Main.TEST_MODE) ) {
            surface.testScan(decodeColour(colourString), size==null ? 10 : size);
            return ok("TEST MODE single colour");
        } else {
            return error("System is not in test mode");
        }
    }

    @POST
    @Path("test/fill/{colour}")
    public Response testFill(@PathParam("colour") String colourString) {
        if ( props.isArgumentPresent(Main.TEST_MODE) ) {
            surface.testFill(decodeColour(colourString));
            return ok("TEST MODE single colour");
        } else {
            return error("System is not in test mode");
        }
    }

    private Colour decodeColour(String colourString) {
        switch ( colourString ) {
            case Colour.RED_STR:
                return Colour.RED;
            case Colour.GREEN_STR:
                return Colour.GREEN;
            case Colour.YELLOW_STR:
                return Colour.YELLOW;
            default:
                return Colour.BLACK;
        }

    }

}
