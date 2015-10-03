package net.amarantha.lightboard.board;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.amarantha.lightboard.module.Cols;
import net.amarantha.lightboard.module.Rows;

@Singleton
public class MockBoard implements LightBoard {

    private double[][][] data;

    private final int rows;
    private final int cols;

    @Inject
    public MockBoard(@Rows int rows, @Cols int cols) {
        this.rows = rows;
        this.cols = cols;
        data = new double[3][rows][cols];
    }

    @Override
    public void init() {
        System.out.println("Mock LightBoard Ready");
    }

    @Override
    public void update(double[][][] data) {
        this.data = data;
    }

    @Override
    public Long getUpdateInterval() {
        return null;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }

    public double[][][] getData() {
        return data;
    }
}
