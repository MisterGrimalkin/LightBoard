package net.amarantha.lightboard;

import com.google.inject.Inject;
import net.amarantha.lightboard.scene.SceneLoader;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.webservice.WebService;

import static net.amarantha.lightboard.utility.LightBoardProperties.isTestMode;
import static net.amarantha.lightboard.utility.LightBoardProperties.isWithServer;

public class LightBoardApplication {

    @Inject private LightBoardProperties props;

    @Inject private LightBoardSurface surface;

    @Inject private SceneLoader sceneLoader;
    @Inject private WebService webService;
    @Inject private Sync sync;

    public void startApplication() {

        surface.init(props.getBoardRows(), props.getBoardCols(), true);

        if ( isTestMode() ) {
            surface.testMode();
        } else {
            surface.clearSurface();
            sceneLoader.start();
        }

        sync.startSyncThread();

        if ( isWithServer() ) {
            webService.start();
        }

    }

}
