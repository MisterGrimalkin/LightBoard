package lightboard.scene.impl;

import lightboard.scene.Scene;
import lightboard.scene.SceneManager;
import lightboard.surface.LightBoardSurface;
import lightboard.zone.LightBoardZone;
import lightboard.zone.impl.TextZone;

public class TeleprompterScene extends Scene {

    private static final String[] messages = {
            "A long time ago",
            " ",
            "In a galaxy",
            "far, far away",
            " ",
            "some words",
            " ",
            "went up",
            " ",
            "a screen",
            " "
            };

    public TeleprompterScene(LightBoardSurface surface) {
        super(surface, "Telepromter");
    }

    @Override
    public void build() {

        TextZone zone = TextZone.scrollUp(getSurface());
        zone.addScrollCompleteHandler(SceneManager::advanceScene);

        StringBuilder sb = new StringBuilder();
        for ( String message : messages ) {
            sb.append(message).append("\n");
        }

        zone.addMessage(0, sb.toString()).setScrollTick(100);

        registerZones(zone);

    }
}
