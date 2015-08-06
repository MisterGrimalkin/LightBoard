package net.amarantha.lightboard;

import com.google.inject.Inject;
import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.module.ApplicationModule;
import net.amarantha.lightboard.module.Debug;
import org.junit.runner.RunWith;

@RunWith(StoryRunner.class) @Modules(ApplicationModule.class)
public class TestPatternRendering {

    @Inject @Debug
    LightBoard board;

    @Story
    public void testPatternRendering() {

        System.out.println(board.getClass().getName());

        System.out.println(board.getCols());
    }

}
