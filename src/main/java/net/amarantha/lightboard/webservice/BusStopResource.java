package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.scene.impl.TravelInformationScene;
import net.amarantha.lightboard.updater.transport.BusDeparture;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

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
        JSONObject json = new JSONObject();
        JSONArray ja = new JSONArray();

        for ( Map.Entry<String, BusDeparture> entry : travelInformationScene.getBusTimesUpdater().getBusDepartures().entrySet() ) {
            JSONObject jsonBus = new JSONObject();
            jsonBus.put("id", entry.getKey());
            BusDeparture bus = entry.getValue();
            jsonBus.put("stop", bus.getStopCode());
            jsonBus.put("bus", bus.getBusNo());
            jsonBus.put("destination", bus.getDestination());
            jsonBus.put("offset", bus.getOffset());
            jsonBus.put("active", bus.isActive());
            ja.add(jsonBus);
        }

        json.put("buses", ja);
        return ok(json.toString());
    }


}
