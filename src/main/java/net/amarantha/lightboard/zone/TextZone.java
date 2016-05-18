package net.amarantha.lightboard.zone;

import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.font.Font;
import net.amarantha.lightboard.font.SimpleFont;

import java.util.LinkedList;
import java.util.Queue;

public class TextZone extends AbstractZone {

    @Override
    public Pattern getNextPattern() {
        String nextMessage = messageQueue.poll();
        if ( nextMessage!=null ) {
            if ( cycleMessages ) {
                messageQueue.offer(nextMessage);
            }
            return font.renderString(nextMessage, getAlignH());
        }
        return null;
    }


    ///////////////////
    // Message Queue //
    ///////////////////

    public TextZone addMessage(String message) {
        messageQueue.offer(message);
        return this;
    }

    public TextZone clearMessages() {
        messageQueue.clear();
        return this;
    }

    private Queue<String> messageQueue = new LinkedList<>();
    private boolean cycleMessages = true;


    //////////
    // Font //
    //////////

    private Font font = new SimpleFont();

    public TextZone setFont(Font font) {
        this.font = font;
        return this;
    }

}
