package net.amarantha.lightboard.updater;

import net.amarantha.lightboard.zone.impl.TextZone;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.javalite.http.Http;

import java.util.*;

public class MessageUpdater extends Updater {

    private Map<Integer, List<String>> messages = new HashMap<>();

    private final static String[] sysMsg = {
            "{red}Feeling Dirty?",
            "{red}Another Shower Is Possible!",
            "{red}If your ticket has been called, don't worry.",
            "{red}Fucin' eh",
            "{red}Well, here's a fine thing"
    };

    private final static String[] usrMsg = {
            "Wanky Spank",
            "Wafty Crank",
            "Smelly Cunt",
            "Stained Necktie",
            "Bill Bong",
            "Silly Wotsit",
            "Can you eat crisps with a fork?",
            "It's behind you"
    };

    public MessageUpdater(TextZone zone) {
        super(zone);

        messages.put(0, Arrays.asList(sysMsg));

        messages.put(1, Arrays.asList(usrMsg));

        zone.setMaxMessagesPerSource(0, 1);
        zone.setMaxMessagesPerSource(1, 2);

    }

    @Override
    public void refresh() {

        int i =0;
        for (Map.Entry<Integer, List<String>> entry : messages.entrySet() ) {
            zone.clearMessages(i);
            for ( String msg : entry.getValue() ) {
                zone.addMessage(i, msg);
            }
            i++;
        }

//        String response = Http.get("http://192.168.0.17:8002/messages").text();
//
//        JSONObject responseObj = JSONObject.fromObject(response);
//        JSONArray messageArray = (JSONArray)responseObj.get("messages");
//
//        Iterator<JSONObject> messageIter = messageArray.iterator();
//        while ( messageIter.hasNext() ) {
//            JSONObject messageObj = messageIter.next();
//            addMessage(messageObj.getString("message"));
//        }

    }
}
