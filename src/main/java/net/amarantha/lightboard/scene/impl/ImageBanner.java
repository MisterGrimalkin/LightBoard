package net.amarantha.lightboard.scene.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.OldSceneManager;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.utility.PropertyManager;
import net.amarantha.lightboard.zone.impl.ImageZone;

public class ImageBanner extends Scene {

    private String filename;

    @Inject private OldSceneManager sceneManager;

    @Inject private ImageZone zone;

    @Inject private PropertyManager props;

    @Inject
    public ImageBanner() {
        super("Image Banner");
        setSceneDuration(null);
    }

    @Override
    public void build() {

        zone.scrollUp();
        zone.setScrollTick(50);
        zone.setRestDuration(props.getInt("bannerTime",2)*1000);
        zone.loadImage(props.getString("bannerImage", "gp192x32.jpg"));
        zone.addScrollCompleteHandler(sceneManager::advanceScene);

        registerZones(zone);

    }
}
