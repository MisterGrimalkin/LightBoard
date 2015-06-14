package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.MessageUpdater;
import net.amarantha.lightboard.updater.ShowerTicketUpdater;
import net.amarantha.lightboard.zone.impl.ClockZone;
import net.amarantha.lightboard.zone.impl.TextZone;

import static net.amarantha.lightboard.entity.HPosition.LEFT;
import static net.amarantha.lightboard.entity.HPosition.RIGHT;
import static net.amarantha.lightboard.entity.VPosition.MIDDLE;

public class ShowerTicketsScene extends Scene {

    private static final int CLOCK_WIDTH = 21;
    private static final int CLOCK_HEIGHT = 13;

    public ShowerTicketsScene(LightBoardSurface surface) {
        super(surface, "Shower Tickets");
    }

    @Override
    public void build() {

        TextZone clock = new ClockZone(getSurface());
        clock.setRegion(0, 0, CLOCK_WIDTH, CLOCK_HEIGHT);

        TextZone messageZone = TextZone.scrollLeft(getSurface());
        messageZone
                .setRegion(0, getRows() - 9, getCols(), 9)
                .setScrollTick(20)
                .setRestDuration(2000)
        ;

        MessageUpdater updater = new MessageUpdater(messageZone);
        updater.setDataRefresh(30000);

        registerZones(clock, messageZone);
        registerUpdaters(updater);

    }
}
