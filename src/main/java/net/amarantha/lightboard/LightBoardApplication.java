package net.amarantha.lightboard;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.SceneLoader;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.webservice.WebService;

public class LightBoardApplication {

    @Inject private LightBoardSurface surface;

    @Inject private LightBoardProperties props;

    @Inject private SceneLoader sceneLoader;
    @Inject private WebService webService;

    @Inject private Sync sync;

    public void startApplication(boolean withServer, boolean testMode) {

        surface.init(props.getBoardRows(), props.getBoardCols(), true);

        if ( testMode ) {
            surface.testMode();
        } else {
            sceneLoader.start();
        }

        if ( withServer ) {
            webService.start();
        }

        sync.startSyncThread();

    }

}
