package net.amarantha.lightboard.zone.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.LightBoardZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CompositeTextZone extends LightBoardZone {

    private List<TextZone> zones = new ArrayList<>();

    private int width = 0;
    private int height = 0;

    @Inject
    public CompositeTextZone(LightBoardSurface surface, Sync sync) {
        super(surface, sync);
        autoReset(false);
    }

    public CompositeTextZone bindZones(TextZone... tz) {
        int i = 0;
        for ( TextZone z : tz) {
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
        for ( TextZone z : zones ) {
            z.pause();
        }
    }

    @Override
    public void resume() {
        super.resume();
        for ( TextZone z : zones ) {
            z.resume();
        }
    }

    private Map<Integer, Boolean> scrolledOut = new HashMap<>();

    private void prepareZone(final int zoneNo, TextZone zone) {
        zone.resetScroll();
        zone.removeAllHandlers();
        zone.autoReset(false);
        zone.autoRender(false);
        zone.clear(true);

        scrolledOut.put(zoneNo, false);
        zone.addScrollCompleteHandler(() -> {
            scrolledOut.put(zoneNo, true);
            if ( allScrolledOut() ) {
                advanceAllZones();
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

    private void advanceAllZones() {
        for (Entry<Integer, Boolean> entry : scrolledOut.entrySet() ) {
            TextZone z = zones.get(entry.getKey());
            z.advanceMessage();
            z.resetScroll();
            scrolledOut.put(entry.getKey(), false);
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
        for (LightBoardZone zone : zones) {
            zone.tick();
        }
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
        for ( TextZone zone : zones ) {
            zone.clear();
            drawn |= zone.render();
        }
        if ( !drawn ) {
            advanceAllZones();
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
