package net.amarantha.lightboard.scene;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.font.SimpleFont;
import net.amarantha.lightboard.font.SmallFont;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.zone.ImageZone;
import net.amarantha.lightboard.zone.TextZone;
import net.amarantha.lightboard.zone.transition.*;

public class SplashScene extends AbstractScene {

    @Inject @Zone private TextZone textZone1;
    @Inject @Zone private TextZone textZone2;
    @Inject @Zone private TextZone textZone3;
    @Inject @Zone private TextZone textZone4;
    @Inject @Zone private ImageZone imageZone1;
//    @Inject @Zone private ImageZone imageZone2;

    @Inject private LightBoardProperties props;



    @Override
    public void build() {
        int typingDuration = 1200;
        int explodeDuration = 800;

        int width = props.getBoardCols();
        int height = props.getBoardRows();

        textZone1
                .setFont(new SmallFont())
                .setRegion(0, 0, width, height)
                .setDisplayTime(300)
                .setOffset(0, -9)
                .setAutoOut(false)
                .setAutoNext(false)
                .setAutoStart(true)
                .setAlignV(AlignV.MIDDLE)
                .setInTransition(new TypeIn().setDuration(typingDuration))
                .setOutTransition(new ExplodeOut().setDuration(explodeDuration))
                .setCanvasLayer(2);

        textZone2
                .setFont(new SimpleFont())
                .setRegion(0, 0, width, height)
                .setDisplayTime(300)
                .setAutoOut(false)
                .setAutoNext(false)
                .setAlignV(AlignV.MIDDLE)
                .setInTransition(new TypeIn().setDuration(typingDuration))
                .setOutTransition(new ExplodeOut().setDuration(explodeDuration))
                .setCanvasLayer(3);

        textZone3
                .setFont(new SmallFont())
                .setRegion(0, 0, width, height)
                .setDisplayTime(2000)
                .setOffset(0, 9)
                .setAutoOut(false)
                .setAutoNext(false)
                .setAlignV(AlignV.MIDDLE)
                .setInTransition(new TypeIn().setDuration(typingDuration))
                .setOutTransition(new ExplodeOut().setDuration(explodeDuration))
                .setCanvasLayer(4);

        textZone4
                .setFont(new SmallFont())
                .setRegion(0, 0, width, height)
                .setDisplayTime(5000)
                .setAutoOut(true)
                .setAutoNext(false)
                .setAlignV(AlignV.MIDDLE)
                .setInTransition(new ExplodeIn().setDuration(explodeDuration))
                .setOutTransition(new ExplodeOut().setDuration(explodeDuration))
                .setCanvasLayer(5);

        imageZone1
                .addImage("gp192x32.jpg")
                .setCanvasLayer(3)
                .setAutoNext(false)
                .setRegion(0, 0, width, height)
                .setDisplayTime(4000)
                .setInTransition(new RainDown().setDuration(2500))
                .setOutTransition(new InterlaceOut().setDuration(300));

//        imageZone2
//                .addImage("glastotiny.jpg")
//                .setCanvasLayer(2)
//                .setAutoNext(false)
//                .setAlignV(AlignV.BOTTOM)
//                .setRegion(0, 0, 192, 32)
//                .setDisplayTime(3000)
////                .setInTransition(new ScrollIn().setEdge(Edge.BOTTOM).setDuration(2000))
////                .setInTransition(new RainDown().setDuration(2500))
////                .setOutTransition(new ScrollOut().setEdge(Edge.TOP).setDuration(2000));
//                .setOutTransition(new InterlaceOut().setDuration(300));

        String ip = props.getIp();
        if ( ip==null || ip.isEmpty() ) {
            ip = "{red}OFFLINE";
        } else {
            ip = "{green}ONLINE {red}at {yellow}" + ip;
        }

        textZone1.addMessage("{red}Bailes + Light");
        textZone2.addMessage("{yellow}LightBoard Display System");
        textZone3.addMessage("{green}Version 2.0");
        textZone4.addMessage("{red}Web Service " + ip);

        textZone1.onInComplete(()->textZone2.in());
        textZone2.onInComplete(()->textZone3.in());
        textZone3.onDisplayComplete(()->{
            textZone1.out();
            textZone2.out();
            textZone3.out();
        });
        textZone3.onOutAt(0.4, () -> textZone4.in());
        textZone4.onOutAt(0.6, () -> imageZone1.in());
        imageZone1.onOutComplete(() -> textZone1.in());
//        imageZone2.onOutComplete(() -> textZone1.in());

    }



}
