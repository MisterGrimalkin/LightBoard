package net.amarantha.lightboard.zone.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.*;
import net.amarantha.lightboard.font.Font;
import net.amarantha.lightboard.font.SimpleFont;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.MessageQueue;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.LightBoardZone;

public class TextZone extends LightBoardZone {

    private Font font;

    @Inject
    public TextZone(LightBoardSurface surface, Sync sync) {
        super(surface, sync);
        setDefaults();
        addScrollCompleteHandler(() -> {
            if (override) {
                TextZone.this.clearOverride();
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


    /////////////
    // Factory //
    /////////////

    public TextZone fixed() {
        scroll(Edge.NO_SCROLL, Edge.NO_SCROLL);
        setScrollTick(1000);
        setRestDuration(1000);
        return this;
    }

    public TextZone scrollLeft() {
        scroll(Edge.RIGHT, Edge.LEFT);
        setScrollTick(25);
        return this;
    }

    public TextZone scrollRight() {
        scroll(Edge.LEFT, Edge.RIGHT);
        setScrollTick(25);
        return this;
    }

    public TextZone scrollUp() {
        scroll(Edge.BOTTOM, Edge.TOP);
        setScrollTick(60);
        return this;
    }

    public TextZone scrollDown() {
        scroll(Edge.TOP, Edge.BOTTOM);
        setScrollTick(60);
        return this;
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

    public TextZone clearAllMessages() {
        messageQueue.clearAllMessages();
        return this;
    }

    public TextZone replaceMessage(String message) {
        clearMessages();
        addMessage(message);
        return this;
    }

    public TextZone clearMessages() {
        messageQueue.clearMessages(0);
        return this;
    }

    public TextZone clearMessages(int id) {
        messageQueue.clearMessages(id);
        return this;
    }

    public TextZone addMessage(String message) {
        return addMessage(0, wrap(message));
    }

    public TextZone addMessage(int id, String message) {
        return addMessage(id, wrap(message));
    }

    public TextZone addMessage(int id, MessageWrapper message) {
        messageQueue.addMessage(id, message);
        return this;
    }

    public MessageWrapper wrap(String message) {
        return new MessageWrapper(message, getScrollFrom(), getScrollTo(), getRestPositionH(), getRestPositionV(), getRestDuration());
    }

    public void setMaxMessagesPerSource(int id, int max) {
        messageQueue.setMaxMessages(id, max);
    }

    public TextZone setFont(Font font) {
        this.font = font;
        return this;
    }

    public Font getFont() {
        return font;
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
