package net.amarantha.lightboard;

import com.google.inject.Inject;
import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.module.ApplicationTestModule;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.MockSync;
import net.amarantha.lightboard.utility.Sync;
import org.junit.runner.RunWith;

@RunWith(StoryRunner.class) @Modules(ApplicationTestModule.class)
public class TestPatternRendering {

    @Inject private LightBoard board;

    @Inject private LightBoardSurface surface;

    @Inject private Sync sync;

    @Story
    public void testPatternRendering() {

        surface.init();

        sync.startSyncThread();

        ((MockSync)sync).runTasks();

        surface.drawPattern(0,0,square);

        ((MockSync)sync).runTasks();




    }

    Pattern square = new Pattern(
            5,
            "#--#-" +
            "#--#-");


}
