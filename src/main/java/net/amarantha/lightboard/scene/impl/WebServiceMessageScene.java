package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.schedule.OldMessageUpdater;
import net.amarantha.lightboard.webservice.BroadcastMessageResource;
import net.amarantha.lightboard.zone.impl.TextZone;

public class WebServiceMessageScene extends Scene {

    public WebServiceMessageScene(LightBoardSurface surface) {
        super(surface, "Web Messages");
    }

    private TextZone zone;

    @Override
    public void build() {

        zone = TextZone.scrollUp(getSurface());
        zone
            .setScrollTick(60)
            .addScrollCompleteHandler(() -> {
                SceneManager.reloadScene();
                zone.clearOverride();
            });
        zone.addMessage(0, "");

        OldMessageUpdater updater = new OldMessageUpdater(zone);
        BroadcastMessageResource.bindUpdater(updater);

        registerZones(zone);
        registerUpdaters(updater);

    }

    @Override
    public boolean isBlocking() {
        return zone.isOverride();
    }
}
