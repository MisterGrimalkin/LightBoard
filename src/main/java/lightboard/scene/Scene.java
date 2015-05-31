package lightboard.scene;

import lightboard.surface.LightBoardSurface;
import lightboard.updater.Updater;
import lightboard.zone.LightBoardZone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Scene {

    private LightBoardSurface surface;
    private List<LightBoardZone> zones = new ArrayList<>();
    private List<Updater> updaters = new ArrayList<>();

    public Scene(LightBoardSurface surface) {
        this.surface = surface;
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

}
