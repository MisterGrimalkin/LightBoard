package net.amarantha.lightboard.zone;

import net.amarantha.lightboard.entity.AlignH;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.surface.Region;
import net.amarantha.lightboard.utility.Sync;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;

public abstract class LightBoardZone {

    protected final LightBoardSurface surface;

    protected LightBoardZone(LightBoardSurface surface) {
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

    public LightBoardZone setScrollTick(int scrollTick) {
        return setScrollTick((long)scrollTick);
    }

    public LightBoardZone setScrollTick(Long scrollTick) {
        this.scrollTick = scrollTick;
        return this;
    }

    public LightBoardZone start() {
        if ( !singleRender ) {
            Sync.addTask(new Sync.Task(scrollTick) {
                @Override
                public void runTask() {
                    tick();
                }
            });
        }
        onScrollComplete();
        resetScroll();
        return this;
    }

    protected boolean paused = true;
    protected boolean singleRender = false;
    protected boolean rendered = false;

    public void pause() {
        paused = true;
    }

    public void resume() {
        rendered = false;
        paused = false;
        tick();
    }

    public void tick() {
//        if ( !paused ) {
            if (resting) {
                if (currentTimeMillis() - lastTick > restDuration) {
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
//        }
    }

    protected Long scrollTick = null;
    private long lastTick;
    protected boolean resting;


    ///////////////
    // Scrolling //
    ///////////////

    private enum Scrolling { IN, OUT }

    protected int contentLeft = 0;
    protected int contentTop = 0;

    private int restX = 0;
    private int restY = 0;

    private int masterDelta = 1;

    private int deltaX = 0;
    private int deltaY = 0;

    private Scrolling currentScrollMode;

    private void initScroll(Scrolling scrolling) {
        currentScrollMode = scrolling;
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
            case TOP:
                contentLeft = restX;
                contentTop = -getContentHeight();
                deltaX = 0;
                deltaY = masterDelta;
                break;
            case LEFT:
                contentLeft = -getContentWidth();
                contentTop = restY;
                deltaX = masterDelta;
                deltaY = 0;
                break;
            case BOTTOM:
                contentLeft = restX;
                contentTop = region.height;
                deltaX = 0;
                deltaY = -masterDelta;
                break;
            case RIGHT:
                contentLeft = region.width;
                contentTop = restY;
                deltaX = -masterDelta;
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
            case TOP:
                deltaX = 0;
                deltaY = -masterDelta;
                break;
            case LEFT:
                deltaX = -masterDelta;
                deltaY = 0;
                break;
            case BOTTOM:
                deltaX = 0;
                deltaY = masterDelta;
                break;
            case RIGHT:
                deltaX = masterDelta;
                deltaY = 0;
                break;
            case NO_SCROLL:
                    onScrollComplete();
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
        if ( !contentVisible() ) { //&& autoReset ) {
                onScrollComplete();
            if ( autoReset ) {
                resetScroll();
            }
        }

    }

    public void resetScroll() {
        initScroll(Scrolling.IN);
        resting = false;
    }

    public void onScrollComplete() {
        scrollCompleteHandlers.forEach(ScrollCompleteHandler::onScrollComplete);
    }

    private boolean contentVisible() {
        return contentLeft < region.width
                && contentLeft+getContentWidth() >= 0
                && contentTop < region.height
                && contentTop+getContentHeight() >= 0;
    }

    private boolean isInRestPosition() {
        return
                ( getContentWidth()<=region.width
                        ||
                        ( getScrollFrom()==Edge.BOTTOM
                            && getScrollTo()==Edge.TOP
                        )
                )
                && getContentHeight()<=region.height
                && Math.abs(contentLeft - restX) < masterDelta
                && Math.abs(contentTop-restY) < masterDelta
                && currentScrollMode.equals(Scrolling.IN);
    }

    protected void doRender() {

        if ( clear ) {
            surface.clearRegion(region);
        }

        if ( !render() && autoReset ) {
            onScrollComplete();
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

    public boolean clear() {
        return surface.clearRegion(region);
    }

    protected boolean drawPattern(int x, int y, Pattern pattern) {
        return surface.drawPattern(region.left+contentLeft+x, region.top+contentTop+y, pattern, region);
    }

    protected boolean drawPattern(int x, int y, Pattern pattern, boolean clearBackground) {
        return surface.drawPattern(region.left+contentLeft+x, region.top+contentTop+y, pattern, clearBackground, region);
    }


    /////////////
    // Options //
    /////////////

    public LightBoardZone region(Region region) {
        this.region = region;
        return this;
    }

    public LightBoardZone masterDelta(int d) {
        masterDelta = d;
        return this;
    }

    public LightBoardZone setRegion(int regionLeft, int regionTop, int regionWidth, int regionHeight) {
        region = surface.safeRegion(regionLeft, regionTop, regionWidth, regionHeight);
        return this;
    }

    public LightBoardZone autoRender(boolean enableTickRender) {
        this.autoRender = enableTickRender;
        return this;
    }

    public LightBoardZone autoReset(boolean autoReset) {
        this.autoReset = autoReset;
        return this;
    }

    public LightBoardZone clear(boolean clear) {
        this.clear = clear;
        return this;
    }

    public LightBoardZone outline(boolean outline) {
        this.outline = outline;
        return this;
    }

    public LightBoardZone invert(boolean invert) {
        this.invert = invert;
        return this;
    }

    public LightBoardZone scroll(Edge from, Edge to) {
        scrollFrom = from;
        scrollTo = to;
        return this;
    }

    public LightBoardZone setRestPosition(AlignH x, AlignV y) {
        restPositionH = x;
        restPositionV = y;
        return this;
    }

    public LightBoardZone setRestDuration(int pause) {
        restDuration = pause;
        return this;
    }

    public LightBoardZone singleRender(boolean singleRender) {
        this.singleRender = singleRender;
        return this;
    }

    private boolean autoRender = true;
    private boolean autoReset = true;

    protected boolean clear = true;
    private boolean outline = false;
    private boolean invert = false;

    protected Region region;

    private Edge scrollFrom = Edge.NO_SCROLL;
    private Edge scrollTo = Edge.NO_SCROLL;
    private AlignH restPositionH = AlignH.CENTRE;
    private AlignV restPositionV = AlignV.MIDDLE;
    private int restDuration = 3000;

    private List<ScrollCompleteHandler> scrollCompleteHandlers = new ArrayList<>();

    public static interface  ScrollCompleteHandler {
        void onScrollComplete();
    }

    public void addScrollCompleteHandler(ScrollCompleteHandler handler) {
        scrollCompleteHandlers.add(handler);
    }

    public void removeAllHandlers() {
        scrollCompleteHandlers.clear();
    }


    /////////////
    // Getters //
    /////////////

    public Edge getScrollFrom() {
        return scrollFrom;
    }

    public Edge getScrollTo() {
        return scrollTo;
    }

    public AlignH getRestPositionH() {
        return restPositionH;
    }

    public AlignV getRestPositionV() {
        return restPositionV;
    }

    public int getRestDuration() {
        return restDuration;
    }

    public Region getRegion() {
        return region;
    }

}
