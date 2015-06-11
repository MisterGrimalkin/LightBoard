package net.amarantha.lightboard.scene;

import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.zone.LightBoardZone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Scene {

    private LightBoardSurface surface;
    private List<LightBoardZone> zones = new ArrayList<>();
    private List<Updater> updaters = new ArrayList<>();

    private Integer sceneDuration = 10000;
    private boolean includeInCycle = true;

    private String name;

    public Scene(LightBoardSurface surface, String name) {
        this.surface = surface;
        this.name = name;
    }

    public abstract void build();

    public void start() {
        zones.forEach(LightBoardZone::start);
        updaters.forEach(Updater::start);
    }

    public void pause() {
        zones.forEach(LightBoardZone::pause);
        zones.forEach(LightBoardZone::clear);
        updaters.forEach(Updater::pause);
    }

    public void resume() {
        zones.forEach(LightBoardZone::resetScroll);
        zones.forEach(LightBoardZone::resume);
        updaters.forEach(Updater::resume);
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
