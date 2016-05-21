package net.amarantha.lightboard;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.scene.BusStopScene;
import net.amarantha.lightboard.scene.OldScene;
import net.amarantha.lightboard.scene.OldSceneManager;
import net.amarantha.lightboard.scene.SplashScene;
import net.amarantha.lightboard.scene.impl.MessagesOldScene;
import net.amarantha.lightboard.scene.impl.TravelInformationOldScene;
import net.amarantha.lightboard.scene.impl.WebServiceMessageOldScene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.webservice.WebService;

public class LightBoardApplication {

    @Inject private LightBoardSurface surface;

    @Inject private LightBoardProperties props;

    @Inject private OldSceneManager sceneManager;

    @Inject private WebServiceMessageOldScene webServiceMessageScene;
    @Inject private TravelInformationOldScene travelInformationScene;
    @Inject private MessagesOldScene messagesScene;

    private OldScene imageBanner;

    @Inject private WebService service;

    @Inject private Sync sync;

    @Inject private Injector injector;

    @Inject private LightBoard board;

    @Inject private SplashScene splashScene;
    @Inject private BusStopScene busStopScene;


    public void startApplication(boolean withServer, boolean testMode) {

        surface.init(props.getBoardRows(), props.getBoardCols(), true);

        if ( testMode ) {
            surface.testMode();
        } else {
            buildScenes();
        }

        surface.clearSurface();
        if ( withServer ) {
            service.startWebService();
        }

        sync.startSyncThread();

    }

    private void buildScenes() {

//        splashScene.build();
//        splashScene.init();
//        splashScene.start();

        busStopScene.build();
        busStopScene.init();
        busStopScene.start();

    }


}
