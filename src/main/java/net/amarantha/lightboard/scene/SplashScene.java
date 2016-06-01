package net.amarantha.lightboard.scene;

import com.google.inject.Inject;
import net.amarantha.lightboard.font.SimpleFont;
import net.amarantha.lightboard.font.SmallFont;
import net.amarantha.lightboard.module.Zone;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.zone.ImageZone;
import net.amarantha.lightboard.zone.TextZone;
import net.amarantha.lightboard.zone.transition.*;

import static net.amarantha.lightboard.entity.Domino.IN;
import static net.amarantha.lightboard.entity.Domino.OUT;

public class SplashScene extends AbstractScene {

    @Inject private LightBoardProperties props;

    @Inject @Zone private TextZone textZone1;
    @Inject @Zone private TextZone textZone2;
    @Inject @Zone private TextZone textZone3;
    @Inject @Zone private TextZone textZone4;
    @Inject @Zone private ImageZone imageZone1;

    @Override
    public void build() {
        int typingDuration = 1200;
        int explodeDuration = 800;

        textZone1
                .setFont(new SmallFont())
                .setAutoStart(true)
                .setOffset(0, -9)
                .setInTransition(new TypeIn().setDuration(typingDuration))
                .setOutTransition(new ExplodeOut().setDuration(explodeDuration))
                .setDisplayTime(300)
                .setCanvasLayer(1);

        textZone2
                .setFont(new SimpleFont())
                .setInTransition(new TypeIn().setDuration(typingDuration))
                .setOutTransition(new ExplodeOut().setDuration(explodeDuration))
                .setDisplayTime(300)
                .setCanvasLayer(2);

        textZone3
                .setFont(new SmallFont())
                .setOffset(0, 9)
                .setInTransition(new TypeIn().setDuration(typingDuration))
                .setOutTransition(new ExplodeOut().setDuration(explodeDuration))
                .setDisplayTime(2000)
                .setCanvasLayer(3);

        textZone4
                .setFont(new SmallFont())
                .setAutoOut(true)
                .setInTransition(new ExplodeIn().setDuration(explodeDuration))
                .setOutTransition(new ExplodeOut().setDuration(explodeDuration))
                .setDisplayTime(5000)
                .setCanvasLayer(1);

        imageZone1
                .setImage("gp192x32.jpg")
                .setAutoOut(true)
                .setInTransition(new RainIn().setDuration(2500))
                .setOutTransition(new InterlaceOut().setDuration(300))
                .setDisplayTime(4000)
                .setCanvasLayer(3);

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

        textZone1
            .afterIn(IN, textZone2
                .afterIn(IN, textZone3
                    .afterDisplay(OUT, textZone1, textZone2, textZone3
                        .whenOutAt(0.4, IN, textZone4
                            .whenOutAt(0.6, IN, imageZone1
                                .afterOut(IN, textZone1))))));

    }

}
