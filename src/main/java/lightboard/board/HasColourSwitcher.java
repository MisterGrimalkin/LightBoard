package lightboard.board;

import java.util.List;

public interface HasColourSwitcher {
    List<String> supportedColours();
    void colour(String colour);
}
