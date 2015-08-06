package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.schedule.PostMessageUpdater;
import net.amarantha.lightboard.webservice.BroadcastMessageResource;
import net.amarantha.lightboard.zone.impl.TextZone;

import javax.inject.Inject;

public class WebServiceMessageScene extends Scene {

    @Inject private SceneManager sceneManager;

    public WebServiceMessageScene(LightBoardSurface surface) {
        super("Web Messages");
    }

    private TextZone zone;

    @Override
    public void build() {

        zone = TextZone.scrollUp(getSurface());
        zone
            .setScrollTick(60)
            .addScrollCompleteHandler(() -> {
                sceneManager.reloadScene();
                zone.clearOverride();
            });
        zone.addMessage(0, "");

        PostMessageUpdater updater = new PostMessageUpdater(zone);
        BroadcastMessageResource.bindUpdater(updater);

        registerZones(zone);
        registerUpdaters(updater);

    }

    @Override
    public boolean isBlocking() {
        return zone.isOverride();
    }
}
