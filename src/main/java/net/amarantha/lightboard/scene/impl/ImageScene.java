package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.zone.impl.ImageZone;

public class ImageScene extends Scene {

    public ImageScene(LightBoardSurface surface) {
        super(surface, "Greenpeace Logo");
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
        zone.addScrollCompleteHandler(SceneManager::advanceScene);

        registerZones(zone);

    }
}
