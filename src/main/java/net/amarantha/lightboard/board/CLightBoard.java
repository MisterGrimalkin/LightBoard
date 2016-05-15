package net.amarantha.lightboard.board;

public class CLightBoard implements LightBoard {

    static {
        System.loadLibrary("lightboard");
    }

    @Override
    public native void init(int rows, int cols);

    @Override
    public native void update(double[][][] data);

    @Override
    public Long getUpdateInterval() { return 50L; }

    @Override
    public int getRows() { return 16; }

    @Override
    public int getCols() { return 192; }

    @Override
    public native void sleep();

    @Override
    public native void wake();

    public static void main(String[] args) {
        new Thread(() -> new CLightBoard().init(32, 192)).start();
    }

}
