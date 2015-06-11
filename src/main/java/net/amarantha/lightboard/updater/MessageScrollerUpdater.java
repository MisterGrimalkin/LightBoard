package net.amarantha.lightboard.updater;

import net.amarantha.lightboard.zone.impl.TextZone;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.javalite.http.Http;

import java.util.Iterator;

public class MessageScrollerUpdater extends Updater {

    public MessageScrollerUpdater(TextZone zone) {
        super(zone);
    }

    @Override
    public void refresh() {

        String response = Http.get("http://192.168.0.17:8002/messages").text();

        clearMessages();

        JSONObject responseObj = JSONObject.fromObject(response);
        JSONArray messageArray = (JSONArray)responseObj.get("messages");

        Iterator<JSONObject> messageIter = messageArray.iterator();
        while ( messageIter.hasNext() ) {
            JSONObject messageObj = messageIter.next();
            addMessage(messageObj.getString("message"));
        }

    }
}
