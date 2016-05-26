package net.amarantha.lightboard.updater.schedule;

import com.google.inject.Inject;
import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.old.TextZone_Old;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUpdater extends Updater {

    private String[] formats;
    private int formatIndex = 0;
    private TextZone_Old zone;

    @Inject
    public DateTimeUpdater(Sync sync) {
        super(sync);
        System.out.println("Date/Time Updater Ready....");
    }

    public DateTimeUpdater setZone(TextZone_Old zone) {
        this.zone = zone;
        return this;
    }

    public DateTimeUpdater setFormats(String... formats) {
        this.formats = formats;
        return this;
    }

    @Override
    public void refresh() {
        if ( formatIndex >= formats.length ) {
            formatIndex = 0;
        }
        zone.replaceMessage("{yellow}" + new SimpleDateFormat(formats[formatIndex++]).format(new Date()));
    }

}
