package net.amarantha.lightboard.zone.old;

import com.google.inject.Inject;
import net.amarantha.lightboard.font.Font;
import net.amarantha.lightboard.font.SimpleFont;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.MessageQueue;
import net.amarantha.lightboard.utility.Sync;

public class TextZone_Old extends LightBoardZone {

    private Font font;

    @Inject
    public TextZone_Old(LightBoardSurface surface, Sync sync) {
        super(surface, sync);
        setDefaults();
        addScrollCompleteHandler(() -> {
            if (override) {
                TextZone_Old.this.clearOverride();
            }
            advanceMessage();
        });
    }

    private void setDefaults() {
        font = new SimpleFont();
        scroll(Edge.NO_SCROLL, Edge.NO_SCROLL);
        setRestPosition(AlignH.CENTRE, AlignV.MIDDLE);
        setRestDuration(3000);
        setScrollTick(60);
    }

    public void advanceMessage() {
        messageQueue.advanceMessage();
    }



    /////////////////////////
    // Zone Implementation //
    /////////////////////////

    private Pattern pattern;
    private MessageWrapper lastMessage;

    @Override
    public boolean render() {
        if ( !paused ) {
            if ( singleRender && clear ) {
                clear();
            }
            MessageWrapper message = getCurrentMessage();
            if ( !message.equals(lastMessage) ) {
                pattern = font.renderString(message.getMessage(), message.setAlignH());
            }
            return drawPattern(0, 0, pattern, true);
        }
        return true;
    }

    @Override
    public int getContentWidth() {
        return font.getStringWidth(getCurrentMessage().getMessage());
    }

    @Override
    public int getContentHeight() {
        return font.getStringHeight(getCurrentMessage().getMessage());
    }

    @Override
    public void resetScroll() {
        advanceMessage();
        super.resetScroll();
    }

    //////////////
    // Messages //
    //////////////

    private MessageQueue messageQueue = new MessageQueue();

    public void resetMessageSources() {
        messageQueue.resetSourceIndex();
    }

    public MessageWrapper getCurrentMessage() {
        return overrideMessage==null?messageQueue.getCurrentMessage():overrideMessage;
    }

    public TextZone_Old clearAllMessages() {
        messageQueue.clearAllMessages();
        return this;
    }

    public TextZone_Old replaceMessage(String message) {
        clearMessages();
        addMessage(message);
        return this;
    }

    public TextZone_Old clearMessages() {
        messageQueue.clearMessages(0);
        return this;
    }

    public TextZone_Old clearMessages(int id) {
        messageQueue.clearMessages(id);
        return this;
    }

    public TextZone_Old addMessage(String message) {
        return addMessage(0, wrap(message));
    }

    public TextZone_Old addMessage(int id, String message) {
        return addMessage(id, wrap(message));
    }

    public TextZone_Old addMessage(int id, MessageWrapper message) {
        messageQueue.addMessage(id, message);
        return this;
    }

    public MessageWrapper wrap(String message) {
        return new MessageWrapper(message, getScrollFrom(), getScrollTo(), getRestPositionH(), getRestPositionV(), getRestDuration());
    }

    public void setMaxMessagesPerSource(int id, int max) {
        messageQueue.setMaxMessages(id, max);
    }

    public TextZone_Old setFont(Font font) {
        this.font = font;
        return this;
    }

    public Font getFont() {
        return font;
    }

    public int countMessages() {
        return messageQueue.countMessages();
    }

    //////////////
    // Override //
    //////////////

    private boolean override = false;
    private MessageWrapper overrideMessage = null;

    public boolean isOverride() {
        return override;
    }

    public void overrideMessage(String message) {
        overrideMessage(wrap(message));
    }

    public void overrideMessage(MessageWrapper message) {
        overrideMessage = message;
        resting = false;
        scroll(message.getScrollFrom(), message.getScrollTo());
        setRestPosition(message.setAlignH(), message.setAlignV());
        setRestDuration(message.getRestDuration());
        resetScroll();
        rendered = false;
        render();
        override = true;
    }

    public void clearOverride() {
        overrideMessage = null;
        override = false;
        setDefaults();
        rendered = false;
        render();
    }

}
