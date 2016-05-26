package net.amarantha.lightboard.zone.old;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.Sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CompositeZone extends LightBoardZone {

    private List<LightBoardZone> zones = new ArrayList<>();

    private int width = 0;
    private int height = 0;

    @Inject
    public CompositeZone(LightBoardSurface surface, Sync sync) {
        super(surface, sync);
        autoReset(false);
    }

    public CompositeZone bindZones(LightBoardZone... tz) {
        int i = 0;
        for ( LightBoardZone z : tz) {
            zones.add(i, z);
            width = Math.max(width, z.getRegion().width);
            height = Math.max(height, z.getRegion().height);
            prepareZone(i, z);
            i++;
        }
        return this;
    }

    @Override
    public void pause() {
        super.pause();
        zones.forEach(LightBoardZone::pause);
    }

    @Override
    public void resume() {
        super.resume();
        zones.forEach(LightBoardZone::resume);
    }

    private Map<Integer, Boolean> scrolledOut = new HashMap<>();

    private void prepareZone(final int zoneNo, LightBoardZone zone) {
        zone.resetScroll();
        zone.removeAllHandlers();
        zone.autoReset(false);
        zone.autoRender(false);
        zone.clear(true);

        scrolledOut.put(zoneNo, false);
        zone.addScrollCompleteHandler(() -> {
            scrolledOut.put(zoneNo, true);
            if ( allScrolledOut() ) {
                clearScrolledOut();
                onScrollComplete();
            }
        });
    }

    private boolean allScrolledOut() {
        for (Entry<Integer, Boolean> entry : scrolledOut.entrySet() ) {
            if ( !entry.getValue() ) {
                return false;
            }
        }
        return true;
    }

    private void clearScrolledOut() {
        for (Entry<Integer, Boolean> entry : scrolledOut.entrySet() ) {
            scrolledOut.put(entry.getKey(), false);
//            zones.get(entry.getKey()).advanceMessage();
            zones.get(entry.getKey()).resetScroll();
        }
    }

    public LightBoardZone start() {
        if ( !singleRender ) {
            sync.addTask(new Sync.Task(scrollTick) {
                @Override
                public void runTask() {
                    tick();
                }
            });
        }
        onScrollComplete();
        resetScroll();
        return this;
    }

    @Override
    public LightBoardZone setRestDuration(int pause) {
        for ( LightBoardZone zone : zones ) {
            zone.setRestDuration(pause);
        }
        return super.setRestDuration(pause);
    }

    @Override
    public LightBoardZone scroll(Edge from, Edge to) {
        for (LightBoardZone zone : zones) {
            zone.scroll(from, to);
        }
        return super.scroll(from, to);
    }

    @Override
    public void tick() {
        zones.forEach(LightBoardZone::tick);
        render();
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
        if ( !paused ) {
            for (LightBoardZone zone : zones) {
                zone.clear();
                drawn |= zone.render();
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
