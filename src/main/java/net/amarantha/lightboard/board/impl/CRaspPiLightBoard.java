package net.amarantha.lightboard.board.impl;

import net.amarantha.lightboard.board.RGBLightBoard;

public class CRaspPiLightBoard implements RGBLightBoard {

    CLightBoard board = new CLightBoard();

    @Override
    public void dump(double[][][] data) {
        board.dump(data);
    }

    @Override
    public void init() {
        board.init();
    }

    @Override
    public void dump(boolean[][] data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long getRefreshInterval() {
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
