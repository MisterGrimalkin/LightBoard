package lightboard.scene.impl;

import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.impl.CompositeZone;
import lightboard.board.zone.impl.TextZone;
import lightboard.scene.Scene;

import static lightboard.util.MessageQueue.Edge.*;
import static lightboard.util.MessageQueue.HPosition.LEFT;
import static lightboard.util.MessageQueue.HPosition.RIGHT;
import static lightboard.util.MessageQueue.VPosition.BOTTOM;
import static lightboard.util.MessageQueue.VPosition.TOP;

public class WordSpinScene extends Scene {

    private final static String[] messages = { "Raspberry", "Pi", "Light", "Board" };

    public WordSpinScene(LightBoardSurface surface) {
        super(surface);
    }

    @Override
    public void build() {

        TextZone z1 = new TextZone(getSurface(), LEFT_EDGE, BOTTOM_EDGE, 0);
        z1.setRestPosition(RIGHT, TOP).clear(false).setScrollTick(25);

        TextZone z2 = new TextZone(getSurface(), TOP_EDGE, LEFT_EDGE, 0);
        z2.setRestPosition(RIGHT, BOTTOM).clear(true).setScrollTick(25);

        TextZone z3 = new TextZone(getSurface(), BOTTOM_EDGE, RIGHT_EDGE, 0);
        z3.setRestPosition(LEFT, TOP).clear(false);

        TextZone z4 = new TextZone(getSurface(), RIGHT_EDGE, TOP_EDGE, 0);
        z4.setRestPosition(LEFT, BOTTOM).clear(false);

        for ( int i=3; i<messages.length; i+=4 ) {
            z1.addMessage(0, messages[i-3]);
            z3.addMessage(0, messages[i-2]);
            z2.addMessage(0, messages[i-1]);
            z4.addMessage(0, messages[i]);
        }

        CompositeZone zone = new CompositeZone(getSurface(),z1,z2,z3,z4);
        zone.setScrollTick(25);

        TextZone tzone = TextZone.scrollLeft(getSurface());
        tzone.setScrollTick(25);
        tzone.addMessage(0, "What the Fuck?");

        registerZones(zone);

    }
}
