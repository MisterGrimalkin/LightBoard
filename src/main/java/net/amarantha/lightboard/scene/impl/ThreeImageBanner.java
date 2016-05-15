package net.amarantha.lightboard.scene.impl;

import com.google.inject.Inject;
import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.scene.OldSceneManager;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.utility.PropertyManager;
import net.amarantha.lightboard.zone.impl.ImageZone;

public class ThreeImageBanner extends Scene {

    @Inject private ImageZone leftZone;
    @Inject private ImageZone middleZone;
    @Inject private ImageZone rightZone;

    @Inject private OldSceneManager sceneManager;

    @Inject private PropertyManager props;

    private final String leftImage;
    private final int leftWidth;

    private final String middleImage;
    private final int middleWidth;

    private final String rightImage;
    private final int rightWidth;

    public ThreeImageBanner(String name, String leftImage, int leftWidth, String middleImage, int middleWidth, String rightImage, int rightWidth) {
        super(name);
        this.leftImage = leftImage;
        this.leftWidth = leftWidth;
        this.middleImage = middleImage;
        this.middleWidth = middleWidth;
        this.rightImage = rightImage;
        this.rightWidth = rightWidth;
    }

    @Override
    public void build() {

        int bannerTime = props.getInt("bannerTime", 5) * 1000;

        leftZone
            .scroll(Edge.TOP, Edge.TOP)
            .setScrollTick(60)
            .setMasterDelta(5)
            .setRestDuration(bannerTime)
            .autoReset(false)
            .clear(true)
            .setRegion(0, 0, leftWidth, 32);
        leftZone.loadImage(leftImage);

        middleZone
            .scroll(Edge.LEFT, Edge.NO_SCROLL)
            .setScrollTick(30)
            .setRestDuration(bannerTime-100)
            .autoReset(false)
            .setRegion(leftWidth, 0, middleWidth, 32);
        middleZone.loadImage(middleImage);

        rightZone
            .scroll(Edge.BOTTOM, Edge.BOTTOM)
            .setScrollTick(60)
            .setMasterDelta(3)
            .setRestDuration(bannerTime-400)
            .autoReset(false)
            .clear(true)
            .setRegion(leftWidth + middleWidth, 0, rightWidth, 32);
        rightZone.loadImage(rightImage);

        leftZone.addScrollRestHandler(middleZone::resume);
        middleZone.addScrollRestHandler(rightZone::resume);
        rightZone.addScrollCompleteHandler(sceneManager::advanceScene);
        leftZone.addScrollCompleteHandler(leftZone::clear);

        registerZones(leftZone, middleZone, rightZone);

    }

    @Override
    public void resume() {
        leftZone.resume();
        leftZone.resetScroll();
        middleZone.resetScroll();
        rightZone.resetScroll();
    }
}
