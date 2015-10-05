package net.amarantha.lightboard.scene.impl;

import com.google.inject.Singleton;
import net.amarantha.lightboard.font.LargeFont;
import net.amarantha.lightboard.font.SmallFont;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.updater.transport.BusUpdater;
import net.amarantha.lightboard.updater.transport.TubeUpdater;
import net.amarantha.lightboard.zone.impl.ClockZone;
import net.amarantha.lightboard.zone.impl.CompositeTextZone;
import net.amarantha.lightboard.zone.impl.TextZone;

import javax.inject.Inject;


@Singleton
public class TravelInformationScene extends Scene {

    @Inject private ClockZone clock;

    @Inject private CompositeTextZone busComposite;
    @Inject private TextZone busNumber;
    @Inject private TextZone busDestinationLeft;
    @Inject private TextZone busTimesLeft;
    @Inject private TextZone busDestinationRight;
    @Inject private TextZone busTimesRight;

    @Inject private TextZone tubeSummary;
    @Inject private TextZone tubeDetail;

    @Inject private BusUpdater busUpdater;
    @Inject private TubeUpdater tubeUpdater;

    @Inject
    public TravelInformationScene() {
        super("Travel Information");
    }

    @Override
    public void build() {

        clock.setRegion(getCols() - CLOCK_WIDTH, 0, CLOCK_WIDTH, BUSES_HEIGHT);

        // Bus
        int busFrameWidth = (getCols()-CLOCK_WIDTH-BUS_NUMBER_WIDTH)/2;

        busNumber
                .scrollRight()
                .setFont(new LargeFont())
                .setRestDuration(3200)
                .setMasterDelta(1)
                .setRegion(0, 0, BUS_NUMBER_WIDTH, BUSES_HEIGHT - 2);

        busDestinationLeft
                .scrollUp()
                .setRestDuration(3400)
                .setRegion(BUS_NUMBER_WIDTH, 0, busFrameWidth, BUSES_HEIGHT/2);

        busTimesLeft
                .scrollDown()
                .setRestDuration(3400)
                .setRegion(BUS_NUMBER_WIDTH, BUSES_HEIGHT/2, busFrameWidth, BUSES_HEIGHT/2);

        busDestinationRight
                .scrollUp()
                .setRestDuration(3600)
                .setRegion(BUS_NUMBER_WIDTH + busFrameWidth, 0, busFrameWidth, BUSES_HEIGHT/2);

        busTimesRight
                .scrollDown()
                .setRestDuration(3600)
                .setRegion(BUS_NUMBER_WIDTH + busFrameWidth, BUSES_HEIGHT/2, busFrameWidth, BUSES_HEIGHT / 2);

        busComposite
                .bindZones(busNumber, busDestinationLeft, busTimesLeft, busDestinationRight, busTimesRight)
                .setScrollTick(100);

        // Tube
        tubeDetail
                .scrollLeft()
                .setRestDuration(5000)
                .setRegion(0, BUSES_HEIGHT, getCols(), TUBE_HEIGHT);

        tubeSummary
                .fixed()
                .setFont(new SmallFont())
                .setRegion(0, getRows() - STATUS_HEIGHT, getCols(), STATUS_HEIGHT);

        registerZones(clock, busNumber, busComposite, tubeDetail, tubeSummary, busComposite);


        // Updaters

        busUpdater
                .setZones(busNumber, busDestinationLeft, busTimesLeft, busDestinationRight, busTimesRight)
                .setDataRefresh(15000);

        tubeUpdater
                .setZones(tubeDetail, tubeSummary)
                .setDataRefresh(60000);

        registerUpdaters(busUpdater, tubeUpdater);

//        testMode();

    }

    public BusUpdater getBusUpdater() {
        return busUpdater;
    }

    public TubeUpdater getTubeUpdater() {
        return tubeUpdater;
    }

    public ClockZone getClock() {
        return clock;
    }

    public CompositeTextZone getBusComposite() {
        return busComposite;
    }

    public TextZone getBusNumber() {
        return busNumber;
    }

    public TextZone getBusDestinationLeft() {
        return busDestinationLeft;
    }

    public TextZone getBusTimesLeft() {
        return busTimesLeft;
    }

    public TextZone getBusDestinationRight() {
        return busDestinationRight;
    }

    public TextZone getBusTimesRight() {
        return busTimesRight;
    }

    public TextZone getTubeSummary() {
        return tubeSummary;
    }

    public TextZone getTubeDetail() {
        return tubeDetail;
    }

    private static final int BUS_NUMBER_WIDTH = 24;
    private static final int CLOCK_WIDTH = 20;

    private static final int BUSES_HEIGHT = 18;
    private static final int TUBE_HEIGHT = 8;
    private static final int STATUS_HEIGHT = 5;

}
