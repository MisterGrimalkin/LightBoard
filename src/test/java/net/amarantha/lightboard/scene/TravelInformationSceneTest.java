package net.amarantha.lightboard.scene;

import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.lightboard.module.ApplicationTestModule;
import net.amarantha.lightboard.scene.impl.TravelInformationScene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.MockSync;
import net.amarantha.lightboard.utility.Sync;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(StoryRunner.class) @Modules(ApplicationTestModule.class)
public class TravelInformationSceneTest {

    @Inject private LightBoardSurface surface;
    @Inject private SceneManager sceneManager;
    @Inject private TravelInformationScene scene;
    @Inject private Sync sync;


    @Story
    public void testScene() {

        surface.init();

        scene.build();
        scene.start();
        scene.resume();

        sync.startSyncThread();

//        for ( int i=0; i<100000; i++ )
            ((MockSync)sync).runAllOnce();



//        sceneManager.addScene(1, scene);




    }


}
