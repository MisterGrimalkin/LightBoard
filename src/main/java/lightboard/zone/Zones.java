package lightboard.zone;

import lightboard.surface.LightBoardSurface;
import lightboard.zone.impl.CompositeZone;
import lightboard.zone.impl.TextZone;

import static lightboard.util.MessageQueue.Edge.*;
import static lightboard.util.MessageQueue.HPosition.LEFT;
import static lightboard.util.MessageQueue.HPosition.RIGHT;
import static lightboard.util.MessageQueue.VPosition.BOTTOM;
import static lightboard.util.MessageQueue.VPosition.TOP;

public class Zones {


    public static CompositeZone startFourSpinner(LightBoardSurface surface, int x, int y, int width, int height, String... messages) {

        TextZone z1 = new TextZone(surface, LEFT_EDGE, BOTTOM_EDGE, 0);
        z1.setRestPosition(RIGHT, TOP).setRestDuration(0).clear(false);

        TextZone z2 = new TextZone(surface, TOP_EDGE, LEFT_EDGE, 0);
        z2.setRestPosition(RIGHT, BOTTOM).setRestDuration(0).clear(false);

        TextZone z3 = new TextZone(surface, BOTTOM_EDGE, RIGHT_EDGE, 0);
        z3.setRestPosition(LEFT, TOP).setRestDuration(0).clear(false);

        TextZone z4 = new TextZone(surface, RIGHT_EDGE, TOP_EDGE, 0);
        z4.setRestPosition(LEFT, BOTTOM).setRestDuration(0).clear(false);

        for ( int i=3; i<messages.length; i+=4 ) {
            z1.addMessage(0, messages[i-3]);
            z3.addMessage(0, messages[i-2]);
            z2.addMessage(0, messages[i-1]);
            z4.addMessage(0, messages[i]);
        }

        CompositeZone zone = new CompositeZone(surface,z1,z2,z3,z4);
        zone.setRegion(x, y, width, height).setScrollTick(25);

        return zone;
    }

    public static TextZone startTelePrompter(LightBoardSurface surface, int x, int y, int width, int height, String... messages) {
        TextZone zone = TextZone.scrollUp(surface);
        zone.setRegion(x, y, width, height);

        StringBuilder sb = new StringBuilder();
        for ( String message : messages ) {
            sb.append(message).append("\n");
        }

        zone.addMessage(0, sb.toString()).setScrollTick(25);

        return zone;
    }


}
