package net.amarantha.lightboard;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.scene.impl.TravelInformationScene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.util.Sync;

public class LightBoardApplication {

    @Inject private LightBoardSurface surface;

    @Inject private TravelInformationScene travelInformationScene;

    @Inject private SceneManager sceneManager;

    public void startApplication() {

        surface.init();

        sceneManager.addScene(2, travelInformationScene, 120000, true);

        sceneManager.startScenes();
        sceneManager.cycleScenes();
        sceneManager.loadScene(2);

        Sync.startSyncThread();

    }


}
