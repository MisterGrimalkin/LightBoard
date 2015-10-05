package net.amarantha.lightboard.board.impl;

import net.amarantha.lightboard.board.LightBoard;
import net.amarantha.lightboard.module.Cols;
import net.amarantha.lightboard.module.Rows;

import javax.inject.Inject;

public class CLightBoard implements LightBoard {

    private int rows;
    private int cols;

    @Inject
    public CLightBoard(@Rows int rows, @Cols int cols) {
        this.rows = rows;
        this.cols = cols;
    }

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
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }

    @Override
    public void sleep() {

    }

    @Override
    public void wake() {

    }

}
