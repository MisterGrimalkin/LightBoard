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
     * Push the supplied data to the physical display
     * @param data Current LightBoardSurface state - indexed: [row][col]
     */
    void dump(boolean[][] data);

    /**
     * Indicates how often the LightBoard should receive data via dump(...)
     * Return <code>null</code> to update the board as often as possible
     * @return
     */
    Long getRefreshInterval();

    /**
     * Height of the physical board in pixels
     */
    int getRows();

    /**
     * Width of the physical board in pixels
     */
    int getCols();

}
