package net.amarantha.lightboard;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.entity.*;
import net.amarantha.lightboard.font.LargeFont;
import net.amarantha.lightboard.font.SimpleFont;
import net.amarantha.lightboard.font.SmallFont;
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
import net.amarantha.lightboard.zone.ImageZone;
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
//        Pattern p = new Pattern(5, 5);
//        p.drawPoint(2, 2, new Colour(1.0,0.0,1.0));
//        p.drawPoint(3, 3, new Colour(1.0,1.0,1.0));
//        p.drawPoint(4, 4, new Colour(0.0,1.0,1.0));
//        surface.drawPattern(0, 0, p);


        System.out.println("IP = " + props.getIp());

        if ( withServer ) {
            service.startWebService();
        }

        sync.startSyncThread();

    }

//    private TextZone zone;

    @Inject private TextZone textZone1;
    @Inject private TextZone textZone2;
    @Inject private TextZone textZone3;
    @Inject private TextZone textZone4;

    @Inject private ImageZone imageZone;

    @Inject private MessageResource messageResource;

    private void old() {

        imageZone.in();
    }

    private void buildScenes() {

        int typingDuration = 1200;
        int explodeDuration = 800;

        textZone1
            .setFont(new SmallFont())
            .setRegion(0, 0, 192, 32)
            .setDisplayTime(300)
            .setOffset(0, -9)
            .setAutoOut(false)
            .setAutoNext(false)
            .setAlignV(AlignV.MIDDLE)
            .setInTransition(new TypeIn().setDuration(typingDuration))
            .setOutTransition(new ExplodeOut().setDuration(explodeDuration))
            .setCanvasLayer(2)
            .init();

        textZone2
            .setFont(new SimpleFont())
            .setRegion(0, 0, 192, 32)
            .setDisplayTime(300)
            .setAutoOut(false)
            .setAutoNext(false)
            .setAlignV(AlignV.MIDDLE)
            .setInTransition(new TypeIn().setDuration(typingDuration))
            .setOutTransition(new ExplodeOut().setDuration(explodeDuration))
            .setCanvasLayer(3)
            .init();

        textZone3
            .setFont(new SmallFont())
            .setRegion(0, 0, 192, 32)
            .setDisplayTime(2000)
            .setOffset(0, 9)
            .setAutoOut(false)
            .setAutoNext(false)
            .setAlignV(AlignV.MIDDLE)
            .setInTransition(new TypeIn().setDuration(typingDuration))
            .setOutTransition(new ExplodeOut().setDuration(explodeDuration))
            .setCanvasLayer(4)
            .init();

        textZone4
            .setFont(new SmallFont())
            .setRegion(0, 0, 192, 32)
            .setDisplayTime(4000)
            .setAutoOut(true)
            .setAutoNext(false)
            .setAlignV(AlignV.MIDDLE)
            .setInTransition(new ExplodeIn().setDuration(explodeDuration))
            .setOutTransition(new ExplodeOut().setDuration(explodeDuration))
            .setCanvasLayer(5)
            .init();

        textZone1.addMessage("{red}Bailes + Light");
        textZone2.addMessage("{yellow}LightBoard Display System");
        textZone3.addMessage("{green}Version 2.0");
        textZone4.addMessage("{red}Web Service online at {yellow}" + props.getIp());

        imageZone
                .addImage("gp192x32.jpg")
                .setAutoNext(false)
                .setRegion(0, 0, 192, 32)
                .setDisplayTime(4000)
                .setInTransition(new RainDown().setDuration(2500))
                .setOutTransition(new ScrollOut().setEdge(Edge.BOTTOM).setDuration(2000))
                .init();

        textZone1.onInComplete(()->textZone2.in());
        textZone2.onInComplete(()->textZone3.in());
        textZone3.onDisplayComplete(()->{
            textZone1.out();
            textZone2.out();
            textZone3.out();
        });
        textZone3.onOutAt(0.4, () -> textZone4.in());
        textZone4.onOutComplete(() -> imageZone.in());
        imageZone.onOutComplete(() -> textZone1.in());

        textZone1.in();

    }

    private void older() {

        textZone1.setRegion(surface.safeRegion(0,0,192,32));
        textZone2.setRegion(surface.safeRegion(0,0,192,32));
        textZone3.setRegion(surface.safeRegion(0,23,192,9));
//        textZone3.setOutline(new Colour(1,1,1));

        textZone1.setOffset(-40, -5);
        textZone2.setOffset(40, -5);

//        textZone1.setOutline(new Colour(1,0,0));
//        textZone2.setOutline(new Colour(0,1,0));

        textZone1.setDisplayTime(9000);
        textZone2.setDisplayTime(6000);
        textZone3.setDisplayTime(0);

        textZone1.setAutoOut(true);
        textZone2.setAutoOut(true);

        textZone1.setAlignH(AlignH.CENTRE);
        textZone1.setAlignV(AlignV.MIDDLE);
        textZone2.setAlignH(AlignH.CENTRE);
        textZone2.setAlignV(AlignV.MIDDLE);

        textZone1.setTick(25);
        textZone2.setTick(25);
        textZone3.setTick(25);

        textZone1.setFont(new LargeFont());
        textZone2.setFont(new LargeFont());
        textZone3.setFont(new SimpleFont());

        textZone1.setInTransition(new RainDown().setDuration(500));
        textZone1.setOutTransition(new ExplodeOut().setDuration(100));
        textZone2.setInTransition(new RainDown().setDuration(500));
        textZone2.setOutTransition(new ExplodeOut().setDuration(100));
        textZone3.setInTransition(new ScrollIn().setEdge(Edge.RIGHT).setDuration(7000));
        textZone3.setOutTransition(new ScrollOut().setEdge(Edge.LEFT).setDuration(7000));

        textZone1.setAutoNext(true);
        textZone2.setAutoNext(true);
        textZone3.setAutoNext(true);

        textZone1.setCanvasLayer(3);
        textZone2.setCanvasLayer(2);
        textZone3.setCanvasLayer(1);

        textZone1.init();
        textZone2.init();
        textZone3.init();

        textZone1.addMessage("103");
        textZone1.addMessage("104");
        textZone1.addMessage("105");
        textZone1.addMessage("106");
        textZone2.addMessage("98");
        textZone2.addMessage("99");
        textZone2.addMessage("100");
        textZone2.addMessage("101");
        textZone3.addMessage("{green}Another Shower {red}is Possible!");

//        textZone1.execute(() -> textZone2.in());
//        textZone2.execute(() -> textZone1.in());

        textZone1.in();
        textZone2.in();
        textZone3.in();




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
