package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.scene.impl.TravelInformationScene;
import net.amarantha.lightboard.updater.transport.BusUpdater;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path("bus")
public class BusStopResource extends Resource {

    private static TravelInformationScene travelInformationScene;

    public BusStopResource() {    }

    @Inject
    public BusStopResource(TravelInformationScene travelInformationScene) {
        BusStopResource.travelInformationScene = travelInformationScene;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBusStops() {
        return ok(travelInformationScene.getBusUpdater().getBusStopJson().toString());
    }

    @POST
    @Path("enable")
    @Produces(MediaType.TEXT_PLAIN)
    public Response enableBus(@QueryParam("id") String id) {
        BusUpdater busUpdater = travelInformationScene.getBusUpdater();
        busUpdater.getBusDepartures().get(id).setActive(true);
        busUpdater.saveBusConfig();
        busUpdater.refresh();
        return ok("Bus info " + id + " enabled");
    }

    @POST
    @Path("disable")
    @Produces(MediaType.TEXT_PLAIN)
    public Response disableBus(@QueryParam("id") String id) {
        BusUpdater busUpdater = travelInformationScene.getBusUpdater();
        busUpdater.getBusDepartures().get(id).setActive(false);
        busUpdater.saveBusConfig();
        busUpdater.refresh();
        return ok("Bus info " + id + " disabled");
    }

}
