package net.amarantha.lightboard.board.impl;

import net.amarantha.lightboard.board.LightBoard;

/**
 * Simple implementation of a colour LightBoard that dumps board state to the console
 */
public class TextBoard implements LightBoard {

    private final int rows;
    private final int cols;

    public TextBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void init() {
        System.out.println("Text Board Ready");
    }

    @Override
    public void update(double[][][] data) {
        StringBuilder sb = new StringBuilder();
        for ( int r=0; r<rows; r++ ) {
            sb.append((r < 10 ? "0" : "") + r + ":");
            for ( int c=0; c<cols; c++ ) {
                double red = data[0][r][c];
                double green = data[1][r][c];
                double blue = data[2][r][c];
                sb.append(red>=0.5||green>=0.5||blue>=0.5 ? "#" : "-");
            }
            sb.append("\n");
        }
        sb.append("\n");
        System.out.println(sb.toString());
    }

    @Override
    public Long getUpdateInterval() {
        return 250L;
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
