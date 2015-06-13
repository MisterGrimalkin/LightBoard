package net.amarantha.lightboard.entity;

import java.util.ArrayList;
import java.util.List;

public class MessageBundle {

    private String name;
    private int maxMessages;
    private String defaultColour;
    private List<String> messages;

    public MessageBundle() {
        messages = new ArrayList<>();

    }

    public MessageBundle(String name, int maxMessages, String defaultColour) {
        this();
        this.name = name;
        this.maxMessages = maxMessages;
        this.defaultColour = defaultColour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxMessages() {
        return maxMessages;
    }

    public void setMaxMessages(int maxMessages) {
        this.maxMessages = maxMessages;
    }

    public String getDefaultColour() {
        return defaultColour;
    }

    public void setDefaultColour(String defaultColour) {
        this.defaultColour = defaultColour;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public MessageBundle addMessage(String message) {
        messages.add(message);
        return this;
    }


    public static class Wrapper {

        private List<MessageBundle> bundles;

        public Wrapper() {
            bundles = new ArrayList<>();
        }

        public List<MessageBundle> getBundles() {
            return bundles;
        }

        public void setBundles(List<MessageBundle> bundles) {
            this.bundles = bundles;
        }

        public Wrapper addBundle(MessageBundle bundle) {
            bundles.add(bundle);
            return this;
        }

    }
}
