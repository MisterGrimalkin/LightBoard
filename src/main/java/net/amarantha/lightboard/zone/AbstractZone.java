package net.amarantha.lightboard.zone;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.AlignH;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.entity.Colour;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.surface.Region;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.transition.AbstractTransition;

public abstract class AbstractZone {

    @Inject private Sync sync;
    @Inject private LightBoardSurface surface;

    private Pattern pattern;
    private int canvasLayer;
    private long displayTime = 1000;
    private long tick = 25;
    private Region region;

    public abstract Pattern getNextPattern();


    /////////////
    // Getters //
    /////////////

    public int getLeft() {
        return region.left;
    }

    public int getRight() {
        return region.right;
    }

    public int getTop() {
        return region.top;
    }

    public int getBottom() {
        return region.bottom;
    }

    public int getWidth() {
        return region.width;
    }

    public int getHeight() {
        return region.height;
    }

    private int xOffset = 0;
    private int yOffset = 0;

    public AbstractZone setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        return this;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public void clear() {
        surface.clearRegion(canvasLayer, region);
    }

    private boolean initialised = false;

    public void init() {
        initialised = true;
        paused = true;
        sync.addTask(new Sync.Task(tick) {
            @Override
            public void runTask() {
                if ( !paused ) {
                    tick();
                }
            }
        });
    }

    public void setTick(long tick) {
        if ( initialised ) {
            throw new IllegalStateException("Must call setTick() before init()");
        }
        this.tick = tick;
    }

    public long getTick() {
        return tick;
    }

    public AbstractZone setDisplayTime(long displayTime) {
        this.displayTime = displayTime;
        return this;
    }

    public AbstractZone setRegion(int left, int top, int width, int height) {
        return setRegion(surface.safeRegion(left, top, width, height));
    }

    public AbstractZone setRegion(Region region) {
        this.region = region;
        return this;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void start() {
        paused = false;
    }

    public void stop() {
        paused = true;
        surface.clearRegion(canvasLayer, region);
    }

    private boolean paused;

    public void tick() {
        switch ( direction ) {
            case IN:
                inTransition.tick();
                break;
            case NONE:
                doTick();
                break;
            case OUT:
                outTransition.tick();
                break;
        }
        if ( outline!=null ) {
            surface.outlineRegion(canvasLayer, outline, region);
        }
    }

    private boolean autoOut = true;

    public AbstractZone setAutoOut(boolean autoOut) {
        this.autoOut = autoOut;
        return this;
    }

    private boolean autoNext = true;

    public AbstractZone setAutoNext(boolean autoNext) {
        this.autoNext = autoNext;
        return this;
    }

    private void doTick() {
        if ( pattern==null ) {
            pattern = getNextPattern();
            if ( pattern!=null ) {
                in();
            }
        } else {
            if (startTime != null && System.currentTimeMillis() - startTime > displayTime) {
                startTime = null;
                if ( displayCompleteCallback!=null ) {
                    displayCompleteCallback.onDisplayComplete();
                }
                if (autoOut) {
                    out();
                }
            }
        }
    }

    private DisplayCompleteCallback displayCompleteCallback;

    public void onDisplayComplete(DisplayCompleteCallback callback) {
        this.displayCompleteCallback = callback;
    }

    public interface DisplayCompleteCallback {
        void onDisplayComplete();
    }

    public interface OutCompleteCallback {
        void onOutComplete();
    }

    private OutCompleteCallback outCompleteCallback;

    public void onOutComplete(OutCompleteCallback callback) {
        this.outCompleteCallback = callback;
    }



    ////////////////
    // Transition //
    ////////////////

    private Transitioning direction = Transitioning.NONE;
    private AbstractTransition inTransition;
    private AbstractTransition outTransition;

    public void in() {
        if ( paused ) {
            start();
        } else {
            surface.clearRegion(canvasLayer, region);
            if (inTransition != null) {
                inTransition.transition(this, this::display);
                direction = Transitioning.IN;
            } else {
                display();
            }
        }
    }

    private Long startTime;

    private void display() {
        if ( displayTime==0 ) {
            drawPattern(getRestX(), getRestY(), pattern);
            out();
        } else {
            startTime = System.currentTimeMillis();
            direction = Transitioning.NONE;
            drawPattern(getRestX(), getRestY(), pattern);
        }
    }

    private Colour outline = null;

    public AbstractZone setOutline(Colour outline) {
        this.outline = outline;
        return this;
    }

    public void drawPoint(int x, int y, Colour colour) {
        surface.drawPoint(canvasLayer, x+region.left+xOffset, y+region.top+yOffset, colour, region);
    }

    public void drawPattern(int x, int y, Pattern pattern) {
        surface.drawPattern(canvasLayer, x+region.left+xOffset, y+region.top+yOffset, pattern, region);
    }

    public void out() {
        if ( outTransition!=null ) {
            outTransition.transition(this, this::end);
            direction = Transitioning.OUT;
        } else {
            end();
        }
    }


    private void end() {
        surface.clearRegion(canvasLayer, region);
        direction = Transitioning.NONE;
        if ( outCompleteCallback!=null ) {
            outCompleteCallback.onOutComplete();
        }
        pattern = getNextPattern();
        if ( autoNext ) {
            if (pattern != null) {
                in();
            }
        }
    }

    private enum Transitioning {
        IN, NONE, OUT
    }

    public AbstractZone setInTransition(AbstractTransition inTransition) {
        this.inTransition = inTransition;
        return this;
    }

    public AbstractZone setOutTransition(AbstractTransition outTransition) {
        this.outTransition = outTransition;
        return this;
    }

    public AbstractZone setCanvasLayer(int canvasLayer) {
        this.canvasLayer = canvasLayer;
        return this;
    }


    ///////////////
    // Alignment //
    ///////////////

    private AlignH alignH = AlignH.CENTRE;
    private AlignV alignV = AlignV.MIDDLE;

    public AlignH getAlignH() {
        return alignH;
    }

    public AlignV getAlignV() {
        return alignV;
    }

    public AbstractZone setAlignV(AlignV alignV) {
        this.alignV = alignV;
        return this;
    }

    public AbstractZone setAlignH(AlignH alignH) {
        this.alignH = alignH;
        return this;
    }

    public int getRestX() {
        switch ( alignH ) {
            case LEFT:
                return 0;
            case CENTRE:
                return (region.width - pattern.getWidth()) / 2;
            case RIGHT:
                return region.width - pattern.getWidth();
        }
        return 0;
    }

    public int getRestY() {
        switch ( alignV ) {
            case TOP:
                return 0;
            case MIDDLE:
                return (region.height - pattern.getHeight()) / 2 ;
            case BOTTOM:
                return region.height - pattern.getHeight();
        }
        return 0;
    }

}
