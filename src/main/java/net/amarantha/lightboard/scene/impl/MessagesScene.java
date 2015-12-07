package net.amarantha.lightboard.scene.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.AlignH;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.zone.impl.CompositeZone;
import net.amarantha.lightboard.zone.impl.TextZone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MessagesScene extends Scene {

    @Inject private SceneManager sceneManager;

    @Inject private LightBoardProperties props;

    @Inject private CompositeZone zone;
    @Inject private TextZone zone1;
    @Inject private TextZone zone2;

    public MessagesScene() {
        super("Messages");
    }

    @Override
    public void build() {

        zone1
            .scrollLeft()
            .setScrollTick(10)
            .setRegion(5, 0, getCols() - 10, getRows() / 2);

        zone2
            .scrollRight()
            .setScrollTick(10)
            .setRegion(5, getRows() / 2, getCols() - 10, getRows() / 2);

        zone.bindZones(zone1, zone2);
        zone.setScrollTick(10);
        zone.addScrollCompleteHandler(() -> {
            System.out.println("CS");
            if ( firstLoad ) {
//                firstLoad = false;
            } else {
                sceneManager.advanceScene();
            }
        });

        zone1.clearAllMessages();
        zone2.clearAllMessages();
        zone1.addMessage("{yellow}LightBoard {green}online");
        zone2.addMessage("at {green}" + props.getIp());

        registerZones(zone);

    }

    private boolean firstLoad = true;


    private List<String> messages = new ArrayList<>();
    private Iterator<String> messageIterator;

    @Override
    public void resume() {
        super.resume();
        System.out.println("R");
        if ( firstLoad ) {
            zone1.addMessage("{yellow}LightBoard {green}online");
            zone2.addMessage("at {green}" + props.getIp());
            firstLoad = false;
        } else {
            zone1.clearAllMessages();
            zone2.clearAllMessages();
            if (!messages.isEmpty()) {
                if (messageIterator == null || !messageIterator.hasNext()) {
                    Collections.shuffle(messages);
                    messageIterator = messages.iterator();
                }
                String m = messageIterator.next();
                String[] bits = m.split("\n");
                zone1.addMessage(bits[0]);
                if (bits.length > 1) {
                    zone2.addMessage(bits[1]);
                } else {
                    zone2.addMessage(" ");
                }
            } else {
                sceneManager.advanceScene();
            }
        }
    }
}
