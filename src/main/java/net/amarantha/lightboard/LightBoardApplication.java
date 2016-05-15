package net.amarantha.lightboard;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.entity.AlignH;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.scene.OldSceneManager;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.impl.MessagesScene;
import net.amarantha.lightboard.scene.impl.TravelInformationScene;
import net.amarantha.lightboard.scene.impl.WebServiceMessageScene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.LightBoardProperties;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.webservice.MessageResource;
import net.amarantha.lightboard.webservice.WebService;
import net.amarantha.lightboard.zone.TextZone;
import net.amarantha.lightboard.zone.transition.*;

public class LightBoardApplication {

    @Inject private LightBoardSurface surface;

    @Inject private LightBoardProperties props;

    @Inject private OldSceneManager sceneManager;

    @Inject private WebServiceMessageScene webServiceMessageScene;
    @Inject private TravelInformationScene travelInformationScene;
    @Inject private MessagesScene messagesScene;

    private Scene imageBanner;

    @Inject private WebService service;

    @Inject private Sync sync;

    @Inject private Injector injector;

    @Inject private LightBoard board;


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

//    private TextZone zone;

    @Inject private TextZone textZone;
    @Inject private MessageResource messageResource;
    @Inject private ScrollIn scrollIn;
    @Inject private ScrollOut scrollOut;
    @Inject private RainDown rainDown;
    @Inject private ExplodeOut explodeOut;
    @Inject private ExplodeIn explodeIn;

    private void buildScenes() {

        explodeIn.setDuration(200);
        explodeOut.setDuration(300);

        rainDown.setDuration(700);

//        scrollIn.setDuration(700);
//        scrollIn.setEdge(Edge.LEFT);

//        scrollOut.setDuration(1000);
//        scrollOut.setEdge(Edge.LEFT);

        textZone.setRegion(surface.safeRegion(0,0,192,32));
        textZone.setDisplayTime(4000);
        textZone.setAutoAdvance(true);
        textZone.setAlignH(AlignH.CENTRE);
        textZone.setAlignV(AlignV.MIDDLE);
        textZone.setTick(25);
        textZone.init();
        textZone.setInTransition(rainDown);
        textZone.setOutTransition(explodeOut);

        textZone.addMessage("You {red}want the truth?\nYou can't handle the truth!");
        textZone.addMessage("Whether {green}I will turn out to be\nthe hero or the villain of my own life\nthese pages must tell");

        messageResource.setZone(textZone);

        textZone.start();




//        int bannerInterval = props.getBannerIntervalSeconds() * 1000;
//
//        imageBanner = (Scene)injector.getInstance(props.getImageBannerClass());
//
//        sceneManager.addScene(0, webServiceMessageScene, null, false);
//        sceneManager.addScene(1, imageBanner, null, true);
//        sceneManager.addScene(2, messagesScene, null, true);
//        sceneManager.addScene(3, travelInformationScene, bannerInterval, true);
//
//        sceneManager.startScenes();
//        sceneManager.cycleScenes();
//        sceneManager.loadScene(1);



    }

}
