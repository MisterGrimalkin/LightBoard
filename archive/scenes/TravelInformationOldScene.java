package net.amarantha.lightboard.scene.old;

import com.google.inject.Singleton;
import net.amarantha.lightboard.font.LargeFont;
import net.amarantha.lightboard.font.SmallFont;
import net.amarantha.lightboard.updater.transport.BusUpdater;
import net.amarantha.lightboard.updater.transport.TubeUpdater;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.webservice.ColourResource;
import net.amarantha.lightboard.zone.old.ClockZoneOld;
import net.amarantha.lightboard.zone.old.CompositeZone;
import net.amarantha.lightboard.zone.old.TextZone_Old;

import javax.inject.Inject;


@Singleton
public class TravelInformationOldScene extends OldScene {

    @Inject private ClockZoneOld clock;

    @Inject private CompositeZone busComposite;
    @Inject private TextZone_Old busNumber;
    @Inject private TextZone_Old busDestinationLeft;
    @Inject private TextZone_Old busTimesLeft;
    @Inject private TextZone_Old busDestinationRight;
    @Inject private TextZone_Old busTimesRight;

    @Inject private TextZone_Old tubeSummary;
    @Inject private TextZone_Old tubeDetail;

    @Inject private BusUpdater busUpdater;
    @Inject private TubeUpdater tubeUpdater;

    @Inject private ColourResource colourResource;
    @Inject private LightBoardProperties props;

    @Inject
    public TravelInformationOldScene() {
        super("Travel Information");
    }

    @Override
    public void build() {

        clock.setRegion(getCols() - CLOCK_WIDTH, 0, CLOCK_WIDTH, BUSES_HEIGHT);

        // Bus
        int busFrameWidth = (getCols()-CLOCK_WIDTH-BUS_NUMBER_WIDTH)/2;

        busNumber
                .setFont(new LargeFont())
                .scrollRight()
                .setRestDuration(3000)
                .setMasterDelta(2)
                .setRegion(0, 0, BUS_NUMBER_WIDTH, BUSES_HEIGHT - 2);

        busDestinationLeft
                .scrollUp()
                .setRestDuration(3000)
                .setRegion(BUS_NUMBER_WIDTH, 0, busFrameWidth, BUSES_HEIGHT/2);

        busTimesLeft
                .scrollDown()
                .setRestDuration(3000)
                .setRegion(BUS_NUMBER_WIDTH, BUSES_HEIGHT/2, busFrameWidth, BUSES_HEIGHT/2);

        busDestinationRight
                .scrollUp()
                .setRestDuration(3000)
                .setRegion(BUS_NUMBER_WIDTH + busFrameWidth, 0, busFrameWidth, BUSES_HEIGHT/2);

        busTimesRight
                .scrollDown()
                .setRestDuration(3000)
                .setRegion(BUS_NUMBER_WIDTH + busFrameWidth, BUSES_HEIGHT/2, busFrameWidth, BUSES_HEIGHT / 2);

        busComposite
                .bindZones(busNumber, busDestinationLeft, busTimesLeft, busDestinationRight, busTimesRight)
                .setScrollTick(30);

        // Tube
        tubeDetail
                .scrollLeft()
                .setRestDuration(5000)
                .setRegion(0, BUSES_HEIGHT, getCols(), props.showTubeSummary() ? TUBE_HEIGHT : TUBE_HEIGHT + STATUS_HEIGHT );

        registerZones(clock, busComposite, tubeDetail);

        if ( props.showTubeSummary() ) {
            tubeSummary
                    .setFont(new SmallFont())
                    .fixed()
                    .setRegion(0, getRows() - STATUS_HEIGHT, getCols(), STATUS_HEIGHT);
            registerZones(tubeSummary);
        }

        busComposite.addScrollCompleteHandler(this::updateBusInformation);

        // Updaters

        busUpdater
                .setDataRefresh(15000);

        tubeUpdater
                .setZones(tubeDetail, tubeSummary)
                .setDataRefresh(60000);

        registerUpdaters(busUpdater, tubeUpdater);

    }

    private String lastColour = null;

    @Override
    public void resume() {
        lastColour = colourResource.getColour();
        colourResource.colour(props.getString("travelInfoColour", "multi"));
        super.resume();
    }

    @Override
    public void pause() {
        if ( lastColour!=null ) {
            colourResource.colour(lastColour);
        }
        super.pause();
    }

    private void updateBusInformation() {
        busUpdater.updateZones(busNumber, busDestinationLeft, busTimesLeft, busDestinationRight, busTimesRight);
    }

    public BusUpdater getBusUpdater() {
        return busUpdater;
    }

    public TubeUpdater getTubeUpdater() {
        return tubeUpdater;
    }

    public ClockZoneOld getClock() {
        return clock;
    }

    public CompositeZone getBusComposite() {
        return busComposite;
    }

    public TextZone_Old getBusNumber() {
        return busNumber;
    }

    public TextZone_Old getBusDestinationLeft() {
        return busDestinationLeft;
    }

    public TextZone_Old getBusTimesLeft() {
        return busTimesLeft;
    }

    public TextZone_Old getBusDestinationRight() {
        return busDestinationRight;
    }

    public TextZone_Old getBusTimesRight() {
        return busTimesRight;
    }

    public TextZone_Old getTubeSummary() {
        return tubeSummary;
    }

    public TextZone_Old getTubeDetail() {
        return tubeDetail;
    }

    private static final int BUS_NUMBER_WIDTH = 24;
    private static final int CLOCK_WIDTH = 20;

    private static final int BUSES_HEIGHT = 18;
    private static final int TUBE_HEIGHT = 8;
    private static final int STATUS_HEIGHT = 5;

}
