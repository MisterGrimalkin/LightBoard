package net.amarantha.lightboard.scene.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.AlignH;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.font.LargeFont;
import net.amarantha.lightboard.font.SmallFont;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.webservice.ShowerTicketResource;
import net.amarantha.lightboard.zone.old.TextZone_Old;

public class ShowerTicketsScene extends Scene {

    private static final int LABEL_WIDTH = 54;
    private static final int MESSAGE_HEIGHT = 9;

    @Inject private TextZone_Old messageZone;
    @Inject private TextZone_Old label;
    @Inject private TextZone_Old female;
    @Inject private TextZone_Old male;

    @Inject
    public ShowerTicketsScene(LightBoardSurface surface) {
        super("Shower Tickets");
    }

    @Override
    public void build() {

        // Shower Tickets
        label
                .setFont(new SmallFont())
                .fixed()
                .setRestPosition(AlignH.RIGHT, AlignV.MIDDLE)
                .singleRender(true)
                .setRegion(0, 0, LABEL_WIDTH, getRows() - MESSAGE_HEIGHT)
                .setScrollTick(10000)
                .setRestDuration(10000)

        ;
        label.addMessage("{red}NEXT SHOWER:");

        female
                .setFont(new LargeFont())
                .fixed()
                .singleRender(true)
                .setRegion(
                        LABEL_WIDTH,
                        0,
                        (getCols()-LABEL_WIDTH)/2,
                        getRows() - MESSAGE_HEIGHT
                )
                .setScrollTick(1000)
                .setRestDuration(1000)
        ;
        female.addMessage("{red}* {yellow}* {green}*");

        male
                .setFont(new LargeFont())
                .fixed()
                .singleRender(true)
                .setRegion(
                        LABEL_WIDTH + ((getCols() - LABEL_WIDTH) / 2),
                        0,
                        (getCols() - LABEL_WIDTH) / 2,
                        getRows() - MESSAGE_HEIGHT
                )
                .setScrollTick(1000)
                .setRestDuration(1000)
        ;
        male.addMessage("{red}* {yellow}* {green}*");

        ShowerTicketResource.setTextZones(female, male);


        // Messages
        messageZone
                .scrollLeft()
                .setRegion(0, getRows() - MESSAGE_HEIGHT, getCols(), MESSAGE_HEIGHT)
                .setScrollTick(20)
                .setRestDuration(2000)
        ;

//        MessageResource.setMessageZone(messageZone);

        registerZones(female, male, messageZone, label);

    }
}
