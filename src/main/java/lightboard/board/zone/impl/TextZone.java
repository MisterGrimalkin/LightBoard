package lightboard.board.zone.impl;

import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.LBZone;
import lightboard.font.Font;
import lightboard.font.SimpleFont;
import lightboard.util.MessageQueue;
import lightboard.util.MessageQueue.Edge;
import lightboard.util.MessageQueue.MessageWrapper;

public class TextZone extends LBZone {

    private Font font;

    public TextZone(LightBoardSurface surface, Edge scrollFrom, Edge scrollTo, int restDuration, Font font) {
        super(surface);
        scroll(scrollFrom, scrollTo);
        restDuration(restDuration);
        this.font = font;
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

    public static TextZone scrollUp(LightBoardSurface s) {
        return new TextZone(s, Edge.BOTTOM_EDGE, Edge.TOP_EDGE, 3000, new SimpleFont());
    }


    /////////////////////////
    // Zone Implementation //
    /////////////////////////

    @Override
    public boolean render() {
        MessageWrapper message = getCurrentMessage();
        boolean drawn = drawPattern(0, 0, font.renderString(message.getMessage(), message.gethPosition()));
        return drawn;
    }

    @Override
    public void onScrollComplete() {
        if ( override ) {
            clearOverride();
        }
        messageQueue.advanceMessage();
//        MessageWrapper message = messageQueue.getCurrentMessage();
//        scroll(message.getScrollFrom(), message.getScrollTo());
//        restPosition(message.gethPosition(), message.getvPosition());
//        restDuration(message.getRestDuration());
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


    //////////////
    // Override //
    //////////////

    private boolean override = false;
    private MessageWrapper overrideMessage = null;

    public void overrideMessage(MessageWrapper message) {
        overrideMessage = message;
        resetScroll();
        override = true;
    }

    public void clearOverride() {
        overrideMessage = null;
        override = false;
    }

}
