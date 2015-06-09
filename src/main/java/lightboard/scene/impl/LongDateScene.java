package lightboard.scene.impl;

import lightboard.scene.Scene;
import lightboard.scene.SceneManager;
import lightboard.surface.LightBoardSurface;
import lightboard.updater.Updater;
import lightboard.updater.schedule.DateTimeUpdater;
import lightboard.zone.LightBoardZone;
import lightboard.zone.impl.TextZone;

import static lightboard.util.MessageQueue.Edge.BOTTOM_EDGE;
import static lightboard.util.MessageQueue.Edge.TOP_EDGE;

public class LongDateScene extends Scene {

    public LongDateScene(LightBoardSurface surface) {
        super(surface, "Long Date");
    }

    @Override
    public void build() {

        TextZone dateZone = new TextZone(getSurface(), BOTTOM_EDGE, TOP_EDGE, 3000);
        dateZone.setRegion(0, 0, getCols(), getRows())
                .setScrollTick(30)
                .addScrollCompleteHandler(SceneManager::advanceScene);

        Updater dateUpdater = new DateTimeUpdater(dateZone, "EEEE d MMMM YYYY");

        registerZones(dateZone);
        registerUpdaters(dateUpdater);
    }

}
