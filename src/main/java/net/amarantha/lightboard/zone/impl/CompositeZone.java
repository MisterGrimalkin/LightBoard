package net.amarantha.lightboard.zone.impl;

import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.LightBoardZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompositeZone extends LightBoardZone {

    private List<TextZone> zones = new ArrayList<>();

    private int width = 0;
    private int height = 0;

    public CompositeZone(LightBoardSurface surface, TextZone... lightBoardZones) {
        super(surface);
        int i = 0;
        for ( TextZone z : lightBoardZones) {
            zones.add(z);
            width = Math.max(width, z.getRegion().width);
            height = Math.max(height, z.getRegion().height);
            prepareZone(i, z);
            i++;
        }
        autoReset(false);
    }

    private Map<Integer, Boolean> scrolledOut = new HashMap<>();

    private void prepareZone(final int zoneNo, TextZone zone) {
        scrolledOut.put(zoneNo, false);
        zone.resetScroll();
        zone.removeAllHandlers();
        zone.autoReset(false);
//        zone.addScrollCompleteHandler(() -> {
//            if ( zoneNo==2 ) {
//                System.out.println("FUCKED");
//            }
//            if ( !scrolledOut.get(zoneNo) ) {
//                scrolledOut.put(zoneNo, true);
//                System.out.println("Zone " + zoneNo + " out");
//                if (allScrolledOut()) {
//                    System.out.println("RESET");
//                    for (TextZone z : zones) {
//                        z.clearOverride();
//                        z.advanceMessage();
//                        z.resetScroll();
//                    }
//                    List<Integer> index = new ArrayList<>(scrolledOut.keySet());
//                    for (Integer i : index) {
//                        scrolledOut.put(i, false);
//                    }
//                }
//            }
//        });
    }

//    private boolean allScrolledOut() {
//        for ( Boolean b : scrolledOut.values() ) {
//            if ( !b ) {
//                return false;
//            }
//        }
//        System.out.println("ALL");
//        return true;
//    }
//
//    private int zonesScrolledOut = 0;
//


    public LightBoardZone start() {
        if ( !singleRender ) {
            Sync.addTask(new Sync.Task(scrollTick) {
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

    //    private boolean paused;

//    @Override
//    public void pause() {
//        paused = true;
//    }
//
//    @Override
//    public void resume() {
//        paused = false;
//    }

    @Override
    public void tick() {
//        if ( !paused ) {
//            super.tick();
            for (LightBoardZone zone : zones) {
                zone.tick();
//                zone.render();
            }
            render();
//        }
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
            drawn |= zone.render();
        }
        if ( !drawn ) {
            for ( TextZone lightBoardZone : zones ) {
                lightBoardZone.advanceMessage();
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
