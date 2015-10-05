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

import static net.amarantha.lightboard.utility.MessageQueue.DEFAULT_MESSAGE;

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
        scene.testMode();
        scene.start();
        scene.resume();

        sync.startSyncThread();

        for ( int i=0; i<100; i++ ) {
            ((MockSync) sync).runTasks();
            if ( !scene.getBusNumber().getCurrentMessage().getMessage().equals(DEFAULT_MESSAGE.getMessage())) {
                System.out.println(stripTags(scene.getBusNumber().getCurrentMessage().getMessage()));
                System.out.println(stripTags(scene.getBusDestinationLeft().getCurrentMessage().getMessage()));
                System.out.println(stripTags(scene.getBusTimesLeft().getCurrentMessage().getMessage()));
                System.out.println(stripTags(scene.getBusDestinationRight().getCurrentMessage().getMessage()));
                System.out.println(stripTags(scene.getBusTimesRight().getCurrentMessage().getMessage()));
                System.out.println();
            }
        }



//        sceneManager.addScene(1, scene);




    }

    private String stripTags(String input) {
        return input.replaceAll("\\{\\w*\\}", "");
    }


}
