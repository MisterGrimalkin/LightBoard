package net.amarantha.lightboard.webservice;

import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.util.Sync;
import org.javalite.http.Http;
import org.javalite.http.Post;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

@Path("system")
public class SystemResource {

    private static String name = null;
    private static String ip = null;
    private static String serverIp = null;
    private static Properties prop;

    public static void loadConfig() {
        try {
            String message = "LightBoard configuration: ";
            prop = new Properties();
            InputStream is = new FileInputStream("lightboard.properties");
            prop.load(is);
            if ( prop.getProperty("name")!=null ) {
                name = prop.getProperty("name");
                message += "'" + name + "'";
            }
            if ( prop.getProperty("ip")!=null ) {
                ip = prop.getProperty("ip");
                message += " serving on " + ip;
            }
            if ( prop.getProperty("serverIp")!=null ) {
                serverIp = prop.getProperty("serverIp");
            }
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void detectMessageServer() {
        if ( ip!=null ) {
            if ( serverIp!=null ) {
                Post p = Http.post(serverIp + ":8002/ticketserver/register", ip);
                if (p.responseCode() != 200) {
                    serverIp = null;
                    detectMessageServer();
                }
            } else {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Scanning for Ticket Server....");
                        for (int i = 1; i < 256; i++) {
                            try {
                                Post p = Http.post("http://192.168.0." + i + ":8002/ticketserver/register", ip);
                                if (p.responseCode() == 200) {
                                    serverIp = "http://192.168.0." + i;
                                    FileOutputStream output = new FileOutputStream("lightboard.properties");
                                    prop.setProperty("serverIp", serverIp);
                                    prop.store(output, null);
                                    break;
                                }
                            } catch (Exception e) {
                            }
                        }
                        System.out.println(serverIp != null ? "Registered with Ticket Server at " + serverIp : "Could not locate Ticket Server!");
                    }
                }, 0);
            }
        }
    }

    public static String getIp() {
        return ip;
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
