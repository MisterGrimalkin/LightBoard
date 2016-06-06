package net.amarantha.lightboard.board;

public class CLightBoard implements LightBoard {

    private int rows;
    private int cols;

    static {
        System.loadLibrary("lightboard");
    }

    @Override
    public void init(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        initNative(rows, cols);
    }

    @Override
    public void update(double[][][] data) {
        for ( int r=0; r<rows; r++ ) {
            for ( int c=0; c<cols; c++ ) {
                setPoint(r, c, data[0][r][c] >= 0.5, data[1][r][c] >= 0.5);
            }
        }
    }

    @Override
    public Long getUpdateInterval() { return null; }

    @Override
    public int getRows() { return rows; }

    @Override
    public int getCols() { return cols; }

    @Override
    public native void sleep();

    @Override
    public native void wake();

    ////////////////////
    // Native Methods //
    ////////////////////

    private native void initNative(int rows, int cols);

    private native void setPoint(int row, int col, boolean red, boolean green);

    private native void flush();

    //////////////////////
    // Direct Test Mode //
    //////////////////////

    public static void main(String[] args) {
        new Thread(() -> new CLightBoard().init(32, 192)).start();
    }

}
