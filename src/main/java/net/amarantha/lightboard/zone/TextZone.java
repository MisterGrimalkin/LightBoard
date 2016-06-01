package net.amarantha.lightboard.zone;

import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.font.Font;
import net.amarantha.lightboard.font.SimpleFont;

import java.util.LinkedList;
import java.util.Queue;

public class TextZone extends AbstractZone {

    @Override
    public Pattern getNextPattern() {
        if ( group ==null ) {
            Message nextMessage = messageQueue.poll();
            if (nextMessage != null) {
                if (cycleMessages) {
                    messageQueue.offer(nextMessage);
                }
                return font.renderString(nextMessage.getText(), getAlignH());
            }
        } else {
            Message nextMessage = group.requestMessage(this);
            if ( nextMessage!=null ) {
                return font.renderString(nextMessage.getText(), getAlignH());
            }
        }
        return null;
    }

    private MessageGroup group;

    public void setGroup(MessageGroup group) {
        this.group = group;
    }

    public MessageGroup getGroup() {
        return group;
    }

    ///////////////////
    // Message Queue //
    ///////////////////

    public TextZone addMessage(String message) {
        return addMessage(new Message(message));
    }

    public TextZone addMessage(Message message) {
        messageQueue.offer(message);
        return this;
    }

    public TextZone clearMessages() {
        messageQueue.clear();
        return this;
    }

    public void setCycleMessages(boolean cycleMessages) {
        this.cycleMessages = cycleMessages;
    }

    private Queue<Message> messageQueue = new LinkedList<>();
    private boolean cycleMessages = true;


    //////////
    // Font //
    //////////

    public TextZone setFont(Font font) {
        this.font = font;
        return this;
    }

    private Font font = new SimpleFont();

}
