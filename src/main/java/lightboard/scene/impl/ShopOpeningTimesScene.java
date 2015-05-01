package lightboard.scene.impl;

import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.impl.TextZone;
import lightboard.scene.Scene;
import lightboard.updater.schedule.DateTimeUpdater;
import lightboard.updater.schedule.OpeningTimesUpdater;

import static lightboard.util.MessageQueue.HPosition.LEFT;
import static lightboard.util.MessageQueue.VPosition.MIDDLE;

public class ShopOpeningTimesScene extends Scene {

    public ShopOpeningTimesScene(LightBoardSurface surface) {
        super(surface);
    }

    public void build() {

        TextZone dateZone = TextZone.fixed(getSurface());
        dateZone
            .setRegion(0, 0, getCols(), getRows() / 2)
            .setRestDuration(0)
            .setRestPosition(LEFT, MIDDLE)
            .setScrollTick(500);
        DateTimeUpdater dateUpdater = new DateTimeUpdater(dateZone, "EEEE d MMMM yyyy  h:mma");
        dateUpdater.setDataRefresh(500);

        TextZone openingTimesZone = TextZone.scrollUp(getSurface());
        openingTimesZone
            .setRegion(0, getRows() / 2, getCols(), getRows() / 2)
            .setRestDuration(3500)
            .setRestPosition(LEFT, MIDDLE)
            .setScrollTick(40);
        OpeningTimesUpdater openingTimesUpdater = new OpeningTimesUpdater(openingTimesZone);
        openingTimesUpdater.setDataRefresh(60000);

        registerZones(dateZone, openingTimesZone);
        registerUpdaters(dateUpdater, openingTimesUpdater);

    }
}
