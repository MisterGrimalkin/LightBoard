package net.amarantha.lightboard.updater.transport;

import com.google.inject.Inject;
import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.impl.TextZone_Old;
import org.javalite.http.Http;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class BusUpdater extends Updater {

    private final static String TFL_BUS_URL = "http://countdown.api.tfl.gov.uk/interfaces/ura/instant_V1";

    protected Map<String, BusDeparture> buses = new HashMap<>();

    @Inject
    public BusUpdater(Sync sync) {
        super(sync);
    }

    private Map<Integer, String> busNumbers;
    private Map<Integer, String> busDestinationsLeft;
    private Map<Integer, String> busDestinationsRight;
    private Map<Integer, String> busTimesLeft;
    private Map<Integer, String> busTimesRight;

    @Override
    public void refresh() {

        loadBusConfig();

        busNumbers = new HashMap<>();
        busDestinationsLeft = new HashMap<>();
        busDestinationsRight = new HashMap<>();
        busTimesLeft = new HashMap<>();
        busTimesRight = new HashMap<>();

        BusData data = new BusData();
        try {
            for (BusDeparture bus : buses.values()) {
                if ( bus.isActive() ) {
                    data.addResult(bus, getDepartureTimesFor(bus));
                }
            }
        } catch ( Exception e ) {
            data.getDataByBusNumber().clear();
        }

        int sourceId = 0;

        if ( data.getDataByBusNumber().entrySet().isEmpty() ) {
            busNumbers.put(sourceId, "*");
            busDestinationsLeft.put(sourceId, "{red}No Data");
            busDestinationsRight.put(sourceId, "{red}No Data");
            busTimesLeft.put(sourceId, "{red}* * *");
            busTimesRight.put(sourceId, "{red}* * *");
        } else {
            for (Entry<String, Map<BusDeparture, List<Long>>> eBus : data.getDataByBusNumber().entrySet()) {

                int activeBuses = numberOfActiveBuses(eBus.getValue());

                if (activeBuses > 0) {

                    String busNumber = "{yellow}" + eBus.getKey();
                    busNumbers.put(sourceId, busNumber);

                    if (activeBuses > 2) {
                        busDestinationsLeft.put(sourceId, "{red}Too");
                        busTimesLeft.put(sourceId, "{red}Many");
                        busDestinationsRight.put(sourceId, "{red}Buses");
                        busTimesRight.put(sourceId, "{red}Returned!");
                    } else {

                        int direction = 1;

                        for (Entry<BusDeparture, List<Long>> timeEntry : eBus.getValue().entrySet()) {

                            String destination = "{yellow}" + timeEntry.getKey().getDestination();

                            List<Long> busTimes = timeEntry.getValue();
                            Collections.sort(busTimes);
                            StringBuilder sb = new StringBuilder();
                            int count = 0;
                            for (Long bt : busTimes) {
                                Long busTime = bt + timeEntry.getKey().getOffset();
                                if (count < 3 && busTime >= 0) {
                                    if (busTime == 0) {
                                        sb.append("{red}due ");
                                    } else {
                                        if (busTime < 5) {
                                            sb.append("{red}");
                                        } else {
                                            sb.append("{green}");
                                        }
                                        sb.append(busTime).append("m ");
                                    }
                                }
                                count++;
                            }
                            String timesMessage = sb.toString();

                            if (direction == 1) {
                                busDestinationsLeft.put(sourceId, destination);
                                busTimesLeft.put(sourceId, timesMessage);
                            } else if (direction == 2) {
                                busDestinationsRight.put(sourceId, destination);
                                busTimesRight.put(sourceId, timesMessage);
                            }
                            direction++;

                            if (activeBuses == 1) {
                                busDestinationsRight.put(sourceId, "{yellow}-");
                                busTimesRight.put(sourceId, "{yellow}-");
                            }

                        }

                    }
                    sourceId++;
                }
            }
        }

    }

    private int currentSource = 0;

    public void updateZones(TextZone_Old busNumberZone, TextZone_Old leftDestinationZone, TextZone_Old leftTimesZone, TextZone_Old rightDestinationZone, TextZone_Old rightTimesZone) {

        busNumberZone.clearAllMessages();
        leftDestinationZone.clearAllMessages();
        leftTimesZone.clearAllMessages();
        rightDestinationZone.clearAllMessages();
        rightTimesZone.clearAllMessages();

        if ( busNumbers!=null) {

            if (currentSource >= busNumbers.size()) {
                currentSource = 0;
            }

            if ( busNumbers.get(currentSource)!=null ) {
                busNumberZone.addMessage(busNumbers.get(currentSource));
                leftDestinationZone.addMessage(busDestinationsLeft.get(currentSource));
                leftTimesZone.addMessage(busTimesLeft.get(currentSource));
                rightDestinationZone.addMessage(busDestinationsRight.get(currentSource));
                rightTimesZone.addMessage(busTimesRight.get(currentSource));
            }

            currentSource++;

        }

    }


    ///////////////////////
    // Config Management //
    ///////////////////////

    public void enableBusStop(String id) {
        buses.get(id).setActive(true);
        saveBusConfig();
        refresh();
    }

    public void disableBusStop(String id) {
        buses.get(id).setActive(false);
        saveBusConfig();
        refresh();
    }

    public void removeBusStop(String id) {
        buses.remove(id);
        saveBusConfig();
        refresh();
    }

    public String addBusStop(long stopCode, String busNo, String destination, long offset) {
        return updateBusStop(UUID.randomUUID().toString(), stopCode, busNo, destination, offset);
    }

    public String updateBusStop(String id, long stopCode, String busNo, String destination, long offset) {
        BusDeparture newBus = new BusDeparture(stopCode, busNo, destination, true, offset);
        buses.put(id, newBus);
        saveBusConfig();
        refresh();
        return id;
    }


    ////////////////////////
    // Config Persistence //
    ////////////////////////

    public void loadBusConfig() {

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("busconfig.json"));
            JSONObject jsonObject = (JSONObject) obj;

            JSONArray busJa = (JSONArray)jsonObject.get("buses");
            Iterator<JSONObject> iter = busJa.iterator();
            while ( iter.hasNext() ) {
                JSONObject bus = iter.next();
                buses.put(
                        (String)bus.get("id"),
                        new BusDeparture(
                                (long)bus.get("stop"),
                                (String)bus.get("bus"),
                                (String)bus.get("destination"),
                                (boolean)bus.get("active"),
                                (long)bus.get("offset")
                        )
                );
            }
        } catch (ParseException | IOException e) {
            saveBusConfig();
            e.printStackTrace();
        }
    }

    public void saveBusConfig() {
        try {
            JSONObject json = getBusStopJson();
            FileWriter file = new FileWriter("busconfig.json");
            file.write(json.toString());
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    ////////////////
    // Build JSON //
    ////////////////

    public JSONObject getBusStopJson() {
        JSONObject json = new JSONObject();
        JSONArray ja = new JSONArray();

        for ( Map.Entry<String, BusDeparture> entry : buses.entrySet() ) {
            JSONObject jsonBus = new JSONObject();
            jsonBus.put("id", entry.getKey());
            BusDeparture bus = entry.getValue();
            jsonBus.put("stop", bus.getStopCode());
            jsonBus.put("bus", bus.getBusNo());
            jsonBus.put("destination", bus.getDestination());
            jsonBus.put("offset", bus.getOffset());
            jsonBus.put("active", bus.isActive());
            ja.add(jsonBus);
        }

        json.put("buses", ja);
        return json;
    }


    //////////////////
    // Zone Binding //
    //////////////////

//    public BusUpdater setZones(TextZone busNumberZone, TextZone leftDestinationZone, TextZone leftTimesZone, TextZone rightDestinationZone, TextZone rightTimesZone) {
//        this.busNumberZone = busNumberZone;
//        this.leftDestinationZone = leftDestinationZone;
//        this.leftTimesZone = leftTimesZone;
//        this.rightDestinationZone = rightDestinationZone;
//        this.rightTimesZone = rightTimesZone;
//        return this;
//    }


    //////////////////////
    // Web Service Call //
    //////////////////////

    private List<Long> getDepartureTimesFor(BusDeparture departure) throws Exception {

        List<Long> result = new ArrayList<>();

        List<String[]> lineArrays = breakIntoLineArrays(getDataFromWebService(departure.getStopCode()));

        if ( lineArrays.isEmpty() ) {
            throw new Exception();
        }

        for ( String[] line : lineArrays ) {
            if ( line[BUS_NUMBER].equals(departure.getBusNo()) ) {
                result.add(minutesIntoFuture(line[BUS_TIME]));
            }
        }

        return result;
    }

    protected String getDataFromWebService(long stopCode) {
        return Http.get(TFL_BUS_URL + "?StopCode1=" + stopCode).text();
    }


    /////////////
    // Utility //
    /////////////

    private int numberOfActiveBuses(Map<BusDeparture, List<Long>> map) {
        int result = 0;
        for ( Entry<BusDeparture, List<Long>> timeEntry : map.entrySet() ) {
            if ( !timeEntry.getValue().isEmpty() ) {
                result ++;
            }
        }
        return result;
    }

    private Long minutesIntoFuture(String timecodeStr) {
        Long time = 0L;
        try {
            Long timecode = Long.parseLong(timecodeStr);
            Date due = new Date(timecode - System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("m");
            time = Long.parseLong(sdf.format(due));
        } catch ( NumberFormatException e ) {
            System.out.println(timecodeStr + " fucked!");
        }
        return time;
    }

    private List<String[]> breakIntoLineArrays(String httpResult) {
        List<String[]> result = new LinkedList<>();
        String[] lines = httpResult.split("\n");
        for (int l = 1; l < lines.length; l++) {
            String line = lines[l];
            String trimmed = line.replaceAll("\\[","").replaceAll("]", "").replace("\"","").replaceAll("\r", "");
            String[] cols = trimmed.split(",");
            result.add(cols);
        }
        return result;
    }

    private static final int BUS_NUMBER = 2;
    private static final int BUS_TIME = 3;

}
