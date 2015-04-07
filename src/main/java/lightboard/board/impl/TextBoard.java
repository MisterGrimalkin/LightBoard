package lightboard.board.impl;

import lightboard.board.LightBoard;

public class TextBoard implements LightBoard {

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
