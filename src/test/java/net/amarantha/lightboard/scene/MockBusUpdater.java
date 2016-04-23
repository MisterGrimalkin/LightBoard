package net.amarantha.lightboard.scene;

import com.google.inject.Inject;
import net.amarantha.lightboard.updater.transport.BusDeparture;
import net.amarantha.lightboard.updater.transport.BusUpdater;
import net.amarantha.lightboard.utility.Now;
import net.amarantha.lightboard.utility.Sync;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class MockBusUpdater extends BusUpdater {

    @Inject Now now;
    private int masterTime = 0;

    public void setMasterTime(int masterTime) {
        this.masterTime = masterTime;
    }

    @Inject
    public MockBusUpdater(Sync sync) {
        super(sync);
    }

    public void clearBuses() {
        buses.clear();
        delays.clear();
        frequencies.clear();
    }

    private Map<String, Integer> delays = new HashMap<>();
    private Map<String, Integer> frequencies = new HashMap<>();

    public String addBus(long stopCode, String busNo, String destination, int delay, int frequency) {
        String id = UUID.randomUUID().toString();
        buses.put(id, new BusDeparture(stopCode, busNo, destination, true));
        delays.put(id, delay);
        frequencies.put(id, frequency);
        return id;
    }

    @Override
    public void loadBusConfig() {}

    @Override
    public void saveBusConfig() {}

    @Override
    protected String getDataFromWebService(long stopCode) {

        Map<String, Integer> busesFound = new HashMap<>();

        String result = "[4,\"1.0\"," + now.now().getTime() + "]\n";

        for ( Entry<String, BusDeparture> entry : buses.entrySet() ) {

            String id = entry.getKey();
            BusDeparture busDeparture = entry.getValue();

            if ( busDeparture.getStopCode()==stopCode ) {

                Integer found = busesFound.get(busDeparture.getBusNo());
                if ( found==null ) {
                    found = 0;
                } else {
                    found++;
                }
                busesFound.put(busDeparture.getBusNo(), found);

                for ( int i=0; i<3; i++ ) {
                    int delay = delays.get(id);
                    int frequency = frequencies.get(id);
                    int minutesUntilArrival = masterTime + delay + ( i * frequency );
                    if ( minutesUntilArrival >= 0 ) {
                        long arrivalTimeCode = new Date(now.now().getTime() + (minutesUntilArrival * 60 * 1000) + 5000).getTime();
                        result += "[1, \"" + busDeparture.getDestination() + "\",\"" + busDeparture.getBusNo() + "\"," + arrivalTimeCode + "]\n";
                    }
                }

            }

        }

        System.out.println(result);

        return result;
    }
}
