package lightboard.zone.impl;

import lightboard.board.LightBoardSurface;
import lightboard.util.MessageQueue;
import lightboard.zone.LBZone;

import java.util.ArrayList;
import java.util.List;

public class CompositeZone extends LBZone {

    private List<LBZone> zones = new ArrayList<>();

    private int width = 0;
    private int height = 0;

    public CompositeZone(LightBoardSurface surface, LBZone... lbZones) {
        super(surface);
        for ( LBZone z : lbZones ) {
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
        for ( LBZone zone : zones ) {
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
        for ( LBZone zone : zones ) {
            drawn |= zone.render();
        }
        if ( !drawn ) {
            for (LBZone zone : zones) {
                zone.resetScroll();
            }
        }
        return drawn;
    }

    @Override
    public void onScrollComplete() {}

    @Override
    public LBZone region(int regionLeft, int regionTop, int regionWidth, int regionHeight) {
        super.region(regionLeft, regionTop, regionWidth, regionHeight);
        for ( LBZone zone : zones ) {
            zone.region(region);
        }
        return this;
    }
}
