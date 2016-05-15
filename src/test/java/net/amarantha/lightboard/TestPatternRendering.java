package net.amarantha.lightboard;

import com.google.inject.Inject;
import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.board.MockBoard;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.module.ApplicationTestModule;
import net.amarantha.lightboard.surface.LightBoardSurface;
import net.amarantha.lightboard.utility.MockSync;
import net.amarantha.lightboard.utility.Sync;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(StoryRunner.class) @Modules(ApplicationTestModule.class)
public class TestPatternRendering {

    @Inject private LightBoard board;

    @Inject private LightBoardSurface surface;

    @Inject private Sync sync;

    @Story
//    @Ignore
    public void testPatternRendering() {

        given_a_lightboard();

        when_sync_ticks();
        then_board_is_blank();

        when_draw_pattern(square);
        when_sync_ticks();
        then_board_state_contains(square);

        when_clear_and_draw_pattern(cross);
        when_sync_ticks();
        then_board_state_contains(cross);

        when_clear_surface();
        then_board_is_blank();

        when_draw_pattern(square);
        when_draw_pattern(cross);
        when_sync_ticks();
        then_board_state_contains(combined1);

        when_clear_surface();
        then_board_is_blank();
        when_draw_pattern_at_$2__$3(cross, 0, 0);
        when_draw_pattern_at_$2__$3(square, 2, 2);
        when_draw_pattern_at_$2__$3(cross, 4, 4);
        then_board_state_contains(combined2);

    }

    void given_a_lightboard() {
        sync.init();
        surface.init(32, 192);
        sync.startSyncThread();
    }

    void when_sync_ticks() {
        ((MockSync)sync).runTasks();
    }

    void when_clear_surface() {
        surface.clearSurface();
    }

    void when_clear_and_draw_pattern(Pattern pattern) {
        surface.drawPattern(0, 0, pattern, true, null);
    }

    void when_draw_pattern(Pattern pattern) {
        surface.drawPattern(0, 0, pattern);
    }

    void when_draw_pattern_at_$2__$3(Pattern pattern, int x, int y) {
        surface.drawPattern(x, y, pattern);
    }

    void then_board_is_blank() {
        for ( int r=0; r<board.getRows(); r++ ) {
            for ( int c=0; c<board.getCols(); c++ ) {
                for ( int p=0; p<3; p++ ) {
                    assertEquals(0.0, ((MockBoard) board).getData()[p][r][c], 0.1);
                }
            }
        }
    }

    void then_board_state_contains(Pattern pattern) {
        for (int r = 0; r<pattern.getHeight(); r++ ) {
            for (int c = 0; c<pattern.getWidth(); c++ ) {
                for ( int p=0; p<3; p++ ) {
                    assertEquals(
                            pattern.getColourValues()[p][r][c],
                            ((MockBoard) board).getData()[p][r][c],
                            0.1
                    );
                }
            }
        }

    }

    Pattern square = new Pattern(
            5,
            "#####" +
            "#---#" +
            "#---#" +
            "#---#" +
            "#####");

    Pattern cross = new Pattern(
            5,
            "--#--" +
            "--#--" +
            "#####" +
            "--#--" +
            "--#--");

    Pattern combined1 = new Pattern(
            5,
            "#####" +
            "#-#-#" +
            "#####" +
            "#-#-#" +
            "#####");

    Pattern combined2 = new Pattern(
            9,
            "--#------" +
            "--#------" +
            "#######--" +
            "--#---#--" +
            "--#---#--" +
            "--#---#--" +
            "--#######" +
            "------#--" +
            "------#--" );

}
