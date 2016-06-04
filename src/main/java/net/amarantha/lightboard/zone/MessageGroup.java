package net.amarantha.lightboard.zone;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MessageGroup {

    public MessageGroup(String[] fields) {
        this.fields = fields;
        nextMessageSet();
    }

    private String id;
    private String[] fields;
    private Map<Integer, Map<String, Message>> allMessages = new HashMap<>();
    private Map<String, Boolean> hasCollected = new HashMap<>();
    private int insertPointer = 0;
    private int requestPointer = -1;

    public void clearMessages() {
        allMessages.clear();
        insertPointer = 0;
        nextMessageSet();
        saveMessages();
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
        saveMessages();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private boolean hasCollected(String id) {
        return hasCollected.get(id)!=null && hasCollected.get(id);
    }

    private boolean allCollected() {
        return !hasCollected.values().contains(false);
    }


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void saveMessages() {
        if ( filename!=null ) {
            try (FileWriter file = new FileWriter(filename)) {
                file.write(getAsJson());
                file.flush();
            } catch (IOException e) {
                System.out.println("Error writing file");
            }
        }
    }

    private String filename;

    public void loadMessages() {
        if ( filename!=null ) {
            try (FileReader file = new FileReader(filename)) {
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(file);
                Object pointer = obj.get("requestPointer");
                if ( pointer!=null ) {
                    requestPointer = Integer.parseInt(pointer.toString());
                }
                JSONArray messagesJson = (JSONArray) obj.get("messages");
                if (messagesJson != null) {
                    Iterator<JSONObject> iter = messagesJson.iterator();
                    allMessages.clear();
                    insertPointer = 0;
                    while (iter.hasNext()) {
                        JSONObject jsonEntry = iter.next();
                        Map<String, Message> messages = new HashMap<>();
                        for (String field : fields) {
                            JSONObject messageJson = (JSONObject) jsonEntry.get(field);
                            if (messageJson != null) {
                                messages.put(field, new Message(messageJson.get("id").toString(), messageJson.get("text").toString()));
                            }
                        }
                        allMessages.put(insertPointer++, messages);
                    }
                }
            } catch (IOException e) {
                saveMessages();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private String getAsJson() {

        JSONObject json = new JSONObject();
        JSONArray messages = new JSONArray();
        json.put("messages", messages);
        json.put("requestPointer", requestPointer);

        for ( Entry<Integer, Map<String, Message>> entry : allMessages.entrySet() ) {
            JSONObject messageObject = new JSONObject();
            for ( Entry<String, Message> messageEntry : entry.getValue().entrySet() ) {
                messageObject.put(messageEntry.getKey(), messageEntry.getValue());
            }
            messages.add(messageObject);
        }

        return json.toJSONString();

    }

}
