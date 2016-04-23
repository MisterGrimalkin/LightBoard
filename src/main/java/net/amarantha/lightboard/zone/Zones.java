package net.amarantha.lightboard.zone;

import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.zone.impl.CompositeZone;
import net.amarantha.lightboard.zone.impl.TextZone;

public class Zones {


    public static CompositeZone startFourSpinner(LightBoardSurface surface, int x, int y, int width, int height, String... messages) {

//        TextZone z1 = new TextZone(surface, Edge.LEFT, Edge.BOTTOM, 0);
//        z1.setRestPosition(AlignH.RIGHT, AlignV.TOP).setRestDuration(0).clear(false);
//
//        TextZone z2 = new TextZone(surface, Edge.TOP, Edge.LEFT, 0);
//        z2.setRestPosition(AlignH.RIGHT, AlignV.BOTTOM).setRestDuration(0).clear(false);
//
//        TextZone z3 = new TextZone(surface, Edge.BOTTOM, Edge.RIGHT, 0);
//        z3.setRestPosition(AlignH.LEFT, AlignV.TOP).setRestDuration(0).clear(false);
//
//        TextZone z4 = new TextZone(surface, Edge.RIGHT, Edge.TOP, 0);
//        z4.setRestPosition(AlignH.LEFT, AlignV.BOTTOM).setRestDuration(0).clear(false);
//
//        for ( int i=3; i<messages.length; i+=4 ) {
//            z1.addMessage(0, messages[i-3]);
//            z3.addMessage(0, messages[i-2]);
//            z2.addMessage(0, messages[i-1]);
//            z4.addMessage(0, messages[i]);
//        }
//
//        CompositeZone zone = new CompositeZone(surface,z1,z2,z3,z4);
//        zone.setRegion(x, y, width, height).setScrollTick(25);
//
//        return zone;
        return null;
    }

    public static TextZone startTelePrompter(LightBoardSurface surface, int x, int y, int width, int height, String... messages) {
//        TextZone zone = TextZone.scrollUp(surface);
//        zone.setRegion(x, y, width, height);
//
//        StringBuilder sb = new StringBuilder();
//        for ( String message : messages ) {
//            sb.append(message).append("\n");
//        }
//
//        zone.addMessage(0, sb.toString()).setScrollTick(25);
//
//        return zone;
        return null;
    }


}
