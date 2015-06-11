package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.schedule.DateTimeUpdater;
import net.amarantha.lightboard.updater.schedule.OpeningTimesUpdater;
import net.amarantha.lightboard.zone.impl.TextZone;

import static net.amarantha.lightboard.entity.HPosition.LEFT;
import static net.amarantha.lightboard.entity.VPosition.MIDDLE;

public class ShopOpeningTimesScene extends Scene {

    public ShopOpeningTimesScene(LightBoardSurface surface) {
        super(surface, "Shop Hours");
        setSceneDuration(2000);
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
