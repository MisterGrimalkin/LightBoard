package net.amarantha.lightboard.scene.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.updater.schedule.DateTimeUpdater;
import net.amarantha.lightboard.zone.impl.TextZone;


public class LongDateScene extends Scene {

    @Inject private SceneManager sceneManager;

    @Inject private TextZone dateZone;

    public LongDateScene(LightBoardSurface surface) {
        super("Long Date");
    }

    @Override
    public void build() {

        dateZone
                .scrollUp()
                .setRegion(0, 0, getCols(), getRows())
                .setScrollTick(30)
                .addScrollCompleteHandler(sceneManager::advanceScene);

        Updater dateUpdater = new DateTimeUpdater(dateZone, "EEEE d MMMM YYYY");

        registerZones(dateZone);
        registerUpdaters(dateUpdater);
    }

}
