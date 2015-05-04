package lightboard.updater.schedule;

import lightboard.board.zone.impl.TextZone;
import lightboard.scene.impl.OpeningTimes;
import lightboard.updater.Updater;

import java.util.List;

import static lightboard.scene.impl.OpeningTimes.getLocalOpeningTimes;

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