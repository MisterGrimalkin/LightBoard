package net.amarantha.lightboard;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.entity.AlignH;
import net.amarantha.lightboard.entity.AlignV;
import net.amarantha.lightboard.entity.Edge;
import net.amarantha.lightboard.font.LargeFont;
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

    @Inject private TextZone textZone1;
    @Inject private TextZone textZone2;

    @Inject private MessageResource messageResource;

    @Inject private ScrollIn scrollIn;
    @Inject private ScrollOut scrollOut;
    @Inject private RainDown rainDown;
    @Inject private ExplodeOut explodeOut;
    @Inject private ExplodeIn explodeIn;

    private void buildScenes() {

        explodeIn.setDuration(300);
        explodeOut.setDuration(300);

        rainDown.setDuration(600);

        scrollOut.setDuration(500);
        scrollOut.setEdge(Edge.TOP);

        textZone1.setRegion(surface.safeRegion(0,0,192,32));
        textZone2.setRegion(surface.safeRegion(0,0,192,32));

        textZone1.setDisplayTime(600);
        textZone2.setDisplayTime(600);

        textZone1.setAutoOut(true);
        textZone2.setAutoOut(true);

        textZone1.setAlignH(AlignH.CENTRE);
        textZone1.setAlignV(AlignV.BOTTOM);
        textZone2.setAlignH(AlignH.CENTRE);
        textZone2.setAlignV(AlignV.TOP);

        textZone1.setTick(25);
        textZone2.setTick(25);

        textZone1.setFont(new LargeFont());
        textZone2.setFont(new LargeFont());

        textZone1.setInTransition(rainDown);
        textZone1.setOutTransition(explodeOut);
        textZone2.setInTransition(explodeIn);
        textZone2.setOutTransition(scrollOut);

        textZone1.setAutoNext(false);
        textZone2.setAutoNext(false);

        textZone1.init();
        textZone2.init();

        textZone1.addMessage("9 9 9 9 9");
        textZone2.addMessage("8 8 8 8 8");
        textZone1.addMessage("7 7 7 7 7");
        textZone2.addMessage("6 6 6 6 6");
        textZone1.addMessage("5 5 5 5 5");
        textZone2.addMessage("4 4 4 4 4");
        textZone1.addMessage("3 3 3 3 3");
        textZone2.addMessage("2 2 2 2 2");
        textZone1.addMessage("1 1 1 1 1");
        textZone2.addMessage("0 0 0 0 0");

        textZone1.onOutComplete(()->textZone2.in());
        textZone2.onOutComplete(()->textZone1.in());

        textZone1.in();




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
