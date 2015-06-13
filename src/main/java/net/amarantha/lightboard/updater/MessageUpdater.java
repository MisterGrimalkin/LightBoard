package net.amarantha.lightboard.updater;


import net.amarantha.lightboard.entity.MessageBundle;
import net.amarantha.lightboard.zone.impl.TextZone;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.javalite.http.Http;

import java.io.IOException;
import java.util.*;

public class MessageUpdater extends Updater {

    private MessageBundle.Wrapper wrapper;

    public MessageUpdater(TextZone zone) {
        super(zone);

        wrapper = new MessageBundle.Wrapper();

        wrapper
                .addBundle(
                    new MessageBundle("Admin", 1, "red")
                        .addMessage("Please Collect A Ticket From The Shower Steward")
                        .addMessage("If Your Number Has Already Been Called You Can Still Use It")
                        .addMessage("Please Respect The Showers and Don't Leave Pubes Everywhere")
                )
                .addBundle(
                    new MessageBundle("User", 2, "green")
                        .addMessage("Awesome bands on tonight")
                        .addMessage("Isn't this great?")
                        .addMessage("Some Other Stuff")
                        .addMessage("And We Have Things to Tell you")
                        .addMessage("I need some breakfast")
                );

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
            for ( String message : bundle.getMessages() ) {
                zone.addMessage(i, defCol + message);
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

        MessageBundle.Wrapper wrapper = new MessageBundle.Wrapper();
        wrapper
                .addBundle(new MessageBundle("Admin", 1, "red").addMessage(""))
                .addBundle(new MessageBundle("User", 3, "green"));

        ObjectMapper mapper = new ObjectMapper();
        try {
            String b = mapper.writeValueAsString(wrapper);
            System.out.println(b);

            MessageBundle.Wrapper w = mapper.readValue(b, MessageBundle.Wrapper.class);
            System.out.println();

//            mapper.readValue(b, )
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
