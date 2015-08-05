package net.amarantha.lightboard.board.impl;

import net.amarantha.lightboard.board.LightBoard;

public class CLightBoard implements LightBoard {

    static {
        System.loadLibrary("lightboard");
    }

    @Override
    public native void init();

    @Override
    public native void update(double[][][] data);

    @Override
    public Long getUpdateInterval() {
        return 10L;
    }

    @Override
    public int getRows() {
        return 32;
    }

    @Override
    public int getCols() {
        return 192;
    }

}
