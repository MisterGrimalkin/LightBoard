package lightboard.scene.impl;

import lightboard.scene.Scene;
import lightboard.scene.SceneManager;
import lightboard.surface.LightBoardSurface;
import lightboard.updater.MessageScrollerUpdater;
import lightboard.zone.impl.TextZone;

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
