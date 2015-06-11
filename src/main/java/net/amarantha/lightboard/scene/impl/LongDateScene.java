package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.updater.schedule.DateTimeUpdater;
import net.amarantha.lightboard.zone.impl.TextZone;


public class LongDateScene extends Scene {

    public LongDateScene(LightBoardSurface surface) {
        super(surface, "Long Date");
    }

    @Override
    public void build() {

        TextZone dateZone = new TextZone(getSurface(), Edge.BOTTOM_EDGE, Edge.TOP_EDGE, 3000);
        dateZone.setRegion(0, 0, getCols(), getRows())
                .setScrollTick(30)
                .addScrollCompleteHandler(SceneManager::advanceScene);

        Updater dateUpdater = new DateTimeUpdater(dateZone, "EEEE d MMMM YYYY");

        registerZones(dateZone);
        registerUpdaters(dateUpdater);
    }

}
