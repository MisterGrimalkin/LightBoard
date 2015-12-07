package net.amarantha.lightboard;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.amarantha.lightboard.board.impl.TextBoard;
import net.amarantha.lightboard.scene.Scene;
import net.amarantha.lightboard.scene.SceneManager;
import net.amarantha.lightboard.scene.impl.*;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.PropertyManager;
import net.amarantha.lightboard.utility.Sync;
import net.amarantha.lightboard.webservice.WebService;

public class LightBoardApplication {

    @Inject private LightBoardSurface surface;

    @Inject private PropertyManager props;

    @Inject private SceneManager sceneManager;

    @Inject private WebServiceMessageScene webServiceMessageScene;
    @Inject private TravelInformationScene travelInformationScene;
    @Inject private ShowerTicketsScene showerTicketsScene;
    @Inject private MessagesScene messagesScene;

    private Scene imageBanner;
    //@Inject private FireAndIceBannerScene imageScene;

    @Inject private WebService service;

    @Inject private Sync sync;

    @Inject private Injector injector;


    public void startApplication(boolean withServer) {

        surface.init();

        int bannerInterval = props.getInt("bannerInterval", 60) * 1000;

        imageBanner = (Scene)injector.getInstance(getImageBannerClass());

        sceneManager.addScene(0, webServiceMessageScene, null, false);
        sceneManager.addScene(1, imageBanner, null, true);
        sceneManager.addScene(2, messagesScene, null, true);
        sceneManager.addScene(3, travelInformationScene, bannerInterval, true);
//        sceneManager.addScene(3, showerTicketsScene, 3000, true);

        sceneManager.startScenes();
        sceneManager.cycleScenes();
        sceneManager.loadScene(1);

        if ( withServer ) {
            service.startWebService();
        }

        sync.startSyncThread();

    }

    private Class getImageBannerClass() {
        String className = props.getString("imageBannerClass", "net.amarantha.lightboard.scene.impl.ImageBanner");
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + className);
        }
        return ImageBanner.class;
    }

}
