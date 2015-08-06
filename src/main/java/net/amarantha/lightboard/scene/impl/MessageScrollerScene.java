package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.MessageUpdater;
import net.amarantha.lightboard.zone.impl.TextZone;

import javax.inject.Inject;

public class MessageScrollerScene extends Scene {

    @Inject private SceneManager sceneManager;

    public MessageScrollerScene(LightBoardSurface surface) {
        super("Message Scroller");
    }

    @Override
    public void build() {

        TextZone zone = TextZone.scrollUp(getSurface());
        zone.setScrollTick(20);

        zone.addScrollCompleteHandler(() -> {
            zone.advanceMessage();
            zone.resetScroll();
            sceneManager.advanceScene();
        });

        MessageUpdater updater = new MessageUpdater(zone);
        updater.setDataRefresh(10000);

        registerZones(zone);
        registerUpdaters(updater);

    }
}
