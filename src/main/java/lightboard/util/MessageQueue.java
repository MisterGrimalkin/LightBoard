package lightboard.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static lightboard.util.MessageQueue.Edge.NO_SCROLL;
import static lightboard.util.MessageQueue.HPosition.CENTRE;
import static lightboard.util.MessageQueue.VPosition.MIDDLE;

public class MessageQueue {

    // Message Sources
    private List<Integer> sources = new ArrayList<>();
    private int sourceIndex = 0;
    private int messagesForThisSource = 0;

    // Messages Indexed by Source
    private Map<Integer, List<MessageWrapper>> allMessages = new HashMap<>();
    private Map<Integer, Integer> messagePointers = new HashMap<>();
    private Map<Integer, Integer> maxMessages = new HashMap<>();


    /////////////////////
    // Current Message //
    /////////////////////

    private MessageWrapper currentMessage;

    public MessageWrapper getCurrentMessage() {
        return (currentMessage!=null?currentMessage:DEFAULT_MESSAGE);
    }

    public void advanceMessage() {

       if ( !sources.isEmpty() ) {

            Integer source = sources.get(sourceIndex);
            if ( source==null ) {
                advanceSourceIndex();
                advanceMessage();
            } else {
                if ( messagesForThisSource>=maxMessages.get(source) ) {
                    advanceSourceIndex();
                    advanceMessage();
                } else {
                    List<MessageWrapper> messages = allMessages.get(source);
                    int messagePointer = messagePointers.get(source);
                    if ( messages!=null && !messages.isEmpty() ) {
                        currentMessage = messages.get(messagePointer);
                        messagesForThisSource++;
                        if (++messagePointer >= messages.size()) {
                            messagePointer = 0;
                            messagesForThisSource = maxMessages.get(source);
                        }
                        messagePointers.put(source, messagePointer);
                    } else {
                        advanceSourceIndex();
                    }
                }
            }

        }

    }

    private void advanceSourceIndex() {
        if ( ++sourceIndex>=sources.size() ) {
            sourceIndex = 0;
        }
        messagesForThisSource = 0;
    }


    ////////////////////////
    // Message Management //
    ////////////////////////

    public MessageQueue addMessage(Integer key, String message) {
        return addMessage(key, new MessageWrapper(message));
    }

    public MessageQueue addMessage(Integer key, String message, Edge scrollIn, Edge scrollOut, HPosition hPosition, VPosition vPosition, int centrePause) {
        return addMessage(key, new MessageWrapper(message, scrollIn, scrollOut, hPosition, vPosition, centrePause));
    }

    public MessageQueue addMessage(int key, MessageWrapper wrapper) {
        getOrCreateMessageList(key).add(wrapper);
        return this;
    }

    public void clearMessages(Integer key) {
        getOrCreateMessageList(key).clear();
        messagePointers.put(key, 0);
    }

    public void setMaxMessages(Integer key, int max) {
        maxMessages.put(key, max);
    }

    public int countMessages(Integer key) {
        return getOrCreateMessageList(key).size();
    }

    public int countMessages() {
        int count = 0;
        for ( Entry<Integer, List<MessageWrapper>> entry : allMessages.entrySet() ) {
            count += entry.getValue().size();
        }
        return count;
    }

    private List<MessageWrapper> getOrCreateMessageList(Integer key) {
        List<MessageWrapper> msgs = allMessages.get(key);
        if ( msgs==null ) {
            msgs = new ArrayList<>();
            allMessages.put(key, msgs);
            sources.add(key);
            messagePointers.put(key, 0);
            if ( maxMessages.get(key)==null ) {
                maxMessages.put(key, 2);
            }
        }
        return msgs;
    }


    /////////////
    // Wrapper //
    /////////////

    public static class MessageWrapper {
        String message;
        Edge scrollFrom;
        Edge scrollTo;
        HPosition hPosition;
        VPosition vPosition;
        int restDuration;
        public MessageWrapper(String message) {
            this(message, NO_SCROLL, NO_SCROLL, CENTRE, MIDDLE, 3000);
        }
        public MessageWrapper(String message, Edge scrollFrom, Edge scrollTo, HPosition hPosition, VPosition vPosition, int restDuration) {
            this.message = message;
            this.scrollFrom = scrollFrom;
            this.scrollTo = scrollTo;
            this.hPosition = hPosition;
            this.vPosition = vPosition;
            this.restDuration = restDuration;
        }

        public String getMessage() {
            return message;
        }

        public Edge getScrollFrom() {
            return scrollFrom;
        }

        public Edge getScrollTo() {
            return scrollTo;
        }

        public HPosition gethPosition() {
            return hPosition;
        }

        public VPosition getvPosition() {
            return vPosition;
        }

        public int getRestDuration() {
            return restDuration;
        }
    }

    public enum Edge {TOP_EDGE, LEFT_EDGE, BOTTOM_EDGE, RIGHT_EDGE, NO_SCROLL}
    public enum VPosition { TOP, MIDDLE, BOTTOM }
    public enum HPosition { LEFT, CENTRE, RIGHT }

    private final static MessageWrapper DEFAULT_MESSAGE = new MessageWrapper("* * *");

}
