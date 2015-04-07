package lightboard.updater.transport;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by grimalkin on 07/04/15.
 */
public class BusStop {

    private final long stopCode;
    private final int maxResultsPerBus;
    private final Map<String, String> routesToDisplay;

    public BusStop(long stopCode, int maxResultsPerBus) {
        this.stopCode = stopCode;
        this.maxResultsPerBus = maxResultsPerBus;
        routesToDisplay = new HashMap<>();
    }

    public long getStopCode() {
        return stopCode;
    }

    public int getMaxResultsPerBus() {
        return maxResultsPerBus;
    }

    public BusStop displayRoute(String busNo, String displayDestinationAs) {
        routesToDisplay.put(busNo, displayDestinationAs);
        return this;
    }

    public Set<String> getRoutesToDisplay() {
        return routesToDisplay.keySet();
    }

    public String getDisplayDestinationAs(String busNo) {
        String result = routesToDisplay.get(busNo);
        return result==null?"":result;
    }

}
