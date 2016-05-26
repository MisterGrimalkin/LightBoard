package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.scene.old.TravelInformationOldScene;
import net.amarantha.lightboard.updater.transport.BusUpdater;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path("bus")
public class BusStopResource extends Resource {

    private static TravelInformationOldScene travelInformationScene;

    public BusStopResource() {    }

    @Inject
    public BusStopResource(TravelInformationOldScene travelInformationScene) {
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
        getBusUpdater().enableBusStop(id);
        return ok("Bus info " + id + " enabled");
    }

    @POST
    @Path("disable")
    @Produces(MediaType.TEXT_PLAIN)
    public Response disableBus(@QueryParam("id") String id) {
        getBusUpdater().disableBusStop(id);
        return ok("Bus info " + id + " disabled");
    }

    @POST
    @Path("remove")
    @Produces(MediaType.TEXT_PLAIN)
    public Response removeBus(@QueryParam("id") String id) {
        getBusUpdater().removeBusStop(id);
        return ok("Bus info " + id + " removed");
    }

    @POST
    @Path("add")
    @Produces(MediaType.TEXT_PLAIN)
    public Response addBus(
        @QueryParam("stopCode") long stopCode,
        @QueryParam("busNo") String busNo,
        @QueryParam("destination") String destination,
        @QueryParam("offset") int offset
    ) {
        String id = getBusUpdater().addBusStop(stopCode, busNo, destination, offset);
        return ok("Bus info " + id + " added");
    }

    @POST
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateBus(
        @QueryParam("id") String id,
        @QueryParam("stopCode") long stopCode,
        @QueryParam("busNo") String busNo,
        @QueryParam("destination") String destination,
        @QueryParam("offset") int offset
    ) {
        getBusUpdater().updateBusStop(id, stopCode, busNo, destination, offset);
        return ok("Bus info " + id + " updated");
    }

    private BusUpdater getBusUpdater() {
        return travelInformationScene.getBusUpdater();
    }

}
