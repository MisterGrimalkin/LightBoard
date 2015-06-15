package net.amarantha.lightboard.webservice;

import net.amarantha.lightboard.entity.MessageBundle;
import net.amarantha.lightboard.zone.impl.TextZone;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Iterator;
import java.util.Map;

@Path("messages")
public class MessageResource {

    private static TextZone messageZone;

    public static void setMessageZone(TextZone messageZone) {
        MessageResource.messageZone = messageZone;
    }

    @POST
    public static Response postMessages(String text) {
        JSONObject json = JSONObject.fromObject(text);
        JSONArray ja = json.getJSONArray("bundles");
        MessageBundle.Wrapper wrapper = new MessageBundle.Wrapper();

        Iterator<JSONObject> iter = ja.iterator();
        while ( iter.hasNext() ) {
            JSONObject obj = iter.next();
            MessageBundle bundle = (MessageBundle)JSONObject.toBean(obj, MessageBundle.class);
            wrapper.addBundle(bundle);
        }

        for ( int i=0; i<wrapper.getBundles().size(); i++ ) {
            MessageBundle bundle = wrapper.getBundles().get(i);
            messageZone.clearMessages(i);
            messageZone.setMaxMessagesPerSource(i, bundle.getMaxMessages());
            for (Map.Entry<String, String> entry : bundle.getMessages().entrySet() ) {
                messageZone.addMessage(i, "{"+bundle.getDefaultColour()+"}"+entry.getValue() );
            }
        }

        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .entity("Post Messages")
                .build();
    }



}
