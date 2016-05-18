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

    /**
     * Override to obtain the next pattern to display
     */
    protected abstract Pattern getNextPattern();


    /////////////////
    // Init & Tick //
    /////////////////

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

    public void pause() {
        paused = true;
        surface.clearRegion(canvasLayer, region);
    }

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


    private void doTick() {
        if ( pattern==null ) {
            pattern = getNextPattern();
            if ( pattern!=null ) {
                in();
            }
        } else {
            if (startTime != null && System.currentTimeMillis() - startTime > displayTime) {
                startTime = null;
                if ( onDisplayComplete !=null ) {
                    onDisplayComplete.execute();
                }
                if (autoOut) {
                    out();
                }
            }
        }
    }


    /////////////
    // Drawing //
    /////////////

    public void drawPoint(int x, int y, Colour colour) {
        surface.drawPoint(canvasLayer, x+region.left+xOffset, y+region.top+yOffset, colour, region);
    }

    public void drawPattern(int x, int y, Pattern pattern) {
        surface.drawPattern(canvasLayer, x+region.left+xOffset, y+region.top+yOffset, pattern, region);
    }

    public void clear() {
        surface.clearRegion(canvasLayer, region);
    }

    public AbstractZone setOutline(Colour outline) {
        this.outline = outline;
        return this;
    }


    ///////////////
    // Callbacks //
    ///////////////

    private ZoneCallback onInAt;
    private double onInAtProgress;
    private ZoneCallback onInComplete;
    private ZoneCallback onDisplayComplete;
    private ZoneCallback onOutAt;
    private double onOutAtProgress;
    private ZoneCallback onOutComplete;

    public void onInAt(double progress, ZoneCallback callback) {
        this.onInAtProgress = progress;
        this.onInAt = callback;
    }

    public void onInComplete(ZoneCallback callback) {
        this.onInComplete = callback;
    }

    public void onDisplayComplete(ZoneCallback callback) {
        this.onDisplayComplete = callback;
    }

    public void onOutAt(double progress, ZoneCallback callback) {
        this.onOutAtProgress = progress;
        this.onOutAt = callback;
    }

    public void onOutComplete(ZoneCallback callback) {
        this.onOutComplete = callback;
    }

    public interface ZoneCallback {
        void execute();
    }


    ////////////////
    // Transition //
    ////////////////

    public void in() {
        if ( paused ) {
            paused = false;
        } else {
            surface.clearRegion(canvasLayer, region);
            if (inTransition != null) {
                inTransition.transition(this, this::display, onInAt, onInAtProgress);
                direction = Transitioning.IN;
            } else {
                display();
            }
        }
    }

    private void display() {
        if ( onInComplete!=null ) {
            onInComplete.execute();
        }
        if ( displayTime==0 ) {
            drawPattern(getRestX(), getRestY(), pattern);
            out();
        } else {
            startTime = System.currentTimeMillis();
            direction = Transitioning.NONE;
            drawPattern(getRestX(), getRestY(), pattern);
        }
    }

    public void out() {
        if ( outTransition!=null ) {
            outTransition.transition(this, this::end, onOutAt, onOutAtProgress);
            direction = Transitioning.OUT;
        } else {
            end();
        }
    }

    private void end() {
        surface.clearRegion(canvasLayer, region);
        direction = Transitioning.NONE;
        if ( onOutComplete !=null ) {
            onOutComplete.execute();
        }
        pattern = getNextPattern();
        if ( autoNext ) {
            if (pattern != null) {
                in();
            }
        }
    }

    public AbstractZone setInTransition(AbstractTransition inTransition) {
        this.inTransition = inTransition;
        return this;
    }

    public AbstractZone setOutTransition(AbstractTransition outTransition) {
        this.outTransition = outTransition;
        return this;
    }

    private enum Transitioning {
        IN, NONE, OUT
    }

    public AbstractZone setDisplayTime(long displayTime) {
        this.displayTime = displayTime;
        return this;
    }

    public AbstractZone setAutoOut(boolean autoOut) {
        this.autoOut = autoOut;
        return this;
    }

    public AbstractZone setAutoNext(boolean autoNext) {
        this.autoNext = autoNext;
        return this;
    }


    //////////////////////////
    // Position & Alignment //
    //////////////////////////

    public AbstractZone setRegion(Region region) {
        this.region = region;
        return this;
    }

    public AbstractZone setRegion(int left, int top, int width, int height) {
        return setRegion(surface.safeRegion(left, top, width, height));
    }

    public AbstractZone setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        return this;
    }

    public AbstractZone setCanvasLayer(int canvasLayer) {
        this.canvasLayer = canvasLayer;
        return this;
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

    public AlignH getAlignH() {
        return alignH;
    }

    public AlignV getAlignV() {
        return alignV;
    }

    public long getTick() {
        return tick;
    }

    public Pattern getPattern() {
        return pattern;
    }


    ////////////
    // Fields //
    ////////////

    private Pattern pattern;
    private Region region;
    private AlignH alignH = AlignH.CENTRE;
    private AlignV alignV = AlignV.MIDDLE;
    private AbstractTransition inTransition;
    private AbstractTransition outTransition;
    private Transitioning direction = Transitioning.NONE;
    private int canvasLayer = 0;
    private Long startTime;
    private long displayTime = 1000;
    private long tick = 25;
    private boolean initialised = false;
    private boolean autoOut = true;
    private boolean autoNext = true;
    private boolean paused;
    private int xOffset = 0;
    private int yOffset = 0;
    private Colour outline = null;

}
