package net.amarantha.lightboard.scene.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.zone.impl.ImageZone;

public class ImageScene extends Scene {

    private String filename;

    @Inject SceneManager sceneManager;

    @Inject
    public ImageScene(LightBoardSurface surface) {
        super("Image Banner");
        setSceneDuration(null);
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public void build() {

        ImageZone zone = ImageZone.scrollUp(getSurface());
        zone.setScrollTick(50);
        zone.setRestDuration(2500);
        zone.loadImage(filename);
        zone.addScrollCompleteHandler(sceneManager::advanceScene);

        registerZones(zone);

    }
}
