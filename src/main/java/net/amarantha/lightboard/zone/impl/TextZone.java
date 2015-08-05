package net.amarantha.lightboard.zone.impl;

import net.amarantha.lightboard.entity.*;
import net.amarantha.lightboard.font.Font;
import net.amarantha.lightboard.font.SimpleFont;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.util.MessageQueue;
import net.amarantha.lightboard.zone.LightBoardZone;

public class TextZone extends LightBoardZone {

    private Font font;

    private Edge defaultScrollFrom;
    private Edge defaultScrollTo;
    private AlignH defaultRestH;
    private AlignV defaultRestV;
    private int defaultRestDuration;

    public TextZone(LightBoardSurface surface, Edge scrollFrom, Edge scrollTo, int restDuration) {
            this(surface, scrollFrom, scrollTo, restDuration, new SimpleFont());
    }

    public TextZone(LightBoardSurface surface, Edge scrollFrom, Edge scrollTo, int restDuration, Font font) {
        super(surface);
        defaultScrollFrom = scrollFrom;
        defaultScrollTo = scrollTo;
        defaultRestH = AlignH.CENTRE;
        defaultRestV = AlignV.MIDDLE;
        defaultRestDuration = restDuration;
        setDefaults();
        this.font = font;
        addScrollCompleteHandler(() -> {
            if (override) {
                TextZone.this.clearOverride();
            }
            advanceMessage();
        });
    }

    private void setDefaults() {
        scroll(defaultScrollFrom, defaultScrollTo);
        setRestPosition(defaultRestH, defaultRestV);
        setRestDuration(defaultRestDuration);
    }

    public void advanceMessage() {
        messageQueue.advanceMessage();
    }


    /////////////
    // Factory //
    /////////////

    public static TextZone fixed(LightBoardSurface s) {
        return new TextZone(s, Edge.NO_SCROLL, Edge.NO_SCROLL, 3000, new SimpleFont());
    }

    public static TextZone scrollLeft(LightBoardSurface s) {
        return new TextZone(s, Edge.RIGHT, Edge.LEFT, 3000, new SimpleFont());
    }

    public static TextZone scrollRight(LightBoardSurface s) {
        return new TextZone(s, Edge.LEFT, Edge.RIGHT, 3000, new SimpleFont());
    }

    public static TextZone scrollUp(LightBoardSurface s) {
        return new TextZone(s, Edge.BOTTOM, Edge.TOP, 3000, new SimpleFont());
    }

    public static TextZone scrollDown(LightBoardSurface s) {
        return new TextZone(s, Edge.TOP, Edge.BOTTOM, 3000, new SimpleFont());
    }


    /////////////////////////
    // Zone Implementation //
    /////////////////////////

    private Pattern pattern;
    private MessageWrapper lastMessage;

    @Override
    public boolean render() {
//        if ( !paused ) {
            if ( singleRender && clear ) {
                clear();
            }
            MessageWrapper message = getCurrentMessage();
            if ( !message.equals(lastMessage) ) {
                pattern = font.renderString(message.getMessage(), message.setAlignH());
            }
            return drawPattern(0, 0, pattern, true);
//        }
//        return true;
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
