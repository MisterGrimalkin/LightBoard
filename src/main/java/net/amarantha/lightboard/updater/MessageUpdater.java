package net.amarantha.lightboard.updater;


import net.amarantha.lightboard.entity.MessageBundle;
import net.amarantha.lightboard.zone.impl.TextZone;

import java.util.Map;

public class MessageUpdater extends Updater {

    private MessageBundle.Wrapper wrapper;

    public MessageUpdater(TextZone zone) {
        super(zone);

        wrapper = new MessageBundle.Wrapper();

        zone.setMaxMessagesPerSource(0, 1);
        zone.setMaxMessagesPerSource(1, 2);

    }

    @Override
    public void refresh() {

        int i =0;
        for ( MessageBundle bundle : wrapper.getBundles() ) {
            zone.clearMessages(i);
            zone.setMaxMessagesPerSource(i, bundle.getMaxMessages());
            String defCol = bundle.getDefaultColour();
            if ( defCol!=null && !defCol.isEmpty() ) {
                defCol = "{" + defCol + "}";
            }
            for ( Map.Entry<String, String> entry : bundle.getMessages().entrySet() ) {
                zone.addMessage(i, defCol + entry.getValue());
            }
            i++;
        }

    }

}
