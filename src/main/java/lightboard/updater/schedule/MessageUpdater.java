package lightboard.updater.schedule;

import lightboard.board.zone.impl.TextZone;
import lightboard.updater.Updater;

import java.util.ArrayList;
import java.util.List;

public class MessageUpdater extends Updater {

    public static MessageUpdater updater(TextZone... scrollers) {
        return new MessageUpdater(scrollers);
    }

    private List<TextZone> zones = new ArrayList<>();
    private List<String> messages = new ArrayList<>();

    private MessageUpdater(TextZone... scroller) {
        super(scroller[0]);
        for ( int i=0; i<scroller.length; i++ ) {
            zones.add(scroller[i]);
        }
    }

    public MessageUpdater addMessages(String... msg) {
        for ( int i=0; i<msg.length; i++ ) {
            messages.add(msg[i]);
        }
        return this;
    }

    @Override
    public void refresh() {
        for ( int i=0; i< zones.size(); i++ ) {
            TextZone zone = zones.get(i);
            zone.overrideMessage(zone.wrap(messages.get(i)));
        }
    }

}
