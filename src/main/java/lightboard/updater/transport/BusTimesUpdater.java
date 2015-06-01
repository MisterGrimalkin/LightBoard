package lightboard.updater.transport;

import lightboard.updater.Updater;
import lightboard.zone.impl.TextZone;
import org.javalite.http.Get;
import org.javalite.http.Http;

import java.text.SimpleDateFormat;
import java.util.*;

public class BusTimesUpdater extends Updater {

    private final static String TFL_BUS_URL = "http://countdown.api.tfl.gov.uk/interfaces/ura/instant_V1";

    private final int stopCode;
    private final String busNumber;
    private final String displayAs;
    private final int resultsToDisplay;
    private final int offset;

    public BusTimesUpdater(TextZone scroller, int stopCode, String busNumber, String displayAs, int resultsToDisplay) {
        this(scroller, stopCode, busNumber, displayAs, resultsToDisplay, 0);
    }

    public BusTimesUpdater(TextZone scroller, int stopCode, String busNumber, String displayAs, int resultsToDisplay, int offset) {
        super(scroller);
        this.stopCode = stopCode;
        this.busNumber = busNumber;
        this.displayAs = displayAs;
        this.resultsToDisplay = resultsToDisplay;
        this.offset = offset;
        System.out.println("Bus Times Updater for " + stopCode + ":" + busNumber + " Ready....");
    }

    @Override
    public void refresh() {

        clearMessages();

        String result = callWebService();

        List<Long> dueTimes = parseResult(result).get(busNumber);

        String msg = "{yellow}" + busNumber + ">" + displayAs + ":{green}";
        if ( dueTimes!=null ) {
            Collections.sort(dueTimes);
//            msg += " -none-";
//        } else {
            for ( Long m : dueTimes ) {
                msg += " " + (m==0 ? "due" : m + "min");
            }
            addMessage(msg);
        }


    }

    private String callWebService() {
        String result = "";
        try {
            Get get = Http.get(TFL_BUS_URL+"?StopCode1="+stopCode);
            result = get.text();
        } catch ( Exception e ) {
            replaceMessage("Error Querying TFL! " + e.getMessage());
        }
        return result;

    }

    private Map<String, List<Long>> parseResult(String result) {
        Map<String, List<Long>> messages = new HashMap<>();
        String[] lines = result.split("\n");
        for ( int l=1; l<lines.length; l++ ) {
            String[] cols = lines[l].split(",");
            String bus = cols[2].substring(1,cols[2].length()-1);
            List<Long> dueTimes = messages.get(bus);
            if ( dueTimes==null ) {
                dueTimes = new ArrayList<>();
                messages.put(bus, dueTimes);
            }
            String timecodeStr = cols[3].split("]")[0];
            Long timecode = null;
            try {
                timecode = Long.parseLong(timecodeStr);
            } catch ( NumberFormatException e ) {
                System.err.println("Bad time " + timecodeStr);
            }
            if ( timecode!=null && dueTimes.size()<resultsToDisplay ) {
                Date due = new Date(timecode-System.currentTimeMillis());
                SimpleDateFormat sdf = new SimpleDateFormat("m");
                Long time = Long.parseLong(sdf.format(due)) + offset;
                if ( time >= 0 ) {
                    dueTimes.add(time);
                }
            }
        }
        return messages;
    }

}
