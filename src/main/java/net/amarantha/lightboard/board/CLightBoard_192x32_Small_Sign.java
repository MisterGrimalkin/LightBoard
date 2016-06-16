package net.amarantha.lightboard.board;

public class CLightBoard_192x32_Small_Sign extends CLightBoard {

    static {
        System.loadLibrary("lightboard_192x32_small_sign");
    }

    ////////////////////
    // Native Methods //
    ////////////////////

    @Override
    protected native void initNative(int rows, int cols);

    @Override
    protected native void setPoint(int row, int col, boolean red, boolean green);

    @Override
    public native void sleep();

    @Override
    public native void wake();

}
