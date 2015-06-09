package lightboard.scene.impl;

import lightboard.scene.Scene;
import lightboard.scene.SceneManager;
import lightboard.surface.LightBoardSurface;
import lightboard.updater.schedule.MessageResource;
import lightboard.updater.schedule.MessageUpdater;
import lightboard.zone.LightBoardZone;
import lightboard.zone.impl.TextZone;

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

        MessageUpdater updater = new MessageUpdater(zone);
        MessageResource.bindUpdater(updater);

        registerZones(zone);
        registerUpdaters(updater);

    }

    @Override
    public boolean isBlocking() {
        return zone.isOverride();
    }
}
