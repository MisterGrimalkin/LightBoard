package net.amarantha.lightboard.updater.schedule;

import net.amarantha.lightboard.scene.impl.OpeningTimes;
import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.zone.impl.TextZone;

import java.util.List;

import static net.amarantha.lightboard.scene.impl.OpeningTimes.getLocalOpeningTimes;

public class OpeningTimesUpdater extends Updater {

    public OpeningTimesUpdater(TextZone zone) {
        super(zone);
        System.out.println("Opening Times Updater Ready....");
    }

    @Override
    public void refresh() {
        clearMessages();
        List<OpeningTimes> localOpeningTimes = getLocalOpeningTimes();
        for (OpeningTimes times : localOpeningTimes) {
            addMessage(times.getInfoString());
        }

    }
}
