package lightboard.scene.impl;

import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.impl.ClockZone;
import lightboard.board.zone.impl.TextZone;
import lightboard.scene.Scene;
import lightboard.updater.Updater;
import lightboard.updater.UpdaterBundle;
import lightboard.updater.transport.BusTimesUpdater;
import lightboard.updater.transport.TubeStatusUpdater;

public class TravelInformationScene extends Scene {

    private static final int CLOCK_WIDTH = 21;
    private static final int CLOCK_HEIGHT = 7;

    public TravelInformationScene(LightBoardSurface surface) {
        super(surface);
    }

    @Override
    public void build() {

        // Tube Status
        TextZone tubeStatusZone = TextZone.scrollLeft(getSurface());
        tubeStatusZone
            .setScrollTick(25)
            .setRestDuration(1000)
            .setRegion(
                0, getRows()/2, getCols()-CLOCK_WIDTH, getRows()/2
            );

        // Bus Arrivals
        TextZone busArrivalsZone = TextZone.scrollUp(getSurface());
        busArrivalsZone
            .setScrollTick(60)
            .setRestDuration(2000)
            .setRegion(
                    0, 0, getCols() - CLOCK_WIDTH, getRows() / 2
            );

        // Bundle Travel Updater
        int numberOfBuses = 3;
        Updater travelUpdater = UpdaterBundle.bundle(
                new TubeStatusUpdater(tubeStatusZone, "bad"),
                new BusTimesUpdater(busArrivalsZone, 53785, "W7", "Mus Hill", numberOfBuses),
                new BusTimesUpdater(busArrivalsZone, 56782, "W7", "Fins Pk", numberOfBuses),
                new BusTimesUpdater(busArrivalsZone, 76713, "W5", "Harringay", numberOfBuses, -3),
                new BusTimesUpdater(busArrivalsZone, 76713, "41", "Tottenham", numberOfBuses),
                new BusTimesUpdater(busArrivalsZone, 76985, "W5", "Archway", numberOfBuses),
                new BusTimesUpdater(busArrivalsZone, 56403, "41", "Archway", numberOfBuses),
                new BusTimesUpdater(busArrivalsZone, 56403, "91", "Traf Sqr", numberOfBuses)
        );
        travelUpdater.setDataRefresh(58000);

        // Clock
        TextZone clockZone = new ClockZone(getSurface());
        clockZone.setRegion(getCols() - CLOCK_WIDTH, 0, CLOCK_WIDTH, getRows());

        // Setup Scene
        registerZones(clockZone, tubeStatusZone, busArrivalsZone);
        registerUpdaters(travelUpdater);

    }

}
