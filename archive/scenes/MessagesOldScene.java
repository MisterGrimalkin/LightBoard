package net.amarantha.lightboard.scene.old;

import com.google.inject.Inject;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.zone.old.CompositeZone;
import net.amarantha.lightboard.zone.old.TextZone_Old;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MessagesOldScene extends OldScene {

    @Inject private OldSceneManager sceneManager;

    @Inject private LightBoardProperties props;

    @Inject private CompositeZone zone;
    @Inject private TextZone_Old zone1;
    @Inject private TextZone_Old zone2;

    public MessagesOldScene() {
        super("Messages");
    }

    @Override
    public void build() {

        zone1
            .scrollLeft()
            .setRestDuration(props.getInt("message1Pause", 1000))
            .setRegion(5, 0, getCols() - 10, getRows() / 2);

        zone2
            .scrollRight()
            .setRestDuration(props.getInt("message2Pause", 18900))
            .setRegion(5, getRows() / 2, getCols() - 10, getRows() / 2);

        zone.bindZones(zone1, zone2);
        zone.setScrollTick(props.getInt("messageScrollTick", 20));
        zone.addScrollCompleteHandler(() -> {
            if ( !firstLoad ) {
                sceneManager.advanceScene();
            }
        });

        zone1.clearAllMessages();
        zone2.clearAllMessages();

        messages.add(props.getString("message1", "{red}Our lovely housemates....            {yellow}Ben, Sarah, Dan, Barri, Gilles, Lewis, Gaby, Manu, Lauren, Jonny, Eli, Annabel, Maddie, Chris, Zorana, Adas, Mischa, Nes, Becca, Kim, Josh, Grace and Rough Keith.... {red}Thank you all, you wonderful people xxxx")
                +"\n"
                +props.getString("message2", "{green}Wolsely Road Crew 2010-2016"));

        registerZones(zone);

    }

    private boolean firstLoad = false;

    private List<String> messages = new ArrayList<>();
    private Iterator<String> messageIterator;

    @Override
    public void resume() {
        super.resume();
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
