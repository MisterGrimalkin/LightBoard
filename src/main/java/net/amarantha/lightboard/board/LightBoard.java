package net.amarantha.lightboard.board;

/**
 * A LightBoard display
 */
public interface LightBoard {

    /**
     * Set up the board
     */
    void init();

    /**
     * Push the supplied RGB data to the physical display
     * @param data Current LightBoardSurface state - indexed: [colour(0=R,1=G,2=B)][row][col]
     */
    void update(double[][][] data);

    /**
     * Indicates how often the LightBoard should receive data via update(...)
     * Return <code>null</code> to update the board as often as possible
     * @return
     */
    Long getUpdateInterval();

    /**
     * Height of the physical board in pixels
     */
    int getRows();

    /**
     * Width of the physical board in pixels
     */
    int getCols();

    void sleep();

    void wake();

}
