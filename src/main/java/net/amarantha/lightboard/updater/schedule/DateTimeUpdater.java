package net.amarantha.lightboard.updater.schedule;

import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.zone.impl.TextZone;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUpdater extends Updater {

    public static DateTimeUpdater tickingClock(TextZone zone) {
        return new DateTimeUpdater(zone, "HH:mm", "HH mm");
    }

    private final String[] formats;
    private int formatIndex = 0;

    public DateTimeUpdater(TextZone zone, String... formats) {
        super(zone);
        this.formats = formats;
        System.out.println("Date/Time Updater Ready....");
    }

    @Override
    public void refresh() {
        if ( formatIndex >= formats.length ) {
            formatIndex = 0;
        }
        replaceMessage("{yellow}"+new SimpleDateFormat(formats[formatIndex++]).format(new Date()));
    }

}
