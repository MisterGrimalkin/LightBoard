package net.amarantha.lightboard;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.scene.impl.ImageScene;
import net.amarantha.lightboard.scene.impl.TravelInformationScene;
import net.amarantha.lightboard.scene.impl.WebServiceMessageScene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.PropertyManager;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.webservice.WebService;

public class LightBoardApplication {

    @Inject private LightBoardSurface surface;
    @Inject private WebService service;

    @Inject private SceneManager sceneManager;
    @Inject private WebServiceMessageScene webServiceMessageScene;
    @Inject private TravelInformationScene travelInformationScene;
    @Inject private ImageScene imageScene;


    @Inject private PropertyManager props;

    public void startApplication(boolean withServer) {

        surface.init();

        imageScene.setFilename("gp192x32.jpg");

        sceneManager.addScene(0, webServiceMessageScene, null, false);
        sceneManager.addScene(1, travelInformationScene, 600000, true);
        sceneManager.addScene(2, imageScene, null, true);

        sceneManager.startScenes();
        sceneManager.cycleScenes();
        sceneManager.loadScene(1);

        if ( withServer ) {
            service.startWebService();
        }

        Sync.startSyncThread();

    }


}
