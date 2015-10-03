package net.amarantha.lightboard.scene.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.utility.PropertyManager;
import net.amarantha.lightboard.zone.impl.ImageZone;

public class ImageScene extends Scene {

    private String filename;

    @Inject private SceneManager sceneManager;

    @Inject private ImageZone zone;

    @Inject private PropertyManager props;

    @Inject
    public ImageScene() {
        super("Image Banner");
        setSceneDuration(null);
    }

    @Override
    public void build() {

        zone.scrollUp();
        zone.setScrollTick(50);
        zone.setRestDuration(2500);
        zone.loadImage(props.getString("bannerImage", "gp192x32.jpg"));
        zone.addScrollCompleteHandler(sceneManager::advanceScene);

        registerZones(zone);

    }
}
