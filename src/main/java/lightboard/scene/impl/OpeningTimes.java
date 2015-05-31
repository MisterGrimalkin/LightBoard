package lightboard.scene.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OpeningTimes {

    public static List<OpeningTimes> getLocalOpeningTimes() {
        List<OpeningTimes> result = new ArrayList<>();
        result.add(budgens());
        result.add(haelan());
        result.add(parkLocal());
        result.add(tesco());
        return result;
    }

    public static OpeningTimes budgens() {
        return new OpeningTimes("Budgens")
                .addTime("Monday",      "07:00", "23:00")
                .addTime("Tuesday",     "07:00", "23:00")
                .addTime("Wednesday",   "07:00", "23:00")
                .addTime("Thursday",    "07:00", "23:00")
                .addTime("Friday",      "07:00", "23:00")
                .addTime("Saturday",    "07:00", "23:00")
                .addTime("Sunday",      "12:00", "18:00");
    }

    public static OpeningTimes haelan() {
        return new OpeningTimes("Haelan Centre")
                .addTime("Monday",      "09:00", "18:00")
                .addTime("Tuesday",     "09:00", "18:00")
                .addTime("Wednesday",   "09:00", "18:00")
                .addTime("Thursday",    "09:00", "18:00")
                .addTime("Friday",      "09:00", "18:00")
                .addTime("Saturday",    "09:00", "18:00")
                .addTime("Sunday",      "11:30", "16:00");
    }

    public static OpeningTimes parkLocal() {
        return new OpeningTimes("Park Local")
                .addTime("Monday",      "08:00", "23:20")
                .addTime("Tuesday",     "08:00", "23:20")
                .addTime("Wednesday",   "08:00", "23:20")
                .addTime("Thursday",    "08:00", "23:20")
                .addTime("Friday",      "08:00", "23:20")
                .addTime("Saturday",    "08:00", "23:20")
                .addTime("Sunday",      "08:00", "23:20");
    }

    public static OpeningTimes tesco() {
        return new OpeningTimes("Tesco")
                .addTime("Monday",      "06:00", "00:00")
                .addTime("Tuesday",     "06:00", "00:00")
                .addTime("Wednesday",   "06:00", "00:00")
                .addTime("Thursday",    "06:00", "00:00")
                .addTime("Friday",      "06:00", "00:00")
                .addTime("Saturday",    "06:00", "00:00")
                .addTime("Sunday",      "06:00", "00:00");

    }

    private final String name;
    private final Map<String, String> openingTimes = new HashMap<>();
    private final Map<String, String> closingTimes = new HashMap<>();

    public OpeningTimes(String name) {
        this.name = name;
    }

    public OpeningTimes addTime(String day, String open, String close) {
        openingTimes.put(day, open);
        closingTimes.put(day, close);
        return this;
    }

    public String getOpeningTime(String day) {
        return openingTimes.get(day);
    }

    public String getClosingTime(String day) {
        return closingTimes.get(day);
    }

    public String getName() {
        return name;
    }

    public String getInfoString() {
        Date open = getOpeningDate();
        Date close = getClosingDate();
        if ( open!=null && close!=null && open.before(close) ) {
            Date now = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("h");
            SimpleDateFormat sdf2 = new SimpleDateFormat(":mm");
            SimpleDateFormat sdf3 = new SimpleDateFormat("a");
            if ( now.before(open) ) {
                String m = sdf2.format(open);
                return "{red}" + name + " CLOSED until "
                        + sdf1.format(open) + ( m.equals(":00") ? "" : m ) + sdf3.format(open).toLowerCase();
            }
            if ( now.before(close) ) {
                String m = sdf2.format(close);
                String closingTime = sdf1.format(close) + ( m.equals(":00") ? "" : m ) + sdf3.format(close).toLowerCase();
                if ( closingTime.equals("12am") ) {
                    closingTime = "Midnight";
                }
                return "{green}" + name + " OPEN until "+ closingTime;
            }
            return "{red}" + name + " CLOSED";
        }
        return "{yellow}" + name + ": ???";
    }

    public Date getOpeningDate() {
        Date today = new Date();
        String todayString = new SimpleDateFormat("dd-MM-yyyy").format(today);
        String todayDay = new SimpleDateFormat("EEEE").format(today);
        String openingTime = getOpeningTime(todayDay);
        try {
            Date openingDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(todayString + " " + openingTime);
            return openingDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getClosingDate() {
        Date today = new Date();
        String todayDay = new SimpleDateFormat("EEEE").format(today);
        String closingTime = getClosingTime(todayDay);
        if ( closingTime.equals("00:00") ) {
            today = new Date(System.currentTimeMillis()+(24*60*60*1000));
        }
        String todayString = new SimpleDateFormat("dd-MM-yyyy").format(today);
        try {
            Date closingDate = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(todayString + " " + closingTime);
            return closingDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
