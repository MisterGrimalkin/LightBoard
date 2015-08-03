package net.amarantha.lightboard.updater.transport;

import java.util.*;
import java.util.Map.Entry;

public class BusData {

    private Map<BusDeparture, List<Long>> times = new HashMap<>();

    public void addResult(BusDeparture busDeparture, List<Long> timeList) {
        times.put(busDeparture, timeList);
    }

    public void addResult(BusDeparture busDeparture, Long time) {
        List<Long> t = times.get(busDeparture);
        if ( t==null ) {
            t = new LinkedList<>();
            times.put(busDeparture, t);
        }
        t.add(time);
    }

    public Map<String, Map<BusDeparture, List<Long>>> getDataByBusNumber() {
        Map<String, Map<BusDeparture, List<Long>>> result = new HashMap<>();
        for ( Entry<BusDeparture, List<Long>> entry : times.entrySet() ) {
            BusDeparture departure = entry.getKey();
            Map<BusDeparture, List<Long>> timesByDeparture = result.get(departure.getBusNo());
            if ( timesByDeparture==null ) {
                timesByDeparture = new HashMap<>();
                result.put(departure.getBusNo(), timesByDeparture);
            }
            timesByDeparture.put(departure, entry.getValue());
        }
        return result;
    }


}
