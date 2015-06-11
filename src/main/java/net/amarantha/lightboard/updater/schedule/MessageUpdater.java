package net.amarantha.lightboard.updater.schedule;

import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.zone.impl.TextZone;

import java.util.ArrayList;
import java.util.List;

public class MessageUpdater extends Updater {

    private List<TextZone> zones = new ArrayList<>();
    private List<String> messages = new ArrayList<>();

    public MessageUpdater(TextZone... zs) {
        super(zs[0]);
        for ( int i=0; i<zs.length; i++ ) {
            this.zones.add(zs[i]);
        }
    }

    public MessageUpdater addMessages(String... msg) {
        for ( int i=0; i<msg.length; i++ ) {
            messages.add(msg[i]);
        }
        return this;
    }

    public void postMessage(String... msg) {
        messages.clear();
        addMessages(msg);
        refresh();
        messages.clear();
    }

    @Override
    public void refresh() {
        for ( int i=0; i< zones.size(); i++ ) {
            TextZone zone = zones.get(i);
            if ( i<messages.size() ) {
                zone.overrideMessage(zone.wrap(messages.get(i)));
//                zone.overrideMessage(new MessageWrapper(messages.get(i), MessageQueue.Edge.TOP_EDGE, MessageQueue.Edge.BOTTOM_EDGE, MessageQueue.HPosition.CENTRE, MessageQueue.VPosition.MIDDLE, 6000));
            }
        }
    }

}
