package net.amarantha.lightboard;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.scene.impl.ImageBanner;
import net.amarantha.lightboard.scene.impl.MessagesScene;
import net.amarantha.lightboard.scene.impl.TravelInformationScene;
import net.amarantha.lightboard.scene.impl.WebServiceMessageScene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.webservice.WebService;

public class LightBoardApplication {

    @Inject private LightBoardSurface surface;

    @Inject private LightBoardProperties props;

    @Inject private SceneManager sceneManager;

    @Inject private WebServiceMessageScene webServiceMessageScene;
    @Inject private TravelInformationScene travelInformationScene;
    @Inject private MessagesScene messagesScene;

    private Scene imageBanner;

    @Inject private WebService service;

    @Inject private Sync sync;

    @Inject private Injector injector;


    public void startApplication(boolean withServer) {

        surface.init(props.getBoardRows(), props.getBoardCols());

        int bannerInterval = props.getBannerIntervalSeconds() * 1000;

        imageBanner = (Scene)injector.getInstance(props.getImageBannerClass());

        sceneManager.addScene(0, webServiceMessageScene, null, false);
        sceneManager.addScene(1, imageBanner, null, true);
        sceneManager.addScene(2, messagesScene, null, true);
        sceneManager.addScene(3, travelInformationScene, bannerInterval, true);

        sceneManager.startScenes();
        sceneManager.cycleScenes();
        sceneManager.loadScene(1);

        if ( withServer ) {
            service.startWebService();
        }

        sync.startSyncThread();

    }

}
