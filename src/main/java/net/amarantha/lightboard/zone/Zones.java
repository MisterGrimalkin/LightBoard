package net.amarantha.lightboard.zone;

import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.entity.HPosition;
import net.amarantha.lightboard.entity.VPosition;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.zone.impl.CompositeZone;
import net.amarantha.lightboard.zone.impl.TextZone;

public class Zones {


    public static CompositeZone startFourSpinner(LightBoardSurface surface, int x, int y, int width, int height, String... messages) {

        TextZone z1 = new TextZone(surface, Edge.LEFT_EDGE, Edge.BOTTOM_EDGE, 0);
        z1.setRestPosition(HPosition.RIGHT, VPosition.TOP).setRestDuration(0).clear(false);

        TextZone z2 = new TextZone(surface, Edge.TOP_EDGE, Edge.LEFT_EDGE, 0);
        z2.setRestPosition(HPosition.RIGHT, VPosition.BOTTOM).setRestDuration(0).clear(false);

        TextZone z3 = new TextZone(surface, Edge.BOTTOM_EDGE, Edge.RIGHT_EDGE, 0);
        z3.setRestPosition(HPosition.LEFT, VPosition.TOP).setRestDuration(0).clear(false);

        TextZone z4 = new TextZone(surface, Edge.RIGHT_EDGE, Edge.TOP_EDGE, 0);
        z4.setRestPosition(HPosition.LEFT, VPosition.BOTTOM).setRestDuration(0).clear(false);

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
