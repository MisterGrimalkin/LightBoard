package net.amarantha.lightboard.zone.transition;

import com.google.inject.Inject;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.zone.AbstractZone;

public abstract class AbstractTransition {

    @Inject protected LightBoardSurface surface;

    public abstract void reset();

    public abstract void tick();

    private long duration;

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public long getSteps() {
        return duration / zone.getTick();
    }

    protected AbstractZone zone;

    public void transition(AbstractZone zone, TransitionCallback callback) {
        this.zone = zone;
        this.callback = callback;
        reset();
    }

    private TransitionCallback callback;

    public interface TransitionCallback {
        void onTransitionComplete();
    }

    protected void complete() {
        if ( callback!=null ) {
            callback.onTransitionComplete();
        }
    }

}
