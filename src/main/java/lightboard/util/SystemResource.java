package lightboard.util;

import lightboard.scene.SceneManager;
import lightboard.updater.WebService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

@Path("system")
public class SystemResource {

    @POST
    @Path("shutdown")
    public Response shutdown() {
        System.out.println("Shutting Down....");
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
                .entity("So Long!")
                .build();
    }

    @POST
    @Path("sleep")
    public Response sleep() {
        System.out.println("Sleeping");
        SceneManager.sleep();
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Sleeping")
                .build();
    }

    @POST
    @Path("wake")
    public Response wake() {
        System.out.println("Waking");
        SceneManager.wake();
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("So Long!")
                .build();
    }

    @GET
    @Path("health")
    @Produces(MediaType.TEXT_PLAIN)
    public static Response heathCheck() {
        System.out.println("Health Check OK");
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("LightBoard Service is Alive").build();
    }

    public static void readName() {
        try {
            Properties prop = new Properties();
            InputStream is = new FileInputStream("lightboard.properties");
            prop.load(is);
            if ( prop.getProperty("name")!=null ) {
                name = prop.getProperty("name");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static String name = "";

    @GET
    @Path("name")
    @Produces(MediaType.TEXT_PLAIN)
    public static Response getName() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(name)
                .build();
    }
}
