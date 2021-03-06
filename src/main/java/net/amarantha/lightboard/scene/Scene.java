package net.amarantha.lightboard.scene;

import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.zone.LightBoardZone;
import net.amarantha.lightboard.zone.impl.TextZone;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Scene {

    @Inject private LightBoardSurface surface;

    private List<LightBoardZone> zones = new ArrayList<>();
    private List<Updater> updaters = new ArrayList<>();

    private Integer sceneDuration = 10000;
    private boolean includeInCycle = true;

    private String name;

    public Scene(String name) {
        this.name = name;
    }

    public abstract void build();

    public void start() {
        zones.forEach(LightBoardZone::start);
        zones.forEach(LightBoardZone::render);
        updaters.forEach(Updater::start);
    }

    public void pause() {
        zones.forEach(LightBoardZone::pause);
        zones.forEach(LightBoardZone::clear);
        updaters.forEach(Updater::pause);
    }

    public void resume() {
        zones.forEach(LightBoardZone::resume);
        zones.forEach(LightBoardZone::resetScroll);
        zones.forEach(LightBoardZone::render);
        updaters.forEach(Updater::resume);
    }

    public void testMode() {
        for ( LightBoardZone zone : zones ) {
            zone.scroll(Edge.NO_SCROLL, Edge.NO_SCROLL);
            zone.setScrollTick(0);
            zone.setRestDuration(0);
            zone.setDontPauseIfContentTooWide(false);
            if ( zone instanceof TextZone ) {
                ((TextZone)zone).clearAllMessages();
            }
        }
    }

    public Integer getSceneDuration() {
        return sceneDuration;
    }

    public void setSceneDuration(Integer sceneDuration) {
        this.sceneDuration = sceneDuration;
    }

    public boolean isIncludeInCycle() {
        return includeInCycle;
    }

    public void setIncludeInCycle(boolean includeInCycle) {
        this.includeInCycle = includeInCycle;
    }

    protected void registerZones(LightBoardZone... zone) {
        Collections.addAll(zones, zone);
    }

    protected void registerUpdaters(Updater... updater) {
        Collections.addAll(updaters, updater);
    }

    public LightBoardSurface getSurface() {
        return surface;
    }

    public int getRows() {
        return surface.getRows();
    }

    public int getCols() {
        return surface.getCols();
    }

    public boolean isBlocking() {
        return false;
    }

    public String getName() {
        return name;
    }
}
