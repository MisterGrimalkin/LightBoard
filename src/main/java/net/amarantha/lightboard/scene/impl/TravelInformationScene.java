package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.font.SmallFont;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.updater.UpdaterBundle;
import net.amarantha.lightboard.updater.transport.BusTimesUpdater;
import net.amarantha.lightboard.updater.transport.TubeStatusUpdater;
import net.amarantha.lightboard.zone.impl.ClockZone;
import net.amarantha.lightboard.zone.impl.TextZone;

public class TravelInformationScene extends Scene {

    private static final int CLOCK_WIDTH = 23;
    private static final int STATUS_HEIGHT = 7;

    public TravelInformationScene(LightBoardSurface surface) {
        super(surface, "Travel Information");
    }


    @Override
    public void build() {

        // Bus Arrivals
        TextZone busArrivalsZone = TextZone.scrollUp(getSurface());
        busArrivalsZone
                .setScrollTick(60)
                .setRestDuration(1500)
                .setRegion(0, 0, getCols()-CLOCK_WIDTH, (getRows()-STATUS_HEIGHT)/2);

        // Tube Status Detail
        TextZone tubeStatusZone = TextZone.scrollLeft(getSurface());
        tubeStatusZone
                .setScrollTick(25)
                .setRestDuration(1000)
                .setRegion(0, (getRows()-STATUS_HEIGHT)/2, getCols()-CLOCK_WIDTH, (getRows()-STATUS_HEIGHT)/2);

        // Tube Status Summary
        TextZone tubeStatusSummaryZone = TextZone.fixed(getSurface());
        tubeStatusSummaryZone
                .setFont(new SmallFont())
            .setScrollTick(1000)
            .setRestDuration(1000)
            .setRegion(0, getRows()-STATUS_HEIGHT, getCols(), STATUS_HEIGHT);

        // Bundle Travel Updater
        int numberOfBuses = 3;
        Updater travelUpdater = UpdaterBundle.bundle(
                new TubeStatusUpdater(tubeStatusZone, tubeStatusSummaryZone, "bad"),
                new BusTimesUpdater(busArrivalsZone, 53785, "W7", "Mus Hill", numberOfBuses),
                new BusTimesUpdater(busArrivalsZone, 56782, "W7", "Fins Pk", numberOfBuses),
                new BusTimesUpdater(busArrivalsZone, 76713, "W5", "Harringay", numberOfBuses, -3),
                new BusTimesUpdater(busArrivalsZone, 76985, "W5", "Archway", numberOfBuses, 3),
                new BusTimesUpdater(busArrivalsZone, 76713, "41", "Tottenham", numberOfBuses),
                new BusTimesUpdater(busArrivalsZone, 56403, "41", "Archway", numberOfBuses),
                new BusTimesUpdater(busArrivalsZone, 56403, "91", "Traf Sqr", numberOfBuses),
                new BusTimesUpdater(busArrivalsZone, 56403, "N91", "Traf Sqr", numberOfBuses)
        );
        travelUpdater.setDataRefresh(58000);

        // Clock
        TextZone clockZone = new ClockZone(getSurface());
        clockZone.setRegion(getCols() - CLOCK_WIDTH, 0, CLOCK_WIDTH, getRows()-STATUS_HEIGHT);

        // Setup Scene
        registerZones(clockZone, tubeStatusZone, tubeStatusSummaryZone, busArrivalsZone);
        registerUpdaters(travelUpdater);

    }

}
