package net.amarantha.lightboard.zone.transition;

import net.amarantha.lightboard.zone.AbstractZone;

public abstract class AbstractTransition {

    /**
     * Called when transition begins
     */
    public abstract void reset();

    /**
     * Called repeatedly by Sync when transition is active
     */
    public abstract void tick();

    /**
     * Activate transition
     * @param zone Zone
     * @param callback To execute when transition is complete
     */
    public void transition(AbstractZone zone, TransitionCallback callback) {
        this.zone = zone;
        this.callback = callback;
        reset();
    }

    protected AbstractZone zone;
    private TransitionCallback callback;


    /**
     * Pass control back to the Zone
     */
    protected void complete() {
        if ( callback!=null ) {
            callback.onTransitionComplete();
        }
    }

    /**
     * Set approximate duration of transition. Will not be clock accurate.
     * @param duration Value in milliseconds
     * @return For method chaining
     */
    public AbstractTransition setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    private long duration;

    /**
     * Functional interface for transition completion
     */
    public interface TransitionCallback {
        void onTransitionComplete();
    }

}
