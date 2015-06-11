package net.amarantha.lightboard.board;

/**
 * LightBoard that supports colour data
 */
public interface RGBLightBoard extends LightBoard {

    /**
     * Push the supplied RGB data to the physical display
     * @param data Current LightBoardSurface state - indexed: [colour(0=R,1=G,2=B)][row][col]
     */
    void dump(double[][][] data);

}
