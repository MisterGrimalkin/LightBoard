package net.amarantha.lightboard.board.impl;

import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.module.Cols;
import net.amarantha.lightboard.module.Rows;

import javax.inject.Inject;

public class CLightBoard implements LightBoard {

    static {
        System.loadLibrary("lightboard");
    }

    @Override
    public native void init(int rows, int cols);

    @Override
    public native void update(double[][][] data);

    @Override
    public native Long getUpdateInterval();

    @Override
    public native int getRows();

    @Override
    public native int getCols();

    @Override
    public native void sleep();

    @Override
    public native void wake();

}
