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

    @Override
    public void tick() {
        super.tick();
        for ( LightBoardZone zone : zones ) {
            zone.tick();
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
            for (LightBoardZone zone : zones) {
                zone.resetScroll();
            }
        }
        return drawn;
    }

    @Override
    public LightBoardZone region(int regionLeft, int regionTop, int regionWidth, int regionHeight) {
        super.region(regionLeft, regionTop, regionWidth, regionHeight);
        for ( LightBoardZone zone : zones ) {
            zone.region(region);
        }
        return this;
    }
}
