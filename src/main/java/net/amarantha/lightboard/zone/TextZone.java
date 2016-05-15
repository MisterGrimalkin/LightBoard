package net.amarantha.lightboard.zone;

import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.font.Font;
import net.amarantha.lightboard.font.SimpleFont;

import java.util.LinkedList;
import java.util.Queue;

public class TextZone extends AbstractZone {

    private Font font = new SimpleFont();

    private boolean cycleMessages = true;

    private Queue<String> messageQueue = new LinkedList<>();

    public void addMessage(String message) {
        messageQueue.offer(message);
    }

    public void clearMessages() {
        messageQueue.clear();
    }

    @Override
    public Pattern getNextPattern() {
        String nextMessage = messageQueue.poll();
        if ( nextMessage!=null ) {
            if ( cycleMessages ) {
                messageQueue.offer(nextMessage);
            }
            System.out.println(nextMessage);
            return font.renderString(nextMessage, getAlignH());
        }
        return null;
    }

    public void setFont(Font font) {
        this.font = font;
    }

}
