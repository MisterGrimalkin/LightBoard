package net.amarantha.lightboard.scene.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.OldScene;
import net.amarantha.lightboard.utility.Now;
import net.amarantha.lightboard.zone.old.CountdownZoneOld;

public class NewYearCountdown extends OldScene {

    @Inject private Now now;

    @Inject private CountdownZoneOld zone;

    @Inject
    public NewYearCountdown() {
        super("New Year Countdown");
    }

    @Override
    public void build() {

        registerZones(zone);

    }
}
