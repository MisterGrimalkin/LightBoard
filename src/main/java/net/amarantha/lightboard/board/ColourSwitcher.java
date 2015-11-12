package net.amarantha.lightboard.board;

import java.util.List;

/**
 * Indicates a LightBoard that can support multiple colour modes
 */
public interface ColourSwitcher {

    List<String> getSupportedColours();

    void setColour(String colour);

    String getColour();

}
