package lightboard.scene;

import lightboard.board.surface.LightBoardSurface;
import lightboard.board.zone.impl.TextZone;
import lightboard.updater.schedule.MessageResource;
import lightboard.updater.schedule.MessageUpdater;

public class WebServiceMessageScene extends Scene {

    public WebServiceMessageScene(LightBoardSurface surface) {
        super(surface);
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
