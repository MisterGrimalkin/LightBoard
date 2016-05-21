package net.amarantha.lightboard.zone;

import java.util.UUID;

public class Message {

    private final String id;
    private final String text;

    public Message(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public Message(String text) {
        id = UUID.randomUUID().toString();
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

}
