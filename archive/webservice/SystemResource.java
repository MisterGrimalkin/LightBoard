package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.Colour;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.font.SmallFont;
import net.amarantha.lightboard.scene.SceneLoader;
import net.amarantha.lightboard.scene.old.OldSceneManager;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.PropertyManager;
import net.amarantha.lightboard.utility.Sync;

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

    private static OldSceneManager sceneManager;
    private static PropertyManager props;
    private static LightBoardSurface surface;
    private static SceneLoader sceneLoader;
    private static Sync sync;

    public SystemResource() {
    }

    @Inject
    public SystemResource(Sync sync, LightBoardSurface surface, OldSceneManager sceneManager, PropertyManager props, SceneLoader sceneLoader) {
        SystemResource.sceneManager = sceneManager;
        SystemResource.props = props;
        SystemResource.surface = surface;
        SystemResource.sync = sync;
        SystemResource.sceneLoader = sceneLoader;
    }

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

//    public static void detectMessageServer() {
//        if ( ip!=null ) {
//            if ( serverIp!=null ) {
//                try {
//                    System.out.println("Getting Ticket Server IP from properties");
//                    Post p = Http.post(serverIp + ":8002/ticketserver/register", ip);
//                    if (p.responseCode() != 200) {
//                        serverIp = null;
//                        detectMessageServer();
//                    }
//                } catch ( Exception e ) {
//                    serverIp = null;
//                }
//            }
//            if ( serverIp==null ) {
//                final Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    private boolean scanning = false;
//                    @Override
//                    public void run() {
//                        if ( !scanning ) {
//                            System.out.println("Scanning for Ticket Server....");
//                            scanning = true;
//                            for (int i = 1; i < 21; i++) {
//                                try {
//                                    System.out.println("Scanning 192.168.0." + i);
//                                    Post p = Http.post("http://192.168.0." + i + ":8002/ticketserver/register", ip);
//                                    if (p.responseCode() == 200) {
//                                        serverIp = "http://192.168.0." + i;
//                                        FileOutputStream output = new FileOutputStream("lightboard.properties");
//                                        prop.setProperty("serverIp", serverIp);
//                                        prop.store(output, null);
//                                        timer.cancel();
//                                        scanning = false;
//                                        break;
//                                    }
//                                } catch (Exception e) { }
//                            }
//                            System.out.println(serverIp != null ? "Registered with Ticket Server at " + serverIp : "Could not locate Ticket Server!");
//                            scanning = false;
//                        }
//                    }
//                }, 0, 20000);
//            }
//        }
//    }
//
//    public static String getIp() {
//        return ip;
//    }

    @GET
    @Path("name")
    @Produces(MediaType.TEXT_PLAIN)
    public static Response getName() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(props.getString("name", "LightBoard-" + (int)Math.floor(Math.random()*100000)))
                .build();
    }

    @GET
    @Path("ticket-server")
    @Produces(MediaType.TEXT_PLAIN)
    public static Response getServerIp() {
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity(serverIp)
                .build();
    }

//    @POST
//    @Path("ticket-server")
//    @Produces(MediaType.TEXT_PLAIN)
//    public static Response refreshServerIp() {
//        serverIp = null;
////        detectMessageServer();
//        return Response.ok()
//                .header("Access-Control-Allow-Origin", "*")
//                .entity("Resetting Ticket Server Connection")
//                .build();
//    }


    @POST
    @Path("shutdown")
    @Produces(MediaType.TEXT_PLAIN)
    public static Response shutdown() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                surface.clearSurface();
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
        Pattern shutdownPattern = new SmallFont().renderString("{red}SHUTTING DOWN");
        int x = (surface.getCols() - shutdownPattern.getWidth()) / 2;
        int y =(surface.getRows() - shutdownPattern.getHeight()) / 2;
        surface.outlineRegion(LightBoardSurface.LAYERS-1, Colour.RED, surface.safeRegion(x-2, y-2, shutdownPattern.getWidth()+4, shutdownPattern.getHeight()+4));
        surface.drawPattern(LightBoardSurface.LAYERS-1, x, y, shutdownPattern);
    }

    @POST
    @Path("sleep")
    @Produces(MediaType.TEXT_PLAIN)
    public Response sleep() {
        sceneManager.sleep();
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Board has gone to sleep")
                .build();
    }

    @POST
    @Path("wake")
    @Produces(MediaType.TEXT_PLAIN)
    public Response wake() {
        sceneManager.wake();
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Board has woken up")
                .build();
    }

}
