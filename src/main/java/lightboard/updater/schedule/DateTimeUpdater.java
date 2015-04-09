package lightboard.updater.schedule;

import lightboard.board.zone.impl.TextZone;
import lightboard.updater.Updater;

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

    }

    @Override
    public void start() {
        super.start(500);
    }

    @Override
    public void refresh() {
        if ( formatIndex >= formats.length ) {
            formatIndex = 0;
        }
        replaceMessage(new SimpleDateFormat(formats[formatIndex++]).format(new Date()));
    }

}
