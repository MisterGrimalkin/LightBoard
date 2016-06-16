package net.amarantha.lightboard.board;

public abstract class CLightBoard implements LightBoard {

    private int rows;
    private int cols;

    @Override
    public void init(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        initNative(rows, cols);
    }

    @Override
    public void update(double[][][] data) {
        for ( int r=0; r<rows; r++ ) {
            for ( int c=0; c<cols; c++ ) {
                setPoint(r, c, data[0][r][c] >= 0.5, data[1][r][c] >= 0.5);
            }
        }
    }

    @Override
    public Long getUpdateInterval() { return 1L; }

    @Override
    public int getRows() { return rows; }

    @Override
    public int getCols() { return cols; }

    protected abstract void initNative(int rows, int cols);

    protected abstract void setPoint(int row, int col, boolean red, boolean green);


}
