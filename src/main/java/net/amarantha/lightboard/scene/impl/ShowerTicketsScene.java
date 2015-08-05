package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.entity.AlignH;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.font.ShowerFont;
import net.amarantha.lightboard.font.SmallFont;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.webservice.MessageResource;
import net.amarantha.lightboard.webservice.ShowerTicketResource;
import net.amarantha.lightboard.zone.impl.TextZone;

public class ShowerTicketsScene extends Scene {

    private static final int LABEL_WIDTH = 54;
    private static final int MESSAGE_HEIGHT = 9;

    public ShowerTicketsScene(LightBoardSurface surface) {
        super(surface, "Shower Tickets");
    }

    @Override
    public void build() {

        // Shower Tickets
        TextZone label = TextZone.fixed(getSurface());
        label
                .setFont(new SmallFont())
                .setRestPosition(AlignH.RIGHT, AlignV.MIDDLE)
                .singleRender(true)
                .setRegion(0, 0, LABEL_WIDTH, getRows() - MESSAGE_HEIGHT)
                .setScrollTick(10000)
                .setRestDuration(10000)

        ;
        label.addMessage("{red}NEXT SHOWER:");

        TextZone female = TextZone.fixed(getSurface());
        female
                .setFont(new ShowerFont())
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

        TextZone male = TextZone.fixed(getSurface());
        male
                .setFont(new ShowerFont())
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
        TextZone messageZone = TextZone.scrollLeft(getSurface());
        messageZone
                .setRegion(0, getRows() - MESSAGE_HEIGHT, getCols(), MESSAGE_HEIGHT)
                .setScrollTick(20)
                .setRestDuration(2000)
        ;

        MessageResource.setMessageZone(messageZone);

        registerZones(female, male, messageZone, label);

    }
}
