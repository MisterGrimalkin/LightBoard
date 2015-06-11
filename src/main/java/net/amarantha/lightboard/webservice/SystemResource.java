package net.amarantha.lightboard.webservice;

import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.util.Sync;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

@Path("system")
public class SystemResource {

    private static String name = "";

    public static void loadConfig() {
        try {
            Properties prop = new Properties();
            InputStream is = new FileInputStream("lightboard.properties");
            prop.load(is);
            if ( prop.getProperty("name")!=null ) {
                name = prop.getProperty("name");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GET
    @Path("name")
    @Produces(MediaType.TEXT_PLAIN)
    public static Response getName() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(name)
                .build();
    }

    @POST
    @Path("shutdown")
    @Produces(MediaType.TEXT_PLAIN)
    public Response shutdown() {
        WebService.stopWebService();
        Sync.stopSyncThread();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 5000);
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("So long and thanks for all the fish")
                .build();
    }

    @POST
    @Path("sleep")
    @Produces(MediaType.TEXT_PLAIN)
    public Response sleep() {
        SceneManager.sleep();
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Board has gone to sleep")
                .build();
    }

    @POST
    @Path("wake")
    @Produces(MediaType.TEXT_PLAIN)
    public Response wake() {
        SceneManager.wake();
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Board has woken up")
                .build();
    }

}
