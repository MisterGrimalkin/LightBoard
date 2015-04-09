package lightboard.board.zone;

import lightboard.board.surface.LightBoardSurface;
import lightboard.board.surface.LightBoardSurface.Region;
import lightboard.util.MessageQueue.Edge;
import lightboard.util.MessageQueue.HPosition;
import lightboard.util.MessageQueue.VPosition;

import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.currentTimeMillis;
import static lightboard.util.MessageQueue.Edge.NO_SCROLL;
import static lightboard.util.MessageQueue.HPosition.CENTRE;
import static lightboard.util.MessageQueue.VPosition.MIDDLE;

public abstract class LBZone {

    protected final LightBoardSurface surface;

    protected LBZone(LightBoardSurface surface) {
        this.surface = surface;
        region = surface.safeRegion(0, 0, surface.getCols(), surface.getRows());
    }


    //////////////////////
    // Abstract Methods //
    //////////////////////

    public abstract boolean render();


    //////////
    // Tick //
    //////////

    private Timer timer;

    public LBZone start() {
        return start(DEFAULT_TICK);
    }

    public LBZone start(int scrollTick) {
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override public void run() {
                tick();
            }
        }, 10, scrollTick);
        resetScroll();
        return this;
    }

    public void tick() {
        if (resting) {
            if ( currentTimeMillis()-lastTick > restDuration) {
                resting = false;
                initScroll(Scrolling.OUT);
            }
        } else {
            updateScroll();
            if (autoRender) {
                doRender();
            }
            lastTick = currentTimeMillis();
        }
    }

    public void stop() {
        if ( timer!=null ) {
            timer.cancel();
        }
        surface.clearSurface();
    }

    private long lastTick;
    private boolean resting;

    private final static int DEFAULT_TICK = 40;


    ///////////////
    // Scrolling //
    ///////////////

    private enum Scrolling { IN, OUT }

    protected int contentLeft = 0;
    protected int contentTop = 0;

    private int restX = 0;
    private int restY = 0;

    private int deltaX = 0;
    private int deltaY = 0;

    private void initScroll(Scrolling scrolling) {
        switch ( scrolling ) {
            case OUT:
                prepareScrollOut();
                break;
            case IN:
                switch (restPositionH) {
                    case LEFT:
                        restX = 0;
                        break;
                    case CENTRE:
                        restX = (region.width - getContentWidth()) / 2;
                        break;
                    case RIGHT:
                        restX = region.width - getContentWidth();
                        break;
                }
                switch (restPositionV) {
                    case TOP:
                        restY = 0;
                        break;
                    case MIDDLE:
                        restY = (region.height - getContentHeight()) / 2;
                        break;
                    case BOTTOM:
                        restY = region.height - getContentHeight();
                        break;
                }
                prepareScrollIn();
                break;
        }
    }

    private void prepareScrollIn() {
        switch (scrollFrom) {
            case TOP_EDGE:
                contentLeft = restX;
                contentTop = -getContentHeight();
                deltaX = 0;
                deltaY = 1;
                break;
            case LEFT_EDGE:
                contentLeft = -getContentWidth();
                contentTop = restY;
                deltaX = 1;
                deltaY = 0;
                break;
            case BOTTOM_EDGE:
                contentLeft = restX;
                contentTop = region.height;
                deltaX = 0;
                deltaY = -1;
                break;
            case RIGHT_EDGE:
                contentLeft = region.width;
                contentTop = restY;
                deltaX = -1;
                deltaY = 0;
                break;
            case NO_SCROLL:
                contentLeft = restX;
                contentTop = restY;
                deltaX = 0;
                deltaY = 0;
                break;
        }
    }

    private void prepareScrollOut() {
        switch (scrollTo) {
            case TOP_EDGE:
                deltaX = 0;
                deltaY = -1;
                break;
            case LEFT_EDGE:
                deltaX = -1;
                deltaY = 0;
                break;
            case BOTTOM_EDGE:
                deltaX = 0;
                deltaY = 1;
                break;
            case RIGHT_EDGE:
                deltaX = 1;
                deltaY = 0;
                break;
            case NO_SCROLL:
                if ( autoReset ) {
                    resetScroll();
                }
                break;
        }
    }

    private void updateScroll() {
        contentLeft += deltaX;
        contentTop += deltaY;
        if ( isInRestPosition() ) {
            resting = true;
        }
        if ( !contentVisible() && autoReset ) {
            resetScroll();
        }

    }

    public void resetScroll() {
        onScrollComplete();
        initScroll(Scrolling.IN);
    }

    public void onScrollComplete() {}

    private boolean contentVisible() {
        return contentLeft < region.width
                && contentLeft+getContentWidth() >= 0
                && contentTop < region.height
                && contentTop+getContentHeight() >= 0;
    }

    private boolean isInRestPosition() {
        return     getContentWidth()<=region.width
                && getContentHeight()<=region.height
                && contentLeft == restX
                && contentTop == restY;
    }

    protected void doRender() {

        if ( clear ) {
            surface.clearRegion(region);
        }

        if ( !render() && autoReset ) {
            resetScroll();
        }

        if ( outline ) {
            surface.outlineRegion(region);
        }

        if ( invert ) {
            surface.invertRegion(region);
        }

    }

    public int getContentWidth() {
        return region.width;
    }

    public int getContentHeight() {
        return region.height;
    }


    /////////////////////
    // Surface Drawing //
    /////////////////////

    protected boolean drawPoint(int x, int y) {
        return surface.drawPoint(region.left+contentLeft+x, region.top+contentTop+y, region);
    }

    protected boolean clearPoint(int x, int y) {
        return surface.clearPoint(region.left + contentLeft + x, region.top + contentTop + y, region);
    }

    protected boolean drawRect(int x, int y, int width, int height, boolean fill) {
        Region toDraw = surface.safeRegion(region.left+contentLeft+x, region.top+contentTop+y, width, height);
        if ( fill ) {
            return surface.fillRegion(toDraw);
        } else {
            return surface.outlineRegion(toDraw);
        }
    }

    protected boolean clearRect(int x, int y, int width, int height) {
        Region toDraw = surface.safeRegion(region.left+contentLeft+x, region.top+contentTop+y, width, height);
        return surface.clearRegion(toDraw);
    }

    protected boolean clear() {
        return surface.clearRegion(region);
    }

    protected boolean drawPattern(int x, int y, boolean[][] pattern) {
        return surface.drawPattern(region.left+contentLeft+x, region.top+contentTop+y, pattern, region);
    }

    protected boolean drawPattern(int x, int y, boolean[][] pattern, boolean clearBackground) {
        return surface.drawPattern(region.left+contentLeft+x, region.top+contentTop+y, pattern, clearBackground, region);
    }


    /////////////
    // Options //
    /////////////

    public LBZone region(Region region) {
        this.region = region;
        return this;
    }

    public LBZone region(int regionLeft, int regionTop, int regionWidth, int regionHeight) {
        region = surface.safeRegion(regionLeft, regionTop, regionWidth, regionHeight);
        return this;
    }

    public LBZone autoRender(boolean enableTickRender) {
        this.autoRender = enableTickRender;
        return this;
    }

    public LBZone autoReset(boolean autoReset) {
        this.autoReset = autoReset;
        return this;
    }

    public LBZone clear(boolean clear) {
        this.clear = clear;
        return this;
    }

    public LBZone outline(boolean outline) {
        this.outline = outline;
        return this;
    }

    public LBZone invert(boolean invert) {
        this.invert = invert;
        return this;
    }

    public LBZone scroll(Edge from, Edge to) {
        scrollFrom = from;
        scrollTo = to;
        return this;
    }

    public LBZone restPosition(HPosition x, VPosition y) {
        restPositionH = x;
        restPositionV = y;
        return this;
    }

    public LBZone restDuration(int pause) {
        restDuration = pause;
        return this;
    }

    private boolean autoRender = true;
    private boolean autoReset = true;

    private boolean clear = true;
    private boolean outline = false;
    private boolean invert = false;

    protected Region region;

    private Edge scrollFrom = NO_SCROLL;
    private Edge scrollTo = NO_SCROLL;
    private HPosition restPositionH = CENTRE;
    private VPosition restPositionV = MIDDLE;
    private int restDuration = 3000;


    /////////////
    // Getters //
    /////////////

    public Edge getScrollFrom() {
        return scrollFrom;
    }

    public Edge getScrollTo() {
        return scrollTo;
    }

    public HPosition getRestPositionH() {
        return restPositionH;
    }

    public VPosition getRestPositionV() {
        return restPositionV;
    }

    public int getRestDuration() {
        return restDuration;
    }

    public Region getRegion() {
        return region;
    }

}
