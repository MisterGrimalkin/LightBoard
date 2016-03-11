package net.amarantha.lightboard.scene.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.utility.Now;
import net.amarantha.lightboard.zone.impl.CountdownZone;
import net.amarantha.lightboard.zone.impl.TextZone;

public class NewYearCountdown extends Scene {

    @Inject private Now now;

    @Inject private CountdownZone zone;

    @Inject
    public NewYearCountdown() {
        super("New Year Countdown");
    }

    @Override
    public void build() {

        registerZones(zone);

    }
}
