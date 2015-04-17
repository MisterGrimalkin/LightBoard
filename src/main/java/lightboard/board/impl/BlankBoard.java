package lightboard.board.impl;

import lightboard.PiTest;
import lightboard.board.PolychromeLightBoard;

public class BlankBoard implements PolychromeLightBoard {

    private int rows;
    private int cols;

    public BlankBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void dump(double[][][] data) {

    }

    @Override
    public void dump(double[][] data) {

    }

    @Override
    public void init() {
        try {
            PiTest.startPulse();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dump(boolean[][] data) {

    }

    @Override
    public int getRefreshInterval() {
        return 1000;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }
}
