package lightboard.board.zone.impl;

import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.LightBoardZone;

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
            for (LightBoardZone zone : zones) {
                zone.tick();
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
            zones.forEach(LightBoardZone::resetScroll);
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
