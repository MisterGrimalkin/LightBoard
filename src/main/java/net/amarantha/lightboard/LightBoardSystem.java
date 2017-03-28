package net.amarantha.lightboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.scene.BlockTestScene;
import net.amarantha.lightboard.scene.SceneLoader;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.updater.transport.BusUpdater;
import net.amarantha.lightboard.updater.transport.TubeUpdater;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.webservice.WebService;
import net.amarantha.utils.properties.PropertiesService;
import net.amarantha.utils.properties.Property;
import net.amarantha.utils.properties.PropertyNotFoundException;

import static net.amarantha.lightboard.Main.NO_HTTP;
import static net.amarantha.lightboard.Main.TEST_MODE;

@Singleton
public class LightBoardSystem {

    @Inject private PropertiesService props;

    @Inject private LightBoardSurface surface;

    @Inject private SceneLoader sceneLoader;
    @Inject private WebService webService;
    @Inject private Sync sync;

    @Inject private TubeUpdater tubeUpdater;
    @Inject private BusUpdater busUpdater;

    @Property("boardRows") private int rows;
    @Property("boardCols") private int cols;

    @Inject private BlockTestScene scene;

    public void start() {

        props.injectPropertiesOrExit(this);

        System.out.println("Starting LightBoardSurface: " + rows + "x" + cols);

        surface.init(rows, cols, true);

        if ( props.isArgumentPresent(TEST_MODE) ) {
            surface.testMode();
        } else {
//            scene.init();
//            sceneLoader.setCurrentScene(scene);
            sceneLoader.start();
            busUpdater.start();
            tubeUpdater.start();
        }

        if ( !props.isArgumentPresent(NO_HTTP) ) {
            webService.start();
        }


        sync.startSyncThread();

    }

}
