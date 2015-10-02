package net.amarantha.lightboard.scene.impl;

import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.zone.impl.TextZone;

import javax.inject.Inject;

public class TeleprompterScene extends Scene {

    @Inject private SceneManager sceneManager;

    @Inject private TextZone zone;

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
        super("Telepromter");
    }

    @Override
    public void build() {

        zone.scrollUp().addScrollCompleteHandler(sceneManager::advanceScene);

        StringBuilder sb = new StringBuilder();
        for ( String message : messages ) {
            sb.append(message).append("\n");
        }

        zone.addMessage(0, sb.toString()).setScrollTick(100);

        registerZones(zone);

    }
}
