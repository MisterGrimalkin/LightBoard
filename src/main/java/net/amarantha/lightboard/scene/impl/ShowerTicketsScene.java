package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.font.SmallFont;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.MessageUpdater;
import net.amarantha.lightboard.updater.ShowerTicketUpdater;
import net.amarantha.lightboard.webservice.MessageResource;
import net.amarantha.lightboard.webservice.ShowerTicketResource;
import net.amarantha.lightboard.zone.impl.ClockZone;
import net.amarantha.lightboard.zone.impl.TextZone;

import static net.amarantha.lightboard.entity.HPosition.LEFT;
import static net.amarantha.lightboard.entity.HPosition.RIGHT;
import static net.amarantha.lightboard.entity.VPosition.MIDDLE;

public class ShowerTicketsScene extends Scene {

    private static final int CLOCK_WIDTH = 0;//21;
    private static final int CLOCK_HEIGHT = 13;

    private static final int LABEL_HEIGHT = 7;
    private static final int MESSAGE_HEIGHT = 9;

    private static final int TICKET_MARGIN_X = 4;
    private static final int TICKET_MARGIN_Y = 2;

    public ShowerTicketsScene(LightBoardSurface surface) {
        super(surface, "Shower Tickets");
    }

    @Override
    public void build() {

        // Clock
//        TextZone clock = new ClockZone(getSurface());
//        clock.setRegion(0, 0, CLOCK_WIDTH, CLOCK_HEIGHT);

        // Shower Tickets
        TextZone label = TextZone.fixed(getSurface());
        label
                .setFont(new SmallFont())
                .setRegion(CLOCK_WIDTH, 0, getCols() - CLOCK_WIDTH, LABEL_HEIGHT)
                .autoRender(false)
                .setScrollTick(10000)
                .setRestDuration(10000)
        ;
        label.addMessage("{red}NEXT SHOWER TICKET NUMBERS");

        TextZone female = TextZone.fixed(getSurface());
        female
                .setRegion(
                        CLOCK_WIDTH,
                        LABEL_HEIGHT,
                        (getCols() - CLOCK_WIDTH) / 2,
                        getRows() - LABEL_HEIGHT - MESSAGE_HEIGHT
                )
//                .autoRender(false)
                .setScrollTick(1000)
                .setRestDuration(1000)
        ;
        female.addMessage("{red}* {yellow}* {green}*");

        TextZone male = TextZone.fixed(getSurface());
        male
                .setRegion(
                        CLOCK_WIDTH + (getCols() - CLOCK_WIDTH) / 2,
                        LABEL_HEIGHT,
                        (getCols() - CLOCK_WIDTH) / 2,
                        getRows() - LABEL_HEIGHT - MESSAGE_HEIGHT
                )
//                .autoRender(false)
                .setScrollTick(1000)
                .setRestDuration(1000)
        ;
        male.addMessage("{red}* {yellow}* {green}*");

        ShowerTicketResource.setTextZones(female, male);


        // Messages
        TextZone messageZone = TextZone.scrollLeft(getSurface());
        messageZone
                .setRegion(0, getRows() - MESSAGE_HEIGHT, getCols(), MESSAGE_HEIGHT)
                .setScrollTick(20)
                .setRestDuration(2000)
        ;

        MessageResource.setMessageZone(messageZone);

        registerZones(label, female, male, messageZone);

    }
}
