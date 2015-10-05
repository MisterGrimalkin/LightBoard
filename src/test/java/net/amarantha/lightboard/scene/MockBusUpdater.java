package net.amarantha.lightboard.scene;

import com.google.inject.Inject;
import net.amarantha.lightboard.updater.transport.BusDeparture;
import net.amarantha.lightboard.updater.transport.BusUpdater;
import net.amarantha.lightboard.utility.Sync;

import java.util.ArrayList;
import java.util.List;

public class MockBusUpdater extends BusUpdater {

    @Inject
    public MockBusUpdater(Sync sync) {
        super(sync);
    }

    @Override
    public void loadBusConfig() {
        buses.clear();
        buses.put("1", new BusDeparture(1, "1", "Better Place", true));
        buses.put("2", new BusDeparture(2, "1", "Best Place", true));
        buses.put("3", new BusDeparture(3, "2", "There", true));
        buses.put("4", new BusDeparture(4, "2", "Back Again", true));
    }

    @Override
    public void saveBusConfig() {}

    @Override
    protected List<Long> getDepartureTimesFor(BusDeparture departure) throws Exception {
        List<Long> result = new ArrayList<>();
        if ( departure.getStopCode()==1 ) {
            result.add(1L);
            result.add(2L);
        }
        else if ( departure.getStopCode()==2 ) {
            result.add(3L);
            result.add(4L);
        }
        if ( departure.getStopCode()==3 ) {
            result.add(5L);
            result.add(6L);
        }
        else if ( departure.getStopCode()==4 ) {
            result.add(7L);
            result.add(8L);
        }
        return result;
    }
}
