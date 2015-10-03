package net.amarantha.lightboard.updater.schedule;

import com.google.inject.Inject;
import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.impl.TextZone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostMessageUpdater extends Updater {

    private List<TextZone> zones = new ArrayList<>();
    private List<String> messages = new ArrayList<>();

    @Inject
    public PostMessageUpdater(Sync sync) {
        super(sync);
    }

    public void setZones(TextZone... zs) {
        Collections.addAll(this.zones, zs);
    }

    public PostMessageUpdater addMessages(String... msg) {
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
//                zone.overrideMessage(new MessageWrapper(messages.get(i), MessageQueue.Edge.TOP, MessageQueue.Edge.BOTTOM, MessageQueue.HPosition.CENTRE, MessageQueue.VPosition.MIDDLE, 6000));
            }
        }
    }

}
