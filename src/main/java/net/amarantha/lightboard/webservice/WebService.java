package net.amarantha.lightboard.webservice;

import com.google.inject.Singleton;
import net.amarantha.lightboard.utility.LightBoardProperties;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Inject;
import java.net.URI;

@Singleton
public class WebService {

    private HttpServer server;

    @Inject private BroadcastMessageResource broadcastMessageResource;
    @Inject private ColourResource colourResource;
    @Inject private SceneResource sceneResource;
    @Inject private SystemResource systemResource;
    @Inject private BusStopResource busStopResource;

    @Inject private LightBoardProperties props;

    public HttpServer startWebService() {

        System.out.println("Starting Web Service....");

        String fullUri = "http://" + props.getIp().trim() + ":8001/lightboard/";

        try {
            final ResourceConfig rc = new ResourceConfig().packages("net.amarantha.lightboard.webservice");
            rc.register(LoggingFilter.class);
            server = GrizzlyHttpServerFactory.createHttpServer(URI.create(fullUri), rc);
            System.out.println("Web Service Online @ " + fullUri);
        } catch ( Exception e ) {
            System.out.println("Could not start Web Service!");
            e.printStackTrace();
        }


        return server;
    }

    public void stopWebService() {
        if ( server!=null ) {
            server.shutdown();
        }
    }


}
