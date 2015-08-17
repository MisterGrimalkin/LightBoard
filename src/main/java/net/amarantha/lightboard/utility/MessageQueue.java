package net.amarantha.lightboard.utility;

import net.amarantha.lightboard.entity.AlignH;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.entity.MessageWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


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
                    Integer messagePointer = messagePointers.get(source);
                    if ( messagePointer==null || messagePointer>=messages.size() ) {
                        messagePointers.put(source, 0);
                        messagePointer = 0;
                    }
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

    public void resetSourceIndex() {
        sourceIndex = 0;
        messagesForThisSource = 0;
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

    public MessageQueue addMessage(Integer key, String message, Edge scrollIn, Edge scrollOut, AlignH alignH, AlignV alignV, int centrePause) {
        return addMessage(key, new MessageWrapper(message, scrollIn, scrollOut, alignH, alignV, centrePause));
    }

    public MessageQueue addMessage(int key, MessageWrapper wrapper) {
        getOrCreateMessageList(key).add(wrapper);
        return this;
    }

    public void clearAllMessages() {
        allMessages.clear();
    }

    public void clearMessages(Integer key) {
        getOrCreateMessageList(key).clear();
//        messagePointers.put(key, 0);
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
//            messagePointers.put(key, 0);
            if ( maxMessages.get(key)==null ) {
                maxMessages.put(key, 2);
            }
        }
        return msgs;
    }


    /////////////
    // Wrapper //
    /////////////

    private final static MessageWrapper DEFAULT_MESSAGE = new MessageWrapper("{red}* {yellow}* {green}*");

}
