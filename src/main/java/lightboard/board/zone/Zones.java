package lightboard.board.zone;

import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.impl.CompositeZone;
import lightboard.board.zone.impl.TextZone;
import lightboard.font.SimpleFont;
import lightboard.updater.UpdaterBundle;
import lightboard.updater.schedule.DateTimeUpdater;
import lightboard.updater.transport.BusTimesUpdater;
import lightboard.updater.transport.TubeStatusUpdater;

import static lightboard.util.MessageQueue.Edge.*;
import static lightboard.util.MessageQueue.HPosition.LEFT;
import static lightboard.util.MessageQueue.HPosition.RIGHT;
import static lightboard.util.MessageQueue.VPosition.BOTTOM;
import static lightboard.util.MessageQueue.VPosition.TOP;

public class Zones {

    public static TextZone startClock(LightBoardSurface surface, int x, int y, int width, int height) {
        TextZone clock = TextZone.fixed(surface);
        clock.region(x, y, width, height).restDuration(500).start();

        DateTimeUpdater.tickingClock(clock).start();
        return clock;
    }

    public static TextZone startTubeStatusDisplay(LightBoardSurface surface, int x, int y, int width, int height, String... linesToDisplay) {
        TextZone zone = TextZone.scrollLeft(surface);
        zone.region(x, y, width, height).start();

        new TubeStatusUpdater(zone, linesToDisplay).start(60000);
        return zone;
    }

    public static TextZone startBusStopDisplay(LightBoardSurface surface, int x, int y, int width, int height) {
        TextZone zone = TextZone.scrollUp(surface);
        zone.region(x, y, width, height).start();

        UpdaterBundle.bundle(
                BusTimesUpdater.updater(zone, 53785, "W7", "Mus Hill", 3),
                BusTimesUpdater.updater(zone, 56782, "W7", "Fins Pk", 3),
                BusTimesUpdater.updater(zone, 76713, "W5", "Harngy", 3),
                BusTimesUpdater.updater(zone, 76713, "41", "Harngy", 3),
                BusTimesUpdater.updater(zone, 76985, "W5", "Archway", 3),
                BusTimesUpdater.updater(zone, 56403, "41", "Archway", 3),
                BusTimesUpdater.updater(zone, 56403, "91", "Traf Sqr", 3)
        ).start(45000);
        return zone;
    }

    public static CompositeZone startFourSpinner(LightBoardSurface surface, int x, int y, int width, int height, String... messages) {

        TextZone z1 = new TextZone(surface, LEFT_EDGE, BOTTOM_EDGE, 0);
        z1.restPosition(RIGHT, TOP).restDuration(0).clear(false);

        TextZone z2 = new TextZone(surface, TOP_EDGE, LEFT_EDGE, 0);
        z2.restPosition(RIGHT, BOTTOM).restDuration(0).clear(false);

        TextZone z3 = new TextZone(surface, BOTTOM_EDGE, RIGHT_EDGE, 0);
        z3.restPosition(LEFT, TOP).restDuration(0).clear(false);

        TextZone z4 = new TextZone(surface, RIGHT_EDGE, TOP_EDGE, 0);
        z4.restPosition(LEFT, BOTTOM).restDuration(0).clear(false);

        for ( int i=3; i<messages.length; i+=4 ) {
            z1.addMessage(0, messages[i-3]);
            z3.addMessage(0, messages[i-2]);
            z2.addMessage(0, messages[i-1]);
            z4.addMessage(0, messages[i]);
        }

        CompositeZone zone = new CompositeZone(surface,z1,z2,z3,z4);
        zone.region(x, y, width, height).start();

        return zone;
    }

    public static TextZone startTelePrompter(LightBoardSurface surface, int x, int y, int width, int height, String... messages) {
        TextZone zone = TextZone.scrollUp(surface);
        zone.region(x, y, width, height);

        StringBuilder sb = new StringBuilder();
        for ( String message : messages ) {
            sb.append(message).append("\n");
        }

        zone.addMessage(0, sb.toString()).start();

        return zone;
    }


}
