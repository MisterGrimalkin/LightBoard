package net.amarantha.lightboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.scene.SceneLoader;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.transport.BusUpdater;
import net.amarantha.lightboard.updater.transport.TubeUpdater;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.webservice.WebService;

import static net.amarantha.lightboard.utility.LightBoardProperties.isTestMode;
import static net.amarantha.lightboard.utility.LightBoardProperties.isWithServer;

@Singleton
public class LightBoardSystem {

    @Inject private LightBoardProperties props;

    @Inject private LightBoardSurface surface;

    @Inject private SceneLoader sceneLoader;
    @Inject private WebService webService;
    @Inject private Sync sync;

    @Inject private TubeUpdater tubeUpdater;
    @Inject private BusUpdater busUpdater;

    public void start() {

        System.out.println("Starting LightBoardSurface: " + props.getBoardRows() + "x" + props.getBoardCols());

        surface.init(props.getBoardRows(), props.getBoardCols(), true);

        if ( isTestMode() ) {
            surface.testMode();
        } else {
            sceneLoader.start();
        }

        if ( isWithServer() ) {
            webService.start();
        }

        busUpdater.start();
        tubeUpdater.start();

        sync.startSyncThread();

    }

}
