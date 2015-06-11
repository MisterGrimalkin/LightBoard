package net.amarantha.lightboard.zone.impl;

import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.zone.LightBoardZone;

public class NoiseZone extends LightBoardZone {

    public static NoiseZone ripplingFlag(LightBoardSurface s) {
        return new NoiseZone(s, 0.5, 0.2, 0.8, 0.05, true, false);
    }

    private double threshold = 0.5;
    private double minThreshold = 0.4;
    private double maxThreshold = 0.8;
    private double thresholdDelta = 0.1;

    private boolean ripple = true;
    private boolean pulse = false;

    public NoiseZone(LightBoardSurface surface, double threshold, double minThreshold, double maxThreshold, double thresholdDelta, boolean ripple, boolean pulse) {
        super(surface);
        this.threshold = threshold;
        this.minThreshold = minThreshold;
        this.maxThreshold = maxThreshold;
        this.thresholdDelta = thresholdDelta;
        this.ripple = ripple;
        this.pulse = pulse;
        setRestDuration(0);
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