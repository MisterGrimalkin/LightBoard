package net.amarantha.lightboard.scene;

import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.font.Font;
import net.amarantha.lightboard.module.ApplicationModule;
import net.amarantha.lightboard.scene.impl.TravelInformationScene;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.Sync;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(StoryRunner.class) @Modules(ApplicationModule.class)
public class TravelInformationSceneTest {

    @Inject private LightBoardSurface surface;
    @Inject private SceneManager sceneManager;
    @Inject private TravelInformationScene scene;

    @Story
    public void testScene() {

        surface.init();

        scene.start();
        sceneManager.addScene(1, scene);

        Sync.startSyncThread();



    }


}
