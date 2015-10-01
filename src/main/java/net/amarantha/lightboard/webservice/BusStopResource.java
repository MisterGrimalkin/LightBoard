package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.scene.impl.TravelInformationScene;
import net.amarantha.lightboard.updater.transport.BusTimesUpdater;

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
        return ok(travelInformationScene.getBusTimesUpdater().getBusStopJson().toString());
    }

    @POST
    @Path("enable")
    @Produces(MediaType.TEXT_PLAIN)
    public Response enableBus(@QueryParam("id") String id) {
        BusTimesUpdater busTimesUpdater = travelInformationScene.getBusTimesUpdater();
        busTimesUpdater.getBusDepartures().get(id).setActive(true);
        busTimesUpdater.saveBusConfig();
        busTimesUpdater.refresh();
        return ok("Bus info " + id + " enabled");
    }

    @POST
    @Path("disable")
    @Produces(MediaType.TEXT_PLAIN)
    public Response disableBus(@QueryParam("id") String id) {
        BusTimesUpdater busTimesUpdater = travelInformationScene.getBusTimesUpdater();
        busTimesUpdater.getBusDepartures().get(id).setActive(false);
        busTimesUpdater.saveBusConfig();
        busTimesUpdater.refresh();
        return ok("Bus info " + id + " disabled");
    }

}
