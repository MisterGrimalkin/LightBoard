package lightboard.scene.impl;

import lightboard.scene.Scene;
import lightboard.scene.SceneManager;
import lightboard.surface.LightBoardSurface;
import lightboard.zone.impl.ImageZone;

public class ImageScene extends Scene {

    public ImageScene(LightBoardSurface surface) {
        super(surface);
        setSceneDuration(null);
    }

    @Override
    public void build() {

        ImageZone zone = ImageZone.scrollUp(getSurface());
        zone.setScrollTick(50);
        zone.setRestDuration(3000);
//        zone.loadImage("sb.jpg");
        zone.loadImage("gp192x32.jpg");
//        zone.loadImage("gp180x16.jpg");
        zone.addScrollCompleteHandler(() -> {
            SceneManager.advanceScene();
        });

        registerZones(zone);

    }
}
