package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.MessageScrollerUpdater;
import net.amarantha.lightboard.zone.impl.TextZone;

public class MessageScrollerScene extends Scene {

    public MessageScrollerScene(LightBoardSurface surface) {
        super(surface, "Message Scroller");
    }

    @Override
    public void build() {

        TextZone zone = TextZone.scrollUp(getSurface());
        zone.setScrollTick(20);

        zone.addScrollCompleteHandler(() -> {
            zone.advanceMessage();
            zone.resetScroll();
            SceneManager.advanceScene();
        });

        MessageScrollerUpdater updater = new MessageScrollerUpdater(zone);
        updater.setDataRefresh(10000);

        registerZones(zone);
        registerUpdaters(updater);

    }
}
