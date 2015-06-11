package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.zone.impl.ImageZone;

public class ImageScene extends Scene {

    private String filename;

    public ImageScene(LightBoardSurface surface, String image) {
        super(surface, "Image Banner");
        this.filename = image;
        setSceneDuration(null);
    }

    @Override
    public void build() {

        ImageZone zone = ImageZone.scrollUp(getSurface());
        zone.setScrollTick(50);
        zone.setRestDuration(1000);
        zone.loadImage(filename);
        zone.addScrollCompleteHandler(SceneManager::advanceScene);

        registerZones(zone);

    }
}
