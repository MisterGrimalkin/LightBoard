package net.amarantha.lightboard.zone;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.AlignH;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.surface.Region;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.transition.AbstractTransition;

public abstract class AbstractZone {

    @Inject private Sync sync;
    @Inject private LightBoardSurface surface;

    private Pattern pattern;

    private long displayTime = 1000;

    public abstract Pattern getNextPattern();

    private long tick = 25;

    private Region region;

    public Region getRegion() {
        return region;
    }

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

    public void clear() {
        surface.clearRegion(region);
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

    public void setDisplayTime(long displayTime) {
        this.displayTime = displayTime;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void start() {
        paused = false;
    }

    public void stop() {
        paused = true;
        surface.clearRegion(region);
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
    }

    private boolean autoOut = true;

    public void setAutoOut(boolean autoOut) {
        this.autoOut = autoOut;
    }

    private boolean autoNext = true;

    public void setAutoNext(boolean autoNext) {
        this.autoNext = autoNext;
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
            surface.clearRegion(region);
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
            surface.drawPattern(getRestX(), getRestY(), pattern);
            out();
        } else {
            startTime = System.currentTimeMillis();
            direction = Transitioning.NONE;
            surface.drawPattern(getRestX(), getRestY(), pattern, region);
        }
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
        surface.clearRegion(region);
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

    public void setInTransition(AbstractTransition inTransition) {
        this.inTransition = inTransition;
    }

    public void setOutTransition(AbstractTransition outTransition) {
        this.outTransition = outTransition;
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

    public void setAlignV(AlignV alignV) {
        this.alignV = alignV;
    }

    public void setAlignH(AlignH alignH) {
        this.alignH = alignH;
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
