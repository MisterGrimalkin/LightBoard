package net.amarantha.lightboard.scene.old;

import com.google.inject.Inject;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.schedule.DateTimeUpdater;
import net.amarantha.lightboard.zone.old.TextZone_Old;


public class LongDateOldScene extends OldScene {

    @Inject private OldSceneManager sceneManager;

    @Inject private TextZone_Old dateZone;

    @Inject private DateTimeUpdater dateUpdater;

    public LongDateOldScene(LightBoardSurface surface) {
        super("Long Date");
    }

    @Override
    public void build() {

        dateZone
                .scrollUp()
                .setRegion(0, 0, getCols(), getRows())
                .setScrollTick(30)
                .addScrollCompleteHandler(sceneManager::advanceScene);

        dateUpdater.setZone(dateZone).setFormats("EEEE d MMMM YYYY");

        registerZones(dateZone);
        registerUpdaters(dateUpdater);
    }

}
