package net.amarantha.lightboard.zone;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MessageGroup {

    private String[] fields;

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MessageGroup(String[] fields) {
        this.fields = fields;
        nextMessageSet();
    }

    private Map<Integer, Map<String, Message>> allMessages = new HashMap<>();

    private int insertPointer = 0;
    private int requestPointer = -1;

    public void clearMessages() {
        allMessages.clear();
        insertPointer = 0;
        nextMessageSet();
    }

    public void addMessages(String input) {
        String[] msgs = input.split(",");
        if ( msgs.length!=fields.length ) {
            throw new IllegalArgumentException("Wrong number of messages, expected " + fields.length);
        }
        Map<String, Message> messages = new LinkedHashMap<>();
        for ( int i=0; i<fields.length; i++ ) {
            messages.put(fields[i], new Message(msgs[i]));
        }
        allMessages.put(insertPointer++, messages);
    }

    private Map<String, Boolean> hasCollected = new HashMap<>();

    private boolean hasCollected(String id) {
        return hasCollected.get(id)!=null && hasCollected.get(id);
    }

    public void nextMessageSet() {
        requestPointer++;
        if ( requestPointer >= allMessages.keySet().size() ) {
            requestPointer = 0;
        }
        for ( String field : fields ) {
            hasCollected.put(field, false);
        }
    }

    public Message requestMessage(TextZone zone) {
        String id = zone.getId();
        if ( hasCollected(id) ) {
            return null;
        }
        Map<String, Message> currentMessages = allMessages.get(requestPointer);
        if ( currentMessages!=null ) {
            Message result = currentMessages.get(id);
            if ( result!=null ) {
                hasCollected.put(id, true);
                if (allCollected()) {
                    nextMessageSet();
                }
                return result;
            }
        }
        return null;
    }

    private boolean allCollected() {
        return !hasCollected.values().contains(false);
    }



}
