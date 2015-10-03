package net.amarantha.lightboard.updater.schedule;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.impl.OpeningTimes;
import net.amarantha.lightboard.updater.Updater;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.zone.impl.TextZone;

import java.util.List;

import static net.amarantha.lightboard.scene.impl.OpeningTimes.getLocalOpeningTimes;

public class OpeningTimesUpdater extends Updater {

    private TextZone zone;

    @Inject
    public OpeningTimesUpdater(Sync sync) {
        super(sync);
        System.out.println("Opening Times Updater Ready....");
    }

    public OpeningTimesUpdater setZone(TextZone zone) {
        this.zone = zone;
        return this;
    }

    @Override
    public void refresh() {
        zone.clearMessages();
        List<OpeningTimes> localOpeningTimes = getLocalOpeningTimes();
        for (OpeningTimes times : localOpeningTimes) {
            zone.addMessage(times.getInfoString());
        }

    }
}
