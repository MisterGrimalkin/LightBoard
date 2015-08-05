package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.entity.AlignH;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.zone.impl.CompositeZone;
import net.amarantha.lightboard.zone.impl.TextZone;

public class WordSpinScene extends Scene {

    private final static String[] messages = { "Raspberry", "Pi", "Light", "Board" };

    public WordSpinScene(LightBoardSurface surface) {
        super(surface, "Word Spin");
    }

    @Override
    public void build() {

        TextZone z1 = new TextZone(getSurface(), Edge.LEFT, Edge.BOTTOM, 0);
        z1.setRestPosition(AlignH.RIGHT, AlignV.TOP).clear(false).setScrollTick(25);

        TextZone z2 = new TextZone(getSurface(), Edge.TOP, Edge.LEFT, 0);
        z2.setRestPosition(AlignH.RIGHT, AlignV.BOTTOM).clear(true).setScrollTick(25);

        TextZone z3 = new TextZone(getSurface(), Edge.BOTTOM, Edge.RIGHT, 0);
        z3.setRestPosition(AlignH.LEFT, AlignV.TOP).clear(false);

        TextZone z4 = new TextZone(getSurface(), Edge.RIGHT, Edge.TOP, 0);
        z4.setRestPosition(AlignH.LEFT, AlignV.BOTTOM).clear(false);

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
