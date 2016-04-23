package net.amarantha.lightboard.font;

import com.googlecode.guicebehave.Modules;
import com.googlecode.guicebehave.Story;
import com.googlecode.guicebehave.StoryRunner;
import net.amarantha.lightboard.entity.Pattern;
import net.amarantha.lightboard.module.ApplicationTestModule;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@RunWith(StoryRunner.class) @Modules(ApplicationTestModule.class)
public class FontTest {

    @Inject Font font;

    @Story
    public void testFont() {

        given_a_font();

        Pattern pattern1 = when_render_string_$1(TEST_STRING_1);
        then_pattern_is_$2(pattern1, RESULT_PATTERN_1);

        Pattern pattern2 = when_render_string_$1(TEST_STRING_2);
        then_pattern_is_$2(pattern2, RESULT_PATTERN_2);

        Pattern pattern3 = when_render_string_$1(TEST_STRING_3);
        then_pattern_is_$2(pattern3, RESULT_PATTERN_1);

    }

    void given_a_font() {
        font.registerPattern('X', new Pattern(3, TEST_PATTERN_1));
        font.registerPattern('O', new Pattern(TEST_PATTERN_2));
    }

    Pattern when_render_string_$1(String input) {
        return font.renderString(input);
    }

    void then_pattern_is_$2(Pattern actual, Pattern expected) {
        assertEquals(expected.toString(), actual.toString());
    }


    ///////////////
    // Test Data //
    ///////////////

    private final boolean T = true;
    private final boolean F = false;

    private final String TEST_PATTERN_1 =
            "#-#" +
            "-#-" +
            "#-#";

    private final boolean[][] TEST_PATTERN_2 =
            {{T,T,T},
             {T,F,T},
             {T,T,T}};

    private final String TEST_STRING_1 = "XOX";
    private final String TEST_STRING_2 = "O\nX";
    private final String TEST_STRING_3 = "{red}X{green}O{yellow}X";

    private final Pattern RESULT_PATTERN_1 =
            new Pattern(11,
                    "#-#-###-#-#" +
                    "-#--#-#--#-" +
                    "#-#-###-#-#"
            );

    private final Pattern RESULT_PATTERN_2 =
            new Pattern(4,
                    "###-" +
                    "#-#-" +
                    "###-" +
                    "#-#-" +
                    "-#--" +
                    "#-#-"
            );

}
