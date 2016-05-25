package net.amarantha.lightboard.scene;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.AlignH;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.font.SimpleFont;
import net.amarantha.lightboard.font.SmallFont;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.zone.ImageZone;
import net.amarantha.lightboard.zone.TextZone;
import net.amarantha.lightboard.zone.transition.InterlaceOut;
import net.amarantha.lightboard.zone.transition.ScrollIn;
import net.amarantha.lightboard.zone.transition.ScrollOut;

public class BusStopScene extends AbstractScene {

    @Inject @Zone private TextZone whatsOn;
    @Inject @Zone private TextZone messages;
    @Inject @Zone private TextZone label;

    @Inject @Zone private ImageZone imageGreenpeace;
    @Inject @Zone private ImageZone imageBL;

    @Inject private LightBoardProperties props;

    @Override
    public void build() {

        int topBarHeight = 12;
        int whatWidth = 70;

        whatsOn
                .setFont(new SimpleFont())
                .setCanvasLayer(3)
                .setAlignH(AlignH.LEFT)
                .setInTransition(new ScrollIn().setEdge(Edge.BOTTOM).setDuration(200))
                .setOutTransition(new ScrollOut().setEdge(Edge.TOP).setDuration(200))
                .setDisplayTime(3000)
                .setAutoStart(true)
                .setAutoOut(true)
                .setAutoNext(true)
                .setRegion(whatWidth, 0, props.getBoardCols() - whatWidth, topBarHeight);

        label
                .setFont(new SmallFont())
                .setCanvasLayer(3)
                .setOffset(-4, 0)
                .setAlignH(AlignH.RIGHT)
                .setAutoOut(false)
                .setAutoNext(false)
                .setAutoStart(true)
                .setRegion(0, 0, whatWidth, topBarHeight);

        messages
                .setFont(new SimpleFont())
                .setCanvasLayer(3)
                .setAlignH(AlignH.CENTRE)
                .setInTransition(new ScrollIn().setEdge(Edge.RIGHT).setDuration(3500))
                .setOutTransition(new ScrollOut().setEdge(Edge.LEFT).setDuration(3500))
                .setDisplayTime(0)
                .setAutoOut(true)
                .setAutoNext(true)
                .setAutoStart(true)
                .setRegion(0, props.getBoardRows() - 11, props.getBoardCols(), 11);

        imageGreenpeace
                .setImage("gp192x32.jpg")
                .setCanvasLayer(1)
                .setAutoNext(false)
                .setAutoOut(true)
                .setAutoStart(true)
                .setRegion(0, 0, props.getBoardCols(), props.getBoardRows())
                .setDisplayTime(4000)
                .setInTransition(new ScrollIn().setEdge(Edge.RIGHT).setDuration(1200))
                .setOutTransition(new InterlaceOut().setDuration(500));

        imageBL
                .setImage("bl.jpg")
                .setCanvasLayer(1)
                .setAutoNext(false)
                .setAutoOut(true)
                .setAutoStart(false)
                .setAlignV(AlignV.TOP)
                .setRegion(props.getBoardCols()-71, 0, props.getBoardCols(), props.getBoardRows())
                .setDisplayTime(4000)
                .setInTransition(new ScrollIn().setEdge(Edge.BOTTOM).setDuration(1200))
                .setOutTransition(new ScrollOut().setEdge(Edge.TOP).setDuration(500));


        whatsOn.addMessage("1700: JAMES");
        whatsOn.addMessage("1800: A BAND");
        whatsOn.addMessage("1900: JESTERS and JUGGLERS");


        messages.addMessage("{green}There is NO Planet B");
        messages.addMessage("{yellow}Feeling Dirty? Another Shower is Possible!");
        messages.addMessage("{red}ALL DAY IN THE DOME: {yellow}4D Coral Reef Experience with Sir David Attenborough");
        messages.addMessage("{red}Come into our lush field to discover our hot showers, delicious cakes, etc)");
        messages.addMessage("{red}Solar energy generated today: 1000Wh and counting");
        messages.addMessage("{green}Become part of our {yellow}energy grid - don’t be shy, grab a stranger and hold their hand to complete the circuit. {red}Who knows where it might lead?");


        label.addMessage("{red}COMING UP NEXT");

        imageGreenpeace.onOutComplete(()->imageBL.in());
        imageBL.onOutComplete(()-> imageGreenpeace.in());




    }
}
