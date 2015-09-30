package net.amarantha.lightboard.webservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.scene.impl.TravelInformationScene;

import javax.ws.rs.Path;

@Singleton
@Path("bus")
public class BusStopResource {

    private static TravelInformationScene travelInformationScene;

    @Inject
    public BusStopResource(TravelInformationScene travelInformationScene) {
        BusStopResource.travelInformationScene = travelInformationScene;
    }



}
