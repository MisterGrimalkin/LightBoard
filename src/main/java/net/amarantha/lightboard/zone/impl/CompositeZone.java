package net.amarantha.lightboard.zone.impl;

import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.zone.LightBoardZone;

import java.util.ArrayList;
import java.util.List;

public class CompositeZone extends LightBoardZone {

    private List<LightBoardZone> zones = new ArrayList<>();

    private int width = 0;
    private int height = 0;

    public CompositeZone(LightBoardSurface surface, LightBoardZone... lightBoardZones) {
        super(surface);
        for ( LightBoardZone z : lightBoardZones) {
            zones.add(z);
            width = Math.max(width, z.getRegion().width);
            height = Math.max(height, z.getRegion().height);
            z.autoRender(false);
            z.autoReset(false);
            z.resetScroll();
        }
        autoReset(false);
    }

    private boolean paused;

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void tick() {
        if ( !paused ) {
            super.tick();
//            render();
            for (LightBoardZone zone : zones) {
                zone.tick();
                zone.render();
            }
        }
    }

    @Override
    public int getContentWidth() {
        return width;
    }

    @Override
    public int getContentHeight() {
        return height;
    }

    @Override
    public boolean render() {
        boolean drawn = false;
        for ( LightBoardZone zone : zones ) {
            drawn |= zone.render();
        }
        if ( !drawn ) {
            for ( LightBoardZone lightBoardZone : zones ) {
                lightBoardZone.resetScroll();
            }
        }
        return drawn;
    }

    @Override
    public LightBoardZone setRegion(int regionLeft, int regionTop, int regionWidth, int regionHeight) {
        super.setRegion(regionLeft, regionTop, regionWidth, regionHeight);
        for ( LightBoardZone zone : zones ) {
            zone.region(region);
        }
        return this;
    }
}
