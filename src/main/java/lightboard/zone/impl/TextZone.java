package lightboard.zone.impl;

import lightboard.font.Font;
import lightboard.font.SimpleFont;
import lightboard.surface.LightBoardSurface;
import lightboard.util.MessageQueue;
import lightboard.util.MessageQueue.Edge;
import lightboard.util.MessageQueue.MessageWrapper;
import lightboard.zone.LightBoardZone;

public class TextZone extends LightBoardZone {

    private Font font;

    private Edge defaultScrollFrom;
    private Edge defaultScrollTo;
    private MessageQueue.HPosition defaultRestH;
    private MessageQueue.VPosition defaultRestV;
    private int defaultRestDuration;

    public TextZone(LightBoardSurface surface, Edge scrollFrom, Edge scrollTo, int restDuration) {
            this(surface, scrollFrom, scrollTo, restDuration, new SimpleFont());
    }

    public TextZone(LightBoardSurface surface, Edge scrollFrom, Edge scrollTo, int restDuration, Font font) {
        super(surface);
        defaultScrollFrom = scrollFrom;
        defaultScrollTo = scrollTo;
        defaultRestH = MessageQueue.HPosition.CENTRE;
        defaultRestV = MessageQueue.VPosition.MIDDLE;
        defaultRestDuration = restDuration;
        setDefaults();
        this.font = font;
        addScrollCompleteHandler(new ScrollCompleteHandler() {
            @Override
            public void onScrollComplete() {
                if (override) {
                    TextZone.this.clearOverride();
                }
                messageQueue.advanceMessage();
            }
        });
    }

    private void setDefaults() {
        scroll(defaultScrollFrom, defaultScrollTo);
        setRestPosition(defaultRestH, defaultRestV);
        setRestDuration(defaultRestDuration);
    }


    /////////////
    // Factory //
    /////////////

    public static TextZone fixed(LightBoardSurface s) {
        return new TextZone(s, Edge.NO_SCROLL, Edge.NO_SCROLL, 3000, new SimpleFont());
    }

    public static TextZone scrollLeft(LightBoardSurface s) {
        return new TextZone(s, Edge.RIGHT_EDGE, Edge.LEFT_EDGE, 3000, new SimpleFont());
    }

    public static TextZone scrollRight(LightBoardSurface s) {
        return new TextZone(s, Edge.LEFT_EDGE, Edge.RIGHT_EDGE, 3000, new SimpleFont());
    }

    public static TextZone scrollUp(LightBoardSurface s) {
        return new TextZone(s, Edge.BOTTOM_EDGE, Edge.TOP_EDGE, 3000, new SimpleFont());
    }

    public static TextZone scrollDown(LightBoardSurface s) {
        return new TextZone(s, Edge.TOP_EDGE, Edge.BOTTOM_EDGE, 3000, new SimpleFont());
    }


    /////////////////////////
    // Zone Implementation //
    /////////////////////////

    @Override
    public boolean render() {
        MessageWrapper message = getCurrentMessage();
        if ( isPoly() ) {
            return drawPattern(0, 0, font.renderString(message.getMessage(), message.gethPosition()).getColourValues(), true);
        } else {
            return drawPattern(0, 0, font.renderString(message.getMessage(), message.gethPosition()).getBinaryValues(), true);
        }
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

    public MessageWrapper getCurrentMessage() {
        return overrideMessage==null?messageQueue.getCurrentMessage():overrideMessage;
    }

    public TextZone clearMessages(int id) {
        messageQueue.clearMessages(id);
        return this;
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

    public void overrideMessage(MessageWrapper message) {
        overrideMessage = message;
        resting = false;
        scroll(message.getScrollFrom(), message.getScrollTo());
        setRestPosition(message.gethPosition(), message.getvPosition());
        setRestDuration(message.getRestDuration());
        resetScroll();
        override = true;
    }

    public void clearOverride() {
        overrideMessage = null;
        override = false;
        setDefaults();
    }

}
