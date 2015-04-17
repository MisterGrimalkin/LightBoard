package lightboard.updater.transport;

import lightboard.board.zone.impl.TextZone;
import lightboard.updater.Updater;
import org.javalite.http.Get;
import org.javalite.http.Http;

import java.text.SimpleDateFormat;
import java.util.*;

public class BusTimesUpdater extends Updater {

    private final static String TFL_BUS_URL = "http://countdown.api.tfl.gov.uk/interfaces/ura/instant_V1";

    public static BusTimesUpdater updater(TextZone scroller, int stopCode, String busNumber, String displayAs, int resultsToDisplay) {
        return new BusTimesUpdater(scroller, stopCode, busNumber, displayAs, resultsToDisplay);
    }

    private final int stopCode;
    private final String busNumber;
    private final String displayAs;
    private final int resultsToDisplay;

    private BusTimesUpdater(TextZone scroller, int stopCode, String busNumber, String displayAs, int resultsToDisplay) {
        super(scroller);
        this.stopCode = stopCode;
        this.busNumber = busNumber;
        this.displayAs = displayAs;
        this.resultsToDisplay = resultsToDisplay;
    }

    @Override
    public void refresh() {

        clearMessages();

        String result = callWebService();

        List<String> messages = parseResult(result).get(busNumber);

        String msg = busNumber + ">" + displayAs + ":";
        if ( messages==null ) {
            msg += " -none-";
        } else {
            for ( String m : messages ) {
                msg += " " + m;
            }
        }

        System.out.println(msg);

        addMessage(msg);

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

    private Map<String, List<String>> parseResult(String result) {
        Map<String, List<String>> messages = new HashMap<>();
        String[] lines = result.split("\n");
        for ( int l=1; l<lines.length; l++ ) {
            String[] cols = lines[l].split(",");
            String bus = cols[2].substring(1,cols[2].length()-1);
            List<String> dueTimes = messages.get(bus);
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
                dueTimes.add((sdf.format(due).equals("0") ? "due" : sdf.format(due)+"min"));
            }
        }
        return messages;
    }

}
