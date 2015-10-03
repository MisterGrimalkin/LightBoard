package net.amarantha.lightboard.zone.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.LightBoardZone;

public class NoiseZone extends LightBoardZone {

    private double threshold = 0.5;
    private double minThreshold = 0.4;
    private double maxThreshold = 0.8;
    private double thresholdDelta = 0.1;

    private boolean ripple = true;
    private boolean pulse = false;

    @Inject
    public NoiseZone(LightBoardSurface surface, Sync sync) {
        super(surface, sync);
        setRestDuration(0);
    }

    public NoiseZone setup(double threshold, double minThreshold, double maxThreshold, double thresholdDelta, boolean ripple, boolean pulse) {
        this.threshold = threshold;
        this.minThreshold = minThreshold;
        this.maxThreshold = maxThreshold;
        this.thresholdDelta = thresholdDelta;
        this.ripple = ripple;
        this.pulse = pulse;
        return this;
    }

    public NoiseZone ripplingFlag() {
        return setup(0.5, 0.2, 0.8, 0.05, true, false);
    }

    @Override
    public int getContentWidth() {
        return getRegion().width;
    }

    @Override
    public int getContentHeight() {
        return getRegion().height;
    }


    @Override
    public boolean render() {
        for ( int x=0; x<getRegion().width; x++ ) {
            for ( int y=0; y<getRegion().height; y++ ) {
                if ( Math.random() > threshold ) {
                    drawPoint(x,y);
                }
                if ( ripple ) {
                    if (threshold <= minThreshold || threshold >= maxThreshold) {
                        thresholdDelta = -thresholdDelta;
                    }
                    threshold += thresholdDelta;
                }
            }
        }
        return true;
    }

    @Override
    public void onScrollComplete() {
        if ( pulse ) {
            if (threshold <= minThreshold || threshold >= maxThreshold) {
                thresholdDelta = -thresholdDelta;
            }
            threshold += thresholdDelta;
        }
    }
}
