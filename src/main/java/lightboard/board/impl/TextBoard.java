package lightboard.board.impl;

import lightboard.board.PolychromeLightBoard;

public class TextBoard implements PolychromeLightBoard {

    private final int rows;
    private final int cols;

    public TextBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    @Override
    public void init() {}

    @Override
    public void dump(boolean[][] data) {
        for ( int r=0; r<rows; r++ ) {
            System.out.print((r<10?"0":"")+r+":");
            for ( int c=0; c<cols; c++ ) {
                System.out.print(data[r][c] ? "#" : "-");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public void dump(double[][] data) {
        for ( int r=0; r<rows; r++ ) {
            System.out.print((r<10?"0":"")+r+":");
            for ( int c=0; c<cols; c++ ) {
                System.out.print(data[r][c]>0.5 ? "#" : "-");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public void dump(double[][][] data) {
        StringBuilder sb = new StringBuilder();
        for ( int r=0; r<rows; r++ ) {
            sb.append((r < 10 ? "0" : "") + r + ":");
            for ( int c=0; c<cols; c++ ) {
                double red = data[0][r][c];
                double green = data[1][r][c];
                double blue = data[2][r][c];
                sb.append(((red + green + blue) / 3) > 0.5 ? "#" : "-");
            }
            sb.append("\n");
        }
        sb.append("\n");
        System.out.println(sb.toString());
    }

    @Override
    public int getRefreshInterval() {
        return 250;
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
