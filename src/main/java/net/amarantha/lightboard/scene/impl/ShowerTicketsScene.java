package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.ShowerTicketUpdater;
import net.amarantha.lightboard.zone.impl.TextZone;

import static net.amarantha.lightboard.entity.HPosition.LEFT;
import static net.amarantha.lightboard.entity.HPosition.RIGHT;
import static net.amarantha.lightboard.entity.VPosition.MIDDLE;

public class ShowerTicketsScene extends Scene {


    public ShowerTicketsScene(LightBoardSurface surface) {
        super(surface, "Shower Tickets");
    }

    @Override
    public void build() {

        // Name Zones
        TextZone block1Name = TextZone.fixed(getSurface());
        block1Name
                .setRegion(0,0,50,getRows()/2)
                .setRestPosition(RIGHT, MIDDLE)
                .setScrollTick(1000);

        TextZone block2Name = TextZone.fixed(getSurface());
        block2Name
                .setRegion(0,getRows()/2,50,getRows()/2)
                .setRestPosition(RIGHT, MIDDLE)
                .setScrollTick(1000);

        // Ticket Zones
        TextZone block1Tickets = TextZone.scrollLeft(getSurface());
        block1Tickets
                .setRegion(58,0,getCols()-50,getRows()/2)
                .setRestDuration(5000)
                .setRestPosition(LEFT, MIDDLE)
                .setScrollTick(20);

        TextZone block2Tickets = TextZone.scrollLeft(getSurface());
        block2Tickets
                .setRegion(58,getRows()/2,getCols()-50,getRows()/2)
                .setRestDuration(5000)
                .setRestPosition(LEFT, MIDDLE)
                .setScrollTick(20);

        ShowerTicketUpdater updater = new ShowerTicketUpdater(block1Name, block2Name, block1Tickets, block2Tickets);
        updater.setDataRefresh(5000);

//        CompositeZone zone = new CompositeZone(getSurface(), block1Name, block1Tickets, block2Name, block2Tickets);
//        zone.setScrollTick(10);

        registerZones(block1Name, block1Tickets, block2Name, block2Tickets);
        registerUpdaters(updater);
//        registerZones(zone);

    }
}
