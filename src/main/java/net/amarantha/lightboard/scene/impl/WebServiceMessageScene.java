package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.scene.OldSceneManager;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.schedule.PostMessageUpdater;
import net.amarantha.lightboard.webservice.BroadcastMessageResource;
import net.amarantha.lightboard.zone.old.TextZone_Old;

import javax.inject.Inject;

public class WebServiceMessageScene extends Scene {

    @Inject private OldSceneManager sceneManager;
    @Inject private PostMessageUpdater updater;

    @Inject
    public WebServiceMessageScene(LightBoardSurface surface) {
        super("Web Messages");
    }

    @Inject private TextZone_Old zone;

    @Override
    public void build() {

        zone
            .scrollUp()
            .setScrollTick(60)
            .addScrollCompleteHandler(() -> {
                sceneManager.reloadScene();
                zone.clearOverride();
            });
        zone.addMessage(0, "");

        updater.setZones(zone);
        BroadcastMessageResource.bindUpdater(updater);
        BroadcastMessageResource.bindScene(0);

        registerZones(zone);
        registerUpdaters(updater);

    }

    @Override
    public boolean isBlocking() {
        return zone.isOverride();
    }
}
